package com.sqx.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.member.dao.MemberDao;
import com.sqx.modules.member.entity.Member;
import com.sqx.modules.member.service.MemberService;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberDao, Member> implements MemberService {


    @Override
    public Result insertMember(Member member){
        baseMapper.insert(member);
        return Result.success();
    }

    @Override
    public Result updateMember(Member member){
        baseMapper.updateById(member);
        return Result.success();
    }

    @Override
    public Result deleteMemberById(Long memberId){
        baseMapper.deleteById(memberId);
        return Result.success();
    }

    @Override
    public Result selectMemberList(Integer memberIndex){
        return Result.success().put("data",baseMapper.selectList(new QueryWrapper<Member>().eq("member_index",memberIndex).orderByAsc("sort")));
    }

    @Override
    public Result selectMemberPage(Integer page,Integer limit,Integer memberIndex){
        IPage<Member> pages=new Page<>(page,limit);
        return Result.success().put("data",new PageUtils(baseMapper.selectPage(pages,new QueryWrapper<Member>().eq("member_index",memberIndex).orderByAsc("sort"))));
    }



}







