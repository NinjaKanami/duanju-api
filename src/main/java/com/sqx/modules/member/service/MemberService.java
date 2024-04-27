package com.sqx.modules.member.service;

import com.sqx.common.utils.Result;
import com.sqx.modules.member.entity.Member;


public interface MemberService {

    Result insertMember(Member member);

    Result updateMember(Member member);

    Result deleteMemberById(Long memberId);

    Result selectMemberList(Integer memberIndex);

    Result selectMemberPage(Integer page,Integer limit,Integer memberIndex);

}
