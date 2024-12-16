package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.query.CustomerQuery;
import com.xxxx.crm.vo.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerMapper extends BaseMapper<Customer,Integer> {

    //通过客户名称查询客户对象
    Customer queryCustomerByName(String name);

    //查询待流失客户数据
    List<Customer> queryLossCustomer();

    //通过客户id批量更新客户的流失状态
    int updateCustomerStateByIds(List<Integer> lossCustomerIds);

    //查询客户贡献数据
    List<Map<String,Object>> queryCustomerContributionByParams(CustomerQuery customerQuery);

    //查询客户构成分析
    List<Map<String,Object>> countCustomerMake();

    //查询客户服务分析
    List<Map<String, Object>> countCustomerServe();
}