package com.madhouse.platform.smartexchange.dto;

import java.io.Serializable;
import java.util.Date;

public class DspReportDto implements Serializable {

	private static final long serialVersionUID = -9186066906032636455L;

	private String padsid;
	private String dadsid;
	private long cnt;
	private long imp;
	private long clk;
	private long distinctImp;
	private long distinctClk;
	private String returncode;
	private String day;
	private String hour;

	private long adCount;
	private long errorCount;
	private long timeoutCount;

	private float fillRate; //填充率
	private float impRate; //曝光率
	private float clkRate;//点击率

	private String dspName;
	private String dspAdspaceName;
	private String pdbName;
	private String pdbAdspaceName;
	private String provinceName;
	private String cityName;

	private Date startDate;
	private Date endDate;
	private boolean dspAdspace;
	private boolean pdb;
	private boolean pdbAdspace;
	private boolean showProvince;
	private boolean showCity;
	private boolean showHour;
	private boolean showDate;

	public String getPadsid() {
		return padsid;
	}

	public void setPadsid(String padsid) {
		this.padsid = padsid;
	}

	public String getDadsid() {
		return dadsid;
	}

	public void setDadsid(String dadsid) {
		this.dadsid = dadsid;
	}

	public long getCnt() {
		return cnt;
	}

	public void setCnt(long cnt) {
		this.cnt = cnt;
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

	public float getFillRate() {
		return fillRate;
	}

	public void setFillRate(float fillRate) {
		this.fillRate = fillRate;
	}

	public float getClkRate() {
		return clkRate;
	}

	public void setClkRate(float clkRate) {
		this.clkRate = clkRate;
	}

	public String getDspName() {
		return dspName;
	}

	public void setDspName(String dspName) {
		this.dspName = dspName;
	}

	public String getDspAdspaceName() {
		return dspAdspaceName;
	}

	public void setDspAdspaceName(String dspAdspaceName) {
		this.dspAdspaceName = dspAdspaceName;
	}

	public String getPdbName() {
		return pdbName;
	}

	public void setPdbName(String pdbName) {
		this.pdbName = pdbName;
	}

	public String getPdbAdspaceName() {
		return pdbAdspaceName;
	}

	public void setPdbAdspaceName(String pdbAdspaceName) {
		this.pdbAdspaceName = pdbAdspaceName;
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

	public boolean getDspAdspace() {
		return dspAdspace;
	}

	public void setDspAdspace(boolean dspAdspace) {
		this.dspAdspace = dspAdspace;
	}

	public boolean getPdb() {
		return pdb;
	}

	public void setPdb(boolean pdb) {
		this.pdb = pdb;
	}

	public boolean getPdbAdspace() {
		return pdbAdspace;
	}

	public void setPdbAdspace(boolean pdbAdspace) {
		this.pdbAdspace = pdbAdspace;
	}

	public long getTimeoutCount() {
		return timeoutCount;
	}

	public void setTimeoutCount(long timeoutCount) {
		this.timeoutCount = timeoutCount;
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

	public float getImpRate() {
		return impRate;
	}

	public void setImpRate(float impRate) {
		this.impRate = impRate;
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

}
