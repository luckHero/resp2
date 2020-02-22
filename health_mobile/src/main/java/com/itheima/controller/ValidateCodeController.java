package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @Author: Lucky
 * @Date:2020-02-15 21:44
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    //用户体检预约发送的验证码
    @RequestMapping("/send4order")
    public Result send4Order(String telephone) {
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);//生成4位数字的验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode.toString());//调用工具类发送验证码
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("验证码:" + validateCode);
        //将验证码存到redis数据库中
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 120 * 60, validateCode.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }




    //用户快速登录发送的验证码
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {

        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);//生成6位数字的验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, validateCode.toString());//调用工具类发送验证码
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("验证码:" + validateCode);

        //将验证码存到redis数据库中
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 120 * 60, validateCode.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    //校验验证码是否正确的
    @RequestMapping("/checkCode")
    public Result checkCode(String validateCode, String telephone) {
        //先获取缓存中的验证码
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //判断验证码是否输入正确
        if (validateCodeInRedis != null  &&  validateCode != null  && validateCode.equals(validateCodeInRedis)) {
            return new Result(true, MessageConstant.VALIDATECODE_SUCCESS);
        }
        return new Result(false, MessageConstant.VALIDATECODE_ERROR);
    }
}
