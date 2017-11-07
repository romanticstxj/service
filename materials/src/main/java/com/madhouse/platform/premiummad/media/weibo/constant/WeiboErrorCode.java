package com.madhouse.platform.premiummad.media.weibo.constant;

/**
 * 新浪微博错误编码
 */
public enum WeiboErrorCode {
	WEC000(0, "成功"), 
	WEC100(100, "系统错误"), 
	WEC101(101, "DSP验证失败"),
	WEC201(201, "缺乏必要的参数"),
	WEC202(202, "参数格式错误"),
	WEC203(203, "JSON数据格式错误"),
	WEC299(299, "其他格式错误"),
	WEC301(301, "客户不存在"),
	WEC302(302, "客户名称为空"),
	WEC303(303, "备注信息为空"),
	WEC304(304, "资质文件格式错误"),
	WEC305(305, "资质文件加载失败"),
	WEC306(306, "设计品牌保护无法推广"), 
	WEC307(307, "行业错误"),
	WEC399(399, "其他客户信息同步错误"),
	WEC403(403, "文件过大"),
	WEC404(404, "URL为空或格式不合法"),
	WEC405(405, "不支持的图片尺寸"),
	WEC406(406, "客户未通过审核"),
	WEC407(407, "图片保存失败"),
	WEC411(411, "title错误"),
	WEC412(412, "desc错误"),
	WEC413(413, "uid错误"), 
	WEC414(414, "mblog_text错误"),
	WEC415(415, "button_url错误"),
	WEC416(416, "非PD限制"),
	WEC417(417, "landingpage_url转换错误"),
	WEC418(418, "button_url转换错误"),
	WEC419(419, "视频-图片数量过多"),
	WEC420(420, "视频-图片上传失败"),
	WEC421(421, "视频-创意审核未通过"),
	WEC422(422, "不支持的创意"),
	WEC423(423, "不可重复创建"),
	WEC424(424, "创建活动对象失败"),
	WEC425(425, "创建微博失败"),
	WEC426(426, "粉丝保护名单"),
	WEC427(427, "button_type错误"),
	WEC428(428, "video_url错误"),
	WEC429(429, "button_url错误"),
	WEC430(430, "九宫格-缺少grid参数"),
	WEC431(431, "九宫格-grid对象数量错误"),
	WEC432(432, "九宫格-tag_url错误"),
	WEC433(433, "九宫格-创建标签对象失败"),
	WEC434(434, "九宫格-标签关联失败"),
	WEC435(435, "九宫格-标签类型错误"),
	WEC436(436, "九宫格-callup_type错误"),
	WEC437(437, "九宫格-h5_url错误"),
	WEC438(438, "九宫格-转换内联失败"),
	WEC439(439, "九宫格-deeplink_url错误"),
	WEC441(441, "button_type错误"),
	WEC446(446, "obj_id校验失败"),
	WEC461(461, "应用类型错误"),
	WEC462(462, "应用参数错误"),
	WEC463(463, "应用上传失败"),
	WEC480(480, "视频大小超过限制"),
	WEC481(481, "视频上传初始化失败"),
	WEC482(482, "视频上传失败"),
	WEC499(499, "其他广告信息同步错误"),
	WEC501(501, "开始时间或（和）结束时间为空，或者格式错误"),
	WEC502(502, "结束时间小于开始时间"),
	WEC599(599, "其他报表信息同步错误制");

	int value;
	String descrip;

	WeiboErrorCode(int value, String descrip) {
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
		for (WeiboErrorCode item : WeiboErrorCode.values()) {
			if (item.getValue() == value) {
				return item.getDescrip();
			}
		}
		return null;
	}

}
