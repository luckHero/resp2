package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Lucky
 * @Date:2020-02-20 23:49
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/getUserName")
    public Result getUserName(){
        //当Spring security完成认证后，会将当前用户信息保存到框架提供的上下文对象
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user);
        if(user!=null){
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        }
        return new Result(false,MessageConstant.GET_USERNAME_FAIL);
    }
}
