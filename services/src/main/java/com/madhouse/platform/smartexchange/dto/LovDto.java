package com.madhouse.platform.smartexchange.dto;

/**
 * Created by zhujiajun
 * 15/8/12 13:44
 */
public class LovDto {

    private Integer id;
    private String code;
    private String lovKey;
    private String value;
    private Integer displayOrder;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLovKey() {
        return lovKey;
    }

    public void setLovKey(String lovKey) {
        this.lovKey = lovKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
