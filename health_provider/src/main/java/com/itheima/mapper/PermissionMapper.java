package com.itheima.mapper;

import com.itheima.pojo.Permission;

import java.util.Set;

public interface PermissionMapper {
    Set<Permission> findByRoleId(Integer roleId);
}
