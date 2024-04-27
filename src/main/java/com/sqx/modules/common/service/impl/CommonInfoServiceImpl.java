package com.sqx.modules.common.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.common.dao.CommonInfoDao;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.course.service.CourseService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author fang
 * @date 2020/7/8
 */
@Service
public class CommonInfoServiceImpl extends ServiceImpl<CommonInfoDao, CommonInfo> implements CommonInfoService {

    @Autowired
    private CommonInfoDao commonInfoDao;
    @Autowired
    private CourseService courseService;
    @Autowired
    private MessageService messageService;



    @Override
    public Result update(CommonInfo commonInfo, SysUserEntity user) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        commonInfo.setCreateAt(sdf.format(now));
        if(commonInfo.getType()==820){
            Result result = courseService.setDyNotifyUrl(commonInfo.getValue());
            String code = String.valueOf(result.get("code"));
            if(!"0".equals(code)){
                return result;
            }
        }else if(commonInfo.getType()==883){
            CommonInfo commonInfo1 = commonInfoDao.selectById(commonInfo.getId());
            Calendar instance = Calendar.getInstance();
            String nowTime = DateUtils.format(instance.getTime(), "yyyy-MM");
            Date date = DateUtils.stringToDate(commonInfo1.getCreateAt(), DateUtils.DATE_TIME_PATTERN);
            String oldTime = DateUtils.format(date, "yyyy-MM");
            if(nowTime.equals(oldTime)){
                return Result.error("当月已设置，请勿重复提交！");
            }
            MessageInfo messageInfo=new MessageInfo();
            messageInfo.setContent(user.getUsername()+"：在“"+sdf.format(now)+"”修改平台利润为："+commonInfo.getValue());
            messageInfo.setCreateAt(DateUtils.format(new Date()));
            messageInfo.setState("20");
            messageInfo.setIsSee("0");
            messageInfo.setUserName(user.getUsername());
            messageInfo.setSendTime(commonInfo1.getMax().split(",")[0]);
            messageService.save(messageInfo);
        }
        commonInfoDao.updateById(commonInfo);

        return Result.success();
    }


    @Override
    public CommonInfo findOne(int id) {
        return commonInfoDao.findOne(id);
    }

    @Override
    public Result delete(long id) {
        commonInfoDao.deleteById(id);
        return Result.success();
    }


    @Override
    public Result updateBody(CommonInfo commonInfo) {
        commonInfoDao.updateById(commonInfo);
        return Result.success();
    }

    @Override
    public Result findByType(Integer type) {
        return Result.success().put("data",commonInfoDao.findOne(type));
    }

    @Override
    public Result findByTypeAndCondition(String condition) {
        return Result.success().put("data",commonInfoDao.findByCondition(condition));
    }

    @Override
    public Result selectTypeList(JSONArray jsonArray){
        for (int i=0;i<jsonArray.size();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String id = jsonObject.getString("id");
            CommonInfo one = commonInfoDao.findOne(Integer.parseInt(id));
            jsonObject.put("commonInfo",one);
        }
        return Result.success().put("data",jsonArray);
    }


    @Scheduled(cron = "0 0 1 1 * ?")
    public void updatePingMoney() {
        CommonInfo commonInfo = commonInfoDao.findOne(883);
        commonInfo.setValue("0");
        String max = commonInfo.getMax();
        String[] split = max.split(",");
        commonInfo.setMax(split[0]+",0");
        commonInfoDao.updateById(commonInfo);
    }

}