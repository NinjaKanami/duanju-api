package com.sqx.modules.pay.controller;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.invite.dao.InviteMoneyDao;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.pay.config.AliPayConstants;
import com.sqx.modules.pay.entity.AliPayWithdrawModel;
import com.sqx.modules.pay.entity.CashOut;
import com.sqx.modules.pay.service.CashOutService;
import com.sqx.modules.pay.service.PayDetailsService;
import com.sqx.modules.utils.AmountCalUtils;
import com.sqx.modules.utils.excel.ExcelData;
import com.sqx.modules.utils.excel.ExportExcelUtils;
import com.sqx.modules.utils.wx.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author fang
 * @date 2020/5/15
 */
@Slf4j
@RestController
@Api(value = "管理平台", tags = {"管理平台"})
@RequestMapping(value = "/cash")
public class CashController {

    /** 充值记录 */
    @Autowired
    private PayDetailsService payDetailsService;
    /** 提现记录 */
    @Autowired
    private CashOutService cashOutService;
    /** app用户 */
    @Autowired
    private UserService userService;
    /** 通用配置 */
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private InviteMoneyDao inviteMoneyDao;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private UserMoneyService userMoneyService;
    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);

    @RequestMapping(value = "/sendMsgByUserId", method = RequestMethod.GET)
    @ApiOperation("管理平台主动推送消息(指定用户)")
    @ResponseBody
    public Result sendMsgByUserId(String title,String content,Long userId){
        UserEntity user = userService.queryByUserId(userId);
        send(user,title,content);
        return Result.success();
    }


    @RequestMapping(value = "/sendMsg", method = RequestMethod.GET)
    @ApiOperation("管理平台主动推送消息")
    @ResponseBody
    public Result sendMsg(String title,String content,String phone,Integer flag){
        if(flag==1){
            //根据手机号推送
            UserEntity userByPhone = userService.queryByPhone(phone);
            if(null==userByPhone){
                return Result.error(-100,"手机号不存在！");
            }
            send(userByPhone,title,content);
        }else{
            //所有人推送
            List<UserEntity> userInfos = userService.list();
            //用户数量较大  使用多线程推送  根据用户数量进行拆分  同时按照3个线程进行推送
            int count = userInfos.size() / 3;
            new Thread(() -> {
                for(int i=0 ;i<count;i++){
                    send(userInfos.get(i),title,content);
                }
            }).start();
            new Thread(() -> {
                for(int i=count ;i<count*2;i++){
                    send(userInfos.get(i),title,content);
                }
            }).start();
            new Thread(() -> {
                for(int i=count*2 ;i<userInfos.size();i++){
                    send(userInfos.get(i),title,content);
                }
            }).start();
           /* for(UserInfo userByPhone:userInfos){

            }*/
        }
        return Result.success();
    }

    private void send(UserEntity userByPhone,String title,String content){
        if (userByPhone.getClientid() != null) {
            userService.pushToSingle(title, content, userByPhone.getClientid());
        }
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setContent(content);
        messageInfo.setTitle(title);
        messageInfo.setState(String.valueOf(5));
        messageInfo.setIsSee("0");
        messageInfo.setUserName(userByPhone.getUserName());
        messageInfo.setUserId(String.valueOf(userByPhone.getUserId()));
        messageService.saveBody(messageInfo);
    }


    @RequestMapping(value = "/selectCashOut", method = RequestMethod.GET)
    @ApiOperation("获取最新的提现信息")
    @ResponseBody
    public Result selectCashOut(){
        return Result.success().put("data",cashOutService.selectCashOutLimit3());
    }

    @RequestMapping(value = "/selectSumPay", method = RequestMethod.GET)
    @ApiOperation("查询用户充值点券")
    @ResponseBody
    public Result selectSumPay(String createTime,String endTime,Long userId){
        return Result.success().put("data",payDetailsService.selectSumPay(createTime,endTime,userId));
    }

    @RequestMapping(value = "/selectUserRecharge", method = RequestMethod.GET)
    @ApiOperation("查询所有用户充值信息列表")
    @ResponseBody
    public Result selectUserRecharge(int page, int limit, String startTime, String endTime, Integer state,String userName,String orderId) {
        return Result.success().put("data", payDetailsService.selectPayDetails(page, limit, startTime, endTime, null, state,userName,orderId));
    }

    @RequestMapping(value = "/selectUserRechargeByUserId", method = RequestMethod.GET)
    @ApiOperation("查询某个用户充值信息列表")
    @ResponseBody
    public Result selectUserRechargeByUserId(int page, int limit, String startTime, String endTime, Long userId, Integer state) {
        return Result.success().put("data", payDetailsService.selectPayDetails(page, limit, startTime, endTime, userId, state, null, null));
    }

    @RequestMapping(value = "/selectUserRechargeByUserIdApp", method = RequestMethod.GET)
    @ApiOperation("查询某个用户充值信息列表")
    @ResponseBody
    public Result selectUserRechargeByUserIdApp(int page, int limit, String startTime, String endTime, Long userId) {
        return Result.success().put("data", payDetailsService.selectPayDetails(page, limit, startTime, endTime, userId, 1, null, null));
    }

    @RequestMapping(value = "/selectPayDetails", method = RequestMethod.GET)
    @ApiOperation("查询提现记录列表")
    @ResponseBody
    public Result selectHelpProfit(int page,int limit,CashOut cashOut){
        return Result.success().put("data",cashOutService.selectCashOutList(page,limit,cashOut));
    }

    @RequestMapping(value = "/excelPayDetails", method = RequestMethod.GET)
    @ApiOperation("查询提现记录列表")
    @ResponseBody
    public void excelPayDetails(CashOut cashOut,
                                HttpServletResponse response)throws Exception {
        ExcelData data = cashOutService.excelPayDetails(cashOut);
        ExportExcelUtils.exportExcel(response,"财务列表.xlsx",data);
    }


    @ApiOperation("财务提现统计")
    @GetMapping("/statisticsCashMoney")
    public Result statisticsMoney(String time,Integer flag){
        Double sumMoney = cashOutService.sumMoney(time, flag);
        Integer countMoney = cashOutService.countMoney(time, flag);
        Integer stayMoney = cashOutService.stayMoney(time, flag);
        Map<String,Object> map=new HashMap<>();
        map.put("sumMoney",sumMoney==null?0.00:sumMoney);
        map.put("countMoney",countMoney==null?0:countMoney);
        map.put("stayMoney",stayMoney==null?0:stayMoney);
        return Result.success().put("data",map);
    }


    @ApiOperation("充值统计")
    @GetMapping("/payMember")
    public Result payMember(String time,Integer flag,Integer payClassify){
        Double sumMoney = payDetailsService.selectSumPayByClassify(time, flag, null,payClassify);
        //1app微信 2微信公众号 3微信小程序 4支付宝app 5支付宝h5 6抖音 7苹果 8快手
        Double weiXinAppMoney = payDetailsService.selectSumPayByClassify(time, flag, 1,payClassify);
        Double weiXinGZHMoney = payDetailsService.selectSumPayByClassify(time, flag, 2,payClassify);
        Double weiXinXCXMoney = payDetailsService.selectSumPayByClassify(time, flag, 3,payClassify);
        Double zhiFuBaoAppMoney = payDetailsService.selectSumPayByClassify(time, flag, 4,payClassify);
        Double zhiFuBaoH5Money = payDetailsService.selectSumPayByClassify(time, flag, 5,payClassify);
        Double dyMoney = payDetailsService.selectSumPayByClassify(time, flag, 6,payClassify);
        Double iosMoney = payDetailsService.selectSumPayByClassify(time, flag, 7,payClassify);
        Double ksMoney = payDetailsService.selectSumPayByClassify(time, flag, 8,payClassify);
        Map<String,Object> map=new HashMap<>();
        map.put("sumMoney",sumMoney==null?0.00:sumMoney);
        map.put("weiXinAppMoney",weiXinAppMoney==null?0.00:weiXinAppMoney);
        map.put("weiXinGZHMoney",weiXinGZHMoney==null?0.00:weiXinGZHMoney);
        map.put("weiXinXCXMoney",weiXinXCXMoney==null?0.00:weiXinXCXMoney);
        map.put("zhiFuBaoAppMoney",zhiFuBaoAppMoney==null?0.00:zhiFuBaoAppMoney);
        map.put("zhiFuBaoH5Money",zhiFuBaoH5Money==null?0.00:zhiFuBaoH5Money);
        map.put("dyMoney",dyMoney==null?0.00:dyMoney);
        map.put("iosMoney",iosMoney==null?0.00:iosMoney);
        map.put("ksMoney",ksMoney==null?0.00:ksMoney);
        return Result.success().put("data",map);
    }


    @ApiOperation("收入统计")
    @GetMapping("/statisticsIncomeMoney")
    public Result statisticsIncomeMoney(String time,Integer flag){
        //cdk兑换
        Double cdkMoney = ordersService.statisticsIncomeMoney(time, flag, 5);
        //短剧消费
        Double courseMoney = ordersService.statisticsIncomeMoney(time, flag, 1);
        //会员开通
        Double vipMoney = ordersService.statisticsIncomeMoney(time, flag, 2);
        //平台收入
        Double pingMoney = ordersService.statisticsPingMoney(time, flag, null);
        //用户收入
        Double userMoney = userMoneyDetailsService.selectSumMoneyByTime(flag,time );
        Map<String,Object> map=new HashMap<>();
        map.put("cdkMoney",cdkMoney==null?0.00:cdkMoney);
        map.put("courseMoney",courseMoney==null?0.00:courseMoney);
        map.put("vipMoney",vipMoney==null?0.00:vipMoney);
        map.put("pingMoney",pingMoney==null?0.00:pingMoney);
        map.put("userMoney",userMoney==null?0.00:userMoney);
        return Result.success().put("data",map);
    }


    @RequestMapping(value = "/alipay/{cashId}", method = RequestMethod.POST)
    @ApiOperation("管理平台确认提现")
    @ResponseBody
    public Result alipayPay(@PathVariable Long cashId) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            CashOut one = cashOutService.selectById(cashId);
            if(one.getClassify()==null || one.getClassify()==1){
                return cashAliPay(one);
            }else{
                return cashWxPay(one);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("转账异常"+e.getMessage());
        }finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙，请稍后再试！");
    }


    public Result cashAliPay(CashOut one){
        //提现订单
        log.error("进来了！！！");
        //订单记录不为空
        if (one == null) {
            return Result.error("提现记录不存在！");
        }
        //订单状态不是待转帐
        if (one.getState()!=0) {
            return Result.error(9999, one.getZhifubaoName() + "转账失败！原因是用户已转账");
        }
        //订单编号为空
        if(StringUtils.isEmpty(one.getOrderNumber())){
            one.setOrderNumber(String.valueOf(System.currentTimeMillis()));
        }
        //配置文件对象
        CommonInfo commonInfo = commonInfoService.findOne(98);

        CommonInfo name = commonInfoService.findOne(12);
        if (commonInfo.getValue() != null && commonInfo.getValue().equals("1")) {

            try {
                CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
                certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");  //gateway:支付宝网关（固定）https://openapi.alipay.com/gateway.do
                certAlipayRequest.setAppId(commonInfoService.findOne(63).getValue());  //APPID 即创建应用后生成,详情见创建应用并获取 APPID
                certAlipayRequest.setPrivateKey(commonInfoService.findOne(65).getValue());  //开发者应用私钥，由开发者自己生成
                certAlipayRequest.setFormat("json");  //参数返回格式，只支持 json 格式
                certAlipayRequest.setCharset(AliPayConstants.CHARSET);  //请求和签名使用的字符编码格式，支持 GBK和 UTF-8
                certAlipayRequest.setSignType(AliPayConstants.SIGNTYPE);  //商户生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐商家使用 RSA2。
                CommonInfo url = commonInfoService.findOne(200);
                certAlipayRequest.setCertPath(url.getValue()+"/appCertPublicKey.crt"); //应用公钥证书路径（app_cert_path 文件绝对路径）
                certAlipayRequest.setAlipayPublicCertPath(url.getValue()+"/alipayCertPublicKey_RSA2.crt"); //支付宝公钥证书文件路径（alipay_cert_path 文件绝对路径）
                certAlipayRequest.setRootCertPath(url.getValue()+"/alipayRootCert.crt");  //支付宝CA根证书文件路径（alipay_root_cert_path 文件绝对路径）
                AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
                AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
                request.setBizContent("{" +
                        "\"out_biz_no\":\""+one.getOrderNumber()+"\"," +             //订单编号
                        "\"trans_amount\":"+new BigDecimal(one.getMoney())+"," +     //转账金额
                        "\"product_code\":\"TRANS_ACCOUNT_NO_PWD\"," +
                        "\"biz_scene\":\"DIRECT_TRANSFER\"," +
                        "\"order_title\":\""+name.getValue() + "佣金结算"+"\"," +
                        "\"payee_info\":{" +
                        "\"identity\":\""+one.getZhifubao()+"\"," +                  //支付宝账号
                        "\"identity_type\":\"ALIPAY_LOGON_ID\"," +
                        "\"name\":\""+one.getZhifubaoName()+"\"," +                  //支付宝名称
                        "}," +
                        "\"remark\":\""+name.getValue() + "佣金结算"+"\"" +
                        "}");
                AlipayFundTransUniTransferResponse response = null;
                response = alipayClient.certificateExecute(request);
                log.error("支付宝转账返回值："+response.getBody());
                //如果转账成功
                if (AliPayConstants.SUCCESS_CODE.equalsIgnoreCase(response.getCode())) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //修改状态为转账成功
                    one.setState(1);
                    //设置转账时间
                    one.setOutAt(sdf.format(new Date()));
                    //更新转账订单
                    cashOutService.update(one);
                    //查询用户
                    UserEntity userInfo=userService.queryByUserId(one.getUserId());
                    if(userInfo!=null && StringUtils.isNotEmpty(userInfo.getWxId())){
                        cashOutService.cashOutSuccess(userInfo.getOpenId(), one.getOutAt(), one.getMoney(), one.getZhifubao(),  commonInfoService.findOne(18).getValue());
                    }
                    return Result.success(one.getZhifubaoName() + "转账成功");
                } else {
                    return Result.error(9999, one.getZhifubaoName() + "转账失败！" + response.getSubMsg());
                }
            } catch (AlipayApiException e) {
                log.error("金额提现异常原因:" + e.getMessage());
                e.printStackTrace();
                return Result.error(9999, one.getZhifubaoName() + "转账失败！" + e.getMessage());
            }
        }else if (commonInfo.getValue() != null && commonInfo.getValue().equals("2")) {
            AlipayClient alipayClient = new DefaultAlipayClient(AliPayConstants.REQUEST_URL,
                    commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), AliPayConstants.FORMAT,
                    AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), AliPayConstants.SIGNTYPE);
            val aliPayWithdrawModel = AliPayWithdrawModel.builder()
                    .out_biz_no(one.getOrderNumber())
                    .amount(new BigDecimal(one.getMoney()))
                    .payee_account(one.getZhifubao())
                    .payee_real_name(one.getZhifubaoName())
                    .payee_type(AliPayConstants.PAY_TYPE)
                    .remark(name.getValue())
                    .build();
            String json = JSON.toJSONString(aliPayWithdrawModel);
            //实例化连接对象
            AlipayFundTransToaccountTransferRequest withdrawRequest = new AlipayFundTransToaccountTransferRequest();
            withdrawRequest.setBizContent(json);
            try {
                AlipayFundTransToaccountTransferResponse response = alipayClient.execute(withdrawRequest);
                if (AliPayConstants.SUCCESS_CODE.equalsIgnoreCase(response.getCode())) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //修改状态为转账成功
                    one.setState(1);
                    //设置转账时间
                    one.setOutAt(sdf.format(new Date()));
                    //更新转账订单
                    cashOutService.update(one);
                    //查询用户
                    UserEntity userInfo=userService.queryByUserId(one.getUserId());
                    if(userInfo!=null && StringUtils.isNotEmpty(userInfo.getWxId())){
                        cashOutService.cashOutSuccess(userInfo.getOpenId(), one.getOutAt(), one.getMoney(), one.getZhifubao(),  commonInfoService.findOne(18).getValue());
                    }
                    return Result.success(one.getZhifubaoName() + "转账成功");
                } else {
                    return Result.error(9999, one.getZhifubaoName() + "转账失败！" + response.getSubMsg());
                }
            } catch (AlipayApiException e) {
                log.error("金额提现异常原因:" + e.getMessage());
                e.printStackTrace();
                return Result.error(9999, one.getZhifubaoName() + "转账失败！" + e.getMessage());

            }
        }else{
            //人工转账后改变状态的
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            one.setState(1);
            one.setOutAt(sdf.format(now));
            cashOutService.update(one);
            UserEntity userInfo=userService.queryByUserId(one.getUserId());
            if(userInfo!=null && StringUtils.isNotEmpty(userInfo.getWxId())){
                cashOutService.cashOutSuccess(userInfo.getOpenId(), one.getOutAt(), one.getMoney(), one.getZhifubao(),  commonInfoService.findOne(18).getValue());
            }
            return Result.success(one.getZhifubaoName() + "转账成功");
        }
    }


    private Result cashWxPay(CashOut one){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        UserEntity userEntity = userService.getById(one.getUserId());
        if(StringUtils.isEmpty(one.getOrderNumber())){
            one.setOrderNumber(String.valueOf(System.currentTimeMillis()));
        }
        String value = commonInfoService.findOne(868).getValue();
        if("1".equals(value)){
            WxPay wxPay=new WxPay();
            CommonInfo mchId = commonInfoService.findOne(76);
            CommonInfo key = commonInfoService.findOne(75);
            //小程序或公众号appid
            if(one.getClassify()==2){
                CommonInfo mchAppId = commonInfoService.findOne(45);
                wxPay.setMch_appid(mchAppId.getValue());
                wxPay.setOpenid(userEntity.getOpenId());
            }else if(one.getClassify()==3){
                CommonInfo mchAppId = commonInfoService.findOne(5);
                wxPay.setMch_appid(mchAppId.getValue());
                wxPay.setOpenid(userEntity.getWxId());
            } else{
                CommonInfo mchAppId = commonInfoService.findOne(74);
                wxPay.setMch_appid(mchAppId.getValue());
                wxPay.setOpenid(userEntity.getWxOpenId());
            }
            //商户号id
            wxPay.setMchid(mchId.getValue());
            //随机字符
            wxPay.setNonce_str(WxPayUtils.generateNonceStr());
            //商户订单号  需保持唯一
            wxPay.setPartner_trade_no(one.getOrderNumber());
            //用户openId

            //NO_CHECK：不校验真实姓名
            //FORCE_CHECK：强校验真实姓名
            wxPay.setCheck_name("NO_CHECK");
            //转账金额 微信为分
            double v = Double.parseDouble(one.getMoney());
            Double mul = AmountCalUtils.mul(v, 100);
            Integer amount=mul.intValue();
            wxPay.setAmount(amount);
            //备注
            CommonInfo one1 = commonInfoService.findOne(12);
            wxPay.setDesc(one1.getValue()+"提现金额到账");
            wxPay.setSign(WxPayUtils.createSign(BeanUtil.beanToMap(wxPay), key.getValue()));


            String xmlParam = XmlUtil.beanToXml(wxPay,WxPay.class);
            WeChatPayRequest weChatPayRequest = new WeChatPayRequest();

            String returnXml = null;
            try {
                CommonInfo zsUlr = commonInfoService.findOne(201);
                returnXml = weChatPayRequest.request(zsUlr.getValue(),WxPayUtils.WX_COM_DO_TRANS_URL,xmlParam,true,mchId.getValue());
                WxResult wxResult = XmlUtil.xmlToBean(returnXml, WxResult.class);
                if(wxResult.getReturn_code().equals("SUCCESS")){
                    if(one.getOrderNumber().equals(wxResult.getPartner_trade_no())){
                        //修改状态为转账成功
                        one.setState(1);
                        //设置转账时间
                        one.setOutAt(sdf.format(new Date()));
                        //更新转账订单
                        cashOutService.update(one);
                        MessageInfo messageInfo = new MessageInfo();
                        messageInfo.setContent(one.getMoney()+"提现已到账");
                        messageInfo.setTitle("提现到账");
                        messageInfo.setState(String.valueOf(5));
                        messageInfo.setUserName(userEntity.getUserName());
                        messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
                        messageInfo.setCreateAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        messageService.saveBody(messageInfo);
                        return Result.success(one.getZhifubaoName() + "转账成功");
                    }else{
                        return Result.error("转账失败！原因："+wxResult.getErr_code_des());
                    }
                }else{
                    return Result.error("转账失败！状态码："+wxResult.getErr_code_des());
                }
            } catch (Exception e) {
                log.error("转账异常："+e.getMessage(),e);
                log.error("postWxTransfers 微信处理异常 ==>{}", ExceptionUtils.getStackTrace(e));
            }
        }else{
            Date now = new Date();
            one.setState(1);
            one.setOutAt(sdf.format(now));
            cashOutService.update(one);
            return Result.success(one.getZhifubaoName() + "转账成功");
        }
        return Result.error("转账失败！");
    }



    @RequestMapping(value = "/refund/{cashId}/{content}", method = RequestMethod.POST)
    @ApiOperation("管理平台退款")
    @ResponseBody
    public Result refund(@PathVariable("cashId") Long cashId,@PathVariable("content") String content) {
        CashOut one = cashOutService.selectById(cashId);
        if (one == null) {
            return Result.error("提现信息不存在");
        }
        //将状态为待提现的退款
        if(one.getState()!=0){
            return Result.error(-100,"状态错误，已经转账或退款!");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        //修改提现订单状态
        one.setState(-1);
        one.setRefund(content);
        one.setOutAt(sdf.format(now));
        cashOutService.update(one);
        Long userId = one.getUserId();

        double v = Double.parseDouble(one.getMoney());
        if(one.getRate()!=null && one.getRate()>0.00){
            v= AmountCalUtils.add(new BigDecimal(v), BigDecimal.valueOf(one.getRate())).doubleValue();
        }
        //将点券退还
        if(one.getSysUserId()!=null){
            UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setSysUserId(one.getSysUserId());
            userMoneyDetails.setTitle("[退款提醒]提现失败:"+one.getMoney()+",退还手续费:"+one.getRate()+",总退还:"+v);
            userMoneyDetails.setContent(content);
            userMoneyDetails.setType(1);
            userMoneyDetails.setClassify(2);
            userMoneyDetails.setMoney(new BigDecimal(v));
            userMoneyDetails.setCreateTime(sdf.format(now));
            userMoneyDetailsService.save(userMoneyDetails);
            userMoneyService.updateSysMoney(1,userId,v);
        }else{
            UserEntity userInfo = userService.queryByUserId(userId);
            UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setUserId(userInfo.getUserId());
            userMoneyDetails.setTitle("[退款提醒]提现失败:"+one.getMoney()+",退还手续费:"+one.getRate()+",总退还:"+v);
            userMoneyDetails.setContent(content);
            userMoneyDetails.setType(1);
            userMoneyDetails.setClassify(2);
            userMoneyDetails.setMoney(new BigDecimal(v));
            userMoneyDetails.setCreateTime(sdf.format(now));
            userMoneyDetailsService.save(userMoneyDetails);
            inviteMoneyDao.updateInviteMoneyCashOut(1,v,userId);
            if (userInfo.getOpenId() != null) {
                //推送提现通知消息
                cashOutService.refundSuccess(userInfo, one.getOutAt(), one.getMoney(),  commonInfoService.findOne(18).getValue(),content);
            }
        }
        return Result.success();
    }

    @GetMapping(value = "/cashMoney")
    @ApiOperation("发起提现")
    public Result cashMoney(Long userId,Double money)
    {
        return cashOutService.sysCashMoney(userId,money);
    }



}
