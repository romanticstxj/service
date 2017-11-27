package com.madhouse.platform.premiummad.constant;

/**
 * 系统常量
 */
public interface SystemConstant {
	
	interface Logging{
		String PREMIUMMAD = "premiummad";
	    String LOGGER_PREMIUMMAD = PREMIUMMAD;//日志中使用的
	    String LOGGER_PREMIUMMAD_ERROR = "premiummadError";
	    String AOP_SERVICE_IMPL_EXPR = "execution(* com.madhouse.platform.premiummad.service.impl.*.*(..))";
	    String AOP_SERVICE_CNTR_EXPR = "execution(* com.madhouse.platform.premiummad.controller.*.*(..))";
	}
	
	interface Request{
		String XFROM = "X-From";
		String XFROM_DEFAULT_VALUE = "exchange.prd.onemad.com";
	    String USERID = "X-User-Id";
	    String CONTENT_TYPE = "Content-Type";
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
		int DICT_REQUEST_BLOCK_TYPE = 4;
		
		long WEEK = 1000L * 60L * 60L * 24L * 7L;
		
		int POLICY_TYPE_RTB = 8; 
		
		int ALL_HOURS = 24;
		int HOUR_ZERO = 0;
		int HOUR_23 = 23;
		
		String URL_REGEX = "^(https?):\\/\\/[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-\\.,@?^=%&:\\/~\\+#]*[\\w\\-\\@?^=%&\\/~\\+#])?$";
	}
	
	interface ErrorMessage{
		String NO_UPDATE_ID = "更新时id不能为空";
		String NO_UPDATE_TYPE = "更新时type不能为空";
		String ERROR_UPDATE_TYPE = "updateType不正确";
		String NO_UPDATE_STATUS = "更新状态时status不能为空";
		String ERROR_UPDATE_STATUS = "更新状态时status范围有误";
		
		/* 策略验证信息 */
		String ERROR_WEIGHT_FORMAT = "权重格式错误";
		
		String NOT_PAST = "日期必须大于等于今天";
	}
	
	interface DB{
		int DESC_LENGTH = 400;
		
		int DIM_DATE = 1;
		int DIM_HOUR = 2;
		int DIM_MEDIA = 4;
		int DIM_ADSPACE = 8;
		int DIM_POLICY = 16;
		int DIM_DSP = 32;
		int DIM_MIN_VAL = DIM_DATE;
		int DIM_MAX_VAL = DIM_DATE + DIM_HOUR + DIM_MEDIA + DIM_ADSPACE + DIM_POLICY + DIM_DSP;
		
		int DIM_SORTING_POLICY = 1;
		int DIM_SORTING_DSP = 2;
		int DIM_SORTING_MEDIA = 4;
		int DIM_SORTING_ADSPACE = 8;
		int DIM_SORTING_LOCATION = 16;
		int DIM_SORTING_DATE = 32;
		int DIM_SORTING_HOUR = 64;
		
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
		
		int IS_LIMIT = 1;
		int IS_NOT_LIMIT = 0;

		//物料审核方式(后端)
		int NO_AUDIT = 0; //不审核
		int AUDIT_BY_SSP = 1; //平台审核
		int AUDIT_BY_MEDIA = 2; //媒体审核
		
		//物料审核状态
		int AUDIT_PASS = 2; //审核通过
		int AUDIT_FAIL = -1; //审核未通过
		int TO_BE_AUDIT_BE = 0; //待审核(后端)
		int TO_BE_AUDIT_FE = 1; //待审核(前端)
		int IN_AUDIT = 1; //审核中(后端)
		int TO_BE_SUBMIT = 0; //待提交(前端)
		
		String impUrlsDelimiter = "`";
	}
	
	/* 媒体审核对象 */
	interface MediaAuditObject {
		Byte MATERIAL = 1;
		Byte ADVERTISER = 2;
	}
}
