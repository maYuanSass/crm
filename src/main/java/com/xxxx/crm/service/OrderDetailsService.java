package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.OrderDetailsMapper;
import com.xxxx.crm.query.OrderDetailsQuery;
import com.xxxx.crm.vo.OrderDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderDetailsService extends BaseService<OrderDetails,Integer> {

    @Resource
    private OrderDetailsMapper orderDetailsMapper;

    /**
     * 分页条件查询订单详情列表
     * @param orderDetailsQuery
     * @return
     */
    public Map<String, Object> queryOrderDetailsByParams(OrderDetailsQuery orderDetailsQuery) {
        Map<String,Object> map = new HashMap<>();
        //1、开启分页功能
        PageHelper.startPage(orderDetailsQuery.getPage(),orderDetailsQuery.getLimit());
        //2、调用dao层的查询方法，返回查询结果
        List<OrderDetails> orderDetails = orderDetailsMapper.selectByParams(orderDetailsQuery);
        //3、使用pageInfo封装查询结果
        PageInfo<OrderDetails> pageInfo = new PageInfo<>(orderDetails);
        //4、设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }
}
