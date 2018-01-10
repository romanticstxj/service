package com.madhouse.platform.premiummad.media.yiche.constant;

/**
 * 素材模板与素材类型的映射关系
 */
public enum YicheTempMaterialTypeMapping {
	QUOT_LIST(38, 0), //报价信息流图文
	QUOT_ICON(2000, 1), //报价信息流组图
	QUOT_IMG(2001, 2), //报价信息流大图
	QUOT_VIDEO(2002, 3); //报价信息流视频

	int templateId;
	int materialType;

	YicheTempMaterialTypeMapping(int templateId, int materialType) {
		this.templateId = templateId;
		this.materialType = materialType;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public int getMaterialType() {
		return materialType;
	}

	public void setMaterialType(int materialType) {
		this.materialType = materialType;
	}

	public static int getMaterialType(int templateId) {
		for (YicheTempMaterialTypeMapping item : YicheTempMaterialTypeMapping.values()) {
			if (item.getTemplateId() == templateId) {
				return item.getMaterialType();
			}
		}
		return 0;
	}
}
