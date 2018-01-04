package com.madhouse.platform.premiummad.media.yiche.constant;

/**
 * 常量
 */
public interface YicheConstant {
	// 响应状态码
	interface ErrorCode {
		int SUCCESS = 200;
		int ERROR = 400;
	}

	// 素材类型
	interface MaterialType {
		int NATIVE = 0; // 图文
	}

	// 素材模板 TODO
	interface Template {
		int APP_NATIVE = 10002; // APP - 信息流广告
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
