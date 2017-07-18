package com.madhouse.platform.premiummad.constant;

/**
 * 广告类型
 */
public enum DeliveryType {
	DT10001(1, "PDB"), 
	DT10002(2, "PD"), 
	DT10003(4, "PMP"),
	DT10004(8, "RTB");
	
	int value;
	String descrip;

	DeliveryType(int value, String descrip) {
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
		for (DeliveryType item : DeliveryType.values()) {
			if (item.getValue() == value) {
				return item.getDescrip();
			}
		}
		return null;
	}
}
