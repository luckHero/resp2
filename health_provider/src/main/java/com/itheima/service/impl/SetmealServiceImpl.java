package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.mapper.SetmealMapper;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Lucky
 * @Date:2020-02-08 22:19
 */
/*
    套餐服务管理
 */
@Service(interfaceClass = SetmealService.class) //这里如果不加这个注解会抛什么异常?
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private JedisPool jedisPool;
    //新增套餐
    @Override
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        setmealMapper.add(setmeal);//往套餐表中添加数据
        this.setSetmealAndCheckGroup(setmeal,checkGroupIds);//操作中间表

        String imgFileName = setmeal.getImg();//获取照片的文件名
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,imgFileName);//将保存到数据库的文件名称页存到redis数据库中
    }
    //分页展示套餐的方法
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();//当前页码
        Integer pageSize = queryPageBean.getPageSize();//每页显示的记录数
        String queryString = queryPageBean.getQueryString();//查询的条件
        PageHelper.startPage(currentPage,pageSize);//使用mybatis的插件,他会自动在sql上去添加limit关键字
        Page<Setmeal> page=setmealMapper.findPage(queryString);//返回的是mybais封装好的list集合,该集合的属性就有分页展示的数据
        return new PageResult(page.getTotal(),page.getResult());
    }

    //操作套餐表和检查组表的方法
    public void setSetmealAndCheckGroup(Setmeal setmeal ,Integer[] checkGroupIds){
        Integer setmealId = setmeal.getId();//获取setmealId
        //添加中间表中关联,用map集合来封装参数条件
        for (Integer checkGroupId : checkGroupIds) {
            Map<String,Integer> map=new HashMap<String,Integer>();
            map.put("setmealId",setmealId);
            map.put("checkGroupId",checkGroupId);
            setmealMapper.setSetmealAndCheckGroup(map);
        }
    }
}
