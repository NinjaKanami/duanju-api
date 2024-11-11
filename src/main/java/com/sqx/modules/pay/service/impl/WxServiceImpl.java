package com.sqx.modules.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.pay.config.WXConfig;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayClassify;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.PayClassifyService;
import com.sqx.modules.pay.service.WxService;
import com.sqx.modules.utils.AmountCalUtils;
import com.sqx.modules.utils.MD5Util;
import com.sqx.modules.utils.WXConfigUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/2/26
 */
@Service
@Slf4j
public class WxServiceImpl implements WxService {
    private static final String SPBILL_CREATE_IP = "127.0.0.1";
    private static final String TRADE_TYPE_APP = "APP";
    private static final String TRADE_TYPE_NATIVE = "NATIVE";
    private static final String TRADE_TYPE_JSAPI = "JSAPI";

    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private InviteService inviteService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private PayClassifyService payClassifyService;

    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");;

    @Override
    public Result payOrder(Long orderId, Integer classify) throws Exception {
        Orders bean=ordersService.selectOrderById(orderId);
        if(bean==null){
            return Result.error("订单生成失败，请重新下单！");
        }
        PayDetails payDetails1 = payDetailsDao.selectByOrderId(bean.getOrdersNo());
        if(payDetails1==null){
            PayDetails payDetails=new PayDetails();
            payDetails.setState(0);
            payDetails.setCreateTime(sdf.format(new Date()));
            payDetails.setOrderId(bean.getOrdersNo());
            payDetails.setUserId(bean.getUserId());
            payDetails.setMoney(bean.getPayMoney().doubleValue());
            payDetails.setClassify(classify);
            payDetails.setType(1);
            payDetailsDao.insert(payDetails);
        }
        return pay(bean.getPayMoney().doubleValue(),classify, bean.getUserId(), bean.getOrdersNo());
    }

    @Override
    public Result payMoney(Long payClassifyId,Long userId, Integer classify) throws Exception {
        PayClassify payClassify = payClassifyService.getById(payClassifyId);
        String generalOrder = getGeneralOrder();
        PayDetails payDetails=new PayDetails();
        payDetails.setState(0);
        payDetails.setCreateTime(sdf.format(new Date()));
        payDetails.setOrderId(generalOrder);
        payDetails.setUserId(userId);
        payDetails.setMoney(payClassify.getPrice().doubleValue());
        payDetails.setClassify(classify);
        payDetails.setType(2);
        payDetails.setRemark(String.valueOf(payClassifyId));
        payDetailsDao.insert(payDetails);
        //创建订单返回对象
        Orders orders = new Orders();
        //设置订单编号
        orders.setOrdersNo(generalOrder);
        //设置支付点券
        orders.setPayMoney(payClassify.getPrice());
        //设置订单类型
        orders.setOrdersType(3);
        //设置支付状态
        orders.setStatus(0);
        //设置用户id
        orders.setUserId(userId);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //设置创建时间
        orders.setCreateTime(df.format(new Date()));
        //插入到订单表中
        ordersService.save(orders);
        return pay(payClassify.getPrice().doubleValue(),classify, userId, generalOrder);
    }

    @Override
    public Result payMoneyOrders(Long payClassifyId,Long userId, Integer classify) throws Exception {
        PayClassify payClassify = payClassifyService.getById(payClassifyId);
        String generalOrder = getGeneralOrder();
        PayDetails payDetails=new PayDetails();
        payDetails.setState(0);
        payDetails.setCreateTime(sdf.format(new Date()));
        payDetails.setOrderId(generalOrder);
        payDetails.setUserId(userId);
        payDetails.setMoney(payClassify.getPrice().doubleValue());
        payDetails.setClassify(classify);
        payDetails.setType(2);
        payDetails.setRemark(String.valueOf(payClassifyId));
        payDetailsDao.insert(payDetails);
        //创建订单返回对象
        Orders orders = new Orders();
        //设置订单编号
        orders.setOrdersNo(generalOrder);
        //设置支付点券
        orders.setPayMoney(payClassify.getPrice());
        //设置订单类型
        orders.setOrdersType(3);
        //设置支付状态
        orders.setStatus(0);
        //设置用户id
        orders.setUserId(userId);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //设置创建时间
        orders.setCreateTime(df.format(new Date()));
        //插入到订单表中
        ordersService.save(orders);
        return Result.success().put("data",payDetails);
    }

