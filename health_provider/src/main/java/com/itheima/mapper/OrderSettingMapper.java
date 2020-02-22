package com.itheima.mapper;

import com.itheima.pojo.OrderDetail;
import com.itheima.pojo.OrderSetting;
import com.itheima.pojo.Reservations;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: Lucky
 * @Date:2020-02-09 23:45
 */
public interface OrderSettingMapper {
    void add(OrderSetting orderSetting);
    void editNumberByOrderDate(OrderSetting orderSetting);
    Long findCountByOrderDate(Date date);

    List<Reservations> getOrderSettingByMonth(Map map);


    OrderSetting findByOrderDate(Date orderDate);

    void editReservationsByOrderDate(OrderSetting orderSetting);
}
