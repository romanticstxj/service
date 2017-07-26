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
    SC20007(20007, "用户id格式不正确"), // 
    SC20008(20008, "状态参数不正确"), // 
    
    /* 媒体模块状态码 */ 
	SC20101(20101, "媒体名称不能重复"), //
	
	/* 广告位模块状态码  */
	SC20201(20201, "媒体方广告位Key不可重复"), //
	SC20202(20202, "dspId重复"), //
	SC20203(20203, "广告位ID重复"), //
	SC20204(20204, "媒体映射和dsp映射信息不能全为空"), //
	SC20205(20205, "更新时请提供正确的广告位ID"), //
	SC20206(20206, "广告位Key不能重复"), //
	SC20207(20207, "广告位名称不能重复"), //
	
	/* DSP模块状态码  */
	SC20301(20301, "DspToken不能重复"), //
	SC20302(20302, "dsp名称不能重复"), //
	
	/* Policy模块状态码  */
	SC20401(20401, "Policy名称不能重复"), //
	SC20402(20402, "id或type不正确"), // 
	
	SC30001(30001, "系统繁忙，请稍后再试"), //
	SC31002(31002, "批量选择为空"), //
	SC31003(31003, "修正参数不能为空"), //
	
	// 广告主和素材提交状态响应吗
	SC200(200, "正常"),
	SC400(400, "请求参数错误 "),
	SC405(405, "权限异常"),
	SC411(411, "重复提交"),
	SC412(412, "广告主行业错误"),
	SC413(413, "广告主未通过审核"),
	SC414(414, "广告主错误"),
	SC415(415, "广告形式错误"),
	SC416(416, "媒体 ID 错误 "),
	SC417(417, "广告位 ID 错误 "),
	SC418(418, "素材 ID 错误"),
	SC419(419, "广告类型错误"),
	SC500(500, "系统异常"),
	SC410001(410001, "提交广告主失败"),
	SC410002(410002, "提交素材失败");

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
