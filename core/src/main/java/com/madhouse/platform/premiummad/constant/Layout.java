package com.madhouse.platform.premiummad.constant;

/**
 * 广告形式
 */
public enum Layout {
	LO10001(101, "Banner-横幅"), 
	LO10002(102, "Banner-焦点"), 
	LO10003(111, "Banner-插屏"), 
	LO10004(112, "Banner-全屏"), 
	LO10005(121, "Banner-开屏"), 
	LO10006(131, "Banner-开机图片"), 
	LO10007(132, "Banner-关机图片"), 
	LO20001(201, "Video-贴片/前贴"), 
	LO20002(201, "Video-贴片/中贴"), 
	LO20003(203, "Video-贴片/后贴"), 
	LO20004(211, "Video-悬浮/暂停"), 
	LO20005(221, "Video-开机视频"), 
	LO20006(222, "Video-关机视频"), 
	LO20007(231, "Banner-开屏视频"), 
	LO30001(301, "Native-图文信息流"), 
	LO30002(302, "Native-图文信息流"),
	LO30003(303, "Native-图文信息流"),
	LO30004(311, "Native-视频信息流");
	
	int value;
	String descrip;

	Layout(int value, String descrip) {
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
		for (Layout item : Layout.values()) {
			if (item.getValue() == value) {
				return item.getDescrip();
			}
		}
		return null;
	}

}
