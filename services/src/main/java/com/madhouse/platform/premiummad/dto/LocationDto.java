package com.madhouse.platform.premiummad.dto;

public class LocationDto {
	
    private Integer id;

    private Integer level;

    private String parentCode;

    private String code;

    private String name;

    private Integer domestic;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDomestic() {
		return domestic;
	}

	public void setDomestic(Integer domestic) {
		this.domestic = domestic;
	}
}
