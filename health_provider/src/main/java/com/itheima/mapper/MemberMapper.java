package com.itheima.mapper;

import com.itheima.pojo.Member;

public interface MemberMapper {
    Member findByTelephone(String telephone);

    void add(Member member);

    Integer findMemberCountByDate(String month);

    Integer findMemberTotalCount();

    Integer findMemberCountAfterDate(String thisWeekMonday);
}
