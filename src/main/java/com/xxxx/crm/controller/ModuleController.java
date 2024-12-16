package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.model.TreeModule;
import com.xxxx.crm.service.ModuleService;
import com.xxxx.crm.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/module")
public class ModuleController extends BaseController {

    @Resource
    private ModuleService moduleService;

    /**
     * 根据角色id查询对应的资源列表
     * @return
     */
    @RequestMapping("/queryAllModules")
    @ResponseBody
    public List<TreeModule> queryAllModules(Integer roleId) {
        return moduleService.queryAllModules(roleId);
    }

    /**
     * 打开角色授权页面
     * @param roleId
     * @return
     */
    @RequestMapping("/toAddGrantPage")
    public String toAddGrantPage(Integer roleId, HttpServletRequest request) {
        //将需要授权的角色id设置到请求域中
        request.setAttribute("roleId",roleId);
        return "role/grant";
    }

    /**
     * 查询所有资源数据
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryModuleList() {
        return moduleService.queryModuleList();
    }

    /**
     * 打开菜单管理页面
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "module/module";
    }

    /**
     * 添加操作
     * @param module
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultInfo addModule(Module module) {
        moduleService.addModule(module);
        return success("添加成功");
    }

    /**
     * 打开添加资源的页面
     * @param grade 层级
     * @param parentId 父菜单id
     * @return
     */
    @RequestMapping("/toAddModulePage")
    public String toAddModulePage(Integer grade, Integer parentId, HttpServletRequest request) {
        //将数据设置到请求域中
        request.setAttribute("grade",grade);
        request.setAttribute("parentId",parentId);
        return "module/add";
    }

    /**
     * 修改资源
     * @param module
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ResultInfo updateModule(Module module) {
        moduleService.updateModule(module);
        return success("修改成功");
    }

    /**
     * 删除资源
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultInfo deleteModule(Integer id) {
        moduleService.deleteModule(id);
        return success("删除成功");
    }

    /**
     * 打开修改资源的页面
     * @param id
     * @return
     */
    @RequestMapping("/toUpdateModulePage")
    public String toUpdateModulePage(Integer id, Model model) {
        //将要修改的资源对象设置到请求域中
        model.addAttribute("module", moduleService.selectByPrimaryKey(id));
        return "module/update";
    }
}
