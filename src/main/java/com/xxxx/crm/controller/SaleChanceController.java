package com.xxxx.crm.controller;

import com.xxxx.crm.annotation.RequiredPermission;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.enums.StateStatus;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.utils.CookieUtil;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    /**
     * 分页条件查询营销机会数据列表     101001
     * @param saleChanceQuery
     * @param flag 如果flag的值不为空，并且值为1，则表示当前查询的是客户开发计划，否则查询营销机会
     * @return
     */
    @RequiredPermission(code = "101001")
    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery,
                                                      Integer flag, HttpServletRequest request) {
        if(flag != null && flag == 1) {
            //查询客户开发计划
            //1、设置分配状态
            saleChanceQuery.setState(StateStatus.STATED.getType());
            //2、设置指派人（当前登录用户的id）
            int userId = LoginUserUtil.releaseUserIdFromCookie(request);
            saleChanceQuery.setAssignMan(userId);
        }
        //查询营销机会数据
        return saleChanceService.querySaleChanceByParams(saleChanceQuery);
    }

    /**
     * 打开营销机会管理页面   1010
     * @return
     */
    @RequiredPermission(code = "1010")
    @RequestMapping("/index")
    public String index() {
        return "saleChance/sale_chance";
    }

    /**
     * 添加操作     101002
     * @param saleChance
     * @return
     */
    @RequiredPermission(code = "101002")
    @RequestMapping("/add")
    @ResponseBody
    public ResultInfo addSaleChance(SaleChance saleChance, HttpServletRequest request) {
        //从cookie中获取当前登录的userName
        String userName = CookieUtil.getCookieValue(request, "userName");
        //设置创建人到saleChance对象中
        saleChance.setCreateMan(userName);
        //调用service层的添加方法
        saleChanceService.addSaleChance(saleChance);
        return success("添加成功");
    }

    /**
     * 打开添加或者修改页面
     * @return
     */
    @RequestMapping("/toSaleChancePage")
    public String toSaleChancePage(Integer saleChanceId, HttpServletRequest request) {
        //判断saleChanceId是否为空
        if(saleChanceId != null) {
            //通过id查询
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(saleChanceId);
            //将数据设置到request域中
            request.setAttribute("saleChance",saleChance);
        }
        return "saleChance/add_update";
    }

    /**
     * 修改操作     101004
     * @param saleChance
     * @return
     */
    @RequiredPermission(code = "101004")
    @PostMapping("/update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance) {
        //调用service层的添加方法
        saleChanceService.updateSaleChance(saleChance);
        return success("更新成功");
    }

    /**
     * 批量删除操作   101003
     * @param ids
     * @return
     */
    @RequiredPermission(code = "101003")
    @PostMapping("/delete")
    @ResponseBody
    public ResultInfo deleteSalChance(Integer[] ids) {
        saleChanceService.deleteSaleChance(ids);
        return success("删除成功");
    }

    /**
     * 修改营销机会的开发状态
     * @param id
     * @param devResult
     * @return
     */
    @PostMapping("/updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id,Integer devResult) {
        saleChanceService.updateSaleChanceDevResult(id,devResult);
        return success("开发状态更新成功");
    }

}
