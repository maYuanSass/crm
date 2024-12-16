package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.CustomerQuery;
import com.xxxx.crm.service.CustomerService;
import com.xxxx.crm.vo.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController {

    @Resource
    private CustomerService customerService;

    /**
     * 多条件分页查询客户信息数据
     * @param customerQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryCustomerByParams(CustomerQuery customerQuery) {
        return customerService.queryCustomerByParams(customerQuery);
    }

    /**
     * 打开客户信息管理页面
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "customer/customer";
    }

    /**
     * 添加客户信息
     * @param customer
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultInfo addCustomer(Customer customer) {
        customerService.addCustomer(customer);
        return success("添加成功");
    }

    /**
     * 打开添加或修改页面
     * @return
     */
    @RequestMapping("/toAddOrUpdateCustomerPage")
    public String toAddOrUpdateCustomerPage(Integer id, HttpServletRequest request) {
        //如果id不为空，则查询客户记录
        if(id != null) {
            Customer customer = customerService.selectByPrimaryKey(id);
            request.setAttribute("customer",customer);
        }
        return "customer/add_update";
    }

    /**
     * 修改客户信息
     * @param customer
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ResultInfo updateCustomer(Customer customer) {
        customerService.updateCustomer(customer);
        return success("修改成功");
    }

    /**
     * 删除客户信息
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultInfo deleteCustomer(Integer id) {
        customerService.deleteCustomer(id);
        return success("删除成功");
    }

    /**
     * 打开客户订单查看页面
     * @param customerId
     * @param model
     * @return
     */
    @RequestMapping("/toCustomerOrderPage")
    public String toCustomerOrderPage(Integer customerId, Model model) {
        Customer customer = customerService.selectByPrimaryKey(customerId);
        model.addAttribute("customer", customer);
        return "customer/customer_order";
    }

    /**
     * 查看客户贡献数据
     * @param customerQuery
     * @return
     */
    @RequestMapping("queryCustomerContributionByParams")
    @ResponseBody
    public Map<String,Object> queryCustomerContributionByParams(CustomerQuery customerQuery) {
        return customerService.queryCustomerContributionByParams(customerQuery);
    }

    /**
     * 查询客户构成分析（折线图数据处理）
     * @return
     */
    @RequestMapping("/countCustomerMake")
    @ResponseBody
    public Map<String,Object> countCustomerMake() {
        return customerService.countCustomerMake();
    }

    /**
     * 查询客户构成分析（饼状图数据处理）
     * @return
     */
    @RequestMapping("/countCustomerMake02")
    @ResponseBody
    public Map<String,Object> countCustomerMake02() {
        return customerService.countCustomerMake02();
    }

    /**
     * 查询客户服务分析（柱状图数据处理）
     * @return
     */
    @RequestMapping("/countCustomerServe")
    @ResponseBody
    public Map<String,Object> countCustomerServe() {
        return customerService.countCustomerServe();
    }
}
