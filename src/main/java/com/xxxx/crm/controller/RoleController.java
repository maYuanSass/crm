package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.RoleQuery;
import com.xxxx.crm.service.RoleService;
import com.xxxx.crm.vo.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    /**
     * 根据userId查询对应的角色列表
     * @return
     */
    @RequestMapping("/queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer userId) {
        return roleService.queryAllRoles(userId);
    }

    /**
     * 分页条件查询角色数据
     * @param roleQuery
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    public Map<String,Object> selectByParams(RoleQuery roleQuery) {
        return roleService.queryByParamsForTable(roleQuery);
    }

    /**
     * 跳转角色管理页面
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "role/role";
    }

    /**
     * 添加操作
     * @param role
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultInfo addRole(Role role) {
        roleService.addRole(role);
        return success("添加成功");
    }

    /**
     * 打开添加或修改的页面
     * @return
     */
    @RequestMapping("/toAddOrUpdatePage")
    public String toAddOrUpdatePage(Integer roleId, HttpServletRequest request) {
        if(roleId != null) {
            Role role = roleService.selectByPrimaryKey(roleId);
            request.setAttribute("role",role);
        }
        return "role/add_update";
    }

    /**
     * 更新操作
     * @param role
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ResultInfo updateRole(Role role) {
        roleService.updateRole(role);
        return success("更新成功");
    }

    /**
     * 删除操作
     * @param roleId
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer roleId) {
        roleService.deleteRole(roleId);
        return success("删除成功");
    }

    /**
     * 角色授权
     * @param roleId
     * @param mIds
     * @return
     */
    @PostMapping("/addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer roleId, Integer[] mIds) {
        roleService.addGrant(roleId, mIds);
        return success("角色授权成功");
    }
}
