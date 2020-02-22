package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    void add(Setmeal setmeal, Integer[] checkGroupIds);

    PageResult findPage(QueryPageBean queryPageBean);

    Setmeal findById(Integer id);

    List<Integer> findCheckGroupBySetmealId(Integer id);

    void edit(Setmeal setmeal, Integer[] checkGroupIds);

    void delete(Integer id);

    List<Setmeal> findAll();

    Setmeal findSetmealById(int id);

    List<Map<String, Object>> findSetmealCount();
}
