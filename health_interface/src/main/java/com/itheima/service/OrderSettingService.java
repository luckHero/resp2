package com.itheima.service;

import com.itheima.pojo.OrderSetting;
import com.itheima.pojo.Reservations;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void add(List<OrderSetting> data);

    List<Reservations> getOrderSettingByMonth(String data);

    void editNumberByDate(OrderSetting orderSetting);
}
