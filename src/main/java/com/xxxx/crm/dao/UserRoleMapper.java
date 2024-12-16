package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {

    //根据用户id查询对应的用户记录
    Integer countUserRoleByUserId(Integer userId);

    //根据用户id删除角色记录
    Integer deleteByUserId(Integer userId);
}