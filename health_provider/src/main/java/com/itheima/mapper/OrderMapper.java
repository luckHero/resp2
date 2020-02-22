package com.itheima.mapper;

import com.itheima.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderMapper {
    List<Order> findByCondition(Order order);

    void add(Order order);


    Map findById4Detial(Integer id);

    Integer findOrderCountByDate(String today);

    Integer findOrderCountAfterDate(String thisWeekMonday);

    Integer findVisitsCountByDate(String today);

    Integer findVisitsCountAfterDate(String thisWeekMonday);

    List<Map> findHotSetmeal();
}
