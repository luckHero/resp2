package com.itheima.mapper;

import com.github.pagehelper.Page;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * @Author: Lucky
 * @Date:2020-01-20 16:16
 */

public interface CheckItemMapper {
    void add(CheckItem checkItem);

    Page<CheckItem> pageQuery(String queryString);

    long findCountByCheckItemId(Integer id); //查询检查项是否关联检查组

    void delete(Integer id);

    void edit(CheckItem checkItem);

    CheckItem findById(Integer id);

    List<CheckItem> findAll();
}
