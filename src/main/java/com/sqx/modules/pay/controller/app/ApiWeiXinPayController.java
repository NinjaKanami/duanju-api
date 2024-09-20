package com.sqx.modules.pay.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.pay.service.WxService;
import com.sqx.modules.utils.MessageUtil;
import com.sqx.modules.utils.SenInfoCheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Element;
import weixin.popular.bean.message.EventMessage;
import weixin.popular.support.ExpireKey;
import weixin.popular.support.expirekey.DefaultExpireKey;
import weixin.popular.util.SignatureUtil;
import weixin.popular.util.StreamUtils;
import weixin.popular.util.XMLConverUtil;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author fang
 * @date 2020/2/26
 */
@RestController
@Api(value = "微信支付", tags = {"微信支付"})
@RequestMapping("/app/wxPay")
@Slf4j
public class ApiWeiXinPayController {

    @Autowired
    private WxService wxService;
    @Autowired
    private CommonInfoService commonInfoService;
    //重复通知过滤
    private static ExpireKey expireKey = new DefaultExpireKey();

    @Login
    @ApiOperation("微信app支付订单")
    @PostMapping("/payAppOrder")
    public Result payAppOrder(Long orderId) throws Exception {
        return wxService.payOrder(orderId,1);
    }

    @Login
    @ApiOperation("微信小程序支付订单")
    @PostMapping("/wxPayJsApiOrder")
    public Result wxPayJsApiOrder(Long orderId) throws Exception {
        return wxService.payOrder(orderId,3);
    }


    @Login
    @ApiOperation("微信公众号支付订单")
    @PostMapping("/wxPayMpOrder")
    public Result wxPayMpOrder(Long orderId) throws Exception {
        return wxService.payOrder(orderId,2);
    }

    @Login
    @ApiOperation("充值点券")
    @PostMapping("/payMoney")
    public Result payMoney(Long payClassifyId, Integer classify,@RequestAttribute Long userId)  throws Exception {
        return wxService.payMoney(payClassifyId,userId,classify);
    }

    @Login
    @ApiOperation("充值点券(只生成订单号)")
    @PostMapping("/payMoneyOrders")
    public Result payMoneyOrders(Long payClassifyId, Integer classify,@RequestAttribute Long userId)  throws Exception {
        return wxService.payMoneyOrders(payClassifyId,userId,classify);
    }

    @Login
    @ApiOperation("虚拟支付签名")
    @PostMapping("/selectSign")
    public Result selectSign(@RequestBody JSONObject jsonObject)  throws Exception {
        JSONObject signData = jsonObject.getJSONObject("signData");
        String sessionKey = jsonObject.getString("sessionKey");
        return wxService.selectSign(signData.toJSONString(),sessionKey);
    }


    @PostMapping("/notify")
    @ApiOperation("微信回调")
    public String wxPayNotify(HttpServletRequest request) {
        String resXml = "";
        try {
            InputStream inputStream = request.getInputStream();
            //将InputStream转换成xmlString
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            String result = wxService.payBack(resXml,1);
            log.info("成功");
            log.info(result);
            return result;
        } catch (Exception e) {
            log.info("微信手机支付失败:" + e.getMessage());
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            log.info("失败");
            log.info(result);
            return result;
        }
    }


    @PostMapping("/notifyJsApi")
    @ApiOperation("微信回调")
    public String notifyJsApi(HttpServletRequest request) {
        String resXml = "";
        try {
            InputStream inputStream = request.getInputStream();
            //将InputStream转换成xmlString
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            String result = wxService.payBack(resXml,3);
            log.info("成功");
            log.info(result);
            return result;
        } catch (Exception e) {
            log.info("微信手机支付失败:" + e.getMessage());
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            log.info("失败");
            log.info(result);
            return result;
        }
    }


    @PostMapping("/notifyMp")
    @ApiOperation("微信回调")
    public String notifyMp(HttpServletRequest request) {
        String resXml = "";
        try {
            InputStream inputStream = request.getInputStream();
            //将InputStream转换成xmlString
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            String result = wxService.payBack(resXml,2);
            log.info("成功");
            log.info(result);
            return result;
        } catch (Exception e) {
            log.info("微信手机支付失败:" + e.getMessage());
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            log.info("失败");
            log.info(result);
            return result;
        }
    }



