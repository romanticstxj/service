package com.madhouse.platform.smartexchange.dto;

import java.io.Serializable;

import com.madhouse.platform.smartexchange.annotation.NotNull;

public class TencentAdspaceDto implements Serializable {

	private static final long serialVersionUID = -7428205766110173101L;

	private Integer id;
	@NotNull
	private String placement_name;
	@NotNull
	private String medium_name; //媒体名称
	@NotNull
	private Integer demandId;
	@NotNull
	private String industryId;
	@NotNull
	private String placement_type;
	@NotNull
	private String placement_sub_type;
	@NotNull
	private String os;

	private String remark;
	private Boolean delFlg;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPlacement_name() {
		return placement_name;
	}

	public void setPlacement_name(String placement_name) {
		this.placement_name = placement_name;
	}

	public String getMedium_name() {
		return medium_name;
	}

	public void setMedium_name(String medium_name) {
		this.medium_name = medium_name;
	}

	public Integer getDemandId() {
		return demandId;
	}

	public void setDemandId(Integer demandId) {
		this.demandId = demandId;
	}

	public String getIndustryId() {
		return industryId;
	}

	public void setIndustryId(String industryId) {
		this.industryId = industryId;
	}

	public String getPlacement_type() {
		return placement_type;
	}

	public void setPlacement_type(String placement_type) {
		this.placement_type = placement_type;
	}

	public String getPlacement_sub_type() {
		return placement_sub_type;
	}

	public void setPlacement_sub_type(String placement_sub_type) {
		this.placement_sub_type = placement_sub_type;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
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

}
