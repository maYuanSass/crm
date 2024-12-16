package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.enums.DevResult;
import com.xxxx.crm.enums.StateStatus;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {

    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询营销机会数据
     *  返回的数据格式必须满足layui中的数据表格要求的格式
     * @param saleChanceQuery
     * @return
     */
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery) {
        Map<String,Object> map = new HashMap<>();
        //1、开启分页功能
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        //2、调用dao层的查询方法，返回查询结果
        List<SaleChance> saleChances = saleChanceMapper.selectByParams(saleChanceQuery);
        //3、使用pageInfo封装查询结果
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChances);
        //4、设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加操作
     * @param saleChance
     */
    @Transactional
    public void addSaleChance(SaleChance saleChance) {
        //1、参数校验（客户名称、联系人、联系电话）
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());

        //2、设置相关字段的默认值
        //is_valid 是否有效 1=有效；0=有效
        saleChance.setIsValid(1);
        //createDate 默认系统当前时间
        saleChance.setCreateDate(new Date());
        //updateDate 默认系统当前时间
        saleChance.setUpdateDate(new Date());
        //判断是否设置了指派人
        if(StringUtils.isBlank(saleChance.getAssignMan())) {
            //如果为空，未设置指派人
            //设置分配状态为 0=未分配
            saleChance.setState(StateStatus.UNSTATE.getType());
            //设置指派时间为 null
            saleChance.setAssignTime(null);
            //设置开发状态为 未开发
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        } else {
            //如果不为空，则已经设置了指派人
            //设置分配状态为 1=已分配
            saleChance.setState(StateStatus.STATED.getType());
            //设置指派时间为 系统当前时间
            saleChance.setAssignTime(new Date());
            //设置开发状态为 开发中
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }
        //3、执行添加操作
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)!=1, "添加失败");
    }

    /**
     * 添加操作的参数校验
     * @param customerName 客户名称
     * @param linkMan 联系人
     * @param linkPhone 联系电话
     */
    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"客户名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系电话不能为空");
        //判断手机号码格式是否正确
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"联系电话格式不正确");
    }

    /**
     * 更新操作
     * @param saleChance
     */
    @Transactional
    public void updateSaleChance(SaleChance saleChance) {
        //1、参数校验
        AssertUtil.isTrue(null == saleChance.getId(),"待更新记录不存在");
        //通过主键查询saleChance对象
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        //判断数据库中对应的记录是否存在
        AssertUtil.isTrue(temp == null,"待更新记录不存在");
        //参数校验
        checkSaleChanceParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());

        //2、设置相关字段的默认值
        saleChance.setUpdateDate(new Date());
        //判断原始数据是否存在 assignMan 指派人
        if(StringUtils.isBlank(temp.getAssignMan())) {
            //判断用户在修改时是否指定了指派人
            if(!StringUtils.isBlank(saleChance.getAssignMan())) {
                //修改分配时间。系统当前时间
                saleChance.setAssignTime(new Date());
                //修改分配状态。1 = 已分配
                saleChance.setState(StateStatus.STATED.getType());
                //修改开发状态。1 = 开发中
                saleChance.setDevResult(DevResult.DEVING.getStatus());
            }
        } else { //原始数据存在指派人
            //用户在修改未指定了指派人。（也就是说原来数据库中有值，现在修改成null）
            if(StringUtils.isBlank(saleChance.getAssignMan())) {
                //修改分配状态。0 = 未分配
                saleChance.setState(StateStatus.UNSTATE.getType());
                //修改开发状态。0 = 未开发
                saleChance.setDevResult(DevResult.UNDEV.getStatus());
            } else {
                //修改前有值，修改后也有值。
                //判断修改前后是否是同一个指派人
                if(!saleChance.getAssignMan().equals(temp.getAssignMan())) {
                    //更新指派时间
                    saleChance.setAssignTime(new Date());
                } else {
                    //更新指派时间为修改前的时间
                    saleChance.setAssignTime(temp.getAssignTime());
                }
            }
        }
        //3、执行更新操作
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)!=1,"更新失败");
    }

    /**
     * 批量删除
     * @param ids
     */
    @Transactional
    public void deleteSaleChance(Integer[] ids) {
        //1、判断id是否为空
        AssertUtil.isTrue(null == ids && ids.length < 1, "待删除记录不存在");
        //2、执行删除操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids) != ids.length, "数据删除失败");
    }

    /**
     * 修改营销机会的开发状态
     * @param id
     * @param devResult
     */
    @Transactional
    public void updateSaleChanceDevResult(Integer id,Integer devResult) {
        //1、判断id是否为空
        AssertUtil.isTrue(null == id,"待更新记录不存在");
        //2、通过id查询营销机会的数据
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);
        //3、判断对象是否为空
        AssertUtil.isTrue(null == saleChance, "待更新记录不存在");
        //4、设置开发状态
        saleChance.setDevResult(devResult);
        //5、执行更新操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1,"开发状态更新失败");
    }
}
