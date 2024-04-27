package com.sqx.modules.invite.controller.app;


import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.annotation.LoginUser;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.invite.entity.InviteAward;
import com.sqx.modules.invite.entity.InviteMoney;
import com.sqx.modules.invite.service.InviteAwardService;
import com.sqx.modules.invite.service.InviteMoneyService;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.urlAddress.service.UrlAddressService;
import com.sqx.modules.utils.SenInfoCheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/9
 */
@Slf4j
@RestController
@Api(value = "邀请收益", tags = {"邀请收益"})
@RequestMapping(value = "/app/invite")
public class AppInviteController {

    @Autowired
    private InviteService inviteService;
    @Autowired
    private UserService userService;
    @Autowired
    private InviteMoneyService inviteMoneyService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private InviteAwardService inviteAwardService;
    @Resource
    private QrConfig qrconig;
    @Autowired
    private UrlAddressService urlAddressService;

    @RequestMapping(value = "/selectInviteCount", method = RequestMethod.GET)
    @ApiOperation("查看我邀请的人员数量")
    @ResponseBody
    public Result selectInviteCount(Integer state,Long userId){
        return Result.success().put("data",inviteService.selectInviteCount(state,userId));
    }

    @Login
    @RequestMapping(value = "/selectUserMoney", method = RequestMethod.GET)
    @ApiOperation("查看我的钱包")
    @ResponseBody
    public Result selectUserMoney(@RequestAttribute("userId") Long userId){
        return Result.success().put("data",userMoneyService.selectUserMoneyByUserId(userId));
    }



    @RequestMapping(value = "/selectInviteAndPoster", method = RequestMethod.GET)
    @ApiOperation("查看我的邀请码和海报二维码")
    @ResponseBody
    public Result selectInviteAndPoster(Long userId){
        UserEntity userEntity = userService.queryByUserId(userId);
        CommonInfo one = commonInfoService.findOne(19);
        Map<String,Object> map=new HashMap<>();
        map.put("url",one.getValue());
        map.put("user",userEntity);
        return Result.success().put("data",map);
    }

    @Login
    @RequestMapping(value = "/selectInviteMoney", method = RequestMethod.GET)
    @ApiOperation("我的收益")
    @ResponseBody
    public Result selectInviteMoney(@RequestAttribute("userId") Long userId){
        InviteMoney inviteMoney = inviteMoneyService.selectInviteMoneyByUserId(userId);
        Integer inviteCount = inviteService.selectInviteCount(-1, userId);
        Map<String,Object> result=new HashMap<>();
        result.put("inviteMoney",inviteMoney);
        result.put("inviteCount",inviteCount);
        return Result.success().put("data",result);
    }

    @GetMapping("/mpCreateQr")
    @ApiOperation("微信小程序推广二维码")
    public void mpCreateQr(String invitationCode,String page, HttpServletResponse response) {
        SenInfoCheckUtil.getPoster(invitationCode,page,response);
    }

    @GetMapping("/dyCreateQr")
    @ApiOperation("抖音小程序推广二维码")
    public void dyCreateQr(String invitationCode,String page, HttpServletResponse response) {
        SenInfoCheckUtil.getDyImg(invitationCode,page,response);
    }

    @GetMapping("/insertQrCode")
    @ApiOperation("生成二维码")
    public void selectQrCode(String content,String courseId, HttpServletResponse servletResponse) throws Exception{
        String invitationCode = commonInfoService.findOne(88).getValue();
        String value="";
        if(StringUtils.isNotBlank(courseId)){
            value=urlAddressService.selectUrlAddressOne().getUrlAddress()+"?invitation="+invitationCode+"&qdCode="+content+"&id="+courseId;
        }else{
            value=urlAddressService.selectUrlAddressOne().getUrlAddress()+"?invitation="+invitationCode+"&qdCode="+content;
        }

        QrCodeUtil.generate(value,qrconig,"png",servletResponse.getOutputStream());
    }

    @Login
    @RequestMapping(value = "/selectInviteByUserIdLists", method = RequestMethod.GET)
    @ApiOperation("查看我邀请的人员列表(查看所有邀请列表)")
    @ResponseBody
    public Result selectInviteByUserIdLists(int page,int limit,@RequestAttribute("userId") Long userId,Integer userType){
        PageUtils pageUtils = inviteService.selectInviteUser(page, limit, userId,null,userType);
        return Result.success().put("data",pageUtils);
    }

    @Login
    @ApiOperation("钱包明细")
    @GetMapping("/queryUserMoneyDetails")
    public Result queryUserMoneyDetails(Integer page, Integer limit,@RequestAttribute("userId") Long userId,Integer classify,Integer type) {
        return userMoneyDetailsService.queryUserMoneyDetails(page, limit,null, userId,2,type);
    }

    @GetMapping("/selectInviteAwardList")
    @ApiOperation("查询邀请奖励列表")
    public Result selectInviteAwardList(Integer page,Integer limit){
        return Result.success().put("data",inviteAwardService.page(new Page<>(page,limit),new QueryWrapper<InviteAward>().orderByAsc("invite_count")));
    }

    @Login
    @GetMapping("/selectInviteAwardByUserId")
    @ApiOperation("查询当前邀请人数的下一个等级")
    public Result selectInviteAwardByUserId(@LoginUser UserEntity userEntity){
        int inviterCount = userService.queryInviterCount(userEntity.getInvitationCode());
        return Result.success().put("data",inviteAwardService.getOne(new QueryWrapper<InviteAward>().gt("invite_count",inviterCount)
                .last(" order by invite_count limit 1")));
    }

    @Login
    @GetMapping("/selectInviteUserListByUserId")
    @ApiOperation("查询当前邀请人列表")
    public Result selectInviteUserListByUserId(@LoginUser UserEntity userEntity){
        return Result.success().put("data",userService.list(new QueryWrapper<UserEntity>().eq("inviter_code",userEntity.getInvitationCode())));
    }

    @Login
    @PostMapping("/inviteMoneyConvertUserMoney")
    @ApiOperation("收益充值京豆")
    public Result inviteMoneyConvertUserMoney(@LoginUser UserEntity userEntity, Long payClassifyId){
        return inviteMoneyService.inviteMoneyConvertUserMoney(userEntity,payClassifyId);
    }

}