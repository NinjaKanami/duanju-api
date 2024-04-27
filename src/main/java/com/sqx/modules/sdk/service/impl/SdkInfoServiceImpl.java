package com.sqx.modules.sdk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.service.UserVipService;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.sdk.dao.SdkInfoDao;
import com.sqx.modules.sdk.entity.SdkInfo;
import com.sqx.modules.sdk.entity.SdkType;
import com.sqx.modules.sdk.service.SdkInfoService;
import com.sqx.modules.sdk.service.SdkTypeService;
import com.sqx.modules.utils.excel.ExcelData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author www.javacoder.top
 * @since 2023-02-20
 */
@Service
public class SdkInfoServiceImpl extends ServiceImpl<SdkInfoDao, SdkInfo> implements SdkInfoService {

    @Autowired
    private SdkTypeService typeService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserVipService userVipService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private OrdersService ordersService;


    @Override
    public Result saveSdkInfo(Long typeId, Integer num,Long sysUserId) {
        SdkType sdkType = typeService.getById(typeId);
        if (sdkType==null){
            return Result.error("卡密类型不存在");
        }
        SdkInfo sdkInfo = new SdkInfo();
        for (int i = 0; i < num; i++) {
            String replace = UUID.randomUUID().toString().replace("-", "");
            sdkInfo.setSdkContent(replace);
            sdkInfo.setStatus(0);
            sdkInfo.setCreateTime(DateUtils.format(new Date()));
            sdkInfo.setTypeId(typeId);
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH,sdkType.getValidDay());
            sdkInfo.setOverdueTime(DateUtils.format(calendar.getTime()));
            sdkInfo.setSdkRemarks(sdkType.getRemarks());
            sdkInfo.setGiveNum(sdkType.getGiveNum());
            sdkInfo.setSysUserId(sysUserId);
            baseMapper.insert(sdkInfo);
        }
        return Result.success();
    }

    @Override
    public IPage<SdkInfo> getSdkList(Integer page, Integer limit, SdkInfo sdkInfo) {
        Page<SdkInfo> pages;
        if (page != null && limit != null) {
            pages = new Page<>(page, limit);
        } else {
            pages = new Page<>();
            pages.setSize(-1);
        }
        return baseMapper.getSdkPage(pages, sdkInfo);
    }

    @Override
    public ExcelData excelSdkList(SdkInfo sdkInfo) {
        List<SdkInfo> sdkList = baseMapper.getSdkList(sdkInfo);
        ExcelData data = new ExcelData();
        data.setName("提现列表");
        List<String> titles = new ArrayList();
        titles.add("编号");titles.add("卡密名称");titles.add("卡密");titles.add("赠送会员天数");
        titles.add("到期时间"); titles.add("领取用户");titles.add("状态");titles.add("创建时间");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for(SdkInfo sdkInfo1:sdkList){
            List<Object> row = new ArrayList();
            row.add(sdkInfo1.getSdkId());
            row.add(sdkInfo1.getSdkRemarks());
            row.add(sdkInfo1.getSdkContent());
            row.add(sdkInfo1.getGiveNum());
            row.add(sdkInfo1.getOverdueTime());
            row.add(sdkInfo1.getNickName());
            //0未使用 1已使用 2已过期
            if(sdkInfo1.getStatus()==0){
               row.add("未使用");
            }else if(sdkInfo1.getStatus()==1){
                row.add("已使用");
            }else if(sdkInfo1.getStatus()==2){
                row.add("已过期");
            }else {
                row.add("未知");
            }
            row.add(sdkInfo1.getCreateTime());
            rows.add(row);
        }
        data.setRows(rows);
        return data;
    }


    @Override
    public Result sdkExchange(Long userId, String sdkContent) {
        SdkInfo content = baseMapper.selectOne(new QueryWrapper<SdkInfo>().eq("sdk_content", sdkContent));
        if(content==null){
            return Result.error("卡密不存在");
        }
        if(content.getStatus()==2){
            return Result.error("卡密已过期");
        }
        if(content.getStatus()==1){
            return Result.error("卡密已使用");
        }
        BigDecimal giveNum = content.getGiveNum();
        userMoneyService.updateMoney(1,userId,giveNum.doubleValue());
        UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
        userMoneyDetails.setClassify(1);
        userMoneyDetails.setMoney(giveNum);
        userMoneyDetails.setUserId(userId);
        userMoneyDetails.setContent("卡密兑换点券");
        userMoneyDetails.setTitle("卡密兑换点券："+giveNum);
        userMoneyDetails.setType(1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
        userMoneyDetailsService.save(userMoneyDetails);
        content.setStatus(1);
        content.setUseTime(DateUtils.format(new Date()));
        content.setUserId(userId);
        baseMapper.updateById(content);
        //创建订单返回对象
        Orders orders = new Orders();
        //设置订单编号
        orders.setOrdersNo(getGeneralOrder());
        //设置支付点券
        orders.setPayMoney(giveNum);
        //设置订单类型
        orders.setOrdersType(5);
        //设置支付状态
        orders.setStatus(1);
        //设置用户id
        orders.setUserId(userId);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //设置创建时间
        orders.setCreateTime(df.format(new Date()));
        orders.setPayTime(orders.getCreateTime());
        //插入到订单表中
        ordersService.save(orders);
        return Result.success("兑换成功");
    }


    public String getGeneralOrder() {
        Date date = new Date();
        String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format + newString;
    }

}
