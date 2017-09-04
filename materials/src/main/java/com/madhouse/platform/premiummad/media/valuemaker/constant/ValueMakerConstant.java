package com.madhouse.platform.premiummad.media.valuemaker.constant;

public enum ValueMakerConstant {
	// 物料审核状态:1未审核，2审核通过，3不通过
	M_STATUS_UNAUDITED(1, "001"), 
	M_STATUS_APPROVED(2, "002"),
	M_STATUS_REFUSED(3, "003"),
	
	//上传返回状态
	RESPONSE_STATUS_200(200,"上传成功"),
	RESPONSE_STATUS_401(401,"认证失败"),
	RESPONSE_STATUS_404(404,"请求Url错误，没有触发业务"),
	RESPONSE_STATUS_422(422,"请求处理失败"),
	RESPONSE_STATUS_500(500,"系统内部错误"),
	
	//ad_type 广告位类型
	VALUEMAKER_AD_TYPE_BANNER(1,"Banner广告"),
	VALUEMAKER_AD_TYPE_OPENSCREEN(2,"开屏广告"),
	VALUEMAKER_AD_TYPE_TABLEPLAQUE(3,"插屏广告"),
	VALUEMAKER_AD_TYPE_INFORMATIONFLOW(4,"信息流广告"),
	
	VALUEMAKER_FROMAT_STATIC(1,"STATIC"),
	VALUEMAKER_FROMAT_DYNAMIC(2,"DYNAMIC"),
	VALUEMAKER_FORMAT_TXT(3,"TXT");
	int value;
	String description;

	ValueMakerConstant(int value, String description) {
		this.value = value;
		this.description = description;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public static String getDescription(int value) {
		for (ValueMakerConstant item : ValueMakerConstant.values()) {
			if (item.getValue() == value) {
				return item.getDescription();
			}
		}
		return "";
	}
	
	//返回状态代码
	public static String getErrorMessage(int key){
		switch (key) {
		case 401:
			return "认证失败";
		case 404:
			return "请求Url错误，没有触发业务";
		case 500:
			return "系统内部错误";
		case 1000:
			return "JSON格式错误";
		case 1001:
			return "创意id缺少或者错误";
		case 1002:
			return "创意id重复";
		case 1003:
			return "创意代码缺少";
		case 1004:
			return "创意行业分类缺少或者错误";
		case 1005:
			return "创意格式缺少或者错误";
		case 1006:
			return "创意宽度缺少或者错误";
		case 1007:
			return "创意高度缺少或者错误";
		case 1008:
			return "adomain_list缺少或者错误";
		case 1009:
			return "系统中创意ID不存在";
		case 1010:
			return "修改失败，创意状态异常";
		case 1011:
			return "创意过期";
		case 1012:
			return "创意时长缺少或者错误";
		case 1013:
			return "素材地址缺少或者错误";
		case 1014:
			return "langdingpage缺少或者错误";
		case 1015:
			return "langdingpage缺少点击检测宏{!vam_click_url}";
		case 1016:
			return "没有调用该API权限";
		case 1017:
			return "移动创意上传方式缺少或者错误";
		case 1018:
			return "移动创意元素缺少或者错误";
		case 1019:
			return "移动创意更新与API不符";
		case 1020:
			return "移动创意ad_type缺少或者错误";
		case 1021:
			return "移动创意元素类型错误";
			
		default:
			return null;
		}
	}
}
