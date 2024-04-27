package com.sqx.modules.pay.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.course.service.CourseService;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayClassify;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.PayClassifyService;
import com.sqx.modules.pay.utils.IosVerify;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author fang
 * @date 2021/8/23
 */
@Slf4j
@Api(value = "苹果支付", tags = {"苹果支付"})
@RestController
@RequestMapping("/app/ios")
public class IosPayController {

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


    @ApiOperation("充值支付生成订单")
    @PostMapping("/insertPayMoneyOrders")
    public Result insertPayMoneyOrders(Long payClassifyId, Long userId){
        PayClassify payClassify = payClassifyService.getById(payClassifyId);
        BigDecimal money=payClassify.getPrice();
        String generalOrder = getGeneralOrder();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PayDetails payDetails = payDetailsDao.selectByOrderId(generalOrder);
        if(payDetails==null){
            payDetails =new PayDetails();
            payDetails.setState(0);
            payDetails.setCreateTime(sdf.format(new Date()));
            payDetails.setOrderId(generalOrder);
            payDetails.setUserId(userId);
            payDetails.setMoney(money.doubleValue());
            payDetails.setClassify(7);
            payDetails.setType(2);
            payDetails.setRemark(String.valueOf(payClassifyId));
            payDetails.setProductId(payClassify.getProductId());
            payDetailsDao.insert(payDetails);
        }
        return Result.success().put("data",payDetails);
    }


    @Login
    @ApiOperation("用户端回调")
    @PostMapping("/isoPayApp")
    public Result isoPayApp(String receipt,String ordersId) {
        String way = commonInfoService.findOne(832).getValue();
        log.error("进入苹果回调："+receipt);
        JSONObject jsonObject = JSONObject.parseObject(receipt);
        String transactionReceipt = jsonObject.getString("transactionReceipt");
        String verifyResult = IosVerify.buyAppVerify(transactionReceipt,Integer.parseInt(way));
        log.error("转换值："+verifyResult);
        if (verifyResult == null) {
            // 苹果服务器没有返回验证结果
            return Result.error("无订单信息!");
        } else {
            // 苹果验证有返回结果------------------
            JSONObject job = JSONObject.parseObject(verifyResult);
            String status = job.getString("status");
            if ("0".equals(status)) // 验证成功
            {
                String rReceipt = job.getString("receipt");
                JSONObject returnJson = JSONObject.parseObject(rReceipt);
                log.error(returnJson.toJSONString());
                /*if(!returnJson.getString("bid").trim().equals("xxxx")){//商户的id不匹配
                    return ResultUtil.error(-200,"订单无效!");
                }*/
                // 产品ID
                String productId = returnJson.getString("product_id");
                // 订单号
                String transactionId = returnJson.getString("transaction_id");
                // 交易日期
                String purchaseDate = returnJson.getString("purchase_date");
                log.info("product_id:"+productId+"   transaction_id : "+transactionId+" purchase_date: "+purchaseDate);
                PayDetails payDetails=payDetailsDao.selectByOrderId(ordersId);
                PayClassify payClassify = payClassifyService.getById(String.valueOf(payDetails.getRemark()));
                //设置查询条件
                userMoneyService.updateMoney(1,payDetails.getUserId(),payClassify.getPayClassifyId());
                UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                userMoneyDetails.setMoney(payClassify.getMoney());
                userMoneyDetails.setUserId(payDetails.getUserId());
                userMoneyDetails.setContent("苹果充值点券");
                userMoneyDetails.setTitle("苹果充值："+payClassify.getMoney());
                userMoneyDetails.setType(1);
                userMoneyDetails.setClassify(1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
                userMoneyDetailsService.save(userMoneyDetails);
            } else {
                return Result.error("订单无效!");
            }
        }
        return Result.success();
    }

    public String getGeneralOrder(){
        Date date=new Date();
        String newString = String.format("%0"+4+"d", (int)((Math.random()*9+1)*1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format+newString;
    }







}
