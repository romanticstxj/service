package com.madhouse.platform.smartexchange.dto;

import com.madhouse.platform.smartexchange.annotation.NotNull;

import java.io.Serializable;

/**
 * User: Ricky
 * Date: 2016/9/6
 * Time: 20:44
 */
public class ProjectDto implements Serializable {

    private Integer id;

    @NotNull
    private String name;
    @NotNull
    private String industry;

    private String cid;
    @NotNull
    private Integer demandId;

    private Boolean delFlg;

    private String demandName;
    
    private String audienceTag;
    
    private String audienceTagName;

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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Integer getDemandId() {
        return demandId;
    }

    public void setDemandId(Integer demandId) {
        this.demandId = demandId;
    }

    public Boolean getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Boolean delFlg) {
        this.delFlg = delFlg;
    }

    public String getDemandName() {
        return demandName;
    }

    public void setDemandName(String demandName) {
        this.demandName = demandName;
    }

	public String getAudienceTag() {
		return audienceTag;
	}

	public void setAudienceTag(String audienceTag) {
		this.audienceTag = audienceTag;
	}

	public String getAudienceTagName() {
		return audienceTagName;
	}

	public void setAudienceTagName(String audienceTagName) {
		this.audienceTagName = audienceTagName;
	}
    
}
