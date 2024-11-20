package com.sqx.modules.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.course.service.CourseService;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayClassify;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.KsService;
import com.sqx.modules.pay.service.PayClassifyService;
import com.sqx.modules.utils.AmountCalUtils;
import com.sqx.modules.utils.HttpClientUtil;
import com.sqx.modules.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author fang
 * @date 2020/2/26
 */
@Service
@Slf4j
public class KsServiceImpl implements KsService {


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
    private CourseService courseService;
    @Autowired
    private PayClassifyService payClassifyService;

    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");;

    @Override
    public Result payOrder(Long orderId) throws Exception {
        Orders bean=ordersService.selectOrderById(orderId);
        UserEntity userEntity = userService.selectUserById(bean.getUserId());
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
            payDetails.setClassify(6);
            payDetails.setType(1);
            payDetailsDao.insert(payDetails);
        }
        return pay(bean.getPayMoney().doubleValue(), bean.getOrdersNo(),userEntity.getKsOpenId());
    }

    @Override
    public Result payMoney(Long payClassifyId,Long userId) throws Exception {
        PayClassify payClassify = payClassifyService.getById(payClassifyId);
        BigDecimal money=payClassify.getPrice();
        UserEntity userEntity = userService.selectUserById(userId);
        String generalOrder = getGeneralOrder();
        PayDetails payDetails=new PayDetails();
        payDetails.setState(0);
        payDetails.setCreateTime(sdf.format(new Date()));
        payDetails.setOrderId(generalOrder);
        payDetails.setUserId(userId);
        payDetails.setMoney(money.doubleValue());
        payDetails.setClassify(6);
        payDetails.setType(2);
        payDetailsDao.insert(payDetails);
        return pay(money.doubleValue(), generalOrder,userEntity.getKsOpenId());
    }


    /**
     * 快手支付订单生成
     * @param moneys  支付点券 带小数点
     * @param outTradeNo 单号
     * @return
     * @throws Exception
     */
    private Result pay(Double moneys,String outTradeNo,String openId) throws Exception {
        CommonInfo oneu = commonInfoService.findOne(18);
        String url;
        url=oneu.getValue()+"/sqx_fast/app/ksPay/notify";
        CommonInfo one = commonInfoService.findOne(12);
        log.info("回调地址："+url);
        Double mul = AmountCalUtils.mul(moneys, 100);
        Integer money =mul.intValue();
        String appId = commonInfoService.findOne(828).getValue();
        // 示例参数
        Map<String, Object> params = new HashMap<>();
        params.put("app_id",appId);
        params.put("out_order_no", outTradeNo); //商户订单号
        params.put("open_id", openId);
        params.put("total_amount", money); //点券。分
        params.put("notify_url", url);  //回调接口
        params.put("subject", one.getValue()); //主题
        params.put("detail", one.getValue()); //商品详情
        params.put("type", commonInfoService.findOne(831).getValue());
        params.put("expire_time", 5*60); //过期时间
        String sign = calcSign(params); //签名
        params.put("sign",sign);
        //发起post请求
        String accessToken = getAccessToken();
        String postJson = HttpClientUtil.doPostJson("https://open.kuaishou.com/openapi/mp/developer/epay/create_order?app_id="+appId+"&access_token="+accessToken,
                JSONObject.toJSONString(params));

        if (StringUtils.isNotEmpty(postJson)) {
            JSONObject jsonObject = JSONObject.parseObject(postJson);
            if ("1".equals(jsonObject.getString("result")))
            {
                return Result.success().put("data",jsonObject.getJSONObject("order_info"));
            } else {
                log.error("快手支付生成失败" + postJson);
                throw new Exception("快手支付生成失败" + postJson);
            }
        }
        return null;
    }




    @Override
    public String payBack(String kwaisign,JSONObject jsonObject) {
        log.error("快手返回参数！"+kwaisign+"----"+jsonObject);
        if(StringUtils.isEmpty(kwaisign)){
            return null;
        }
        String message_id = jsonObject.getString("message_id");
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            String biz_type = jsonObject.getString("biz_type");
            if ("PAYMENT".equalsIgnoreCase(biz_type)) {
                String s = jsonObject.toJSONString() + commonInfoService.findOne(829).getValue();
                String s1 = MD5Util.encodeByMD5(s);
                /*if(!kwaisign.equals(s)){
                    log.error("签名不一致！"+s1+"----"+kwaisign);
                    return null;
                }*/
                String orderNo = data.getString("out_order_no");
                log.error("订单号！！"+orderNo);
                PayDetails payDetails = payDetailsDao.selectByOrderId(orderNo);
                if(payDetails.getState()==0){
                    String format = sdf.format(new Date());
                    payDetailsDao.updateState(payDetails.getId(),1,format,"");
                    if(payDetails.getType()==1){
                        Orders orders = ordersService.selectOrderByOrdersNo(payDetails.getOrderId());
                        orders.setPayWay(7);
                        orders.setStatus(1);
                        orders.setPayTime(DateUtils.format(new Date()));
                        ordersService.updateById(orders);
                        UserEntity user = userService.selectUserById(orders.getUserId());
                        Map map = inviteService.updateInvite(user, orders.getOrdersNo(),orders.getOrdersType(), orders.getPayMoney());
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
                        userMoneyService.updateMoney(1,payDetails.getUserId(),payDetails.getMoney());
                        UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                        userMoneyDetails.setMoney(BigDecimal.valueOf(payDetails.getMoney()));
                        userMoneyDetails.setUserId(payDetails.getUserId());
                        userMoneyDetails.setContent("快手充值点券");
                        userMoneyDetails.setTitle("快手充值："+payDetails.getMoney());
                        userMoneyDetails.setType(1);
                        userMoneyDetails.setClassify(1);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
                        userMoneyDetailsService.save(userMoneyDetails);
                    }

                }


                JSONObject resultJson = new JSONObject();
                resultJson.put("result",1);
                resultJson.put("message_id",message_id);

                return resultJson.toString();
            } else if("REFUND".equalsIgnoreCase(biz_type)){
                String s = jsonObject.toJSONString() + commonInfoService.findOne(829).getValue();
                String s1 = MD5Util.encodeByMD5(s);
                if(!kwaisign.equals(s)){
                    log.error("签名不一致！"+s1+"----"+kwaisign);
                    return null;
                }
                String orderNo = data.getString("out_order_no");
                log.error("退款成功订单号！！"+orderNo);
                JSONObject resultJson = new JSONObject();
                resultJson.put("result",1);
                resultJson.put("message_id",message_id);

                return resultJson.toString();
            }else{
                JSONObject resultJson = new JSONObject();
                resultJson.put("result",1);
                resultJson.put("message_id",message_id);
                return resultJson.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject resultJson = new JSONObject();
            resultJson.put("result",0);
            resultJson.put("message_id",message_id);
            return resultJson.toString();
        }
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
        String appId = commonInfoService.findOne(828).getValue();
        CommonInfo oneu = commonInfoService.findOne(19);
        String url;
        url=oneu.getValue()+"/sqx_fast/app/dyPay/notify";
        // 示例参数
        Map<String, Object> params = new HashMap<>();
        params.put("app_id",appId);
        params.put("out_order_no", orders.getOrdersNo()); //商户订单号
        params.put("out_refund_no", orders.getOrdersNo());
        params.put("notify_url", url);
        params.put("reason", "系统退款");
        params.put("refund_amount", new Double(orders.getPayMoney().doubleValue()*100).intValue()+""); //点券。分
        String sign = calcSign(params); //签名
        params.put("sign",sign);
        //发起post请求
        String accessToken = getAccessToken();
        String postJson = HttpClientUtil.doPostJson("https://open.kuaishou.com/openapi/mp/developer/epay/apply_refund?app_id="+appId+"&access_token="+accessToken,
                JSONObject.toJSONString(params));

        //使用官方API退款
        JSONObject jsonObject = JSONObject.parseObject(postJson);
        log.error("快手退款返回值："+postJson);
        String result = jsonObject.getString("result");
        if(!"1".equals(result)){
            return false;
        }
        return true;
    }


    public String getAccessToken(){
        String url="https://open.kuaishou.com/oauth2/access_token";
        Map<String,String> param=new HashMap<>();
        param.put("app_id",commonInfoService.findOne(828).getValue());
        param.put("app_secret",commonInfoService.findOne(829).getValue());
        param.put("grant_type","client_credentials");
        String s = HttpClientUtil.doPost(url, param);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String result = jsonObject.getString("result");
        if("1".equals(result)){
            return jsonObject.getString("access_token");
        }
        log.error(s);
        return null;
    }

    /**
     * 获取参数 Map 的签名结果
     *
     * @param signParamsMap 含义见上述示例
     * @return 返回签名结果
     */
    public String calcSign(Map<String, Object> signParamsMap) {
        // 去掉 value 为空的
        Map<String, Object> trimmedParamMap = signParamsMap.entrySet()
                .stream()
                .filter(item -> !Strings.isNullOrEmpty(item.getValue().toString()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // 按照字母排序
        Map<String, Object> sortedParamMap = trimmedParamMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        // 组装成待签名字符串。(注，引用了guava工具)
        String paramStr = Joiner.on("&").withKeyValueSeparator("=").join(sortedParamMap.entrySet());
        String signStr = paramStr + commonInfoService.findOne(829).getValue();

        // 生成签名返回。(注，引用了commons-codec工具)
        return DigestUtils.md5Hex(signStr);
    }




}
