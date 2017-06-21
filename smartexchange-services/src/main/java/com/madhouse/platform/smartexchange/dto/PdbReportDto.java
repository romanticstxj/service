package com.madhouse.platform.smartexchange.dto;

import java.io.Serializable;
import java.util.Date;

public class PdbReportDto implements Serializable {

	private static final long serialVersionUID = -8154765213008514870L;

	private String dadsid;
	private String padsid;
	private long cnt;
	private long imp;
	private long clk;
	private long distinctImp;
	private long distinctClk;
	private String returncode;
	private String day;
	private String hour;

	private long adCount;//有返回广告的数量
	private long errorCount;//错误数
	private long timeoutCount;

	private float fillRate;
	private float impRate; //曝光率
	private float clkRate;//点击率

	private String mediaName;
	private String adspaceName;
	private String provinceName;
	private String cityName;

	private Date startDate;
	private Date endDate;

	private boolean pdbAdspace;
	private boolean showProvince;
	private boolean showCity;
	private boolean showHour;
	private boolean showDate;

	public String getDadsid() {
		return dadsid;
	}

	public void setDadsid(String dadsid) {
		this.dadsid = dadsid;
	}

	public String getPadsid() {
		return padsid;
	}

	public void setPadsid(String padsid) {
		this.padsid = padsid;
	}

	public long getCnt() {
		return cnt;
	}

	public void setCnt(long cnt) {
		this.cnt = cnt;
	}

	public String getReturncode() {
		return returncode;
	}

	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}

	public long getAdCount() {
		return adCount;
	}

	public void setAdCount(long adCount) {
		this.adCount = adCount;
	}

	public long getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(long errorCount) {
		this.errorCount = errorCount;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getAdspaceName() {
		return adspaceName;
	}

	public void setAdspaceName(String adspaceName) {
		this.adspaceName = adspaceName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public float getFillRate() {
		return fillRate;
	}

	public void setFillRate(float fillRate) {
		this.fillRate = fillRate;
	}

	public boolean getPdbAdspace() {
		return pdbAdspace;
	}

	public void setPdbAdspace(boolean pdbAdspace) {
		this.pdbAdspace = pdbAdspace;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public boolean getShowProvince() {
		return showProvince;
	}

	public void setShowProvince(boolean showProvince) {
		this.showProvince = showProvince;
	}

	public boolean getShowHour() {
		return showHour;
	}

	public void setShowHour(boolean showHour) {
		this.showHour = showHour;
	}

	public boolean getShowCity() {
		return showCity;
	}

	public void setShowCity(boolean showCity) {
		this.showCity = showCity;
	}

	public boolean getShowDate() {
		return showDate;
	}

	public void setShowDate(boolean showDate) {
		this.showDate = showDate;
	}

	public long getImp() {
		return imp;
	}

	public void setImp(long imp) {
		this.imp = imp;
	}

	public long getClk() {
		return clk;
	}

	public void setClk(long clk) {
		this.clk = clk;
	}

	public long getDistinctImp() {
		return distinctImp;
	}

	public void setDistinctImp(long distinctImp) {
		this.distinctImp = distinctImp;
	}

	public long getDistinctClk() {
		return distinctClk;
	}

	public void setDistinctClk(long distinctClk) {
		this.distinctClk = distinctClk;
	}

	public long getTimeoutCount() {
		return timeoutCount;
	}

	public void setTimeoutCount(long timeoutCount) {
		this.timeoutCount = timeoutCount;
	}

	public float getImpRate() {
		return impRate;
	}

	public void setImpRate(float impRate) {
		this.impRate = impRate;
	}

	public float getClkRate() {
		return clkRate;
	}

	public void setClkRate(float clkRate) {
		this.clkRate = clkRate;
	}

}
