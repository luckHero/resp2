package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.OrderDetail;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author: Lucky
 * @Date:2020-02-17 18:54
 */
/*
    会员服务
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;

    @RequestMapping("/login")
    public Result login(@RequestBody OrderDetail orderDetail, HttpServletResponse response) {
        String telephone = orderDetail.getTelephone();//获取手机号
        String validateCode = orderDetail.getValidateCode();//获取用户的手机号
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);  //获取缓存中的验证码
        if (validateCodeInRedis != null && validateCode != null && validateCode.equals(validateCodeInRedis)) { //判断验证码是否输入正确
            Member member = memberService.findByTelephone(telephone);
            if (member == null) {//不是会员,注册成会员
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                memberService.add(member);
            }
            //客户端写入Cookie,跟踪用户,根据cookie的内容,判断是那个用户
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            cookie.setPath("/");//路径
            cookie.setMaxAge(60 * 60 * 24 * 30);//有效期
            response.addCookie(cookie);

            String json = JSON.toJSON(member).toString();  //会员序列化成json
            //将会员信息保存到reids中
            jedisPool.getResource().setex(telephone, 60 * 30, json);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        } else {//验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }
}
