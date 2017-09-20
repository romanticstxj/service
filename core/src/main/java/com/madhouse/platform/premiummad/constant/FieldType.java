package com.madhouse.platform.premiummad.constant;

public enum FieldType {
	
	startDate("startDate", "开始日期");
	
	String en;
	String cn;
	
	private FieldType(String en, String cn) {
		this.en = en;
		this.cn = cn;
	}

	public String getEn() {
		return en;
	}
	
	public String getCn() {
		return cn;
	}

	public static String getChineseMessage(String en){
		String chineseMessage = (en == null) ? "" : en;
		FieldType[] fieldTypes = values();
		for(FieldType ft: fieldTypes){
			if(ft.getEn().equals(en)){
				chineseMessage = ft.getCn();
				break;
			}
		}
		
		return chineseMessage;
	}
	
	public static void main(String args[]){
		String ft = FieldType.getChineseMessage("startDate");
		System.out.println(ft);
	}
}
