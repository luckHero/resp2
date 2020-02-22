package com.itheima.mapper;


import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealMapper {

    void add(Setmeal setmeal);
    void setSetmealAndCheckGroup(Map map);

    Page<Setmeal> findPage(String queryString);

    Setmeal findById(Integer id);

    List<Integer> findCheckGroupBySetmealId(Integer id);

    void edit(Setmeal setmeal);

    void deleteAssociation(Integer id);

    void delete(Integer id);

    List<Setmeal> findAll();

    Setmeal findSetmealById(int id);

    List<Map<String, Object>> findSetmealCount();
}
