package com.sqx.modules.pay.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.course.service.CourseService;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayClassify;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.DyService;
import com.sqx.modules.pay.service.PayClassifyService;
import com.sqx.modules.pay.utils.DYSign;
import com.sqx.modules.pay.utils.DouYinSign;
import com.sqx.modules.utils.AmountCalUtils;
import com.sqx.modules.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author fang
 * @date 2020/2/26
 */
@Service
@Slf4j
public class DyServiceImpl implements DyService {


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
        return pay(bean.getPayMoney().doubleValue(), bean.getOrdersNo());
    }

    @Override
    public Result payMoney(Long payClassifyId,Long userId) throws Exception {
        PayClassify payClassify = payClassifyService.getById(payClassifyId);
        BigDecimal money=payClassify.getMoney();
        String generalOrder = getGeneralOrder();
        PayDetails payDetails=new PayDetails();
        payDetails.setState(0);
        payDetails.setCreateTime(sdf.format(new Date()));
        payDetails.setOrderId(generalOrder);
        payDetails.setUserId(userId);
        payDetails.setMoney(money.doubleValue());
        payDetails.setClassify(6);
        payDetails.setType(2);
        payDetails.setRemark(String.valueOf(payClassifyId));
        payDetailsDao.insert(payDetails);
        return pay(money.doubleValue(), generalOrder);
    }

    @Override
    public Result payVirtualAppOrder(Long orderId) throws Exception {
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
            payDetails.setClassify(6);
            payDetails.setType(1);
            payDetailsDao.insert(payDetails);
        }
        CommonInfo oneu = commonInfoService.findOne(19);
        String url;
        url=oneu.getValue()+"/sqx_fast/app/dyPay/virtualNotify";
        Double mul = AmountCalUtils.mul(bean.getPayMoney().doubleValue(), 100);
        Integer money =mul.intValue();
        JSONObject data=new JSONObject();
        data.put("outOrderNo",bean.getOrdersNo());
        data.put("totalAmount",money);
        data.put("payNotifyUrl",url);
        JSONArray skuList=new JSONArray();
        JSONObject sku=new JSONObject();
        sku.put("skuId",String.valueOf(orderId));
        sku.put("price",money);
        sku.put("quantity",1);
        Orders orders = ordersService.selectOrderById(orderId);
        if(orders.getOrdersType()==1){
            Course course = courseService.getById(bean.getCourseId());
            sku.put("title",course.getTitle());
            JSONArray imageList=new JSONArray();
            imageList.add(course.getTitleImg());
            sku.put("imageList",imageList);
        }else if(orders.getOrdersType()==2){
            sku.put("title","会员");
            JSONArray imageList=new JSONArray();
            imageList.add("https://taobao.xianmxkj.com/custom.jpg");
            sku.put("imageList",imageList);
        }

        sku.put("type",401);
        sku.put("tagGroupId","tag_group_7272625659888041996");

        skuList.add(sku);
        data.put("skuList",skuList);
        JSONObject schema=new JSONObject();
        schema.put("path","page/path/index");
        data.put("orderEntrySchema",schema);
        // 请求时间戳
        long timestamp = System.currentTimeMillis()/1000L;
        // 开发者填入自己的小程序app_id
        String appId = commonInfoService.findOne(805).getValue();
        // 随机字符串
        String nonceStr = UUID.randomUUID().toString();
        // 应用公钥版本,每次重新上传公钥后需要更新,可通过「开发管理-开发设置-密钥设置」处获取
        String keyVersion = commonInfoService.findOne(819).getValue();
        // 应用私钥,用于加签 重要：1.测试时请修改为开发者自行生成的私钥;2.请勿将示例密钥用于生产环境;3.建议开发者不要将私钥文本写在代码中
        String h5Url = commonInfoService.findOne(19).getValue().split("://")[1];
        String filePath = "/www/wwwroot/"+h5Url+"/service/rsa_private_key.pem";
        String privateKeyStr = getZs(filePath);
        //String privateKeyStr = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDLOTIHNI97uXcABr0ytLOfXnOGU0Mj/FlIct1BMlxrUlKv6hsThWNufacu1o+4CAL6TNBAJjE94fN2jbutidjNccbU7Si7T0ZU9KWTistXeC8O9uWLfydRQVj/sA3ddmNmgA+0iCa9xGGdxXpTZmLHyK7aA+7uOfGvF+7Su0XwLdA047xuzIISN3omtSUgUEq2GkdAxRS5aMbD+86T3rFWjo9WdmL3cgvIfPf2oGA0NR5cYKiWAZmfq9Bo1X0VmjGEPgl+HMlg62YLJmKb2lKP2J23vzmSzfGI1ugrlr7EiqVsGHCQefOuU776a2G2Pp+vQPetaTOBPckATszBSjXRAgMBAAECggEAB06orlqk+CFslnsnjW2y8b2PMNrIlMAro6/Bpej+Kru8jmfADn0i3KO3AbekVk0vcjmV2WCmL5/yxaYGBBDrU0POjvRyHP4WPNLJK1t3wU7ofkTBbzkkvEa5wQPfE8IAg/yB1A97OSKZEfe6VMpfLejY5Kz0h+tcddvJ8hjaxG6IxcjFvMbRgapMe4WVMc5vV5wn4iugJoAQUXMBNLoaBh/oCKuaDlEgaDb+8gw5UXueveyG+mPdVfsXqkCKGbJ+5r2IiKF23dozVZSv80ZKb4Lm46JazhH2gm3Lr4OMPWq2efK0cwuFoPmlzGtx7ncjCsOqgBs08KlAsOBS9De7AQKBgQD/w1pAjdbXzMOzvZ/fZ76KGQqMaWIN1AR4Ju6KbA6d/gTSOfDav6K8gtHJ6rve1OcjloG6VQlpypkKRJYv6zo7gSv9FTPOl6n2BYcQtdR8HOpnRmU4FFPbGrvOIVb2ekvfmjHf9FiWUfdS7Nx5goxzTMyiq+JEOfX83dErO9WpIQKBgQDLaWJtUPT7iFay7gvMK3ABPzNPChB7uFbNOhqcj08m7hUnyIgPenAe1n2W+9m17fMwpsWTK/cvQGnGfACwYOZHPu+UyLoahxbVBchvZbKnfEp0hi7iUuKIPNxaJ+GfOsUO3CWjmA9CCgoZzvUmPh4aJP+TAeKCcJhr0K+6/emGsQKBgCUiDLlwnm+oaAVxk4ORAWX1asWmCzlsvdVf+aQZOioQFk0bYm+wAQWTjLffH7WjfYd6M42FCR/V7VBDUvbUFRlMkMFm0aW9+Uwh01FGxPncDOA/pTR2JxKZmAi+aGzSpq9pKLKWPEJe1iSxBPWTUabv0IoRoIE9VQyIe/Tl9AhhAoGBAJzL0VlerFkwEVS/9kwdt6reYtisc2RLBm4QOe8w8NybbadLBsaXpNHAmPLHlFyO3YVFKMt3eoTr7B1Z/NX4+8kzlE5mJD7Knyj52jU0eXBteJ81x/Ih3gkSkPDWCS9KiBgaTtE6J5jKUFrwkzw1adLRbkiNjWLrFDMXnD5R968hAoGBAKY836V1Vv4yUXwHZmj7s53AHT1C2qN2JRMWr0z+eC4KJNf2rm7Iz68xFvZfpjy70CREDjghQzNl3wgJRJ4vopZRAfFW+feR2BcmBI2Oat8UeaJDDXP4BcKMM3pmWfBQZ4uh4aViNxLfH6GYaCB5+6AapEpBGPBFqjFrJsRhw5wZ";
        // 生成好的data

        String byteAuthorization = DYSign.getByteAuthorization(privateKeyStr, data.toJSONString(), appId, nonceStr, timestamp, keyVersion);
        JSONObject result=new JSONObject();
        result.put("data",data);
        result.put("byteAuthorization",byteAuthorization);
        return Result.success().put("data",result);
    }

    @Override
    public Result payVirtualAppOrder(Long payClassifyId,Long userId) throws Exception {
        PayClassify payClassify = payClassifyService.getById(payClassifyId);
        BigDecimal money=payClassify.getPrice();
        String generalOrder = getGeneralOrder();
        PayDetails payDetails=new PayDetails();
        payDetails.setState(0);
        payDetails.setCreateTime(sdf.format(new Date()));
        payDetails.setOrderId(generalOrder);
        payDetails.setUserId(userId);
        payDetails.setMoney(money.doubleValue());
        payDetails.setClassify(6);
        payDetails.setType(2);
        payDetails.setRemark(String.valueOf(payClassifyId));
        payDetailsDao.insert(payDetails);
        CommonInfo oneu = commonInfoService.findOne(19);
        String url;
        url=oneu.getValue()+"/sqx_fast/app/dyPay/virtualNotify";
        Double mul = AmountCalUtils.mul(money.doubleValue(), 100);
        Integer moneys =mul.intValue();
        JSONObject data=new JSONObject();
        data.put("outOrderNo",generalOrder);
        data.put("totalAmount",moneys);
        data.put("payNotifyUrl",url);
        JSONArray skuList=new JSONArray();
        JSONObject sku=new JSONObject();
        sku.put("skuId",String.valueOf(moneys));
        sku.put("price",moneys);
        sku.put("quantity",1);
        sku.put("title","充值");
        JSONArray imageList=new JSONArray();
        imageList.add("https://taobao.xianmxkj.com/custom.jpg");
        sku.put("imageList",imageList);
        sku.put("type",401);
        sku.put("tagGroupId","tag_group_7272625659888041996");

        skuList.add(sku);
        data.put("skuList",skuList);
        JSONObject schema=new JSONObject();
        schema.put("path","page/path/index");
        data.put("orderEntrySchema",schema);
        // 请求时间戳
        long timestamp = System.currentTimeMillis()/1000L;
        // 开发者填入自己的小程序app_id
        String appId = commonInfoService.findOne(805).getValue();
        // 随机字符串
        String nonceStr = UUID.randomUUID().toString();
        // 应用公钥版本,每次重新上传公钥后需要更新,可通过「开发管理-开发设置-密钥设置」处获取
        String keyVersion = commonInfoService.findOne(819).getValue();
        // 应用私钥,用于加签 重要：1.测试时请修改为开发者自行生成的私钥;2.请勿将示例密钥用于生产环境;3.建议开发者不要将私钥文本写在代码中
        String h5Url = commonInfoService.findOne(19).getValue().split("://")[1];
        String filePath = "/www/wwwroot/"+h5Url+"/service/rsa_private_key.pem";
        String privateKeyStr = getZs(filePath);
        //String privateKeyStr = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDLOTIHNI97uXcABr0ytLOfXnOGU0Mj/FlIct1BMlxrUlKv6hsThWNufacu1o+4CAL6TNBAJjE94fN2jbutidjNccbU7Si7T0ZU9KWTistXeC8O9uWLfydRQVj/sA3ddmNmgA+0iCa9xGGdxXpTZmLHyK7aA+7uOfGvF+7Su0XwLdA047xuzIISN3omtSUgUEq2GkdAxRS5aMbD+86T3rFWjo9WdmL3cgvIfPf2oGA0NR5cYKiWAZmfq9Bo1X0VmjGEPgl+HMlg62YLJmKb2lKP2J23vzmSzfGI1ugrlr7EiqVsGHCQefOuU776a2G2Pp+vQPetaTOBPckATszBSjXRAgMBAAECggEAB06orlqk+CFslnsnjW2y8b2PMNrIlMAro6/Bpej+Kru8jmfADn0i3KO3AbekVk0vcjmV2WCmL5/yxaYGBBDrU0POjvRyHP4WPNLJK1t3wU7ofkTBbzkkvEa5wQPfE8IAg/yB1A97OSKZEfe6VMpfLejY5Kz0h+tcddvJ8hjaxG6IxcjFvMbRgapMe4WVMc5vV5wn4iugJoAQUXMBNLoaBh/oCKuaDlEgaDb+8gw5UXueveyG+mPdVfsXqkCKGbJ+5r2IiKF23dozVZSv80ZKb4Lm46JazhH2gm3Lr4OMPWq2efK0cwuFoPmlzGtx7ncjCsOqgBs08KlAsOBS9De7AQKBgQD/w1pAjdbXzMOzvZ/fZ76KGQqMaWIN1AR4Ju6KbA6d/gTSOfDav6K8gtHJ6rve1OcjloG6VQlpypkKRJYv6zo7gSv9FTPOl6n2BYcQtdR8HOpnRmU4FFPbGrvOIVb2ekvfmjHf9FiWUfdS7Nx5goxzTMyiq+JEOfX83dErO9WpIQKBgQDLaWJtUPT7iFay7gvMK3ABPzNPChB7uFbNOhqcj08m7hUnyIgPenAe1n2W+9m17fMwpsWTK/cvQGnGfACwYOZHPu+UyLoahxbVBchvZbKnfEp0hi7iUuKIPNxaJ+GfOsUO3CWjmA9CCgoZzvUmPh4aJP+TAeKCcJhr0K+6/emGsQKBgCUiDLlwnm+oaAVxk4ORAWX1asWmCzlsvdVf+aQZOioQFk0bYm+wAQWTjLffH7WjfYd6M42FCR/V7VBDUvbUFRlMkMFm0aW9+Uwh01FGxPncDOA/pTR2JxKZmAi+aGzSpq9pKLKWPEJe1iSxBPWTUabv0IoRoIE9VQyIe/Tl9AhhAoGBAJzL0VlerFkwEVS/9kwdt6reYtisc2RLBm4QOe8w8NybbadLBsaXpNHAmPLHlFyO3YVFKMt3eoTr7B1Z/NX4+8kzlE5mJD7Knyj52jU0eXBteJ81x/Ih3gkSkPDWCS9KiBgaTtE6J5jKUFrwkzw1adLRbkiNjWLrFDMXnD5R968hAoGBAKY836V1Vv4yUXwHZmj7s53AHT1C2qN2JRMWr0z+eC4KJNf2rm7Iz68xFvZfpjy70CREDjghQzNl3wgJRJ4vopZRAfFW+feR2BcmBI2Oat8UeaJDDXP4BcKMM3pmWfBQZ4uh4aViNxLfH6GYaCB5+6AapEpBGPBFqjFrJsRhw5wZ";
        // 生成好的data
        String byteAuthorization = DYSign.getByteAuthorization(privateKeyStr, data.toJSONString(), appId, nonceStr, timestamp, keyVersion);
        JSONObject result=new JSONObject();
        result.put("data",data);
        result.put("byteAuthorization",byteAuthorization);
        return Result.success().put("data",result);
    }


    public static String getZs(String filePath) {
        File file = new File(filePath);
        BufferedReader reader = null;
        try {

            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            StringBuilder stringBuilder=new StringBuilder();
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                stringBuilder.append(tempString);
                line++;
            }
            log.info("抖音获取到的秘钥："+stringBuilder);
            String str = stringBuilder.toString();
            str=str.replace("-----BEGIN PRIVATE KEY-----","");
            str=str.replace("-----END PRIVATE KEY-----","");
            reader.close();
            return str;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("抖音解析秘钥报错！"+e.getMessage(),e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return null;
    }


    /**
     * 抖音支付订单生成
     * @param moneys  支付点券 带小数点
     * @param outTradeNo 单号
     * @return
     * @throws Exception
     */
    private Result pay(Double moneys,String outTradeNo) throws Exception {
        CommonInfo oneu = commonInfoService.findOne(19);
        String url;
        url=oneu.getValue()+"/sqx_fast/app/dyPay/notify";
        CommonInfo one = commonInfoService.findOne(12);
        log.info("回调地址："+url);
        Double mul = AmountCalUtils.mul(moneys, 100);
        Integer money =mul.intValue();
        String appId = commonInfoService.findOne(805).getValue();
        String SALT = commonInfoService.findOne(815).getValue();
        // 示例参数
        Map<String, Object> params = new HashMap<>();
        params.put("app_id",appId);
        params.put("out_order_no", outTradeNo); //商户订单号
        params.put("total_amount", money); //点券。分
        params.put("notify_url", url);  //回调接口
        params.put("subject", one.getValue()); //主题
        params.put("body", one.getValue()); //商品详情
        params.put("valid_time", 5*60); //过期时间
        String sign = DouYinSign.requestSign(params,SALT); //签名
        params.put("sign",sign);
        //发起post请求
        String postJson = HttpClientUtil.doPostJson("https://developer.toutiao.com/api/apps/ecpay/v1/create_order", JSONObject.toJSONString(params));

        if (StringUtils.isNoneBlank(postJson)) {
            JSONObject jsonObject = JSONObject.parseObject(postJson);
            if ("success".equals(jsonObject.get("err_tips")))
            {
                return Result.success().put("data",jsonObject.get("data").toString());
            } else {
                log.error("抖音支付生成失败" + postJson);
                throw new Exception("抖音支付生成失败" + postJson);
            }
        }
        return null;
    }




    @Override
    public String payBack(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            InputStream in = request.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.close();
            in.close();
            String responseJson = new String(out.toByteArray(),"utf-8");
            log.info("responseJson：{}",responseJson);
            JSONObject jsonObject = JSONObject.parseObject(responseJson);
            JSONObject msgJson = jsonObject.getJSONObject("msg");
            String resultCode = jsonObject.getString("type");
            if ("payment".equalsIgnoreCase(resultCode)) {
                List<String> sortedString = new ArrayList<>();
                //token
                sortedString.add("token");
                //时间戳
                sortedString.add(jsonObject.getString("timestamp"));
                //随机数
                sortedString.add(jsonObject.getString("nonce"));
                //msg
                sortedString.add(jsonObject.getString("msg"));
                Collections.sort(sortedString);
                StringBuilder sb = new StringBuilder();
                sortedString.forEach(sb::append);
                String msg_signature = jsonObject.getString("msg_signature");
                String sign = DouYinSign.callbackSign(sortedString);
                log.info("支付回调接口密钥签名：{}",sign);
                if(!sign.equals(msg_signature)) {//判断签名，此处验签有问题
                    JSONObject resultJson = new JSONObject();
                    resultJson.put("err_no",8);
                    resultJson.put("err_tips","error");
                }
                String orderNo =  msgJson.getString("cp_orderno"); //商户订单号
                String order_id =  msgJson.getString("order_id");  //抖音订单id
                Integer orderAmount =  msgJson.getInteger("total_amount");  //支付点券

                //todo 处理支付成功后的订单业务
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
                        userMoneyDetails.setMoney(BigDecimal.valueOf(payDetails.getMoney()));
                        userMoneyDetails.setUserId(payDetails.getUserId());
                        userMoneyDetails.setContent("抖音充值点券");
                        userMoneyDetails.setTitle("抖音充值："+payClassify.getMoney()+",赠送："+payClassify.getGiveMoney());
                        userMoneyDetails.setType(1);
                        userMoneyDetails.setClassify(1);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
                        userMoneyDetailsService.save(userMoneyDetails);
                    }

                }


                JSONObject resultJson = new JSONObject();
                resultJson.put("err_no",0);
                resultJson.put("err_tips","success");

                return resultJson.toString();
            } else {
                //订单编号
                String outTradeNo = msgJson.getString("cp_orderno");
                log.error("订单" + outTradeNo + "支付失败");
                JSONObject resultJson = new JSONObject();
                resultJson.put("err_no",0);
                resultJson.put("err_tips","error");
                return resultJson.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject resultJson = new JSONObject();
            resultJson.put("err_no",8);
            resultJson.put("err_tips","error");
            return resultJson.toString();
        }
    }


    @Override
    public String virtualNotify(JSONObject jsonObject) {
        try {
            log.error("回调了！！"+jsonObject.toJSONString());
            JSONObject msgJson = jsonObject.getJSONObject("msg");
            String resultCode = jsonObject.getString("type");
            if ("payment".equalsIgnoreCase(resultCode)) {

                String orderNo =  msgJson.getString("out_order_no"); //商户订单号
                //todo 处理支付成功后的订单业务
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
                        userMoneyDetails.setMoney(BigDecimal.valueOf(payDetails.getMoney()));
                        userMoneyDetails.setUserId(payDetails.getUserId());
                        userMoneyDetails.setContent("抖音充值点券");
                        userMoneyDetails.setTitle("抖音充值："+payClassify.getMoney()+",赠送："+payClassify.getGiveMoney());
                        userMoneyDetails.setType(1);
                        userMoneyDetails.setClassify(1);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
                        userMoneyDetailsService.save(userMoneyDetails);
                    }

                }


                JSONObject resultJson = new JSONObject();
                resultJson.put("err_no",0);
                resultJson.put("err_tips","success");

                return resultJson.toString();
            } else {
                //订单编号
                String outTradeNo = msgJson.getString("out_order_no");
                log.error("订单" + outTradeNo + "支付失败");
                JSONObject resultJson = new JSONObject();
                resultJson.put("err_no",0);
                resultJson.put("err_tips","success");
                return resultJson.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("抖音支付回调异常："+e.getMessage(),e);
            JSONObject resultJson = new JSONObject();
            resultJson.put("err_no",8);
            resultJson.put("err_tips","error");
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
        String appId = commonInfoService.findOne(805).getValue();
        // 示例参数
        Map<String, Object> params = new HashMap<>();
        params.put("app_id",appId);
        params.put("out_order_no", orders.getOrdersNo()); //商户订单号
        params.put("out_refund_no", orders.getOrdersNo());
        params.put("reason", "系统退款");
        params.put("total_amount", new Double(orders.getPayMoney().doubleValue()*100).intValue()+""); //点券。分
        String SALT = commonInfoService.findOne(815).getValue();
        String sign = DouYinSign.requestSign(params,SALT); //签名
        params.put("sign",sign);
        //发起post请求
        String postJson = HttpClientUtil.doPostJson("https://developer.toutiao.com/api/apps/ecpay/v1/create_refund", JSONObject.toJSONString(params));
        //使用官方API退款
        JSONObject jsonObject = JSONObject.parseObject(postJson);
        log.error("抖音退款返回值："+postJson);
        String err_no = jsonObject.getString("err_no");
        if(!"0".equals(err_no)){
            return false;
        }
        return true;
    }



}