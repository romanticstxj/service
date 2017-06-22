package com.madhouse.platform.smartexchange.dto;

import java.io.Serializable;

/**
 * User: Ricky
 * Date: 2016/9/6
 * Time: 20:44
 */
public class IndustryDto implements Serializable {

    private Integer id;

    private String name;

    private String code;

    private String parentCode;

    private String type;

    private String industry;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
}
