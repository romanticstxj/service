package com.madhouse.platform.premiummad.media.constant;

/**
 * 腾讯审核结果
 */
public enum LetvErrorCode {
	LEC101(101, "广告加载失败-广告加载经常是因为根据广告 url 地址获取信息时超时,确保 url 正常的情况下重试"),
	LEC102(102, "必填项，不支持的文件格式，目前支持的文件格式：jpg,png,swf,flv,mp4,vpaid"),
	LEC104(104, "执行插入过程异常该错误经常是因为将广告记录到Adx超时,确保url正常的情况下重试"),
	LEC105(105, "广告所属的广告主不能为空  添加广告的 advertiser 属性"),
	LEC106(106, "广告生效时间为空或者不能解析  注：时间格式为 YYYY-mm-dd"),
	LEC107(107, "广告失效时间为空或者不是有效格式或者广告失效时间小于当前时间注：时间格式为 YYYY-mm-dd"),
	LEC108(108, "广告尺寸不符合 adx 所有媒体广告位要求  更换适当尺寸素材"),
	LEC110(110, "参数中存在广告地址为空"),
	LEC111(111, "第三方监测地址错误  核对监测地址后重试"),
	LEC112(112, "同步广告物料的 ad 参数必须要有  添加 ad 参数列表"),
	LEC113(113, "广告物料的跳转地址 landingpage 参数不能为空添加可用的 landingpage 参数"),
	LEC114(114, "广告时长不能解析  将时长值修改为整数值"),
	LEC115(115, "广告的宽度必填且为整数类型值  将宽度修改为整数值"),
	LEC116(116, "广告的高度必填且为整数类型值  将高度修改为整数值"),
	LEC117(117, "此广告 url 与 adx 库的已审核通过广告重复 更换广告 url 后重新上传"),
	LEC118(118, "广告对应的平台为整数类型值  将平台修改为整数值"),
	LEC119(119, "adx 平台库没有此广告对应的平台值根据所给平台表更改正确平台值"),
	LEC120(120, "广告的媒体属性不正确  更改正确媒体值"),
	LEC121(121, "广告主非白名单  填写规范完整的广告主名称"),
	LEC122(122, "广告的行业属性不正确  更改正确行业值"),
	LEC123(123, "广告的广告位类型 display 不正确更改正确广告位类型 display值"),
	LEC201(201, "上传时间不能解析 注：时间格式为 YYYY-mm-dd"),
	LEC301(301, "adurl 不能为空  添加 adurl 属性"),
	LEC302(302, "该广告 url 并未同步到 Adx 中确保url正常的情况下调用同步操作同步广告到 Adx"),
	LEC303(303, "缺少获取状态的 crid 字段，即缺少创意 id填入 crid"),
	LEC304(304, "该 crid 尚未同步到 adx，请稍候再试确保 crid 正常的情况下调用同步操作同步广告到 Adx"),
	LEC401(401, "报表查询的开始时间为空或者不能解析  注：时间格式为 YYYY-mm-dd"),
	LEC402(402, "报表查询的结束时间为空或者不能解析  注：时间格式为 YYYY-mm-dd"),
	LEC403(403, "报表查询时间跨度超过 7 天  日期段跨度最大允许 7 天"),
	LEC404(404, "报表查询时的结束时间早于开始时间核对时间后重试"),
	LEC501(501, "ad_model_id 参数必填且唯一模版 id 错误，添加正确的模版 id，可以通过 modellist 接口获取所有可用的模版 id"),
	LEC502(502, "crid 参数必填且唯一，不能有中文和空格填写创意 id，创意 id 应该在DSP 内唯一，相同的创意 id会导致之后上传的覆盖之前上传的广告"),
	LEC503(503, "广告模板对应的上传字段不全或者无效参照 modellist，补齐必须字段"),
	LEC505(505, "同步广告物料的 newad 参数必须要有添加 newad 参数列表"),
	LEC506(506, "title 长度不符合模版要求修改 title 长度"),
	LEC507(507, "data 长度不符合模版要求修改 data 长度"),
	LEC508(508, "素材尺寸不符合模版要求更换适当尺寸素材"),
	LEC509(509, "视频素材长度不符合模版要求（时长）广告时长不符合模版要求"),
	LEC510(510, "新上传广告接口暂时停止普通模板上传请使用旧接口上传");

	int value;
	String descrip;

	LetvErrorCode(int value, String descrip) {
		this.value = value;
		this.descrip = descrip;
	}

	public int getValue() {
		return value;
	}

	public String getDescrip() {
		return descrip;
	}

	public static String getDescrip(int value) {
		for (LetvErrorCode item : LetvErrorCode.values()) {
			if (item.getValue() == value) {
				return item.getDescrip();
			}
		}
		return null;
	}

}
