package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CustomerLossMapper;
import com.xxxx.crm.dao.CustomerReprieveMapper;
import com.xxxx.crm.query.CustomerReprieveQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.CustomerReprieve;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerReprieveService extends BaseService<CustomerReprieve,Integer> {

    @Resource
    private CustomerReprieveMapper customerReprieveMapper;
    @Resource
    private CustomerLossMapper customerLossMapper;

    /**
     * 分页条件查询暂缓措施数据列表
     * @param customerReprieveQuery
     * @return
     */
    public Map<String, Object> queryCustomerReprieveByParams(CustomerReprieveQuery customerReprieveQuery) {
        Map<String,Object> map = new HashMap<>();
        //1、开启分页功能
        PageHelper.startPage(customerReprieveQuery.getPage(),customerReprieveQuery.getLimit());
        //2、调用dao层的查询方法，返回查询结果
        List<CustomerReprieve> customerReprieves = customerReprieveMapper.selectByParams(customerReprieveQuery);
        //3、使用pageInfo封装查询结果
        PageInfo<CustomerReprieve> pageInfo = new PageInfo<>(customerReprieves);
        //4、设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加暂缓措施操作
     * @param customerReprieve
     */
    @Transactional
    public void addCustomerRepr(CustomerReprieve customerReprieve) {
        //1、参数校验
        checkParams(customerReprieve.getLossId(), customerReprieve.getMeasure());
        //2、设置参数的默认值
        customerReprieve.setIsValid(1);
        customerReprieve.setCreateDate(new Date());
        customerReprieve.setUpdateDate(new Date());
        //3、执行添加操作，判断受影响的行数
        AssertUtil.isTrue(customerReprieveMapper.insertSelective(customerReprieve) < 1, "添加失败");
    }

    /**
     * 添加操作的参数校验
     * @param lossId
     * @param measure
     */
    private void checkParams(Integer lossId, String measure) {
        // 流失客户ID lossId    非空，数据存在
        AssertUtil.isTrue(null == lossId || customerLossMapper.selectByPrimaryKey(lossId) == null, "流失客户记录不存在");
        // 暂缓措施内容 measure   非空
        AssertUtil.isTrue(StringUtils.isBlank(measure), "暂缓措施内容不能为空");
    }

    /**
     * 修改操作
     * @param customerReprieve
     */
    public void updateCustomerRepr(CustomerReprieve customerReprieve) {
        //1、参数校验
        //主键id 非空
        AssertUtil.isTrue(null == customerReprieve.getId() || customerReprieveMapper.selectByPrimaryKey(customerReprieve.getId()) == null, "待更新记录不存在！");
        //2、参数校验
        checkParams(customerReprieve.getLossId(), customerReprieve.getMeasure());
        //3、设置参数的默认值
        customerReprieve.setUpdateDate(new Date());
        //4、执行修改操作，判断受影响的行数
        AssertUtil.isTrue(customerReprieveMapper.updateByPrimaryKeySelective(customerReprieve) < 1, "修改失败");
    }

    /**
     * 删除操作
     * @param id
     */
    @Transactional
    public void deleteCustomerRepr(Integer id) {
        //1、参数校验
        AssertUtil.isTrue(null == id,"待删除记录不存在");
        //通过id查询暂缓数据
        CustomerReprieve customerReprieve = customerReprieveMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(null == customerReprieve,"待删除记录不存在");
        //2、设置相关字段的默认值
        customerReprieve.setIsValid(0);
        customerReprieve.setUpdateDate(new Date());
        //3、执行更新操作，判断受影响的行数
        AssertUtil.isTrue(customerReprieveMapper.updateByPrimaryKeySelective(customerReprieve) < 1,"删除失败");
    }
}
