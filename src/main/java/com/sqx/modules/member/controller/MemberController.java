package com.sqx.modules.member.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.member.entity.Member;
import com.sqx.modules.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "会员特权", tags = {"会员特权"})
@RequestMapping(value = "/member")
public class MemberController {


    @Autowired
    private MemberService memberService;

    @GetMapping("/selectMemberList")
    @ApiOperation("查询列表（不带分页）")
    public Result selectMemberList(Integer memberIndex){
        return memberService.selectMemberList(memberIndex);
    }


    @GetMapping("/selectMemberPage")
    @ApiOperation("查询列表（带分页")
    public Result selectMemberPage(Integer page,Integer limit,Integer memberIndex){
        return memberService.selectMemberPage(page, limit,memberIndex);
    }

    @PostMapping("/updateMember")
    @ApiOperation("修改会员特权")
    public Result updateMember(@RequestBody Member member){
        return memberService.updateMember(member);
    }


    @PostMapping("/deleteMemberById")
    @ApiOperation("删除会员特权")
    public Result deleteMemberById(Long memberId){
        return memberService.deleteMemberById(memberId);
    }

    @PostMapping("/insertMember")
    @ApiOperation("新增会员特权")
    public Result insertMember(@RequestBody Member member){
        return memberService.insertMember(member);
    }





}
