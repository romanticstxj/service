package com.madhouse.platform.premiummad.media.constant;

/**
 * 腾讯审核结果
 */
public enum TencentErrorCode {
	TEC000(0, "成功"), 
	TEC100(100, "系统错误，请稍后再试"), 
	TEC101(101, "DSP ID，Token或者IP验证失败"),
	TEC102(102, "业务处理的过程中发生错误"),
	TEC200(200, "DSP没有权限操作该广告主"),
	TEC300(300, "必须的参数没有传入"),
	TEC301(301, "至少必须传入此组参数中的一个"),
	TEC302(302, "参数必须为整数"),
	TEC303(303, "参数格式错误"),
	TEC304(304, "参数不在允许的值的范围内"),
	TEC305(305, "参数不是正数"),
	TEC306(306, "参数不是合法的URL"), 
	TEC307(307, "参数太长，超出了允许的长度范围"),
	TEC308(308, "参数不是合法的YYYY-MM-DD的日期"),
	TEC309(309, "参数不是合法的JSON数据，无法被解析"),
	TEC310(310, "可以被解析但是是空的JSON数据"),
	TEC311(311, "参数太短，小于允许的长度范围"),
	TEC312(312, "可以被解析的JSON数据，但不是API要求的JSON格式"),
	TEC313(313, "参数不是0或正数"),
	TEC314(314, "参数的值太大，超过了允许的最大值"),
	TEC315(315, "参数的值不允许被修改"),
	TEC316(316, "页面暂时不开放"), 
	TEC400(400, "广告主ID不存在/没有匹配的广告主信息"),
	TEC500(500, "广告主名称重复"),
	TEC501(501, "广告主ID不存在"),
	TEC502(502, "广告主不能被删除"),
	TEC503(503, "广告主不能被修改"),
	TEC504(504, "广告主行业不合法"),
	TEC505(505, "广告主URL为空或者是URL不合法"),
	TEC506(506, "广告主vocation_code不合法"),
	TEC507(507, "广告主area为空"),
	TEC508(508, "广告主qualification_class不合法"),
	TEC510(510, "广告主qualification_files不合法"),
	TEC511(511, "广告主file_name不合法"),
	TEC512(512, "广告主file_url不合法"),
	TEC513(513, "不支持的广告主资质文件的格式，目前支持的文件格式：jpg,jpeg,gif,png"),
	TEC515(515, "广告主name为空"),
	TEC516(516, "广告主MEMO为空"),
	TEC517(517, "广告主同步API在一天内相同的广告主资质信息重复调用,5分钟内不允许重复提交完全相同的广告主资质信息内容"),
	TEC518(518, "品牌gpb不需要自己上传广告主"),
	TEC519(519, "广告主还未通过审核"),
	TEC529(529, "广告主投放资质不可修改"),
	TEC601(601, "文件加载失败"),
	TEC602(602, "未知的文件格式"),
	TEC603(603, "不支持的格式"),
	TEC604(604, "Flv素材获取不到时长信息"),
	TEC605(605, "URL对应的素材发生了变化，请换一个URL"),
	TEC606(606, "执行插入过程中发生了错误，请关注是否是同时上传"),
	TEC609(609, "素材URL为空或者是地址不合法"),
	TEC610(610, "目标地址为空或者是地址不合法"),
	TEC611(611, "必须指定广告主"),
	TEC612(612, "第三方曝光监测地址错误"),
	TEC613(613, "素材过大，超过素材的大小限制"),
	TEC614(614, "传入的file_info格式错误，无法解析成数组"),
	TEC615(615, "URL对应的广告主发生变化，不能上传"),
	TEC616(616, "同一次请求中，一个素材URL出现了多次"),
	TEC617(617, "限制素材上传个数"),
	TEC618(618, "第三方曝光监测地址不在白名单里"),
	TEC619(619, "第三方曝光监测数目超出限制"),
	TEC620(620, "DSP侧的素材ID为空或含有不合法字符或长度超过了64"),
	TEC625(625, "该DSP不支持投放社交化广告"),
	TEC626(626, "sns_type参数不合法"),
	TEC627(627, "社交化广告的文件格式不支持，只支持gif和jpg"),
	TEC628(628, "素材尺寸不支持社交化广告"),
	TEC629(629, "素材上传个数达到最大日上限"),
	TEC630(630, "file_vid含有不合法字符或长度超过了255"),
	TEC631(631, "display_id不合法"),
	TEC632(632, "monitor_position不合法"),
	TEC633(633, "monitor_position的个数与monitor_url的个数不一致"),
	TEC634(634, "DSP侧的素材ID不允许修改"),
	TEC635(635, "同一个DSP侧的素材ID下的文件有一些参数的值不相等"),
	TEC636(636, "file_text参数不合法"),
	TEC637(637, "DSP侧的素材ID在传入file_text参数的情况下必须也同时传入"),
	TEC638(638, "在多素材的情况下，由于同一个DSP侧的素材ID下的其他的文件有错误，此文件跳过处理"),
	TEC639(639, "在多素材的情况下，同一个DSP侧的素材ID下的文件个数超过了允许的最大个数"),
	TEC640(640, "传入的order_info格式错误，无法解析成数组"),
	TEC641(641, "同一次请求中，一个dsp_order_id出现了多次"),
	TEC642(642, "flv声音出错，无音频"),
	TEC643(643, "该广告主不允许上传曝光监测点"),
	TEC644(644, "第三方点击监测地址错误"),
	TEC645(645, "第三方点击监测数目超出限制"),
	TEC646(646, "第三方点击监测地址不在白名单里"),
	TEC647(647, "flv素材尺寸比例错误"),
	TEC648(648, "flv素材播放时长错误"),
	TEC649(649, "ott素材仅支持flv格式"),
	TEC650(650, "ott素材大小限制16：9&&width>1920"),
	TEC651(651, "ott素材只支持上传一个flv文件，"),
	TEC652(652, "display_id设置无效"),
	TEC653(653, "display_id和素材不匹配"),
	TEC654(654, "display_id和素材不匹配,微动图"),
	TEC655(655, "display_id和素材不匹配,新闪屏"),
	TEC656(656, "order接口返回的素材没有通用素材"),
	TEC657(657, "新闪屏中视频时长不符合规格1-5s"),
	TEC658(658, "文件地址长度超过数据库字段长度1000"),
	TEC659(659, "目标地址长度超过数据库字段长度1000"),
	TEC700(700, "广告位不存在"),
	TEC801(801, "结束时间大于开始时间"),
	TEC802(802, "超过开始和结束时间限制"),
	TEC803(803, "开始和结束时间不能跨年"),
	TEC900(900, "用户不存在"),
	TEC901(901, "更新数组为空"),
	TEC902(902, "用户名称重复"),
	TEC903(903, "dsp帐号只允许改密码"),
	TEC904(904, "达到最大账号数"),
	TEC1101(1101, "广告创意(dsp_order_id)已存在"),
	TEC1102(1102, "广告创意(dsp_order_id)不存在"),
	TEC1103(1103, "广告创意修改-广告形式id不能被修改"),
	TEC1104(1104, "广告创意修改-广告形式id不存在"),
	TEC1105(1105, "广告创意ad_content必填"),
	TEC1106(1106, "广告创意ad_content必须是数组"),
	TEC1107(1107, "广告创意ad_content必填字段"),
	TEC1108(1108, "广告创意ad_content.file_text为空"),
	TEC1109(1109, "广告创意ad_content.file_url为空"),
	TEC1110(1110, "广告创意ad_content.file_text超长>255"),
	TEC1111(1111, "广告创意ad_content.file_url超长>1000"),
	TEC1112(1112, "广告创意ad_content.file_url不合法"),
	TEC1113(1113, "广告创意ad_content.file_md5必须是32位md5值"),
	TEC1114(1114, "广告创意ad_content.file_md5未找到该广告主在系统中匹配项创意，请先上传一个文件，再使用该字段。"),
	TEC1115(1115, "广告创意ad_content内容和display_id不匹配。注意关于文件扩展名不匹配的错误提示，不能直接修改文件扩展名，需要文件实际格式和定义匹配"),
	TEC1116(1116, "广告形式微动图特殊处理失败wdt10711-news_client_msg5-新闻广告主端-信息流GIF-外链"),
	TEC1117(1117, "广告形式新闪屏特殊处理失败xsp11267-App_News_Splash"),
	TEC1118(1118, "没有数据"),
	TEC1119(1119, "广告创意修改-广告形式id不开放"),
	TEC1120(1120, "monitor_url监测地址，url解析异常"),
	TEC1121(1121, "monitor_url监测地址，采用了HTTPS监测"),
	TEC1122(1122, "monitor_url监测地址，域名有误"),
	TEC1123(1123, "monitor_url监测地址，非腾讯媒体"),
	TEC1124(1124, "monitor_url监测地址，宏参数有问题"),
	TEC1125(1125, "monitor_url监测地址，内部接口调用失败"),
	TEC1126(1126, "广告创意ad_ext.evokeapp.app_name超长 >255"),
	TEC1127(1127, "广告创意ad_ext.evokeapp.pkg_name超长 >255"),
	TEC1128(1128, "广告创意ad_ext.evokeapp.deep_link超长 >1000"),
	TEC1129(1129, "5分钟内有完全重复的相同内容请求调用接口");

	int value;
	String descrip;

	TencentErrorCode(int value, String descrip) {
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
		for (TencentErrorCode item : TencentErrorCode.values()) {
			if (item.getValue() == value) {
				return item.getDescrip();
			}
		}
		return null;
	}

}
