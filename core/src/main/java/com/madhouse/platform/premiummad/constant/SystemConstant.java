package com.madhouse.platform.premiummad.constant;

/**
 * 系统常量
 */
public interface SystemConstant {
	
	public interface Logging{
		String PREMIUMMAD = "premiummad";
	    String LOGGER_PREMIUMMAD = PREMIUMMAD;//日志中使用的
	    String LOGGER_PREMIUMMAD_ERROR = "premiummadError";
	}
	
	public interface Request{
		String XFROM = "X-From";
	    String USERID = "X-User-Id";
	}

	public interface OtherConstant{
		// magic constant
		String ZERO = "0";
		int BASE_FACTOR = 2;
		String ADSPACE_BID_FLOOR = "bidFloor";
		int RATIO_FEN_TO_YUAN = 100;
		
		String SYSTEM_ADMIN_MEDIA_ID = "-1";
		String SYSTEM_ADMIN_POLICY_ID = "-1";
		
		int DICT_MEDIA_CATEGORY = 1;
		int DICT_ADSPACE_LAYOUT = 2;
		int DICT_LOCATION = 3;
		
		long WEEK = 1000L * 60L * 60L * 24L * 7L;
	}
	
	public interface ErrorMessage{
		String NO_UPDATE_ID = "更新时id不能为空";
		String NO_UPDATE_TYPE = "更新时type不能为空";
		String ERROR_UPDATE_TYPE = "updateType不正确";
		String NO_UPDATE_STATUS = "更新状态时status不能为空";
		String ERROR_UPDATE_STATUS = "更新状态时status范围有误";
		
		/* 策略验证信息 */
		String ERROR_WEIGHT_FORMAT = "权重格式错误";
	}
}
