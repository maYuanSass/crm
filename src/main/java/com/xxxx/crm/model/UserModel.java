package com.xxxx.crm.model;

/**
 * 封装用户登录后要存到cookie中的字段
 */
public class UserModel {

    private String userName;
    private String trueName;
    private String userIdStr; //加密后的用户id

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }
}
