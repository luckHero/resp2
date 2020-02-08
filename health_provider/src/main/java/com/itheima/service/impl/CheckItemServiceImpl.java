package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.mapper.CheckItemMapper;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: Lucky
 * @Date:2020-01-20 16:13
 */
/*
    检查项的服务
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired  //注入实现持久层的mapper
    private CheckItemMapper checkItemMapper;

    // 添加检查项的方法
    @Override
    public void add(CheckItem checkItem) {
      //  System.out.println(checkItem);
        checkItemMapper.add(checkItem);
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();    //获取分页的当前页数
        Integer pageSize = queryPageBean.getPageSize();          //获取分页的每页记录数
        String queryString = queryPageBean.getQueryString();     //分页的查询条件
        // 调用mybatis的分页插件,这里不需要返回值
        PageHelper.startPage(currentPage,pageSize);
        //调用mapper层的方法,返回一个关于CheckItem的 继承ArrayList 的集合
        Page<CheckItem> page=checkItemMapper.pageQuery(queryString);
        List<CheckItem> rows = page.getResult();   //展示到页面的list集合
        long total = page.getTotal();
        return new PageResult(total,rows);
    }

    //删除检查项的方法
    @Override
    public void delete(Integer id) {
        //判断检查项是否关联检查组

        long count = checkItemMapper.findCountByCheckItemId(id);
        if(count>0){
            //检查项关联到检查组了,不允许删除
            throw new RuntimeException();
        }
        checkItemMapper.delete(id);
    }
    //数据回显的方法
    @Override
    public CheckItem findById(Integer id) {
        CheckItem checkItem=checkItemMapper.findById(id);
        return checkItem;
    }

    //编辑的方法
    @Override
    public void edit(CheckItem checkItem) {
        checkItemMapper.edit(checkItem);
    }

    //新增检查组查询所有检查项的方法
    @Override
    public List<CheckItem> findAll() {
        List<CheckItem> list=checkItemMapper.findAll();
        return list;
    }
}
