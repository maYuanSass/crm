package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CustomerLossMapper;
import com.xxxx.crm.dao.CustomerMapper;
import com.xxxx.crm.dao.CustomerOrderMapper;
import com.xxxx.crm.query.CustomerQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.vo.Customer;
import com.xxxx.crm.vo.CustomerLoss;
import com.xxxx.crm.vo.CustomerOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CustomerService extends BaseService<Customer,Integer> {

    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private CustomerOrderMapper customerOrderMapper;
    @Resource
    private CustomerLossMapper customerLossMapper;

    /**
     * 多条件分页查询客户信息数据列表
     *  返回的数据格式必须满足layui中的数据表格要求的格式
     * @param customerQuery
     * @return
     */
    public Map<String,Object> queryCustomerByParams(CustomerQuery customerQuery) {
        Map<String,Object> map = new HashMap<>();
        //1、开启分页功能
        PageHelper.startPage(customerQuery.getPage(),customerQuery.getLimit());
        //2、调用dao层的查询方法，返回查询结果
        List<Customer> customers = customerMapper.selectByParams(customerQuery);
        //3、使用pageInfo封装查询结果
        PageInfo<Customer> pageInfo = new PageInfo<>(customers);
        //4、设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加操作
     * @param customer
     */
    @Transactional
    public void addCustomer(Customer customer) {
        //1、参数校验
        checkCustomerParams(customer.getName(),customer.getFr(),customer.getPhone());
        //判断客户名称的唯一性
        Customer temp = customerMapper.queryCustomerByName(customer.getName());
        //判断客户名称是否存在
        AssertUtil.isTrue(temp != null,"客户名称不能重复");

        //2、设置相关字段的默认值
        customer.setIsValid(1);
        customer.setState(0);
        customer.setCreateDate(new Date());
        customer.setUpdateDate(new Date());
        //客户编号
        String khno = "KH" + System.currentTimeMillis();
        customer.setKhno(khno);

        //3、执行添加操作，判断受影响的行数
        AssertUtil.isTrue(customerMapper.insertSelective(customer) != 1,"添加失败");
    }

    /**
     * 添加操作的参数校验
     * @param name
     * @param fr
     * @param phone
     */
    private void checkCustomerParams(String name, String fr, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(name),"客户名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(fr),"法人代表不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空");
        //校验手机号码格式
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号码格式不正确");
    }

    /**
     * 修改操作
     * @param customer
     */
    @Transactional
    public void updateCustomer(Customer customer) {
        //1、参数校验
        AssertUtil.isTrue(null == customer.getId(),"待更新记录不存在");
        Customer temp = customerMapper.selectByPrimaryKey(customer.getId());
        AssertUtil.isTrue(temp == null,"待更新记录不存在");
        checkCustomerParams(customer.getName(),customer.getFr(),customer.getPhone());
        //通过客户名称查询客户
        temp = customerMapper.queryCustomerByName(customer.getName());
        //判断客户记录是否存在。且客户id和更新记录的id一致
        AssertUtil.isTrue(temp != null && !(temp.getId()).equals(customer.getId()),"客户名称已存在，请重新输入");

        //2、设置相关字段的默认值
        customer.setUpdateDate(new Date());

        //3、执行更新操作，判断受影响的行数
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(customer) < 1, "修改失败");
    }

    /**
     * 删除操作
     * @param id
     */
    @Transactional
    public void deleteCustomer(Integer id) {
        //1、参数校验
        AssertUtil.isTrue(id == null, "待删除记录不存在");
        Customer customer = customerMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(customer == null,"待删除记录不存在");
        //2、设置相关字段的默认值
        customer.setIsValid(0);
        customer.setUpdateDate(new Date());
        //2、执行更新操作，判断受影响的行数
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(customer) != 1,"删除失败");
    }

    /**
     * 更新客户的流失状态
     */
    @Transactional
    public void updateCustomerState() {
        //1、查看待流失的客户数据
        List<Customer> lossCustomerList = customerMapper.queryLossCustomer();
        //2、将流失的客户数据批量添加到客户流失表中
        //判断流失客户数据是否存在
        if(lossCustomerList != null && lossCustomerList.size() > 0) {
            //定义一个集合来接收所有流失客户的id
            List<Integer> lossCustomerIds = new ArrayList<>();

            //定义一个流失客户集合
            List<CustomerLoss> customerLossList = new ArrayList<>();
            //遍历查看到的流失客户的列表
            lossCustomerList.forEach(customer -> {
                //定义一个流失客户对象
                CustomerLoss customerLoss = new CustomerLoss();
                customerLoss.setCreateDate(new Date());  //创建时间
                customerLoss.setCusManager(customer.getCusManager());  //客户经理
                customerLoss.setCusName(customer.getName());    //客户名称
                customerLoss.setCusNo(customer.getKhno());      //客户编号
                customerLoss.setIsValid(1);
                customerLoss.setUpdateDate(new Date());
                customerLoss.setState(0);  //客户流失状态（默认暂缓流失0 | 确认流失状态1）
                //客户对吼下单时间
                CustomerOrder customerOrder = customerOrderMapper.queryLossCustomerOrderByCustomerId(customer.getId());
                //判断客户订单书否存在，如果存在，则设置最后下单时间
                if(customerOrder != null) {
                    customerLoss.setLastOrderTime(customerOrder.getOrderDate());
                }
                //将流失客户对象设置到集合中
                customerLossList.add(customerLoss);

                //将流失客户的id设置到对应的集合中
                lossCustomerIds.add(customer.getId());
            });
            //批量添加流失客户记录
            AssertUtil.isTrue(customerLossMapper.insertBatch(customerLossList) != customerLossList.size(),"客户流失数据转移失败");

            //3、批量更新客户的流失状态 0正常 | 1流失
            AssertUtil.isTrue(customerMapper.updateCustomerStateByIds(lossCustomerIds) != lossCustomerIds.size(),"客户流失数据转移失败");
        }

    }

    /**
     * 查询客户贡献数据
     * @param customerQuery
     * @return
     */
    public Map<String,Object> queryCustomerContributionByParams(CustomerQuery customerQuery) {
        Map<String,Object> map = new HashMap<>();
        //1、开启分页功能
        PageHelper.startPage(customerQuery.getPage(),customerQuery.getLimit());
        //2、调用dao层的查询方法，返回查询结果
        List<Map<String,Object>> customers = customerMapper.queryCustomerContributionByParams(customerQuery);
        //3、使用pageInfo封装查询结果
        PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(customers);
        //4、设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 查询客户构成（折线图数据处理）
     * @return
     */
    public Map<String,Object> countCustomerMake() {
        Map<String,Object> map = new HashMap<>();
        //查询客户构成数据的列表
        List<Map<String,Object>> dataList = customerMapper.countCustomerMake();
        //折线图 x轴数据
        List<String> xData = new ArrayList<>();
        //折线图 x轴数据
        List<Integer> yData = new ArrayList<>();

        //判断数据列表，循环设置
        if(dataList != null && dataList.size() > 0) {
            dataList.forEach(entity -> {
                //获取level对应的数据设置到x轴中
                xData.add(entity.get("level").toString());
                //获取total对应的数据设置到y轴中
                yData.add(Integer.parseInt(entity.get("total").toString()));
            });
        }
        //将x轴的数据和y轴的数据设置到map中
        map.put("xData",xData);
        map.put("yData",yData);
        return map;
    }

    /**
     * 查询客户构成（饼状图数据处理）
     * @return
     */
    public Map<String, Object> countCustomerMake02() {
        Map<String,Object> map = new HashMap<>();
        //查询客户构成数据的列表
        List<Map<String,Object>> dataList = customerMapper.countCustomerMake();
        //饼状图 数组（数组中是字符串）
        List<String> data1 = new ArrayList<>();
        //饼状图 数组（数组中是对象）
        List<Map<String,Object>> data2 = new ArrayList<>();

        //判断数据列表，循环设置
        if(dataList != null && dataList.size() > 0) {
            //遍历集合
            dataList.forEach(entity -> {
                //饼状图 数组（数组中是字符串）
                data1.add(entity.get("level").toString());
                //饼状图 数组（数组中是对象）
                Map<String,Object> dataMap = new HashMap<>();
                dataMap.put("name",entity.get("level"));
                dataMap.put("value",entity.get("total"));
                data2.add(dataMap);
            });
        }
        //将x轴的数据和y轴的数据设置到map中
        map.put("data1",data1);
        map.put("data2",data2);
        return map;
    }

    /**
     * 查询客户服务分析（柱状图数据处理）
     * @return
     */
    public Map<String, Object> countCustomerServe() {
        Map<String,Object> map = new HashMap<>();
        //查询客户构成数据的列表
        List<Map<String,Object>> dataList = customerMapper.countCustomerServe();
        //柱状图 x轴数据
        List<String> xData = new ArrayList<>();
        //柱状图 x轴数据
        List<Integer> yData = new ArrayList<>();

        //判断数据列表，循环设置
        if(dataList != null && dataList.size() > 0) {
            dataList.forEach(entity -> {
                //获取serve_type对应的数据，并判断类型，然后设置到x轴的数据中
                if(entity.get("serve_type").toString().equals("6")) {
                    xData.add("咨询");
                } else if(entity.get("serve_type").toString().equals("7")) {
                    xData.add("建议");
                } else if(entity.get("serve_type").toString().equals("8")) {
                    xData.add("投诉");
                } else {
                    xData.add("其他");
                }
                //获取total对应的数据设置到y轴的数据中
                yData.add(Integer.parseInt(entity.get("total").toString()));
            });
        }
        //将x轴的数据和y轴的数据设置到map中
        map.put("xData",xData);
        map.put("yData",yData);
        return map;
    }
}