    /**
     * 微信公众号消息管理
     *
     * @return openid
     */
    @RequestMapping(value = "/notifyXPayBak", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("虚拟支付回调")
    @ResponseBody
    public void connectWeixinsqxBak(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //公众号Token
        CommonInfo token = commonInfoService.findOne(826);
        //公众号EncodingAESKey
        CommonInfo EncodingAESKey = commonInfoService.findOne(827);
        //微信APPID
        CommonInfo appid = commonInfoService.findOne(45);
        //后台服务域名配置
        CommonInfo yuming = commonInfoService.findOne(19);
        //邀请码
        CommonInfo one = commonInfoService.findOne(88);
        //后台服务名称
        CommonInfo name = commonInfoService.findOne(12);
        ServletInputStream inputStream = request.getInputStream();
        ServletOutputStream outputStream = response.getOutputStream();
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        //加密模式
        String encrypt_type = request.getParameter("encrypt_type");
        String msg_signature = request.getParameter("msg_signature");
        WXBizMsgCrypt wxBizMsgCrypt = null;
        //加密方式
        boolean isAes = "aes".equals(encrypt_type);
        if (isAes) {
            try {
                wxBizMsgCrypt = new WXBizMsgCrypt(SenInfoCheckUtil.getMpAccessToken(), EncodingAESKey.getValue() , appid.getValue());
            } catch (AesException e) {
                e.printStackTrace();
            }
        }

        //首次请求申请验证,返回echostr
        if (isAes && echostr != null) {
            try {
                echostr = URLDecoder.decode(echostr, "utf-8");
                assert wxBizMsgCrypt != null;
                String echostr_decrypt = wxBizMsgCrypt.verifyUrl(msg_signature, timestamp, nonce, echostr);
                outputStreamWrite(outputStream, echostr_decrypt);
                return;
            } catch (AesException e) {
                e.printStackTrace();
            }
        } else if (echostr != null) {
            outputStreamWrite(outputStream, echostr);
            return;
        }

        EventMessage eventMessage = null;
        if (isAes) {
            try {
                //获取XML数据（含加密参数）
                String postData = StreamUtils.copyToString(inputStream, Charset.forName("utf-8"));
                //解密XML 数据
                assert wxBizMsgCrypt != null;
                String xmlData = wxBizMsgCrypt.decryptMsg(msg_signature, timestamp, nonce, postData);
                //XML 转换为bean 对象
                eventMessage = XMLConverUtil.convertToObject(EventMessage.class, xmlData);
            } catch (AesException e) {
                e.printStackTrace();
            }
        } else {
            if (signature == null) {
                System.out.println("The request signature is null");
                return;
            }
            //验证请求签名
            if (!signature.equals(SignatureUtil.generateEventMessageSignature(token.getValue() , timestamp, nonce))) {
                System.out.println("The request signature is invalid");
                return;
            }

            if (inputStream != null) {
                //XML 转换为bean 对象
                eventMessage = XMLConverUtil.convertToObject(EventMessage.class, inputStream);
            }
        }

        String fromUserName = eventMessage.getFromUserName();
        String key = fromUserName + "__"
                + eventMessage.getToUserName() + "__"
                + eventMessage.getMsgId() + "__"
                + eventMessage.getCreateTime();
        if (expireKey.exists(key)) {
            //重复通知不作处理
            return;
        } else {
            expireKey.add(key);
        }

        String event = eventMessage.getEvent();
        String msgType1 = eventMessage.getMsgType();
        String eventKey = eventMessage.getEventKey();
        log.error("getEvent----" + event);
        log.error("getMsgType----" + msgType1);
        log.error("eventKey----" + eventKey);
        //判断请求是否事件类型 event 用户关注公众号事件
        if (MessageUtil.MESSAGE_EVENT.equals(msgType1) && "xpay_goods_deliver_notify".equals(event)) {
            List<Element> otherElements = eventMessage.getOtherElements();
            Element element1 = otherElements.get(1);
            String textContent = element1.getTextContent();
            String notifyXPay = wxService.notifyXPay(textContent);
            outputStreamWrite(outputStream, notifyXPay);
        }
        outputStreamWrite(outputStream, "<xml><Errcode>0</ErrCode><ErrMsg><![CDATA[success]]></ErrMsg></xml>");
    }

    /**
     * 微信小程序消息推送
     *
     * @return openid
     */
    @RequestMapping(value = "/notifyXPay", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("小程序虚拟支付回调")
    @ResponseBody
    public void connectWeixinsqx(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //微信APPID
        CommonInfo appid = commonInfoService.findOne(45);
        //消息推送Token
        CommonInfo token = commonInfoService.findOne(826);
        //消息推送EncodingAESKey
        CommonInfo EncodingAESKey = commonInfoService.findOne(827);
        ServletInputStream inputStream = request.getInputStream();
        ServletOutputStream outputStream = response.getOutputStream();
        //首次验证参数&明文参数
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        //加密模式(安全模式)
        String encrypt_type = request.getParameter("encrypt_type");
        String msg_signature = request.getParameter("msg_signature");
        String accessToken = SenInfoCheckUtil.getMpAccessToken();
        WXBizMsgCrypt wxBizMsgCrypt = null;
        //加密方式
        boolean isAes = "aes".equals(encrypt_type);

        if (isAes) {
            try {
                wxBizMsgCrypt = new WXBizMsgCrypt(token.getValue(), EncodingAESKey.getValue() , appid.getValue());
            } catch (AesException e) {
                e.printStackTrace();
            }
        }

        EventMessage eventMessage = null;

        if (isAes) {
            //若为安全模式消息推送
            try {
                //获取XML数据（含加密参数）
                String postData = StreamUtils.copyToString(inputStream, Charset.forName("utf-8"));
                //解密XML 数据
                assert wxBizMsgCrypt != null;
                String xmlData = wxBizMsgCrypt.decryptMsg(msg_signature, timestamp, nonce, postData);
                //XML 转换为bean 对象
                eventMessage = XMLConverUtil.convertToObject(EventMessage.class, xmlData);
            } catch (AesException e) {
                e.printStackTrace();
            }
        } else {
            //若非安全模式消息推送 则明文模式消息推送 或 验证消息推送
            if (signature == null) {
                System.out.println("The request signature is null");
                return;
            }
            //验证请求签名
            if (!signature.equals(SignatureUtil.generateEventMessageSignature(token.getValue() , timestamp, nonce))) {
                System.out.println("The request signature is invalid");
                return;
            }
            //首次请求申请验证,返回echostr
            if (echostr != null) {
                echostr = URLDecoder.decode(echostr, "utf-8");
                outputStreamWrite(outputStream, echostr);
                log.info("=========首次验证echostr========="+echostr);
                return;
            }
            //明文模式消息推送
            if (inputStream != null) {
                //XML 转换为bean 对象
                eventMessage = XMLConverUtil.convertToObject(EventMessage.class, inputStream);
            }
        }

        String key = eventMessage.getFromUserName() + "__"
                + eventMessage.getToUserName() + "__"
                + eventMessage.getMsgId() + "__"
                + eventMessage.getCreateTime();

        if (expireKey.exists(key)) {
            //重复通知不作处理
            return;
        } else {
            expireKey.add(key);
        }

        String event = eventMessage.getEvent();
        String msgType1 = eventMessage.getMsgType();
        String eventKey = eventMessage.getEventKey();
        log.error("getEvent----" + event);
        log.error("getMsgType----" + msgType1);
        log.error("eventKey----" + eventKey);
        //判断请求是否事件类型 event 用户关注公众号事件
        if (MessageUtil.MESSAGE_EVENT.equals(msgType1) && "xpay_goods_deliver_notify".equals(event)) {
            List<Element> otherElements = eventMessage.getOtherElements();
            Element element1 = otherElements.get(1);
            String textContent = element1.getTextContent();
            String notifyXPay = wxService.notifyXPay(textContent);
            outputStreamWrite(outputStream, notifyXPay);
        }
        outputStreamWrite(outputStream, "<xml><Errcode>0</ErrCode><ErrMsg><![CDATA[success]]></ErrMsg></xml>");
    }

    /**
     * 数据流输出
     */
    private void outputStreamWrite(OutputStream outputStream, String text) {
        try {
            outputStream.write(text.getBytes("utf-8"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
