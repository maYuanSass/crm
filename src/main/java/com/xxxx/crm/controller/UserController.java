package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     * @param userName
     * @param userPwd
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public ResultInfo userLogin(String userName, String userPwd) {
        ResultInfo resultInfo = new ResultInfo();
        //调用service层的登录方法
        UserModel userModel = userService.userLogin(userName, userPwd);
        //设置ResultInfo的result值（将数据返回给请求）
        resultInfo.setResult(userModel);
        return resultInfo;
    }

    /**
     * 修改密码
     * @param request
     * @param oldPassword
     * @param newPassword
     * @param repeatPassword
     * @return
     */
    @PostMapping("/updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request, String oldPassword,
                                         String newPassword, String repeatPassword) {
        ResultInfo resultInfo = new ResultInfo();
        //获取cookie中的用户id
        Integer userId  = LoginUserUtil.releaseUserIdFromCookie(request);
        //调用service层的修改密码方法
        userService.updatePassword(userId, oldPassword, newPassword, repeatPassword);
        return resultInfo;
    }

    /**
     * 打开修改密码页面
     * @return
     */
    @RequestMapping("/toPasswordPage")
    public String toPasswordPage() {
        return "user/password";
    }

    /**
     * 打开基本资料页面
     * @return
     */
    @RequestMapping("/toSettingPage")
    public String toSettingPage(HttpServletRequest request) {
        //获取cookie中的用户id
        Integer userId  = LoginUserUtil.releaseUserIdFromCookie(request);
        User user = userService.selectByPrimaryKey(userId);
        request.setAttribute("user",user);
        return "user/info";
    }

    /**
     * 查询所有的销售人员
     * @return
     */
    @RequestMapping("/queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales() {
        return userService.queryAllSales();
    }

    /**
     * 分页条件查询用户列表
     * @param userQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> selectByParams(UserQuery userQuery) {
        return userService.queryByParamsForTable(userQuery);
    }

    /**
     * 打开用户列表页面
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "user/user";
    }

    /**
     * 添加操作
     * @param user
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultInfo addUser(User user) {
        userService.addUser(user);
        return success("添加成功");
    }

    /**
     * 打开添加或修改页面
     * @return
     */
    @RequestMapping("/toAddOrUpdateUserPage")
    public String toAddOrUpdateUserPage(Integer id,HttpServletRequest request) {
        if(id != null) {
            User user = userService.selectByPrimaryKey(id);
            request.setAttribute("userInfo",user);
        }
        return "user/add_update";
    }

    /**
     * 修改操作
     * @param user
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ResultInfo updateUser(User user) {
        userService.updateUser(user);
        return success("修改成功");
    }

    /**
     * 删除操作
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids) {
        userService.deleteUser(ids);
        return success("删除成功");
    }

    /**
     * 查询所有的客户经理
     * @return
     */
    @GetMapping("/queryAllCustomerManagers")
    @ResponseBody
    public List<Map<String,Object>> queryAllCustomerManagers() {
        return userService.queryAllCustomerManagers();
    }
}
