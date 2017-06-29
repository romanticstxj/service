package com.madhouse.platform.premiummad.entity;

public class Dict {
		
	private Integer id;
	
	private String code;	//常量code
	
	private String name;	//常量名
	
	private String desc;	//描述

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
