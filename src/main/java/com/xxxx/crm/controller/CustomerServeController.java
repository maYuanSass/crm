package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.CustomerServeQuery;
import com.xxxx.crm.service.CustomerServeService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.CustomerServe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/customer_serve")
public class CustomerServeController extends BaseController {

    @Resource
    private CustomerServeService customerServeService;

    /**
     * 分页多条件查询客户服务数据
     * @param customerServeQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryCustomerServeByParams(CustomerServeQuery customerServeQuery,
                                                         Integer flag, HttpServletRequest request) {
        //判断是否执行服务处理，如果是则查询分配给当前登录用户的服务记录
        if(flag != null && flag == 1) {
            //设置查询条件：分配人
            customerServeQuery.setAssigner(LoginUserUtil.releaseUserIdFromCookie(request));
        }
        return customerServeService.queryCustomerServeByParams(customerServeQuery);
    }

    /**
     * 打开客户服务创建、服务分配、服务处理、服务反馈、服务归档页面
     * @param type
     * @return
     */
    @RequestMapping("/index/{type}")
    public String index(@PathVariable(value = "type") Integer type) {
        //判断类型是否为空
        if(type != null) {
            if(type == 1) {
                //进入服务创建页面
                return "customerServe/customer_serve";
            } else if(type == 2) {
                //进入服务分配页面
                return "customerServe/customer_serve_assign";
            } else if(type == 3) {
                //进入服务处理页面
                return "customerServe/customer_serve_proce";
            } else if(type == 4) {
                //进入服务反馈页面
                return "customerServe/customer_serve_feed_back";
            } else if(type == 5) {
                //进入服务归档页面
                return "customerServe/customer_serve_archive";
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * 打开服务添加页面
     * @return
     */
    @RequestMapping("/toAddCustomerServePage")
    public String toAddCustomerServePage() {
        return "customerServe/customer_serve_add";
    }

    /**
     * 服务添加操作
     * @param customerServe
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultInfo addCustomerServe(CustomerServe customerServe) {
        customerServeService.addCustomerServe(customerServe);
        return success("条件成功");
    }

    /**
     * 服务更新操作（服务分配、服务处理、服务反馈）
     * @param customerServe
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResultInfo updateCustomerServe(CustomerServe customerServe) {
        customerServeService.updateCustomerServe(customerServe);
        return success("更新成功");
    }

    /**
     * 打开服务分配页面
     * @param id
     * @return
     */
    @RequestMapping("/toCustomerServeAssignPage")
    public String toCustomerServeAssignPage(Integer id, Model model) {
        //通过id查询服务记录
        CustomerServe customerServe = customerServeService.selectByPrimaryKey(id);
        //设置到请求域中
        model.addAttribute("customerServe",customerServe);
        return "customerServe/customer_serve_assign_add";
    }

    /**
     * 打开服务处理页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/toCustomerServeProcePage")
    public String toCustomerServeProcePage(Integer id, Model model) {
        //通过id查询服务记录
        CustomerServe customerServe = customerServeService.selectByPrimaryKey(id);
        //设置到请求域中
        model.addAttribute("customerServe",customerServe);
        return "customerServe/customer_serve_proce_add";
    }

    /**
     * 打开服务反馈页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/toCustomerServeFeedBackPage")
    public String toCustomerServeFeedBackPage(Integer id,Model model) {
        //通过id查询服务记录
        CustomerServe customerServe = customerServeService.selectByPrimaryKey(id);
        //设置到请求域中
        model.addAttribute("customerServe",customerServe);
        return "customerServe/customer_serve_feed_back_add";
    }
}
