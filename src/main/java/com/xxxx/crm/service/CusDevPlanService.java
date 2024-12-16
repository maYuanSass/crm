package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CusDevPlanMapper;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.CusDevPlan;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan,Integer> {

    @Resource
    private CusDevPlanMapper cusDevPlanMapper;
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询营销机会管理数据列表
     *  返回的数据格式必须要满足layui中的数据表格要求的格式，否则显示不出来
     * @param cusDevPlanQuery
     * @return
     */
    public Map<String,Object> queryCusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery) {
        Map<String,Object> map = new HashMap<>();
        //1、开启分页功能
        PageHelper.startPage(cusDevPlanQuery.getPage(),cusDevPlanQuery.getLimit());
        //2、调用dao层的查询方法，返回查询结果
        List<CusDevPlan> cusDevPlans = cusDevPlanMapper.selectByParams(cusDevPlanQuery);
        //3、使用pageInfo封装查询结果
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlans);
        //4、设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加客户开发计划
     * @param cusDevPlan
     */
    @Transactional
    public void addCusDevPlan(CusDevPlan cusDevPlan) {
        //1、参数校验
        checkCusDevPlanParams(cusDevPlan);
        //2、设置相关字段的默认值
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
        //3、执行添加操作，判断受影响的行数
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan)!=1,"添加失败");
    }

    /**
     * 添加操作的参数校验
     * @param cusDevPlan
     */
    private void checkCusDevPlanParams(CusDevPlan cusDevPlan) {
        //营销机会id    非空
        Integer saleChanceId = cusDevPlan.getSaleChanceId();
        AssertUtil.isTrue(saleChanceId == null
                && saleChanceMapper.selectByPrimaryKey(saleChanceId) == null,"数据异常，请重试");
        //计划项内容     非空
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()),"计划项内容不能为空");
        //计划时间      非空
        AssertUtil.isTrue(null == cusDevPlan.getPlanDate(), "计划时间不能为空");
    }

    /**
     * 更新操作
     * @param cusDevPlan
     */
    @Transactional
    public void updateCusDevPlan(CusDevPlan cusDevPlan) {
        //1、参数校验
        AssertUtil.isTrue(null == cusDevPlan.getId()
                || cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId()) == null,"数据异常，请重试");
        checkCusDevPlanParams(cusDevPlan);
        //2、设置相关字段的默认值
        cusDevPlan.setUpdateDate(new Date());
        //3、执行更新操作，判断受影响的行数
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1,"更新失败");
    }

    /**
     * 删除操作
     * @param id
     */
    public void deleteCusDevPlan(Integer id) {
        //1、判断id是否为空，且数据存在
        AssertUtil.isTrue(null == id,"待删除记录不存在");
        //2、通过id查询计划项对象
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        //设置 is_valid = 0
        cusDevPlan.setIsValid(0);
        cusDevPlan.setUpdateDate(new Date());
        //执行更新操作
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1,"删除失败");
    }
}
