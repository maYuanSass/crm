package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CustomerLossMapper;
import com.xxxx.crm.query.CustomerLossQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.CustomerLoss;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerLossService extends BaseService<CustomerLoss,Integer> {

    @Resource
    private CustomerLossMapper customerLossMapper;

    /**
     * 分页条件查询客户流失数据列表
     * @param customerLossQuery
     * @return
     */
    public Map<String, Object> queryCustomerLossByParams(CustomerLossQuery customerLossQuery) {
        Map<String,Object> map = new HashMap<>();
        //1、开启分页功能
        PageHelper.startPage(customerLossQuery.getPage(),customerLossQuery.getLimit());
        //2、调用dao层的查询方法，返回查询结果
        List<CustomerLoss> customerLossList = customerLossMapper.selectByParams(customerLossQuery);
        //3、使用pageInfo封装查询结果
        PageInfo<CustomerLoss> pageInfo = new PageInfo<>(customerLossList);
        //4、设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 确认流失操作（更新流失客户的流失状态）
     * @param id
     * @param lossReason
     */
    @Transactional
    public void updateCustomerLossStateById(Integer id, String lossReason) {
        //1、参数校验
        AssertUtil.isTrue(null == id,"待确认流失的客户不存在");
        //通过id查询流失客户的记录
        CustomerLoss customerLoss = customerLossMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(null == customerLoss,"待确认流失的客户不存在");
        AssertUtil.isTrue(StringUtils.isBlank(lossReason),"流失原因不能为空");
        //2、设置相关字段的默认值
        customerLoss.setState(1);
        customerLoss.setLossReason(lossReason);
        customerLoss.setConfirmLossTime(new Date());
        customerLoss.setUpdateDate(new Date());
        //3、执行更新操作，判断受影响的行数
        AssertUtil.isTrue(customerLossMapper.updateByPrimaryKeySelective(customerLoss) < 1,"确认流失失败");
    }
}
