package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.mapper.MemberMapper;
import com.itheima.mapper.OrderMapper;
import com.itheima.mapper.OrderSettingMapper;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderDetail;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: Lucky
 * @Date:2020-02-16 12:13
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingMapper orderSettingMapper; //预约设置
    @Autowired
    private MemberMapper memberMapper;//操作会员的那张表
    @Autowired
    private OrderMapper orderMapper; //预约套餐的那张表

    //vx预约的方法
    @Override
    public Result order(OrderDetail orderDetail) throws Exception {

        //获取预约日期,转换成Date类型
        Date orderDate = DateUtils.parseString2Date(orderDetail.getOrderDate());//获取预约日期,将日期转换成Date
        //1.根据日期查询orderSetting 是否当天可以预约
        OrderSetting orderSetting = orderSettingMapper.findByOrderDate(orderDate);
        if (orderSetting == null) {//表示当前日期没有预约,预约失败
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //2.判断当天日期有没有约满
        if (orderSetting.getReservations() >= orderSetting.getNumber()) { //人数已经满了
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //3.判断用户是否在一天预约同一个套餐
        String telephone = orderDetail.getTelephone();
        Member member = memberMapper.findByTelephone(telephone);//根据手机号查询是不是会员
        if (member != null) {//是会员
            Integer memberId = member.getId();//会员的id
            String setmealId = orderDetail.getSetmealId();//套餐的id
            Order order = new Order(memberId, orderDate, Integer.parseInt(setmealId));//预约对象
            List<Order> list = orderMapper.findByCondition(order);//根据会员的id,预约日期,套餐的id判断是否预约同一个套餐
            if (list != null && list.size() > 0) { //用户在重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        } else {//不是会员,这是这个member是空
//            member = new Member();  这里的会员不需要创建了,
            member.setName(orderDetail.getName());
            member.setIdCard(orderDetail.getIdCard());
            member.setPhoneNumber(orderDetail.getTelephone());
            member.setSex(orderDetail.getSex());
            member.setRegTime(new Date());
            memberMapper.add(member);//完成会员注册
        }

        //4.预约成功更新预约人数
        //在预约表中添加信息
        Order order = new Order();
        order.setMemberId(member.getId()); //设置会员Id
        order.setOrderDate(orderDate);//预约日期
        order.setOrderType(orderDetail.getOrderType());//预约类型
        order.setOrderStatus(Order.ORDERSTATUS_NO);//到诊状态
        order.setSetmealId(Integer.parseInt(orderDetail.getSetmealId()));//套餐的ID
        orderMapper.add(order);//保存预约信息
        orderSetting.setReservations(orderSetting.getReservations() + 1);//预约完成后,设置当天预约人数
        orderSettingMapper.editReservationsByOrderDate(orderSetting);
        return new Result(true, MessageConstant.ORDER_SUCCESS, order.getId());
    }

    @Override
    public Map findById(Integer id)throws Exception {//体检人  体检套餐  体检日期  预约类型
        Map map = orderMapper.findById4Detial(id);
        if (map != null) {
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate", DateUtils.parseDate2String(orderDate));
        }

        return map;
    }


}
