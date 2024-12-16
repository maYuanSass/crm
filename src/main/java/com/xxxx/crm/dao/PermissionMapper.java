package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {

    //通过角色id查询该角色对应权限记录
    Integer countPermissionByRoleId(Integer roleId);

    //通过角色id删除权限记录
    void deletePermissionByRoleId(Integer roleId);

    //查询角色拥有的所有的资源id
    List<Integer> queryRoleHasModuleIdByRoleId(Integer roleId);

    //通过用户id查询对应的资源权限列表
    List<String> queryUserHasRoleHasPermissionByUserId(Integer userId);

    //通过资源id查询权限记录
    Integer countPermissionByModuleId(Integer id);

    //通过资源id删除权限记录
    void deletePermissionByModuleId(Integer id);
}