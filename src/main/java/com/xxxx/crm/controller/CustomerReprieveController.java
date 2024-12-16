package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.CustomerReprieveQuery;
import com.xxxx.crm.service.CustomerReprieveService;
import com.xxxx.crm.vo.CustomerReprieve;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/customer_rep")
public class CustomerReprieveController extends BaseController {

    @Resource
    private CustomerReprieveService customerReprieveService;

    /**
     * 分页条件查询暂缓措施数据
     * @param customerReprieveQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryCustomerReprieveByParams(CustomerReprieveQuery customerReprieveQuery) {
        return customerReprieveService.queryCustomerReprieveByParams(customerReprieveQuery);
    }

    /**
     * 添加操作
     * @param customerReprieve
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultInfo addCustomerRepr(CustomerReprieve customerReprieve) {
        customerReprieveService.addCustomerRepr(customerReprieve);
        return success("添加成功");
    }

    /**
     * 修改操作
     * @param customerReprieve
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public ResultInfo updateCustomerRepr(CustomerReprieve customerReprieve) {
        customerReprieveService.updateCustomerRepr(customerReprieve);
        return success("修改成功");
    }

    /**
     * 打开添加或修改暂缓数据的页面
     * @param lossId
     * @return
     */
    @RequestMapping("/toAddOrUpdateCustomerReprPage")
    public String toAddOrUpdateCustomerReprPage(Integer lossId, Model model, Integer id) {
        //将流失客户id存到request作用域中
        model.addAttribute("lossId",lossId);

        //通过主键id查询暂缓数据
        if(id != null) {
            CustomerReprieve customerRep = customerReprieveService.selectByPrimaryKey(id);
            model.addAttribute("customerRep",customerRep);
        }
        return "customerLoss/customer_rep_add_update";
    }

    /**
     * 删除操作
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResultInfo deleteCustomerRepr(Integer id) {
        customerReprieveService.deleteCustomerRepr(id);
        return success("删除成功");
    }
}
