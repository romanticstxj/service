package com.madhouse.platform.premiummad.constant;

public enum StatusCode {
	/* 系统对外可显示状态码  */
	SC20000(20000, "操作成功"), //
	SC20001(20001, "当前用户无权访问该数据"), // 
	SC20002(20002, "缺少必填参数"), //
	SC20003(20003, "查无此数据"), //
    SC20008(20008, "状态参数不正确"), // 
    SC20009(20009, "请提供用户ID"), // 
    
    /* 媒体模块状态码 */ 
	SC20101(20101, "不能添加同名的媒体"), //
	
	/* 广告位模块状态码  */
	SC20201(20201, "不能添加相同的媒体方广告位Key"), //
	SC20202(20202, "请选择不同的DSP"), //
	SC20203(20203, "DSP广告位的映射信息不全"), //
	SC20204(20204, "DSP广告位的映射信息中必须提供DSP"), //
	SC20206(20206, "系统异常，请重新创建一次广告位"), //
	SC20207(20207, "不能添加同名的广告位"), //
	SC20208(20208, "Logo信息要么都填，要么都不填"), //
	SC20209(20209, "封面信息要么都填，要么都不填"), //
	
	/* DSP模块状态码  */
	SC20301(20301, "系统异常，请重新创建一次DSP"), //
	SC20302(20302, "不能添加同名的DSP"), //
	SC20303(20303, "需求方URL不合法"), //
	
	/* Policy模块状态码  */
	SC20401(20401, "不能添加同名的策略"), //
	SC20402(20402, "策略的结束日期不能小于今天"), //
	SC20403(20403, "此策略已过期，不能进行任何操作"), //
	SC20404(20404, "请添加广告位"), //
	SC20405(20405, "请添加需求方"), //
	SC20406(20406, "只能选择一个DSP"), //
	
	/* 报表模块状态码  */
	SC20501(20501, "请提供正确的查询报表参数"), //
	SC20502(20502, "请提供正确的维度"), //
	SC20503(20503, "请提供正确的报表类型"), //
	SC20504(20504, "请提供正确的实时类型"), //
	SC20505(20505, "请提供正确的开始日期"), //
	SC20506(20506, "请提供正确的结束日期"), //
	SC20507(20507, "按小时维度的报表只能选择一天的数据"), //
	
	/* 异常流量模块状态码  */
	SC20601(20601, "已存在此异常流量"), //
	SC20602(20602, "已存在此白名单媒体"), //
	
	/* 报表导出模块状态码  */
	SC20701(20701, "报表任务不存在"), //
	SC20702(20702, "报表下载失败"), //
	SC20703(20703, "报表任务还未处理完成"), //
	SC20704(20704, "当前用户无权限下载此报表"), //
	SC20705(20705, "Linux访问文件权限有误"), //
	
	/* 系统内部状态码  */
	SC30001(30001, "系统繁忙，请稍后再试"), //
	SC30002(30002, "数据库操作异常，可能出现并发操作"), //
	SC31005(31005, "Json转换错误"), //
	SC31006(31006, "输入数据不合法"), //
	SC31007(31007, "用户ID格式不正确"), //  
	SC31008(31008, "此接口不存在，请输入一个正确的URL"), //  
	
	/* 广告主、素材审核状态响应码 */
	SC200(200, "正常"),
	SC400(400, "请求参数错误 "),
	SC405(405, "权限异常"),
	SC411(411, "重复提交"),
	SC412(412, "广告主行业错误"),
	SC413(413, "广告主未通过审核"),
	SC414(414, "广告主ID错误"),
	SC415(415, "广告形式错误"),
	SC416(416, "媒体 ID错误 "),
	SC417(417, "广告位 ID错误 "),
	SC418(418, "素材 ID错误"),
	SC419(419, "URL格式错误"),
	SC420(420, "Deal ID错误"),
	SC500(500, "内部错误");

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
