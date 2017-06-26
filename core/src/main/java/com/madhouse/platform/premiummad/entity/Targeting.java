package com.madhouse.platform.premiummad.entity;

/**
 * Created by zhujiajun
 * 15/10/8 15:44
 */
public class Targeting extends BaseEntity {

    private static final long serialVersionUID = -5917393633647397277L;

    private Integer id;
    private Integer supplierAdspaceId;
    private Integer demandAdspaceId;
    private Integer demandId;
    private String targetType;
    private String targetCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierAdspaceId() {
        return supplierAdspaceId;
    }

    public void setSupplierAdspaceId(Integer supplierAdspaceId) {
        this.supplierAdspaceId = supplierAdspaceId;
    }

    public Integer getDemandAdspaceId() {
        return demandAdspaceId;
    }

    public void setDemandAdspaceId(Integer demandAdspaceId) {
        this.demandAdspaceId = demandAdspaceId;
    }

    public Integer getDemandId() {
        return demandId;
    }

    public void setDemandId(Integer demandId) {
        this.demandId = demandId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }
}
