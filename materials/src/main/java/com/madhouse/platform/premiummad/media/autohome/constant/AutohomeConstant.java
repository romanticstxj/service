package com.madhouse.platform.premiummad.media.autohome.constant;

/**
 * 常量
 */
public interface AutohomeConstant {
	
	interface RetCode {
		int AUTOHOME_STATUS_SUCCESS = 1;
	}

	interface MaterialType {
		int CHARACTER = 1001; // 文字链
		int IMAGE = 1002; // 图片
		int NATIVE = 1003; // 图文
	}

	interface Template {
		int APP_NATIVE = 10002; // APP - 信息流广告
	}
	
	interface Platform {
		int PC = 1;
		int MOBLILE = 1;
		int APP = 2;
	}
}
