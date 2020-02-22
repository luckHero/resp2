package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.pojo.OrderDetail;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @Author: Lucky
 * @Date:2020-02-16 11:23
 */
/*
    预约套餐的controller
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/submit")
    public Result submit(@RequestBody OrderDetail orderDetail) {

        String telephone = orderDetail.getTelephone();//获取手机号
        String validateCode = orderDetail.getValidateCode();//获取用户的手机号
        //先获取缓存中的验证码
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //判断验证码是否输入正确
//        if (validateCodeInRedis != null && validateCode != null && validateCode.equals(validateCodeInRedis)) {

            orderDetail.setOrderType(Order.ORDERTYPE_WEIXIN);//封装预约类型
            Result result = null;
            try {
                result = orderService.order(orderDetail); //远程调用服务端
            } catch (Exception e) { //调用失败,返回前端信息
                e.printStackTrace();
                return result;
            }
                 //判断用户是否预约成功
            if (result.isFlag()) {//预约成功reachable  发送预约成功的信息
                try {
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, orderDetail.getOrderDate());
                } catch (Exception e) {//表示发送预约成功的短息发送失败,用户接受不到预约成功的短息
                    e.printStackTrace();
                }
            }
                return result;
//        } else {//验证码错误
//                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
//        }
    }


    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

}
