package com.madhouse.platform.premiummad.constant;

/**
 * 系统常量
 */
public interface SystemConstant {
	
	interface Logging{
		String PREMIUMMAD = "premiummad";
	    String LOGGER_PREMIUMMAD = PREMIUMMAD;//日志中使用的
	    String LOGGER_PREMIUMMAD_ERROR = "premiummadError";
	}
	
	interface Request{
		String XFROM = "X-From";
	    String USERID = "X-User-Id";
	}

	interface OtherConstant{
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
		
		int POLICY_TYPE_RTB = 8; 
	}
	
	interface ErrorMessage{
		String NO_UPDATE_ID = "更新时id不能为空";
		String NO_UPDATE_TYPE = "更新时type不能为空";
		String ERROR_UPDATE_TYPE = "updateType不正确";
		String NO_UPDATE_STATUS = "更新状态时status不能为空";
		String ERROR_UPDATE_STATUS = "更新状态时status范围有误";
		
		/* 策略验证信息 */
		String ERROR_WEIGHT_FORMAT = "权重格式错误";
	}
	
	interface DB{
		int DIM_DATE = 1;
		int DIM_HOUR = 2;
		int DIM_MEDIA = 4;
		int DIM_ADSPACE = 8;
		int DIM_POLICY = 16;
		int DIM_DSP = 32;
		int DIM_MIN_VAL = DIM_DATE;
		int DIM_MAX_VAL = DIM_DATE + DIM_HOUR + DIM_MEDIA + DIM_ADSPACE + DIM_POLICY + DIM_DSP;
		
		int IS_REALTIME = 1;
		int IS_OFFLINE = 0;
		
		int TYPE_DEFAULT = 1;
		int TYPE_CARRIER = 2;
		int TYPE_DEVICE = 4;
		int TYPE_CONN = 8;
		int TYPE_LOCATION = 16;
		int TYPE_MEDIA = 32;
		int TYPE_MIN_VAL = TYPE_DEFAULT;
		int TYPE_MAX_VAL = TYPE_DEFAULT + TYPE_CARRIER + TYPE_CONN + TYPE_DEVICE + TYPE_LOCATION + TYPE_MEDIA;
	}
}
