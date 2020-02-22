package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.pojo.Reservations;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @Author: Lucky
 * @Date:2020-02-09 23:16
 */
@RestController
@RequestMapping("/ordersetting")
public class OrederSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    //文件上传,实现预约数据批量导入
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) {
        //解析上传文件
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);//使用POI解析EXCEl数据
            List<OrderSetting> listOrderSetting = new ArrayList<OrderSetting>();//Excel表中的数据封装到集合发布到Dubbo上
            for (String[] strings : list) {
                String orderDate = strings[0];//这是获取每一行的预约日期
                String number = strings[1];//这是获取每一行的预约人数
                //封装Excel表中每一行的数据
                //  System.out.println(orderDate);
                Date date = new Date(orderDate);
                //  System.out.println(date);
                OrderSetting orderSetting = new OrderSetting(date, Integer.parseInt(number));//这里的日期有bug
                System.out.println(orderSetting.getOrderDate());
                listOrderSetting.add(orderSetting);
            }
            orderSettingService.add(listOrderSetting);
        } catch (Exception e) {
            e.printStackTrace();
            //文件解析失败
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS); //预约成功
    }

    //根据月份查询对应的预约设置的数据
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date) {//date格式 YY-MM
        try {
            List<Reservations> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    //用户修改可预约人数的方法
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        try {
            orderSettingService.editNumberByDate(orderSetting);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);//预约失败
        }
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
    }

}
