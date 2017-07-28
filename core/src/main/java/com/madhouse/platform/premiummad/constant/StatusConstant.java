package com.madhouse.platform.premiummad.constant;

/**
 * 业务常量
 */
public interface StatusConstant {

	Integer DEMAND_TIMEOUT_START = 1; // 需求方平台允许超时时间范围-开始
	Integer DEMAND_TIMEOUT_END = 500; // 需求方平台允许超时时间范围-结束
	String TARGETING_TYPE_LOCATION = "001"; //地域定向
	
	// 物料表中，brandType：区分 保洁和非保洁
	String MATERIAL_BRAND_TYPE_PG = "001";
	String MATERIAL_BRAND_TYPE_OTHERS = "002";
}
