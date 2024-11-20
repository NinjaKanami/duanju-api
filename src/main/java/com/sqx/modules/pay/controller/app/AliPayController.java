package com.sqx.modules.pay.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.orders.dao.OrdersDao;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.pay.config.AliPayConstants;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayClassify;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.PayClassifyService;
import com.yungouos.pay.alipay.AliPay;
import com.yungouos.pay.entity.AliPayH5Biz;
import com.yungouos.pay.util.PaySignUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 支付宝支付处理--暂不做同步处理、回调方式使用异步
 */
@Slf4j
@RestController
@Api(value = "支付宝支付", tags = {"支付宝支付"})
@RequestMapping("/app/aliPay")
public class AliPayController {

    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private UserService userService;
    @Autowired
    private InviteService inviteService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private PayClassifyService payClassifyService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @RequestMapping(value = "/notifyApp", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public void notifyApp(HttpServletRequest request, HttpServletResponse response) {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        try {
            log.info("回调成功！！！");
            boolean flag = AlipaySignature.rsaCheckV1(params, commonInfoService.findOne(64).getValue(), AliPayConstants.CHARSET, "RSA2");
            log.info(flag + "回调验证信息");
            if (flag) {
                String tradeStatus = params.get("trade_status");
                if("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)){
                    //支付宝返回的订单编号
                    String outTradeNo = params.get("out_trade_no");
                    //支付宝支付单号
                    String tradeNo = params.get("trade_no");
                    PayDetails payDetails = payDetailsDao.selectByOrderId(outTradeNo);
                    if(payDetails.getState()==0){
                        String format = sdf.format(new Date());
                        payDetailsDao.updateState(payDetails.getId(),1,format,tradeNo);
                        if(payDetails.getType()==1){
                            Orders orders = ordersService.selectOrderByOrdersNo(payDetails.getOrderId());
                            orders.setPayWay(4);
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
                                log.warn("订单信息已被更改！");
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
                            userMoneyDetails.setContent("支付宝充值点券");
                            userMoneyDetails.setTitle("支付宝充值点券："+payClassify.getMoney()+",赠送："+payClassify.getGiveMoney());
                            userMoneyDetails.setType(1);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
                            userMoneyDetailsService.save(userMoneyDetails);
                            Orders orders = ordersService.selectOrderByOrdersNo(payDetails.getOrderId());
                            orders.setPayTime(DateUtils.format(new Date()));
                            orders.setPayWay(4);
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
                            // 乐观锁
                            boolean b = ordersService.update(orders, new QueryWrapper<Orders>()
                                    .eq("orders_id", orders.getOrdersId())
                                    .eq("status", 0));
                            if(!b){
                                log.warn("订单信息已被更改！");
                                throw new RuntimeException("订单状态异常");
                            }
                        }

                    } else {
                        log.info("订单表信息丢失！");
                    }
                }

            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.info("回调验证失败！！！");
        }
    }


    @ApiOperation("支付宝回调")
    @RequestMapping("/notifyAppYunOS")
    @Transactional(rollbackFor = Exception.class)
    public String notifyAppYunOS(HttpServletRequest request, HttpServletResponse response){
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        String outTradeNo = params.get("outTradeNo");
        String code = params.get("code");
        String key = commonInfoService.findOne(169).getValue();
        try {
            boolean flag = PaySignUtil.checkNotifySign(request, key);
            if(flag){
                if("1".equals(code)){
                    PayDetails payDetails=payDetailsDao.selectByOrderId(outTradeNo);
                    if(payDetails.getState()==0) {
                        String format = sdf.format(new Date());
                        payDetailsDao.updateState(payDetails.getId(),1,format,null);
                        if(payDetails.getType()==1){
                            Orders orders = ordersService.selectOrderByOrdersNo(payDetails.getOrderId());
                            orders.setPayWay(4);
                            orders.setStatus(1);
                            orders.setPayTime(DateUtils.format(new Date()));
                            ordersService.updateById(orders);
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
                            userMoneyDetails.setContent("支付宝充值点券");
                            userMoneyDetails.setTitle("支付宝充值点券："+payClassify.getMoney()+",赠送："+payClassify.getGiveMoney());
                            userMoneyDetails.setType(1);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
                            userMoneyDetailsService.save(userMoneyDetails);
                            Orders orders = ordersService.selectOrderByOrdersNo(payDetails.getOrderId());
                            orders.setPayTime(DateUtils.format(new Date()));
                            orders.setPayWay(4);
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
                }
                return "SUCCESS";
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("云购os支付报错！"+e.getMessage());
        }
        return null;
    }

    @Login
    @ApiOperation("支付宝支付订单")
    @RequestMapping(value = "/payOrder", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payOrder(Long orderId,Integer classify) {
        //通知页面地址
        CommonInfo one = commonInfoService.findOne(18);
        String returnUrl = one.getValue() + "/#/pages/task/recharge";
        CommonInfo one3 = commonInfoService.findOne(12);
        String name = one3.getValue();
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        log.info("回调地址:" + url);
        Orders orders = ordersDao.selectById(orderId);
        PayDetails payDetails = payDetailsDao.selectByOrderId(orders.getOrdersNo());
        if(payDetails==null){
            payDetails=new PayDetails();
            payDetails.setState(0);
            payDetails.setCreateTime(sdf.format(new Date()));
            payDetails.setOrderId(orders.getOrdersNo());
            payDetails.setUserId(orders.getUserId());
            payDetails.setMoney(orders.getPayMoney().doubleValue());
            payDetails.setType(1);
            if(classify==1){
                payDetails.setClassify(4);
            }else{
                payDetails.setClassify(5);
            }
            payDetailsDao.insert(payDetails);
        }
        if (classify == 1) {
            return payApp(name, orders.getOrdersNo(), orders.getPayMoney().doubleValue());
        } else {
            return payH5(name, orders.getOrdersNo(), orders.getPayMoney().doubleValue(), returnUrl);
        }
    }

    @Login
    @ApiOperation("支付宝支付订单")
    @RequestMapping(value = "/payMoney", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payMoney(Long payClassifyId, Integer classify,@RequestAttribute Long userId) {
        //通知页面地址
        CommonInfo one = commonInfoService.findOne(18);
        String returnUrl = one.getValue() + "/#/pages/task/recharge";
        CommonInfo one3 = commonInfoService.findOne(12);
        String name = one3.getValue();
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        log.info("回调地址:" + url);
        String generalOrder = getGeneralOrder();
        PayClassify payClassify = payClassifyService.getById(payClassifyId);
        PayDetails payDetails=new PayDetails();
        payDetails.setState(0);
        payDetails.setCreateTime(sdf.format(new Date()));
        payDetails.setOrderId(generalOrder);
        payDetails.setUserId(userId);
        payDetails.setMoney(payClassify.getPrice().doubleValue());
        if(classify==1){
            payDetails.setClassify(4);
        }else{
            payDetails.setClassify(5);
        }
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
        if (classify == 1) {
            return payApp(name, generalOrder, payClassify.getPrice().doubleValue());
        } else {
            return payH5(name, generalOrder, payClassify.getPrice().doubleValue(), returnUrl);
        }
    }


    public String getGeneralOrder() {
        Date date = new Date();
        String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format + newString;
    }


    /**
     * 说明： 支付宝订单退款
     *
     * @return 公共返回参数 code,msg,   响应参数实例: https://docs.open.alipay.com/api_1/alipay.trade.refund
     */
    public String alipayRefund(Orders orders) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
        AlipayTradeRefundRequest alipay_request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(orders.getOrdersNo());//订单编号
        model.setTradeNo(orders.getTradeNo());//支付宝订单交易号
        model.setRefundAmount(orders.getPayMoney().toString());//退款点券 不得大于订单点券
        model.setRefundReason(orders.getRefundContent());//退款说明
        model.setOutRequestNo(orders.getOrdersNo());//标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
        alipay_request.setBizModel(model);
        try {
            AlipayTradeRefundResponse alipay_response = alipayClient.execute(alipay_request);
            String alipayRefundStr = alipay_response.getBody();
            log.info(alipayRefundStr);
            return alipayRefundStr;
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }



    public Result payApp(String name, String generalOrder, Double money) {
        CommonInfo one = commonInfoService.findOne(18);
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        String result = "";
        CommonInfo payWay = commonInfoService.findOne(201);
        try {
            if ("1".equals(payWay.getValue())) {
                //构造client
                CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
                //设置网关地址
                certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
                //设置应用Id
                certAlipayRequest.setAppId(commonInfoService.findOne(63).getValue());
                //设置应用私钥
                certAlipayRequest.setPrivateKey(commonInfoService.findOne(65).getValue());
                //设置请求格式，固定值json
                certAlipayRequest.setFormat("json");
                //设置字符集
                certAlipayRequest.setCharset(AliPayConstants.CHARSET);
                //设置签名类型
                certAlipayRequest.setSignType(AliPayConstants.SIGNTYPE);
                CommonInfo urls = commonInfoService.findOne(200);
                certAlipayRequest.setCertPath(urls.getValue() + "/appCertPublicKey.crt"); //应用公钥证书路径（app_cert_path 文件绝对路径）
                certAlipayRequest.setAlipayPublicCertPath(urls.getValue() + "/alipayCertPublicKey_RSA2.crt"); //支付宝公钥证书文件路径（alipay_cert_path 文件绝对路径）
                certAlipayRequest.setRootCertPath(urls.getValue() + "/alipayRootCert.crt");  //支付宝CA根证书文件路径（alipay_root_cert_path 文件绝对路径）
                //构造client
                AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);

                //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
                AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
                //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
                AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
                model.setBody(name);
                model.setSubject(name);
                model.setOutTradeNo(generalOrder);
                model.setTimeoutExpress("30m");
                model.setTotalAmount(money +"");
                model.setProductCode("QUICK_MSECURITY_PAY");
                request.setBizModel(model);
                request.setNotifyUrl(url);
                //这里和普通的接口调用不同，使用的是sdkExecute
                AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
                if (response.isSuccess()) {
                    result = response.getBody();
                } else {
                    return Result.error("获取订单失败！");
                }
                return Result.success().put("data", result);
            } else if("2".equals(payWay.getValue())){
                //实例化客户端
                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
                //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
                AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
                //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
                AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
                model.setBody(name);
                model.setSubject(name);
                model.setOutTradeNo(generalOrder);
                model.setTimeoutExpress("30m");
                model.setTotalAmount(String.valueOf(money));
                model.setProductCode("QUICK_MSECURITY_PAY");
                request.setBizModel(model);
                request.setNotifyUrl(url);
                AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
                if (response.isSuccess()) {
                    result = response.getBody();
                } else {
                    return Result.error("获取订单失败！");
                }
                return Result.success().put("data", result);
            }else{
                url=one.getValue()+"/sqx_fast/app/aliPay/notifyAppYunOS";
                log.info("回调地址:"+url);
                String mchId = commonInfoService.findOne(168).getValue();
                String key = commonInfoService.findOne(169).getValue();
                result = AliPay.appPay(generalOrder, String.valueOf(money), mchId, name ,null, url, null, null, null, null,key);
                return Result.success().put("data", result);
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return Result.error(-100, "获取订单失败！");
    }

    public Result payH5(String name, String generalOrder, Double money, String returnUrl) {
        CommonInfo payWay = commonInfoService.findOne(201);
        CommonInfo one = commonInfoService.findOne(18);
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        try {
            if ("1".equals(payWay.getValue())) {
                //构造client
                CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
                //设置网关地址
                certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
                //设置应用Id
                certAlipayRequest.setAppId(commonInfoService.findOne(63).getValue());
                //设置应用私钥
                certAlipayRequest.setPrivateKey(commonInfoService.findOne(65).getValue());
                //设置请求格式，固定值json
                certAlipayRequest.setFormat("json");
                //设置字符集
                certAlipayRequest.setCharset(AliPayConstants.CHARSET);
                //设置签名类型
                certAlipayRequest.setSignType(AliPayConstants.SIGNTYPE);
                CommonInfo urls = commonInfoService.findOne(200);
                certAlipayRequest.setCertPath(urls.getValue() + "/appCertPublicKey.crt"); //应用公钥证书路径（app_cert_path 文件绝对路径）
                certAlipayRequest.setAlipayPublicCertPath(urls.getValue() + "/alipayCertPublicKey_RSA2.crt"); //支付宝公钥证书文件路径（alipay_cert_path 文件绝对路径）
                certAlipayRequest.setRootCertPath(urls.getValue() + "/alipayRootCert.crt");  //支付宝CA根证书文件路径（alipay_root_cert_path 文件绝对路径）
                //构造client
                AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
                AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
                JSONObject order = new JSONObject();
                order.put("out_trade_no", generalOrder); //订单号
                order.put("subject", name); //商品标题
                order.put("product_code", "QUICK_WAP_WAY");
                order.put("body", name);//商品名称
                order.put("total_amount", money + ""); //点券
                alipayRequest.setBizContent(order.toString());
                alipayRequest.setNotifyUrl(url); //在公共参数中设置回跳和通知地址
                alipayRequest.setReturnUrl(returnUrl); //线上通知页面地址
                String result = alipayClient.pageExecute(alipayRequest).getBody();
                return Result.success().put("data", result);
            } else if ("2".equals(payWay.getValue())){
                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
                AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
                JSONObject order = new JSONObject();
                order.put("out_trade_no", generalOrder); //订单号
                order.put("subject", name); //商品标题
                order.put("product_code", "QUICK_WAP_WAY");
                order.put("body", name);//商品名称
                order.put("total_amount", money); //点券
                alipayRequest.setBizContent(order.toString());
                //在公共参数中设置回跳和通知地址
                alipayRequest.setNotifyUrl(url);
                //通知页面地址
                alipayRequest.setReturnUrl(returnUrl);
                String form = alipayClient.pageExecute(alipayRequest).getBody();
                return Result.success().put("data", form);
            }else{
                url=one.getValue()+"/sqx_fast/app/aliPay/notifyAppYunOS";
                log.info("回调地址:"+url);
                String mchId = commonInfoService.findOne(168).getValue();
                String key = commonInfoService.findOne(169).getValue();
                AliPayH5Biz aliPayH5Biz = AliPay.h5Pay(generalOrder, String.valueOf(money), mchId, name, null, url, returnUrl, null, null, null,null,key);
                return Result.success().put("data", aliPayH5Biz.getForm());
            }
        } catch (AlipayApiException e) {
            log.error("CreatPayOrderForH5", e);
        }
        return Result.error("获取订单信息错误！");
    }



}
