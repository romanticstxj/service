package com.madhouse.platform.smartexchange.dto;

import java.io.Serializable;
import java.util.List;

/**
 * User: Ricky
 * Date: 2016/11/8
 * Time: 14:51
 */
public class BatchUpdateCampaign implements Serializable {

    private List<CridCidDto> cridCidDtos;

    private String cid ;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public List<CridCidDto> getCridCidDtos() {
        return cridCidDtos;
    }

    public void setCridCidDtos(List<CridCidDto> cridCidDtos) {
        this.cridCidDtos = cridCidDtos;
    }
}
