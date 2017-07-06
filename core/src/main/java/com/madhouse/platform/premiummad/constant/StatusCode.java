package com.madhouse.platform.premiummad.constant;

public enum StatusCode {
	SC20000(20000, "操作成功"), //
	SC21003(21003, "userId为空"), //
	SC21004(21004, "缺少必填参数"), //
	SC21013(21013, "url为空"), //
	SC21015(21015, "url不正确"), //
	SC21019(21019, "userIds为null"), // 
	SC21024(21024, "id不正确"), // 
	SC21026(21026, "日期格式错误"), //
	SC21028(21028, "没有权限访问"), //
	SC21014(21014, "url不是以http或https开头"), //
	SC21029(21029, "创建广点通广告位失败"), //
	SC21030(21030, "dsp 和 pdb字段值不可以相同"), //
    SC21031(21031, "不允许批量操作多个dsp的物料"), //
    SC21032(21032, "请提供正确的更新类型"), //

	SC22001(22001, "需求方平台名称不能重复"), //
	SC22002(22002, "需求方平台广告位名称不能重复"), // 
	SC22003(22003, "供应方广告位名称不能重复"), //
	SC22004(22004, "供应方媒体名称不能重复"), //
	SC22005(22005, "需求方平台的允许超时时间不在范围内"), //
	SC22006(22006, "需求方平台广告位关联需求方平台Id错误"), //
	SC22007(22007, "供应方广告位关联供应方媒体Id错误"), //
	SC22008(22008, "广告位key重复"), //
	SC22009(22009, "广告位密钥重复"), //
	SC22010(22010, "广告位尺寸不合法"), //

	SC23001(23001, "需求方平台下有需求方广告位不能删除"), // 
	SC23002(23002, "供应方媒体下有供应方广告位不能删除"), // 
	SC23003(23003, "需求方平台广告位有关联不能删除"), // 
	SC23004(23004, "供应方广告位有关联不能删除"), // 

	SC31001(31001, "系统繁忙，请稍后再试"), //
	
	SC31002(31002, "批量选择为空"), //
	SC31003(31003, "修正参数不能为空"), //
	
	SC410001(410001, "提交广告主失败"),
	SC410002(410002, "提交素材失败");

	int value;
	String descrip;

	StatusCode(int value, String descrip) {
		this.value = value;
		this.descrip = descrip;
	}

	public int getValue() {
		return value;
	}

	public String getDescrip() {
		return descrip;
	}

}