    @Override
    public Result selectSign(String signData,String sessionKey){
        String paySigStr = MD5Util.calculateHmacSha256("requestVirtualPayment" + '&' + signData,commonInfoService.findOne(822).getValue());
        String signatureStr = MD5Util.calculateHmacSha256(signData,sessionKey);
        Map<String,Object> data=new HashMap<>();
        data.put("paySig",paySigStr);
        data.put("signature",signatureStr);
        return Result.success().put("data",data);
    }



    /**
     * 微信支付订单生成
     * @param moneys  支付点券 带小数点
     * @param type 类型 1app  2 二维码支付  3小程序 公众号支付
     * @param userId 用户id
     * @param outTradeNo 单号
     * @return
     * @throws Exception
     */
    private Result pay(Double moneys,Integer type,Long userId,String outTradeNo) throws Exception {
        CommonInfo oneu = commonInfoService.findOne(19);
        String url;
        if(type==3){
            url=oneu.getValue()+"/sqx_fast/app/wxPay/notifyJsApi";
        }else if(type==2){
            url=oneu.getValue()+"/sqx_fast/app/wxPay/notifyMp";
        }else{
            url=oneu.getValue()+"/sqx_fast/app/wxPay/notify";
        }
        String currentTimeMillis=(System.currentTimeMillis() / 1000)+"";
        CommonInfo one = commonInfoService.findOne(12);
        log.info("回调地址："+url);
        Double mul = AmountCalUtils.mul(moneys, 100);
        String money =String.valueOf(mul.intValue());
        String generateNonceStr = WXPayUtil.generateNonceStr();
        WXConfig config = new WXConfig();
        if(type==1){
            config.setAppId(commonInfoService.findOne(74).getValue());
        }else if(type==2){
            config.setAppId(commonInfoService.findOne(5).getValue());
        }else{
            config.setAppId(commonInfoService.findOne(45).getValue());
        }
        config.setKey(commonInfoService.findOne(75).getValue());
        config.setMchId(commonInfoService.findOne(76).getValue());
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", generateNonceStr);
        String body =one.getValue();
        data.put("body", body);
        //生成商户订单号，不可重复
        data.put("out_trade_no", outTradeNo);
        data.put("total_fee", money);
        //自己的服务器IP地址
        data.put("spbill_create_ip", SPBILL_CREATE_IP);
        //异步通知地址（请注意必须是外网）
        data.put("notify_url", url);
        //交易类型
        data.put("trade_type", type==1?TRADE_TYPE_APP:TRADE_TYPE_JSAPI);
        //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
        data.put("attach", "");
        data.put("sign", WXPayUtil.generateSignature(data, config.getKey(),
                WXPayConstants.SignType.MD5));
        if(type==3){
            UserEntity userEntity = userService.queryByUserId(userId);
            if(StringUtils.isNotBlank(userEntity.getOpenId())){
                data.put("openid", userEntity.getOpenId());
            }
        }else if(type==2){
            UserEntity userEntity = userService.queryByUserId(userId);
            if(StringUtils.isNotBlank(userEntity.getWxId())){
                data.put("openid", userEntity.getWxId());
            }
        }
        //使用官方API请求预付订单
        Map<String, String> response = wxpay.unifiedOrder(data);
        for(String key : response.keySet()){
            log.info("微信支付订单微信返回参数：keys:"+key+"    value:"+response.get(key).toString());
        }
        if ("SUCCESS".equals(response.get("return_code"))) {//主要返回以下5个参数
            if(type==1){
                Map<String, String> param = new HashMap<>();
                param.put("appid", config.getAppID());
                param.put("partnerid", response.get("mch_id"));
                param.put("prepayid", response.get("prepay_id"));
                param.put("package", "Sign=WXPay");
                param.put("noncestr", generateNonceStr);
                param.put("timestamp", currentTimeMillis);
                param.put("sign", WXPayUtil.generateSignature(param, config.getKey(),
                        WXPayConstants.SignType.MD5));
                param.put("outtradeno", outTradeNo);
                return Result.success().put("data",param);
            }else{
                Map<String, String> param = new HashMap<>();
                param.put("appid", config.getAppID());
                param.put("partnerid", response.get("mch_id"));
                param.put("prepayid", response.get("prepay_id"));
                param.put("noncestr", generateNonceStr);
                param.put("timestamp",currentTimeMillis);
                    /*param.put("sign", WXPayUtil.generateSignature(param, config.getKey(),
                            WXPayConstants.SignType.MD5));*/
                String stringSignTemp = "appId=" + config.getAppID() + "&nonceStr=" + generateNonceStr + "&package=prepay_id=" + response.get("prepay_id") + "&signType=MD5&timeStamp=" + currentTimeMillis+ ""+"&key="+config.getKey();
                String sign = MD5Util.md5Encrypt32Upper(stringSignTemp).toUpperCase();
                param.put("sign",sign);
                param.put("outtradeno", outTradeNo);
                param.put("package", "prepay_id="+response.get("prepay_id"));//给前端返回的值
                param.put("mweb_url", response.get("mweb_url"));
                param.put("trade_type", response.get("trade_type"));
                param.put("return_msg", response.get("return_msg"));
                param.put("result_code", response.get("result_code"));
                param.put("signType", "MD5");
                return Result.success().put("data",param);
            }
        }
        return Result.error("获取订单失败");
    }

