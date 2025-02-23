package com.xxxx.crm.model;

/**
 * 角色资源授权--树形结构所需要的字段
 */
public class TreeModule {

    private Integer id;
    private Integer pId;
    private String name;

    private boolean checked = false; //复选框是否被勾选

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
