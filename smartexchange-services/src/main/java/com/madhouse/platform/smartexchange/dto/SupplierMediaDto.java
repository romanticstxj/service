package com.madhouse.platform.smartexchange.dto;

import com.madhouse.platform.smartexchange.annotation.NotNull;

import java.io.Serializable;

/**
 * Created by zhujiajun
 * 15/7/31 11:18
 */
public class SupplierMediaDto implements Serializable {

    private static final long serialVersionUID = -5346954564510240886L;

    private Integer id;
    @NotNull
    private String name;
    private String remark;
    private Boolean delFlg;
    
    private String mediaType;//heroapp or network

    private Integer supplierAdspaceCount; //用于前端判断是否能够删除
    
    private String requiresMaterialVerifying; //物料是否需要校验

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Boolean delFlg) {
        this.delFlg = delFlg;
    }

    public Integer getSupplierAdspaceCount() {
        return supplierAdspaceCount;
    }

    public void setSupplierAdspaceCount(Integer supplierAdspaceCount) {
        this.supplierAdspaceCount = supplierAdspaceCount;
    }

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getRequiresMaterialVerifying() {
		return requiresMaterialVerifying;
	}

	public void setRequiresMaterialVerifying(String requiresMaterialVerifying) {
		this.requiresMaterialVerifying = requiresMaterialVerifying;
	}
}
