package com.madhouse.platform.smartexchange.dto;

import java.io.Serializable;
import java.util.List;

/**
 * User: Ricky
 * Date: 2016/12/2
 * Time: 11:13
 */
public class BatchUpdateHttps  implements Serializable {

    private List<SupplierAdspaceDto> supplierAdspaceDtos;

    public List<SupplierAdspaceDto> getSupplierAdspaceDtos() {
        return supplierAdspaceDtos;
    }

    public void setSupplierAdspaceDtos(List<SupplierAdspaceDto> supplierAdspaceDtos) {
        this.supplierAdspaceDtos = supplierAdspaceDtos;
    }
}