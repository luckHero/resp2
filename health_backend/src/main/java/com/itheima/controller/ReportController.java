package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import javafx.beans.binding.ObjectExpression;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: Lucky
 * @Date:2020-02-21 11:18
 */
/*
    统计数量报表
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    MemberService memberService;
    @Reference
    SetmealService setmealService;
    @Reference
    private ReportService reportService;

    /*
        统计会员的controller
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        Map<String, Object> map = new HashMap<String, Object>();//返回到前台数据的Map集合
        List<String> months = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();//获得日历对象，模拟时间就是当前
        calendar.add(Calendar.MONTH, -12);//获得当前时间往前推12个月的时间

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM");

        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);//往后退一个月
            Date date = calendar.getTime(); //获取当前时间
            months.add(simpleDateFormat.format(date));
        }
        map.put("months", months);
        try {
            List<Integer> memberCount = memberService.findMemberCountByMonth(months);
            map.put("memberCount", memberCount);
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    /*
        套餐占比情况
        前端数据的格式是Map<String,Object>
        setmealName:"套餐名称,套餐名称2"
        setmealCount:[
        {name:"套餐名称",value:"套餐数量"},]
        {name:"套餐名称2",value:"套餐数量2"},]
        {name:"套餐名称3",value:"套餐数量3"},]

     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        Map<String, Object> data = new HashMap<String, Object>(); //这里的map是返回到前端数据的
        try {
            List<Map<String, Object>> setmealCount = setmealService.findSetmealCount(); //套餐的数据名称和预约的数量
            List<String> list = new ArrayList<String>();//套餐名称

            for (Map<String, Object> map : setmealCount) {
                String setmealName = (String) map.get("name");//套餐名称
                list.add(setmealName);
                data.put("setmealName", setmealName);
            }
            data.put("setmealCount", setmealCount);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    /*
        运营数据的统计
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map<String, Object> map = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
}
