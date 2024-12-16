package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;

public class CustomerReprieveQuery extends BaseQuery {

    private Integer lossId;  //流失客户的id

    public Integer getLossId() {
        return lossId;
    }

    public void setLossId(Integer lossId) {
        this.lossId = lossId;
    }
}
