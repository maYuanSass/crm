package com.xxxx.crm.enums;

/**
 * 客户服务状态枚举类
 */
public enum CustomerServeStatus {
    // 服务创建
    CREATED("fw_001"),
    // 服务分配
    ASSIGNED("fw_002"),
    // 服务处理
    PROCED("fw_003"),
    // 服务反馈
    FEED_BACK("fw_004"),
    // 服务归档
    ARCHIVED("fw_005");

    private String state;

    CustomerServeStatus(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
