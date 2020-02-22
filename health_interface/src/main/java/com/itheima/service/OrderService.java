package com.itheima.service;

import com.itheima.entity.Result;
import com.itheima.pojo.OrderDetail;

import java.util.Map;

public interface OrderService  {
    Result order(OrderDetail orderDetail)throws Exception;

    Map findById(Integer id)throws Exception;
}