    @Override
    public String payBack(String resXml,Integer type) {
        WXConfig config = null;
        try {
            config = new WXConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.error("进入回调了！！！");
        if(type==1){
            config.setAppId(commonInfoService.findOne(74).getValue());
        }else if(type==2){
            config.setAppId(commonInfoService.findOne(5).getValue());
        }else{
            config.setAppId(commonInfoService.findOne(45).getValue());
        }
        config.setKey(commonInfoService.findOne(75).getValue());
        config.setMchId(commonInfoService.findOne(76).getValue());
        WXPay wxpay = new WXPay(config);
        String xmlBack = "";
        Map<String, String> notifyMap = null;
        try {
            notifyMap = WXPayUtil.xmlToMap(resXml);         // 调用官方SDK转换成map类型数据
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {//验证签名是否有效，有效则进一步处理
                log.error("验证成功！！！");
                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//商户订单号
                if (return_code.equals("SUCCESS")) {
                    if (out_trade_no != null) {
                        // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户的订单状态从退款改成支付成功
                        // 注意特殊情况：微信服务端同样的通知可能会多次发送给商户系统，所以数据持久化之前需要检查是否已经处理过了，处理了直接返回成功标志
                        //业务数据持久化
                        log.error("订单号！！！"+out_trade_no);
                        PayDetails payDetails = payDetailsDao.selectByOrderId(out_trade_no);
                        if(payDetails.getState()==0){
                            String format = sdf.format(new Date());
                            payDetailsDao.updateState(payDetails.getId(),1,format,"");
                            if(payDetails.getType()==1){
                                Orders orders = ordersService.selectOrderByOrdersNo(payDetails.getOrderId());
                                orders.setPayTime(DateUtils.format(new Date()));
                                orders.setPayWay(type);
                                orders.setStatus(1);
                                UserEntity user = userService.selectUserById(orders.getUserId());
                                Map map = inviteService.updateInvite(user,orders.getOrdersNo(), orders.getOrdersType(), orders.getPayMoney());
                                Object memberUserId = map.get("memberUserId");
                                if(memberUserId!=null){
                                    orders.setMemberUserId(Long.parseLong(String.valueOf(memberUserId)));
                                    orders.setMemberMoney(new BigDecimal(String.valueOf(map.get("memberMoney"))));
                                    orders.setMemberType(Integer.parseInt(String.valueOf(map.get("memberType"))));
                                }
                                Object channelUserId = map.get("channelUserId");
                                if(channelUserId!=null){
                                    orders.setChannelUserId(Long.parseLong(String.valueOf(channelUserId)));
                                    orders.setChannelMoney(new BigDecimal(String.valueOf(map.get("channelMoney"))));
                                }
                                Object pingMoney = map.get("pingMoney");
                                if(pingMoney!=null){
                                    orders.setPingMoney(new BigDecimal(String.valueOf(map.get("pingMoney"))));
                                }
                                ordersService.updateById(orders);
                                ordersService.insertOrders(orders);
                            }else{
                                String remark = payDetails.getRemark();
                                PayClassify payClassify = payClassifyService.getById(Long.parseLong(remark));
                                BigDecimal add = payClassify.getMoney().add(payClassify.getGiveMoney());
                                userMoneyService.updateMoney(1,payDetails.getUserId(),add.doubleValue());
                                UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                                userMoneyDetails.setClassify(1);
                                userMoneyDetails.setMoney(add);
                                userMoneyDetails.setUserId(payDetails.getUserId());
                                userMoneyDetails.setContent("微信充值点券");
                                userMoneyDetails.setTitle("微信充值点券："+payClassify.getMoney()+",赠送："+payClassify.getGiveMoney());
                                userMoneyDetails.setType(1);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
                                userMoneyDetailsService.save(userMoneyDetails);
                                Orders orders = ordersService.selectOrderByOrdersNo(payDetails.getOrderId());
                                orders.setPayTime(DateUtils.format(new Date()));
                                orders.setPayWay(type);
                                orders.setStatus(1);
                                UserEntity user = userService.selectUserById(orders.getUserId());
                                Map map = inviteService.updateInvite(user,orders.getOrdersNo(), 1, orders.getPayMoney());
                                Object memberUserId = map.get("memberUserId");
                                if(memberUserId!=null){
                                    orders.setMemberUserId(Long.parseLong(String.valueOf(memberUserId)));
                                    orders.setMemberMoney(new BigDecimal(String.valueOf(map.get("memberMoney"))));
                                    orders.setMemberType(Integer.parseInt(String.valueOf(map.get("memberType"))));
                                }
                                Object channelUserId = map.get("channelUserId");
                                if(channelUserId!=null){
                                    orders.setChannelUserId(Long.parseLong(String.valueOf(channelUserId)));
                                    orders.setChannelMoney(new BigDecimal(String.valueOf(map.get("channelMoney"))));
                                }
                                Object pingMoney = map.get("pingMoney");
                                if(pingMoney!=null){
                                    orders.setPingMoney(new BigDecimal(String.valueOf(map.get("pingMoney"))));
                                }
                                ordersService.updateById(orders);
                            }

                        }
                        System.err.println("微信手机支付回调成功订单号:" + out_trade_no + "");
                        xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        System.err.println("微信手机支付回调成功订单号:" + out_trade_no + "");
                        xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    }
                }else{
                }
                return xmlBack;
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                System.err.println("手机支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (Exception e) {
            System.err.println("手机支付回调通知失败" + e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            log.error("回调异常："+e.getMessage(),e);
        }
        return xmlBack;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String notifyXPay(String out_trade_no) {
        // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户的订单状态从退款改成支付成功
        // 注意特殊情况：微信服务端同样的通知可能会多次发送给商户系统，所以数据持久化之前需要检查是否已经处理过了，处理了直接返回成功标志
        //业务数据持久化
        log.error("订单号！！！"+out_trade_no);
        PayDetails payDetails = payDetailsDao.selectByOrderId(out_trade_no);
        if(payDetails.getState()==0){
            String format = sdf.format(new Date());
            payDetailsDao.updateState(payDetails.getId(),1,format,"");
            if(payDetails.getType()==1){
                Orders orders = ordersService.selectOrderByOrdersNo(payDetails.getOrderId());
                orders.setPayTime(DateUtils.format(new Date()));
                orders.setPayWay(3);
                orders.setStatus(1);
                orders.setPayTime(DateUtils.format(new Date()));

                UserEntity user = userService.selectUserById(orders.getUserId());
                Map map = inviteService.updateInvite(user,orders.getOrdersNo(), orders.getOrdersType(), orders.getPayMoney());
                Object memberUserId = map.get("memberUserId");
                if(memberUserId!=null){
                    orders.setMemberUserId(Long.parseLong(String.valueOf(memberUserId)));
                    orders.setMemberMoney(new BigDecimal(String.valueOf(map.get("memberMoney"))));
                    orders.setMemberType(Integer.parseInt(String.valueOf(map.get("memberType"))));
                }
                Object channelUserId = map.get("channelUserId");
                if(channelUserId!=null){
                    orders.setChannelUserId(Long.parseLong(String.valueOf(channelUserId)));
                    orders.setChannelMoney(new BigDecimal(String.valueOf(map.get("channelMoney"))));
                }
                Object pingMoney = map.get("pingMoney");
                if(pingMoney!=null){
                    orders.setPingMoney(new BigDecimal(String.valueOf(map.get("pingMoney"))));
                }

                // 乐观锁
                boolean b = ordersService.update(orders, new QueryWrapper<Orders>()
                        .eq("orders_id", orders.getOrdersId())
                        .eq("status", 0));
                if(!b){
                    throw new RuntimeException("订单状态异常");
                }

                ordersService.insertOrders(orders);
            }else{
                String remark = payDetails.getRemark();
                PayClassify payClassify = payClassifyService.getById(Long.parseLong(remark));
                BigDecimal add = payClassify.getMoney().add(payClassify.getGiveMoney());
                userMoneyService.updateMoney(1,payDetails.getUserId(),add.doubleValue());
                UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                userMoneyDetails.setClassify(1);
                userMoneyDetails.setMoney(add);
                userMoneyDetails.setUserId(payDetails.getUserId());
                userMoneyDetails.setContent("微信充值点券");
                userMoneyDetails.setTitle("微信充值点券："+payClassify.getMoney()+",赠送："+payClassify.getGiveMoney());
                userMoneyDetails.setType(1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
                userMoneyDetailsService.save(userMoneyDetails);
                Orders orders = ordersService.selectOrderByOrdersNo(payDetails.getOrderId());
                orders.setPayTime(DateUtils.format(new Date()));
                orders.setPayWay(3);
                orders.setStatus(1);
                // 乐观锁
                boolean b = ordersService.update(orders, new QueryWrapper<Orders>()
                        .eq("orders_id", orders.getOrdersId())
                        .eq("status", 0));
                if(!b){
                    throw new RuntimeException("订单状态异常");
                }
            }

        }
        System.err.println("微信手机支付回调成功订单号:" + out_trade_no + "");
        return "<xml><Errcode>0</ErrCode><ErrMsg><![CDATA[success]]></ErrMsg></xml>";
    }



    public String getGeneralOrder(){
        Date date=new Date();
        String newString = String.format("%0"+4+"d", (int)((Math.random()*9+1)*1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format+newString;
    }


    @Override
    public boolean refund(Orders orders){
        WXConfigUtil config = null;
        String h5Url = commonInfoService.findOne(19).getValue().split("://")[1];
        String filePath = "/www/wwwroot/"+h5Url+"/service/apiclient_cert.p12";
        try {
            config = new WXConfigUtil(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int commInfoId = 0;
        Integer payWay = orders.getPayWay(); //支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6点券）
        switch (payWay){
            case 1 : commInfoId = 74; break; //appId
            case 2 : commInfoId = 5; break; //公众号id
            case 3 : commInfoId = 45; break; //小程序id
        }
        config.setAppId(commonInfoService.findOne(commInfoId).getValue());
        config.setKey(commonInfoService.findOne(75).getValue());
        config.setMchId(commonInfoService.findOne(76).getValue());
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", WXPayUtil.generateNonceStr());
        try{
            data.put("sign", WXPayUtil.generateSignature(data, config.getKey(), WXPayConstants.SignType.MD5));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        data.put("out_trade_no", orders.getOrdersNo()); //订单号,支付单号一致
        data.put("out_refund_no", orders.getOrdersNo()); //退款单号，同一笔用不同的退款单号
        double total_fee = 0.00;
        data.put("total_fee", new Double(orders.getPayMoney().doubleValue()*100).intValue()+""); //1块等于微信支付传入100);
        data.put("refund_fee", new Double(orders.getPayMoney().doubleValue()*100).intValue()+""); //1块等于微信支付传入100);
        //使用官方API退款
        try{
            Map<String, String> response = wxpay.refund(data);
            if ("SUCCESS".equals(response.get("return_code"))) {//主要返回以下5个参数
                System.err.println("退款成功");
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            log.info("返回");
            e.printStackTrace();
            return false;
        }
    }
}
