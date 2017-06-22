package com.madhouse.platform.smartexchange.dto;

import com.madhouse.platform.smartexchange.annotation.NotNull;

import java.io.Serializable;

/**
 * Created by zhujiajun
 * 15/7/31 11:14
 */
public class DemandDto implements Serializable {

    private static final long serialVersionUID = -902503552882729581L;

    private Integer id;
    @NotNull
    private String name;
    @NotNull
    private String url;
    @NotNull
    private Integer timeout;
    @NotNull
    private String type;
    private Boolean delFlg;

    private String typeName;
    private Integer demandAdspaceCount; //用于前端判断是否能够删除

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Boolean getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Boolean delFlg) {
        this.delFlg = delFlg;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getDemandAdspaceCount() {
        return demandAdspaceCount;
    }

    public void setDemandAdspaceCount(Integer demandAdspaceCount) {
        this.demandAdspaceCount = demandAdspaceCount;
    }
}
