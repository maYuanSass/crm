package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.CustomerLossQuery;
import com.xxxx.crm.service.CustomerLossService;
import com.xxxx.crm.vo.CustomerLoss;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/customer_loss")
public class CustomerLossController extends BaseController {

    @Resource
    private CustomerLossService customerLossService;

    /**
     * 打开客户流失管理页面
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "customerLoss/customer_loss";
    }

    /**
     * 分页条件查询客户流失数据
     * @param customerLossQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryCustomerLossByParams(CustomerLossQuery customerLossQuery) {
        return customerLossService.queryCustomerLossByParams(customerLossQuery);
    }

    /**
     * 打开添加暂缓或者详情页面
     * @param lossId
     * @return
     */
    @RequestMapping("/toCustomerLossPage")
    public String toCustomerLossPage(Integer lossId, Model model) {
        //通过流失客户id查询对应的流失客户的记录
        CustomerLoss customerLoss = customerLossService.selectByPrimaryKey(lossId);
        model.addAttribute("customerLoss",customerLoss);
        return "customerLoss/customer_rep";
    }

    /**
     * 客户确认流失操作（更新流失客户的流失状态）
     * @param id
     * @param lossReason
     * @return
     */
    @PostMapping("/updateCustomerLossStateById")
    @ResponseBody
    public ResultInfo updateCustomerLossStateById(Integer id, String lossReason) {
        customerLossService.updateCustomerLossStateById(id,lossReason);
        return success("确认流失成功");
    }
}
