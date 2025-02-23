package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.ModuleMapper;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.dao.RoleMapper;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Permission;
import com.xxxx.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private ModuleMapper moduleMapper;

    /**
     * 根据userId查询对应的角色列表
     * @return
     */
    public List<Map<String,Object>> queryAllRoles(Integer userId) {
        return roleMapper.queryAllRoles(userId);
    }

    /**
     * 添加操作
     * @param role
     */
    @Transactional
    public void addRole(Role role) {
        //1、参数校验
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名称不能为空");
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp != null, "角色名称已存在，请重新输入");
        //2、设置参数的默认值
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        //3、执行添加操作，判断受影响的行数
        AssertUtil.isTrue(roleMapper.insertSelective(role) != 1,"添加失败");
    }

    /**
     * 修改操作
     * @param role
     */
    @Transactional
    public void updateRole(Role role) {
        //1、参数校验
        AssertUtil.isTrue(null == role.getId(), "待更新记录不存在！");
        Role temp = roleMapper.selectByPrimaryKey(role.getId());
        AssertUtil.isTrue(null == temp, "待更新记录不存在");
        //角色名称   非空，名称唯一
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "角色名称不能为空！");
        temp = roleMapper.selectByRoleName(role.getRoleName());
        //判断角色记录是否存在（如果不存在，表示可使用；如果存在，且角色ID与当前更新的角色ID不一致，表示角色名称不可用）
        AssertUtil.isTrue(null != temp && (!temp.getId().equals(role.getId())), "角色名称已存在，不可使用！");

        //2、设置参数的默认值
        role.setUpdateDate(new Date());

        //3、执行更新操作，判断受影响的行数
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1, "修改角色失败！");
    }

    /**
     * 删除操作
     * @param roleId
     * @return void
     */
    @Transactional
    public void deleteRole(Integer roleId) {
        //1、参数校验
        AssertUtil.isTrue(null == roleId, "待删除记录不存在！");
        Role role = roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(null == role, "待删除记录不存在！");
        //2、设置删除状态
        role.setIsValid(0);
        role.setUpdateDate(new Date());
        //3、执行更新操作
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1, "角色删除失败！");
    }

    /**
     * 角色授权
     *  最终会将角色id和资源id保存到权限表t_permission
     *      直接添加权限：不合适，会出现重复的权限数据（执行修改权限操作后，删除某个权限后却又不完全删除时，前端就会传递数据库中已经存在的权限数据）
     *      推荐使用：
     *          先将原有的所有权限记录全部删除，再将需要设置的权限记录进行添加（类比用户关联角色操作）
     *              1、通过角色id查询该角色对应权限记录
     *              2、如果权限存在，则删除对应的角色拥有的权限记录
     *              3、如果有权限记录，则添加权限记录（批量添加）
     * @param roleId
     * @param mIds
     */
    @Transactional
    public void addGrant(Integer roleId, Integer[] mIds) {
        //1、通过角色id查询该角色对应权限记录
        Integer count = permissionMapper.countPermissionByRoleId(roleId);
        //2、如果权限存在，则删除对应的角色拥有的权限记录
        if(count > 0) {
            //删除权限记录
            permissionMapper.deletePermissionByRoleId(roleId);
        }
        //3、如果有权限记录，则添加权限记录（批量添加）
        if(mIds != null && mIds.length > 0) {
            //定义一个Permission集合
            List<Permission> list = new ArrayList<>();
            //遍历mIds
            for(Integer mId : mIds) {
                Permission permission = new Permission();
                permission.setRoleId(roleId);
                permission.setModuleId(mId);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mId).getOptValue()); //授权码
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                //将对象设置到集合中
                list.add(permission);
            }
            //执行批量添加的操作，判断受影响的行数
            AssertUtil.isTrue(permissionMapper.insertBatch(list) != list.size(),"角色授权失败");
        }
    }
}
