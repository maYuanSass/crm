package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.query.DataDicQuery;
import com.xxxx.crm.service.DataDicService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/data_dic")
public class DataDicController extends BaseController {

    @Resource
    private DataDicService dataDicService;

    /**
     * 打开字典管理页面
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "dataDic/data_dic";
    }

    /**
     * 分页条件查询字典管理数据
     * @param dataDicQuery
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> queryDataDicByParams(DataDicQuery dataDicQuery) {
        return dataDicService.queryDataDicByParams(dataDicQuery);
    }
}
