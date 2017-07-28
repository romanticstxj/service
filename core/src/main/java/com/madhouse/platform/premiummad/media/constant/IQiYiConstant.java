package com.madhouse.platform.premiummad.media.constant;

public enum IQiYiConstant {

	// 返回code码
	RESPONSE_SUCCESS(0, "上传成功"), 
	RESPONSE_AUTH_FAIL(1001, "认证错误"), 
	RESPONSE_PARAM_FAIL(4001, "参数错误"), 
	RESPONSE_SERVER_FAIL(5001, "服务端错误"), 
	RESPONSE_MAX_UPLOAD_SIZE_FAIL(5002, "用户上传超过限制"), 
	RESPONSE_MAX_REQUEST_SIZE_FAIL(5003, " 应用请求超过限制"),

	// 创意类型
	AD_TYPE_1(1, "贴片创意"), 
	AD_TYPE_2(2, "暂停创意"), 
	AD_TYPE_3(3, "云交互贴片"), 
	AD_TYPE_4(4, "角标"), // 只支持移动端
	AD_TYPE_9(9, "overlay"),

	// 投放平台类型
	PLATFORM_PC(1, "pc端"), 
	PLATFORM_MOBILE(2, "移动端"),

	// 广告位操作类型.注意,value[1,2]无用,为自定义.实际使用op=create或update
	AD_OP_CREATE(1, "create"), 
	AD_OP_UPDATE(2, "update"),

	// 区分素材是属RTB 还是 PMP （PDB+PD）,0 代表 PMP素材， 1代表 RTB素材
	IS_PMP_PMP(0, "PMP"), 
	IS_PMP_RTB(1, "RTB"),

	// 物料审核状态.注意,value[0,1,2,3,4]无用,为自定义.返回物料状态码为字符串INIT,AUDIT_WAIT...
	M_STATUS_INIT(0, "INIT"), // 上传 至生产通道失败
	M_STATUS_INIT_PRODUCT(5, "INIT_PRODUCT"), // 生产中
	M_STATUS_PRODUCT_FAIL(6, "PRODUCT_FAIL"), // 生产失败

	M_STATUS_AUDIT_WAIT(1, "AUDIT_WAIT"), // 处理成功,等待审核
	M_STATUS_AUDIT_UNPASS(2, "AUDIT_UNPASS"), // 审核未通过
	M_STATUS_COMPLETE(3, "COMPLETE"), // 审核通过,可以使用
	M_STATUS_OFF(4, "OFF"), // 投放下线

	// 广告主审核状态.注意,value[0,1,2]无用,为自定义.返回广告主状态码为字符串WAIT,PASS...
	AD_STATUS_WAIT(0, "WAIT"), // 待审核
	AD_STATUS_PASS(0, "PASS"), // 审核通过
	AD_STATUS_UNPASS(0, "UNPASS"); // 审核不通过

	int value;
	String description;

	IQiYiConstant(int value, String description) {
		this.value = value;
		this.description = description;
	}

	public int getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}
}
