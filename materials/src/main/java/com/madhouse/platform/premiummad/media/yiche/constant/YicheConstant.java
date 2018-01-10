package com.madhouse.platform.premiummad.media.yiche.constant;

/**
 * 常量
 */
public interface YicheConstant {
	// 响应状态码
	interface ErrorCode {
		int REQUEST_SUCCESS = 200;
		int REQUEST_FAIL = 400; //校验失败
		int REQUEST_EXPIRED = 10200001; // 请求过期
		int DSP_NOT_EXIST = 10200002; // DSP不存在
		int SIGNATURE_FAILED = 10200003; // 签名校验失败
		int TEMPLATE_NOT_EXIST = 10200004; // 模板不存在
		int ORDER_NOT_EXIST = 10200005; // 订单不存在
		int MATERIAL_NOT_EXIST = 10200006; // 素材不存在
		int MATERIAL_NO_CLICK_MONITOR = 10200007; // H5代码没有点击检测标签
	}

	// 素材模板
	interface Template {
		int QUOTE_LIST= 38;//报价信息流图文
	}
	
	// 投放平台
	interface Platform {
		int PC_WAP = 0;
		int IOS = 1;
		int ANDROID = 2;
	}
	
	// 素材审核状态
	interface AuditStatus {
		String UNAUDIT = "wait";
		String AUDITED = "accept";
		String REJECTED = "reject";
	}
}
