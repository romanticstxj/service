package com.madhouse.platform.premiummad.constant;

public enum StatusCode {
	/* 系统状态码  */
	SC20000(20000, "操作成功"), //
	SC20001(20001, "缺少必填参数"), //
	SC20002(20002, "id不正确"), // 
	SC20003(20003, "此用户没有任何数据，请添加"), //
    SC20004(20004, "请提供正确的更新类型"), //
    SC20005(20005, "更新时id不能为空"), // 
    SC20006(20006, "此用户无权查看此数据"), // 
    
    /* 媒体模块状态码 */ 
	SC20101(20101, "媒体名称不能重复"), //
	
	/* 广告位模块状态码  */
	SC20201(20201, "媒体Key重复"), //
	SC20202(20202, "dspId重复"), //
	SC20203(20203, "广告位ID重复"), //
	SC20204(20204, "媒体映射和dsp映射信息不能全为空"), //
	SC20205(20205, "更新时请提供正确的广告位ID"), //
	SC20206(20206, "广告位Key不能重复"), //
	
	SC30001(30001, "系统繁忙，请稍后再试"); //
	

	int value;
	final String descrip;

	StatusCode(int value, String descrip) {
		this.value = value;
		this.descrip = descrip;
	}

	public int getValue() {
		return value;
	}

	public String getDescrip() {
		return descrip;
	}

}
