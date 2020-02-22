package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.OrderSettingMapper;
import com.itheima.pojo.OrderSetting;
import com.itheima.pojo.Reservations;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.MarshalledObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Lucky
 * @Date:2020-02-09 23:44
 */
/*
    预约设置服务
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingMapper orderSettingMapper;

    @Override
    public void add(List<OrderSetting> data) {
        if (data != null) {//判断集合中是否有数据
            for (OrderSetting orderSetting : data) {
                Long orderSettingNumber = orderSettingMapper.findCountByOrderDate(orderSetting.getOrderDate());//这个是判断某一天是否有人预约
                if (orderSettingNumber > 0) {//有则修改数据
                    orderSettingMapper.editNumberByOrderDate(orderSetting);
                } else {//没有就保存到数据库
                    orderSettingMapper.add(orderSetting);
                }
            }
        }
    }

    //通过月份获取可预约人数,
    @Override
    public List<Reservations> getOrderSettingByMonth(String data) {
        String begin = data + "-1";
        String end = data + "-31";
        Map<String, String> map = new HashMap<String, String>();
        map.put("begin", begin);
        map.put("end", end);
        List<Reservations> list = orderSettingMapper.getOrderSettingByMonth(map);
        return list;
    }

    //编辑可预约人数的方法
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {//在修改可预约人数时,要进行判断,万一没有预约要进行添加操作
        Long count = orderSettingMapper.findCountByOrderDate(orderSetting.getOrderDate()); //查询当天有没有预约
        if (count > 0) {
            orderSettingMapper.editNumberByOrderDate(orderSetting); //就直接在新增操作的那个方法里用到的修改的方法
        }else {
            orderSettingMapper.add(orderSetting);//没有预约操作就进行添加
        }
    }
}
