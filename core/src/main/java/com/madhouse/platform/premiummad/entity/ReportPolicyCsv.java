package com.madhouse.platform.premiummad.entity;

import java.util.Date;

import com.madhouse.platform.premiummad.annotation.CsvColumn;

public class ReportPolicyCsv{
    private Integer id;

    private Integer policyId;
    @CsvColumn(title="Deal ID")
    private Integer policyDealId;
    @CsvColumn(title="流量策略名称")
    private String policyName;
    
    private Integer policyType;	//策略类型
    @CsvColumn(title="交易模式")
    private String policyTypeName; //策略类型名称(1: PDB, 2: PD, 4: PMP, 8: RTB)

    private Integer dspId;
    @CsvColumn(title="需求方")
    private String dspName;
    
    private Integer mediaId;
    @CsvColumn(title="媒体")
    private String mediaName;
    
    private Integer adspaceId;
    @CsvColumn(title="广告位")
    private String adspaceName;
    
    private Integer carrier; //运营商code
    
    private Integer conn; //联网方式code
    
    private Integer device; //设备类型code
    
    private String location; //地域列
    @CsvColumn(title="城市")
    private String cityName;
    @CsvColumn(title="省份")
    private String provinceName;
    @CsvColumn(title="国家")
    private String countryName;
    @CsvColumn(title="小时")
    private Byte hour;

    private Date date;
    @CsvColumn(title="日期")
    private String dateStr;
    @CsvColumn(title="请求数")
    private Long reqs;
    @CsvColumn(title="填充数")
    private Long bids;
    @CsvColumn(title="超时数")
    private Long timeouts;
    @CsvColumn(title="错误数")
    private Long errs;
    @CsvColumn(title="赢得竞价数")
    private Long wins;
    @CsvColumn(title="曝光数")
    private Long imps;
    @CsvColumn(title="点击数")
    private Long clks;
    @CsvColumn(title="有效曝光数")
    private Long vimps;
    @CsvColumn(title="有效点击数")
    private Long vclks;

    private Double income;

    private Double cost;
    
    @CsvColumn(title="填充率")
	private String bidsRateStr;
	@CsvColumn(title="点击率")
	private String vclksRateStr;
	@CsvColumn(title="媒体收入")
	private String incomeStr;
	@CsvColumn(title="需求方花费")
	private String costStr;
	@CsvColumn(title="差价")
	private String priceDiffStr;
	@CsvColumn(title="利润率")
	private String profitRateStr;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPolicyId() {
		return policyId;
	}
	public void setPolicyId(Integer policyId) {
		this.policyId = policyId;
	}
	public Integer getPolicyDealId() {
		return policyDealId;
	}
	public void setPolicyDealId(Integer policyDealId) {
		this.policyDealId = policyDealId;
	}
	public String getPolicyName() {
		return policyName;
	}
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}
	public Integer getPolicyType() {
		return policyType;
	}
	public void setPolicyType(Integer policyType) {
		this.policyType = policyType;
	}
	public String getPolicyTypeName() {
		return policyTypeName;
	}
	public void setPolicyTypeName(String policyTypeName) {
		this.policyTypeName = policyTypeName;
	}
	public Integer getDspId() {
		return dspId;
	}
	public void setDspId(Integer dspId) {
		this.dspId = dspId;
	}
	public String getDspName() {
		return dspName;
	}
	public void setDspName(String dspName) {
		this.dspName = dspName;
	}
	public Integer getMediaId() {
		return mediaId;
	}
	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
	}
	public String getMediaName() {
		return mediaName;
	}
	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}
	public Integer getAdspaceId() {
		return adspaceId;
	}
	public void setAdspaceId(Integer adspaceId) {
		this.adspaceId = adspaceId;
	}
	public String getAdspaceName() {
		return adspaceName;
	}
	public void setAdspaceName(String adspaceName) {
		this.adspaceName = adspaceName;
	}
	public Integer getCarrier() {
		return carrier;
	}
	public void setCarrier(Integer carrier) {
		this.carrier = carrier;
	}
	public Integer getConn() {
		return conn;
	}
	public void setConn(Integer conn) {
		this.conn = conn;
	}
	public Integer getDevice() {
		return device;
	}
	public void setDevice(Integer device) {
		this.device = device;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public Byte getHour() {
		return hour;
	}
	public void setHour(Byte hour) {
		this.hour = hour;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public Long getReqs() {
		return reqs;
	}
	public void setReqs(Long reqs) {
		this.reqs = reqs;
	}
	public Long getBids() {
		return bids;
	}
	public void setBids(Long bids) {
		this.bids = bids;
	}
	public Long getTimeouts() {
		return timeouts;
	}
	public void setTimeouts(Long timeouts) {
		this.timeouts = timeouts;
	}
	public Long getErrs() {
		return errs;
	}
	public void setErrs(Long errs) {
		this.errs = errs;
	}
	public Long getWins() {
		return wins;
	}
	public void setWins(Long wins) {
		this.wins = wins;
	}
	public Long getImps() {
		return imps;
	}
	public void setImps(Long imps) {
		this.imps = imps;
	}
	public Long getClks() {
		return clks;
	}
	public void setClks(Long clks) {
		this.clks = clks;
	}
	public Long getVimps() {
		return vimps;
	}
	public void setVimps(Long vimps) {
		this.vimps = vimps;
	}
	public Long getVclks() {
		return vclks;
	}
	public void setVclks(Long vclks) {
		this.vclks = vclks;
	}
	public Double getIncome() {
		return income;
	}
	public void setIncome(Double income) {
		this.income = income;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
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
	public String getCostStr() {
		return costStr;
	}
	public void setCostStr(String costStr) {
		this.costStr = costStr;
	}
	public String getPriceDiffStr() {
		return priceDiffStr;
	}
	public void setPriceDiffStr(String priceDiffStr) {
		this.priceDiffStr = priceDiffStr;
	}
	public String getProfitRateStr() {
		return profitRateStr;
	}
	public void setProfitRateStr(String profitRateStr) {
		this.profitRateStr = profitRateStr;
	}
	
}