package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomerReportController extends BaseController {

    /**
     * 统计报表（使用路径变量区别）
     *  type = 0
     *      客户贡献分析
     *  type = 1
     *      客户构成分析
     *  type = 2
     *      客户服务分析
     *  type = 3
     *      客户流失分析
     * @param type
     * @return
     */
    @RequestMapping("/report/{type}")
    public String index(@PathVariable(value = "type") Integer type) {
        //判断统计报表的类型
        if(type != null) {
            if(type == 0) {
                //客户贡献分析
                return "report/customer_contri";
            } else if(type == 1) {
                //客户构成分析
                return "report/customer_make";
            } else if(type == 2) {
                //客户服务分析
                return "report/customer_serve";
            } else if(type == 3) {
                //客户流失分析
                return "report/customer_loss";
            }
        }
        return "";
    }
}
