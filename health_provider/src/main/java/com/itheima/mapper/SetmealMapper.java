package com.itheima.mapper;


import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.Map;

public interface SetmealMapper {

    void add(Setmeal setmeal);
    void setSetmealAndCheckGroup(Map map);

    Page<Setmeal> findPage(String queryString);
}
