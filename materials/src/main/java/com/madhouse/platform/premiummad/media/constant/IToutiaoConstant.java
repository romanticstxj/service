package com.madhouse.platform.premiummad.media.constant;

public enum IToutiaoConstant {

	// 物料审核状态.注意,value[0,1,2,3,4]无用,为自定义.返回物料状态码为字符串approved，unaudited。。
	M_STATUS_APPROVED(0, "approved"), // 审核通过
	M_STATUS_UNAUDITED(1, "unaudited"), // 未审核
	M_STATUS_REFUSED(2, "refused"), // 审核未通过
	M_STATUS_ERROR(3, "error"), // 审核错误
	M_STATUS_SUCCESS(4, "success"), // 上传成功

	// 头条广告位类型
	TOUTIAO_FEED_LP_LARGE(1, "690 * 286"), OUTIAO_FEED_LP_SMALL(2, "228 * 150"),

	// nurl
	NURL(1, "http://win.madserving.com/toutiao/winnotice?adspaceid={adspaceid}&uid={user_id}&request_id={request_id}&adid={adid}&price={bid_price}&ip={ip}&timestamp={timestamp}&did={did}"),

	TABLE_NAME(1, "md_adspace"),

	// beta
	IMP_MONITOR_PM(1, "http://imp.beta.madserving.com/imp/v2?{ext}");
	// 生产:
	// IMP_MONITOR_PM(1,"http://imp.premium.madserving.com/imp/v2?{ext}");

	int value;
	String description;

	IToutiaoConstant(int value, String description) {
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
