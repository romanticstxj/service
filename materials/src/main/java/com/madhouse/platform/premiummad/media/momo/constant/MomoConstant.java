package com.madhouse.platform.premiummad.media.momo.constant;

public enum MomoConstant {

	// 广告创意尺寸
	FEED_LANDING_PAGE_LARGE_IMG(1, "690*345"), //大图
	FEED_LANDING_PAGE_SQUARE_IMG(2, "360*360"), //单图
	NEARBY_LANDING_PAGE_NO_IMG(2, "150*150"); //图标
	
    int value;
    String description;

    MomoConstant(int value,String description) {
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
