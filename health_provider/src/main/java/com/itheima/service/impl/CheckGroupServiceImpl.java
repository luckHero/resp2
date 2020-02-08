package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.mapper.CheckGroupMapper;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Lucky
 * @Date:2020-02-07 12:53
 */
/*
    检查组的管理
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupMapper checkGroupMapper;

    //新增检查组,同时关联检查项
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupMapper.add(checkGroup); //添加检查组的那张表
        this.setCheckGroupAndCheckItem(checkGroup,checkitemIds);
    }
    //分页查询检查组
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage(); //获取分页参数的当前页码
        Integer pageSize = queryPageBean.getPageSize();//获取分页参数的显示条数
        String queryString = queryPageBean.getQueryString();//获取分页查询的查询条件
        PageHelper.startPage(currentPage,pageSize);//调用mybatis的插件

        //这里就是mybatis的插件提供的方法
        Page<CheckGroup> page=checkGroupMapper.findPage(queryString); //基于拦截器的自动在sql语句中添加limit关键字
        List<CheckGroup> rows = page.getResult(); //填充数据返回到controller
        long total = page.getTotal();
        return new PageResult(total,rows);
    }
    //根据id查询单个检查组
    @Override
    public CheckGroup findById(Integer id) {
       CheckGroup checkGroup= checkGroupMapper.findById(id);
        return checkGroup;
    }
    //根据检查组查询检查项
    @Override
    public List<Integer> findCheckItemByCheckGroupId(Integer id) {
        List<Integer> checkItemIds=checkGroupMapper.findCheckItemByCheckGroupId(id);
        return checkItemIds;
    }
    //编辑检查组的方法,同时关联检查项
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkItemIds) {
        //先添加t_checkgroup表
        checkGroupMapper.edit(checkGroup);
        //管理当前检查组的检查项
        checkGroupMapper.deleteAssociation(checkGroup.getId());
        //重新关联检查组的检查项
       this.setCheckGroupAndCheckItem(checkGroup,checkItemIds);
    }
    //查询所有的检查组
    @Override
    public List<CheckGroup> findAll() {
        List<CheckGroup> checkGroupList=checkGroupMapper.findAll();
        return checkGroupList;
    }
    //删除检查组
    @Override
    public void delete(Integer id) {
        checkGroupMapper.deleteAssociation(id);//先删除中间表的外键关联
        checkGroupMapper.delete(id);//在删除检出组的信息
    }

    //操作检查组合检查项的关联关系的方法
    public void setCheckGroupAndCheckItem(CheckGroup checkGroup,Integer[] checkItemIds){
        Integer id = checkGroup.getId(); //获取检查组的id
        //操作中间表
        if (checkItemIds != null && checkItemIds.length >0) {
            for (Integer checkitemId : checkItemIds) {
                Map<String, Integer> map = new HashMap<String, Integer>();//封装参数
                map.put("checkgroup_id", id);
                map.put("checkitem_id", checkitemId);
                System.out.println(map.get("checkgroup_id"));
                System.out.println(map.get("checkitem_id"));
                checkGroupMapper.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
