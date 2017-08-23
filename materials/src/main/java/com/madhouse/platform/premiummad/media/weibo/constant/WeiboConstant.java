package com.madhouse.platform.premiummad.media.weibo.constant;

public enum WeiboConstant{
	//API接口调用返回表示码
	RESPONSE_SUCCESS(0,"执行成功"),
	RESPONSE_OATH_FAILUE(1,"系统认证失败"),
	RESPONSE_PARAS_ERROR(2,"请求参数错误"),
	RESPONSE_OTHER_ERROR(3,"其他错误"),
	
	//广告主审核状态码
	C_STATUS_APPROVED(1,"审核通过"),
	C_STATUS_REFUSED(2,"审核不通过"),
	C_STATUS_UNAUDITED(0,"待审核"),
	
	//系统错误
	SYSTEM_ERROR(100,"系统错误"),
	DSP_ERROR(101,"DSP验证失败"),
	
	//格式与参数错误
	PARAS_MISSING(201,"缺乏必要参数"),
	PARAS_FORMAT_ERROR(202,"参数格式错误"),
	PARAS_JSON_ERROR(203,"JSON数据格式错误"),
	PARAS_OTHER_ERROR(299,"其他格式错误"),
	
	//客户信息同步相关错误
	CLIENT_NOT_EXISTS(301,"客户不存在"),
	CLIENT_NAME_MISSING(301,"客户名称为空"),
	CLIENT_MEMO_MISSING(302,""),
	CLIENT_QULIFICATION_ERROR(303,""),
	CLIENT_QULIFICATION_LOAD_ERROR(304,""),
	CLIENT_BRAND_ERROR(305,""),
	CLIENT_CATEGORY_ERROR(306,""),
	CLIENT_OTHER_ERROR(399,""),
	
	//广告信息同步相关错误
	MATERIAL_FILE_OVER_SIZE(403,""),
	MATERIAL_URL_ERROR(404,""),
	MATERIAL_SIZE_NOT_SUPPORTS(405,""),
	MATERIAL_PIC_ERROR(406,""),
	MATERIAL_SAVE_ERROR(407,""),
	MATERIAL_TITLE_ERROR(411,""),
	MATERIAL_DESC_ERROR(412,""),
	MATERIAL_UID_ERROR(413,""),
	MATERIAL_MBLOG_TEXT_ERROR(414,""),
	MATERIAL_BUTTON_URL_ERROR(415,""),
	MATERIAL_NOT_PD_ERROR(416,""),
	MATERIAL_LANGDINGPAGE_ERROR(417,""),
	MATERIAL_OVER_PIC_ERROR(419,""),
	MATERIAL_UPLOAD_ERROR(420,""),
	MATERIAL_VIDEO_NOT_PASS(421,""),
	MATERIAL_NOT_SUPPORTS(422,""),
	MATERIAL_REPEAT_ERROR(423,""),
	
	
	M_STATUS_APPROVED(1,"审核已通过"),
	M_STATUS_REFUSED(-1,"审核未通过"),
	M_STATUS_UNAUDITED(0,"待审核");
	
	
	
	int value;
	String description;
	WeiboConstant(int value,String description){
		this.value = value;
		this.description = description;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}