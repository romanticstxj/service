package com.madhouse.platform.smartexchange.dto;

import java.util.List;

/**
 * Created by zhujiajun
 * 15/8/3 14:10
 * 供应方广告位关联需求方广告位包装对象
 * 前台使用so low
 */
public class SupplierDemandWrapperDto {

    private List<SupplierDemandDto> supplierDemandDtos;

    public List<SupplierDemandDto> getSupplierDemandDtos() {
        return supplierDemandDtos;
    }

    public void setSupplierDemandDtos(List<SupplierDemandDto> supplierDemandDtos) {
        this.supplierDemandDtos = supplierDemandDtos;
    }
}
