package com.madhouse.platform.smartexchange.constant;

import java.util.ResourceBundle;

/**
 * 系统常量
 */
public interface SystemConstant {

    ResourceBundle bundle = ResourceBundle.getBundle("exchange");

    int RESPONSECODE_SUCCESS = 0;// 成功
    int RESPONSECODE_ERROR = 1;// 程序处理错误，参数不对，如userId为空，广告主名称重复，上传文件格式不对等等
    int RESPONSECODE_FATAL = 2;// 系统错误，json转换异常，反射错误，数据库连接错误

    String SMARTEXCHANGE = "smartexchange";
    String DATASOURCE_SMARTEXCHANGE = SMARTEXCHANGE;
    String DATASOURCE_SANDBOX = "sesandbox";
    String LOGGER_SMARTEXCHANGE = SMARTEXCHANGE;//日志中使用的
    String LOGGER_SMARTEXCHANGE_TASK = "smartexchangeTask";

    String TABLENAME_ADSPACE = "se_supplier_adspace";

    String DEMAND_TYPE_PREMIUM = "1";
    String DEMAND_TYPE_BAIDU = "2";
    String DEMAND_TYPE_PG = "3";

    // ------每次请求必带的参数
    String URL = "url";
    String USERID = "userId";

    String BATCH_DATA_KEY = "dataList";//批量操作时,Map传参中的key值

    String MEMBERID = bundle.getString("memberId");
    String APPID = bundle.getString("appid");
    String AGID = bundle.getString("agid");
    String KEY = bundle.getString("key");
    long WEEK = 1000L * 60L * 60L * 24L * 7L;
    

}
