package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.DataDicMapper;
import com.xxxx.crm.query.DataDicQuery;
import com.xxxx.crm.vo.DataDic;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataDicService extends BaseService<DataDic,Integer> {

    @Resource
    private DataDicMapper dataDicMapper;

    /**
     * 分页条件查询字典数据列表
     * @param dataDicQuery
     * @return
     */
    public Map<String, Object> queryDataDicByParams(DataDicQuery dataDicQuery) {
        Map<String,Object> map = new HashMap<>();
        //1、开启分页功能
        PageHelper.startPage(dataDicQuery.getPage(),dataDicQuery.getLimit());
        //2、调用dao层的查询方法，返回查询结果
        List<DataDic> dataDics = dataDicMapper.selectByParams(dataDicQuery);
        //3、使用pageInfo封装查询结果
        PageInfo<DataDic> pageInfo = new PageInfo<>(dataDics);
        //4、设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }
}
