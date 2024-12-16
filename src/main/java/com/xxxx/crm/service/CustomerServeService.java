package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CustomerMapper;
import com.xxxx.crm.dao.CustomerServeMapper;
import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.enums.CustomerServeStatus;
import com.xxxx.crm.query.CustomerServeQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.CustomerServe;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerServeService extends BaseService<CustomerServe,Integer> {

    @Resource
    private CustomerServeMapper customerServeMapper;
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private UserMapper userMapper;

    /**
     * 分页多条件查询客户服务数据列表
     * @param customerServeQuery
     * @return
     */
    public Map<String, Object> queryCustomerServeByParams(CustomerServeQuery customerServeQuery) {
        Map<String,Object> map = new HashMap<>();
        //1、开启分页功能
        PageHelper.startPage(customerServeQuery.getPage(),customerServeQuery.getLimit());
        //2、调用dao层的查询方法，返回查询结果
        List<CustomerServe> customerServeList = customerServeMapper.selectByParams(customerServeQuery);
        //3、使用pageInfo封装查询结果
        PageInfo<CustomerServe> pageInfo = new PageInfo<>(customerServeList);
        //4、设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加操作
     * @param customerServe
     */
    @Transactional
    public void addCustomerServe(CustomerServe customerServe) {
        //1、参数校验
        //客户名 customer 非空
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getCustomer()),"客户名不能为空");
        //判断客户是否存在
        AssertUtil.isTrue(null == customerMapper.queryCustomerByName(customerServe.getCustomer()),"客户不存在");
        //判断服务类型 serveType 非空
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServeType()),"请选择服务类型");
        //服务请求内容 serviceRequest 非空
        AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceRequest()),"服务请求内容不能为空");

        //2、设置相关字段的默认值
        //服务状态  服务创建状态  fw_001
        customerServe.setState(CustomerServeStatus.CREATED.getState());
        customerServe.setIsValid(1);
        customerServe.setCreateDate(new Date());
        customerServe.setUpdateDate(new Date());

        //3、执行添加操作，判断受影响的函数
        AssertUtil.isTrue(customerServeMapper.insertSelective(customerServe) < 1,"添加失败");
    }

    /**
     * 服务更新（服务分配、服务处理、服务反馈）
     * @param customerServe
     */
    @Transactional
    public void updateCustomerServe(CustomerServe customerServe) {
        //1、参数校验 和 设置相关字段的默认值
        //客户服务id 非空且记录存在
        AssertUtil.isTrue(customerServe.getId() == null || customerServeMapper.selectByPrimaryKey(customerServe.getId()) == null, "待更新服务记录不存在");
        //判断客户服务的服务状态
        if(CustomerServeStatus.ASSIGNED.getState().equals(customerServe.getServeType())) {
            //服务分配操作
            //分配人 非空 分配用户记录存在
            AssertUtil.isTrue(userMapper.queryUserByName(customerServe.getAssigner()) == null,"待分配用户不存在");
            //分配时间 创建时间
            customerServe.setAssignTime(new Date());
            customerServe.setCreateDate(new Date());

        } else if(CustomerServeStatus.PROCED.getState().equals(customerServe.getServeType())) {
            //服务处理操作
            //服务内容 非空
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceProce()),"服务处理内容不能为空");
            //服务时间
            customerServe.setServiceProceTime(new Date());


        } else if(CustomerServeStatus.FEED_BACK.getState().equals(customerServe.getServeType())) {
            //服务反馈操作
            //反馈内容 非空
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getServiceProceResult()),"服务反馈内容不能为空");
            //服务满意度 非空
            AssertUtil.isTrue(StringUtils.isBlank(customerServe.getMyd()),"请选择服务满意度");
            //修改服务状态（设置为归档状态=fw_005）
            customerServe.setState(CustomerServeStatus.ARCHIVED.getState());
        }

        //设置更新时间
        customerServe.setUpdateDate(new Date());
        //2、执行更新操作，判断受影响的行数
        AssertUtil.isTrue(customerServeMapper.updateByPrimaryKeySelective(customerServe) < 1,"更新失败");
    }
}
