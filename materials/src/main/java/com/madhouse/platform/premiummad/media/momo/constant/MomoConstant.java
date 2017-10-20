package com.madhouse.platform.premiummad.media.momo.constant;

public enum MomoConstant {

	// 广告创意尺寸
	FEED_LANDING_PAGE_LARGE_IMG(1, "690*345"), FEED_LANDING_PAGE_SQUARE_IMG(2, "360*360");
	
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
