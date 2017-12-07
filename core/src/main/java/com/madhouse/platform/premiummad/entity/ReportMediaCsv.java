package com.madhouse.platform.premiummad.entity;

public class ReportMediaCsv extends ReportMedia{
	
	private String bidsRateStr;
	
	private String vclksRateStr;
	private String incomeStr;
	private String ecpmStr;
	private String ecpcStr;
	
	public String getBidsRateStr() {
		return bidsRateStr;
	}
	public void setBidsRateStr(String bidsRateStr) {
		this.bidsRateStr = bidsRateStr;
	}
	public String getVclksRateStr() {
		return vclksRateStr;
	}
	public void setVclksRateStr(String vclksRateStr) {
		this.vclksRateStr = vclksRateStr;
	}
	public String getIncomeStr() {
		return incomeStr;
	}
	public void setIncomeStr(String incomeStr) {
		this.incomeStr = incomeStr;
	}
	public String getEcpmStr() {
		return ecpmStr;
	}
	public void setEcpmStr(String ecpmStr) {
		this.ecpmStr = ecpmStr;
	}
	public String getEcpcStr() {
		return ecpcStr;
	}
	public void setEcpcStr(String ecpcStr) {
		this.ecpcStr = ecpcStr;
	}
}
