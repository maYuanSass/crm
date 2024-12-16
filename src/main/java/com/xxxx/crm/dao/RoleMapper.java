package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {

    //根据userId查询对应的角色列表（只需要id与roleName）
    List<Map<String,Object>> queryAllRoles(Integer userId);

    //根据角色名查询角色记录
    Role selectByRoleName(String roleName);
}