package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.service.PermissionService;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;
    @Resource
    private PermissionService permissionService;

    /**
     * 跳转系统登录页
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * 跳转系统欢迎页
     * @return
     */
    @RequestMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    /**
     * 跳转后台管理主页面
     * @return
     */
    @RequestMapping("/main")
    public String main(HttpServletRequest request) {
        //获取cookie中的用户id的值
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //查询用户对象，设置session作用域
        User user = userService.selectByPrimaryKey(userId);
        request.getSession().setAttribute("user",user);

        /**
         * 在跳转主页时，需要知道当前登录用户拥有哪些资源权限
         */

        //通过当前登录用户id查询当前用户拥有的资源列表（查询对应资源的授权码）
        List<String> permissions = permissionService.queryUserHasRoleHasPermissionByUserId(userId);
        //设置到session作用域中
        request.getSession().setAttribute("permissions",permissions);

        return "main";
    }
}
