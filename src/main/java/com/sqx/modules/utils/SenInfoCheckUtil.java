package com.sqx.modules.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.sqx.modules.common.service.CommonInfoService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@Component
public class SenInfoCheckUtil {

    private static Logger logger = LoggerFactory.getLogger(SenInfoCheckUtil.class);

    private static String MpAccessToken;
    private static long lastMpTokenFetchTime = 0;

    private static String DyAccessToken;
    private static long lastDyTokenFetchTime = 0;

    // 这里使用静态，让 service 属于类
    private static CommonInfoService commonInfoService;

    // 注入的时候，给类的 service 注入
    @Autowired
    public void setWxChatContentService(CommonInfoService commonInfoService) {
        SenInfoCheckUtil.commonInfoService = commonInfoService;
    }


    /**
     * 获取Token  小程序
     * @return AccessToken
     */
    public static String getMpToken() {
        long currentTime = System.currentTimeMillis();
        if (MpAccessToken == null || (currentTime - lastMpTokenFetchTime > 2 * 30 * 60 * 1000)) {
            MpAccessToken = getMpAccessToken();
            lastMpTokenFetchTime = currentTime;
        }
        return MpAccessToken;
    }

    public static String getDyToken() {
        long currentTime = System.currentTimeMillis();
        if (DyAccessToken == null || (currentTime - lastDyTokenFetchTime > 2 * 30 * 60 * 1000)) {
            DyAccessToken = getDyAccessToken();
            lastDyTokenFetchTime = currentTime;
        }
        return DyAccessToken;
    }


    public static void getImg(String relation,String goodsId,String type, String page,HttpServletResponse response){
        String mpToken = getMpToken();
        //获取二维码数据
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+mpToken;
        Map<String,Object> map = Maps.newHashMap();
        map.put("scene",relation+"&"+goodsId+"&"+type);
        String value = commonInfoService.findOne(105).getValue();
        if("是".equals(value)){
            map.put("page",page);
        }
        map.put("width", 280);
        String jsonString = JSON.toJSONString(map);
        InputStream inputStream = sendPostBackStream(url, jsonString);
        //生成二维码图片
        response.setContentType("image/png");
        try{
            BufferedImage bi = ImageIO.read(inputStream);
            ImageIO.write(bi, "JPG", response.getOutputStream());
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 获取二维码图片
     */
    public static void getDyImg(String invitationCode,String page, HttpServletResponse response){
        String dyToken = getDyToken();
        //获取二维码数据
        String url = "https://open.douyin.com/api/apps/v1/qrcode/create/";

        JSONObject map = new JSONObject();
        String path="{"+page+"}?{invitationCode="+invitationCode+"}";
        try {
            path = URLEncoder.encode(path,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String appid = commonInfoService.findOne(805).getValue();
        map.put("appid",appid);
        map.put("path",path);
        map.put("app_name","douyin");
        map.put("is_circle_code",false);
        map.put("set_icon",true);

        String s = HttpClientUtil.doPostJson(url, map.toJSONString(),dyToken);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String err_no = jsonObject.getString("err_no");
        if(!"0".equals(err_no)){
            logger.error("抖音二维码生成失败："+jsonObject.getString("err_msg"));
        }
        JSONObject data = jsonObject.getJSONObject("data");
        String img = data.getString("img");
        BufferedImage bi = base64ToBufferedImage(img);
        if(bi!=null){
            //生成二维码图片
            response.setContentType("image/png");
            try{
                ImageIO.write(bi, "JPG", response.getOutputStream());
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        }
    }


    /**
     * base64 编码转换为 BufferedImage
     * @param base64
     * @return
     */
    public static BufferedImage base64ToBufferedImage(String base64) {
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            byte[] byteArray = decoder.decode(base64);
            ByteArrayInputStream bai = new ByteArrayInputStream(byteArray);
            return ImageIO.read(bai);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取二维码图片
     */
    public static void getPoster(String invitationCode,String page, HttpServletResponse response){
        String mpToken = getMpToken();
        //获取二维码数据
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+mpToken;
        Map<String,Object> map = Maps.newHashMap();
        map.put("scene",invitationCode);
        if(StringUtils.isNotEmpty(page)){
            map.put("page",page);
        }
        map.put("width", 280);
        String jsonString = JSON.toJSONString(map);
        InputStream inputStream = sendPostBackStream(url, jsonString);
        //生成二维码图片
        response.setContentType("image/png");
        try{
            BufferedImage bi = ImageIO.read(inputStream);
            ImageIO.write(bi, "JPG", response.getOutputStream());
            inputStream.close();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }




    private static InputStream sendPostBackStream(String url, String param) {
        PrintWriter out = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //解决乱码问题
            OutputStreamWriter outWriter =new OutputStreamWriter(conn.getOutputStream(), "utf-8");
            out =new PrintWriter(outWriter);
            // 发送请求参数
            if(StringUtils.isNotBlank(param)) {
                out.print(param);
            }
            // flush输出流的缓冲
            out.flush();
            return conn.getInputStream();
        } catch (Exception e) {
            logger.error("发送 POST 请求出现异常！"+e);
        } finally{
            IOUtils.closeQuietly(out);
        }
        return null;
    }


    /**
     * 获取access_token
     * 每个两个小时自动刷新AcessTocken
     */
    public static String getMpAccessToken(){
        String appid = commonInfoService.findOne(45).getValue();
        String secret = commonInfoService.findOne(46).getValue();
        String jsonResult = HttpClientUtil.doPost("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret);
        JSONObject parseObject = JSON.parseObject(jsonResult);
        logger.info("=========accessTokenOut========="+parseObject.toJSONString());

        String errcode = parseObject.getString("errcode");
        String accessToken = parseObject.getString("access_token");
        String expiresIn = parseObject.getString("expires_in");
        return accessToken;
    }

    /**
     * 获取access_token
     * 每个两个小时自动刷新AcessTocken
     */

    public static String getDyAccessToken(){
        String appid = commonInfoService.findOne(805).getValue();
        String secret = commonInfoService.findOne(806).getValue();
        Map<String,String> jsonObject=new HashMap<>();
        jsonObject.put("appid",appid);
        jsonObject.put("secret",secret);
        jsonObject.put("grant_type","client_credential");
        String jsonResult = HttpClientUtil.doPost("https://open.douyin.com/oauth/client_token/",jsonObject);

        JSONObject parseObject = JSON.parseObject(jsonResult);
        logger.info("=========accessTokenOut========="+parseObject.toJSONString());
        JSONObject data = parseObject.getJSONObject("data");
        return data.getString("access_token");
    }




}
