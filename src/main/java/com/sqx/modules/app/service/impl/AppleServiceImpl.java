package com.sqx.modules.app.service.impl;


import com.auth0.jwk.Jwk;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.service.IAppleService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.PublicKey;

/**
 * @Description 苹果登录service
 * @author fang
 * @date 2020/11/4
 */

@Slf4j
@Component
public class AppleServiceImpl implements IAppleService {

    @Override
    public Result getAppleUserInfo(String identityToken) throws Exception {
        //验证identityToken
        if (!verify(identityToken)) {
            log.error("苹果解析失败！");
            return Result.error("苹果账号验证失败，请退出重试！");
        }
        //对identityToken解码
        JSONObject json = parserIdentityToken(identityToken);
        if (json == null) {
            return Result.error("苹果账号验证失败，请退出重试！");
        }
        String appleUserId = String.valueOf(json.get("sub"));
        log.error("苹果账号解析成功："+appleUserId);
        System.err.println(appleUserId);
        return Result.success().put("data",appleUserId);
    }

    /**
     * 对前端传来的JWT字符串identityToken的第二部分进行解码
     * 主要获取其中的aud和sub，aud大概对应ios前端的包名，sub大概对应当前用户的授权的openID
     *
     * @param identityToken 身份token
     * @return {"aud":"com.xkj.****","sub":"000***.8da764d3f9e34d2183e8da08a1057***.0***","c_hash":"UsKAuEoI-****","email_verified":"true","auth_time":1574673481,"iss":"https://appleid.apple.com","exp":1574674081,"iat":1574673481,"email":"****@qq.com"}
     */
    private JSONObject parserIdentityToken(String identityToken) {
        String[] arr = identityToken.split("\\.");
        String decode = new String(Base64.decodeBase64(arr[1]));
        String substring = decode.substring(0, decode.indexOf("}") + 1);
        return JSONObject.fromObject(substring);
    }


    public Boolean verify(String jwt) throws Exception {
        JSONArray arr = getAuthKeys();
        if (arr == null) {
            log.error("获取不到苹果的验证秘钥！！");
            return false;
        }

        JSONObject authKey = null;
        //先取苹果第一个key进行校验
        if(arr.size()==2){
            authKey = JSONObject.fromObject(arr.getString(0));
            if (verifyExc(jwt, authKey)) {
                log.error("苹果解析成功！1");
                return true;
            } else {
                //再取第二个key校验
                authKey = JSONObject.fromObject(arr.getString(1));
                return verifyExc(jwt, authKey);
            }
        }else{
            authKey = JSONObject.fromObject(arr.getString(0));
            if (verifyExc(jwt, authKey)) {
                log.error("苹果解析成功！1");
                return true;
            }
            //再取第二个key校验
            authKey = JSONObject.fromObject(arr.getString(1));
            if(verifyExc(jwt, authKey)){
                log.error("苹果解析成功！2");
                return true;
            }else{
                authKey = JSONObject.fromObject(arr.getString(2));
                return verifyExc(jwt, authKey);
            }
        }
    }


    /**
     * 对前端传来的identityToken进行验证
     *
     * @param jwt     对应前端传来的 identityToken
     * @param authKey 苹果的公钥 authKey
     * @return
     * @throws Exception
     */
    private static Boolean verifyExc(String jwt, JSONObject authKey) throws Exception {

        Jwk jwa = Jwk.fromValues(authKey);
        PublicKey publicKey = jwa.getPublicKey();

        String aud = "";
        String sub = "";
        if (jwt.split("\\.").length > 1) {
            String claim = new String(Base64.decodeBase64(jwt.split("\\.")[1]));
            aud = JSONObject.fromObject(claim).get("aud").toString();
            sub = JSONObject.fromObject(claim).get("sub").toString();
        }
        JwtParser jwtParser = Jwts.parser().setSigningKey(publicKey);
        jwtParser.requireIssuer("https://appleid.apple.com");
        jwtParser.requireAudience(aud);
        jwtParser.requireSubject(sub);

        try {
            Jws<Claims> claim = jwtParser.parseClaimsJws(jwt);
            if (claim != null && claim.getBody().containsKey("auth_time")) {
                System.out.println(claim);
                return true;
            }
            return false;
        } catch (ExpiredJwtException e) {
            log.error("[AppleServiceImpl.verifyExc] [error] [apple identityToken expired]", e);
            return false;
        } catch (Exception e) {
            log.error("[AppleServiceImpl.verifyExc] [error] [apple identityToken illegal]", e);
            return false;
        }
    }


    /**
     * 获取苹果的公钥
     *
     * @return
     */
    private static JSONArray getAuthKeys() {
        String url = "https://appleid.apple.com/auth/keys";
        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = restTemplate.getForObject(url, JSONObject.class);
        if (json != null) {
            return json.getJSONArray("keys");
        }
        return null;
    }


}