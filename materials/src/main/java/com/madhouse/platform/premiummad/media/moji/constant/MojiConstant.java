package com.madhouse.platform.premiummad.media.moji.constant;

public enum MojiConstant {
	M_STATUS_SUCCESS(200, "上传成功"),//上传成功;审核通过
	M_STATUS_SIGN_ERROR(400,"签名错误"),//签名错误
	M_STATUS_IP_ERROR(403,"IP不在白名单内"),//IP不在白名单内
	M_STATUS_PAR_ERROR(404,"参数错误"),//参数错误
	M_STATUS_SERVICE_ERROR(500,"success"),//服务器错误
	M_STATUS_UNAUDITED(201,"未审核"),
	M_STATUS_ERROR(202,"审核失败"),//审核失败
	
    //头条广告位类型
    MOJI_BANNER(2,"banner"),//banner
    MOJI_SPLASH(3,"开屏"),//开屏
	
	SHOW_TYPE_1(1,"纯图:image_url必填"),
	SHOW_TYPE_2(2,"左图右文:image_url必填，标题、描述必填"),
	SHOW_TYPE_3(3,"上文下图:image_url必填，标题必填，描述选填");

	int value;
	String description;

	MojiConstant(int value, String description) {
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
