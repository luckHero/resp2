package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.PermissionMapper;
import com.itheima.mapper.RoleMapper;
import com.itheima.mapper.UserMapper;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @Author: Lucky
 * @Date:2020-02-20 17:37
 */
/*
    用户认证授权的服务
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;//用户Mapper
    @Autowired
    private RoleMapper roleMapper; //角色Mapper
    @Autowired
    private PermissionMapper permissionMapper;//权限Mapper

    @Override
    public User findByUserName(String userName) {
        User user = userMapper.findByUserName(userName);
        if (user == null) {
            return null;
        }
        Integer userId = user.getId();//获取用户的Id
        Set<Role> roles = roleMapper.findByUserId(userId); //根据用户Id查询角色
        if (roles != null) {
            for (Role role : roles) {
                Set<Permission> permissions = permissionMapper.findByRoleId(role.getId());
                role.setPermissions(permissions); //角色关联权限
            }
        }
        user.setRoles(roles);//用户关联角色
        return user;
    }
}
