package com.madhouse.platform.premiummad.media.letv.constant;

public enum LetvConstant {

    PLATFORM_PC(1,"PC"),
    PLATFORM_ANDROID_WAP(4,"Android WAP"),
    PLATFORM_ANDROID_PHONE(6,"Android phone"),
    PLATFORM_IPHONE(7,"iPhone"),
    PLATFORM_IPAD(8,"iPad"),
    PLATFORM_IOS_WAP(19,"IOS WAP"),

    MEDIA_TYPE_LETV(1,"乐视网"),
    MEDIA_TYPE_OTHER(2,"其他媒体"),

    MONITOR_IMP(0,"impression"),
    MONITOR_CLK(1,"click"),

    RESPONSE_SUCCESS(0,"执行成功"),
    RESPONSE_SYSTEM_AUTH_FAIL(1,"系统认证失败"),
    RESPONSE_PARAM_CHECK_FAIL(2,"请求参数校验错误"),
   
    //广告类型
    AD_TYPE_PAGE(0, "页面"),
    AD_TYPE_DRAW_CURTAIN(1, "拉幕"),
    AD_TYPE_PREVIOUS_POST(2, "前贴"),
    AD_TYPE_STANDARD_PLATE(3, "标版"),
    AD_TYPE_IN_POST(4, "中贴"),
    AD_TYPE_POST(5, "后贴"),
    AD_TYPE_SUSPEND(6, "暂停"),
    AD_TYPE_FLOAT(7, "浮层"),
    AD_TYPE_SCREEN_SAVER(8, "屏保"),
    AD_TYPE_STARTING_UP(9, "开机"),
    AD_TYPE_SHUTDOWN(10, "关机"),
    AD_TYPE_BANNER(12, "横幅"),
    AD_TYPE_ILLUSTRATION(13, "插屏"),
    AD_TYPE_FOCUSMAP(14, "焦点图"),
    AD_TYPE_APPLICATION_STARTUP_DIAGRAM(16, "应用启动图"),
    AD_TYPE_STREAM(17, "信息流");

    int value;
    String description;

    LetvConstant(int value,String description) {
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
