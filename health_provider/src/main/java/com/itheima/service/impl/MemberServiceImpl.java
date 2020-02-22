package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.MemberMapper;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Lucky
 * @Date:2020-02-17 19:44
 */
/*
    会员服务
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    //根据手机号查询会员信息
    @Override
    public Member findByTelephone(String telephone) {
        return memberMapper.findByTelephone(telephone);
    }

    //保存会员的方法
    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password != null) { //若密码不为空,使用MD5加密
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberMapper.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {
        List<Integer> list=new ArrayList<Integer>();
        for (String month : months) {
            month=month+".31";
            Integer count= memberMapper.findMemberCountByDate(month);
            list.add(count);
        }
        return list;
    }


}
