package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    void add(CheckGroup checkGroup,Integer[] checkitemIds);

    PageResult findPage(QueryPageBean queryPageBean);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup, Integer[] checkItemIds);

    List<CheckGroup> findAll();

    void delete(Integer id);
}
