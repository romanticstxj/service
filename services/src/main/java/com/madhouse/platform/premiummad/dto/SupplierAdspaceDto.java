package com.madhouse.platform.smartexchange.dto;

import java.io.Serializable;
import java.util.List;

import com.madhouse.platform.smartexchange.annotation.NotNull;

public class SupplierAdspaceDto implements Serializable {

	private static final long serialVersionUID = -842790879624041042L;

	private Integer id;
	@NotNull
	private String name;
	@NotNull
	private Integer mediaId;
	private String adspaceKey;
	private Long expectImp;
	private Long expectClk;
	private Float expectCtr;
	private String remark;
	private Boolean delFlg;
    private Boolean supportHttps;

	private String mediaName;
	private Integer relatedCount;

	/**
	 * 供应方广告位尺寸
	 */
	private int width;
	private int height;
	private String wh;//前端多个尺寸填写是一个字段，按,和* 分割

	private List<DemandAdspaceDto> demandAdspaces;
	private Integer adType; //广告类型
	private String adTypeName;

	private Double price;
	private String priceType;
	
	private String creativeType; //广告位的物料格式
	private int maxCreativeSize; //物料最大尺寸
	private int titleLength; //广告位标题长度
	private int descLength; //广告位描述长度
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMediaId() {
		return mediaId;
	}

	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
	}

	public String getAdspaceKey() {
		return adspaceKey;
	}

	public void setAdspaceKey(String adspaceKey) {
		this.adspaceKey = adspaceKey;
	}

	public Long getExpectImp() {
		return expectImp;
	}

	public void setExpectImp(Long expectImp) {
		this.expectImp = expectImp;
	}

	public Long getExpectClk() {
		return expectClk;
	}

	public void setExpectClk(Long expectClk) {
		this.expectClk = expectClk;
	}

	public Float getExpectCtr() {
		return expectCtr;
	}

	public void setExpectCtr(Float expectCtr) {
		this.expectCtr = expectCtr;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getDelFlg() {
		return delFlg;
	}

	public void setDelFlg(Boolean delFlg) {
		this.delFlg = delFlg;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public Integer getRelatedCount() {
		return relatedCount;
	}

	public void setRelatedCount(Integer relatedCount) {
		this.relatedCount = relatedCount;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public List<DemandAdspaceDto> getDemandAdspaces() {
		return demandAdspaces;
	}

	public void setDemandAdspaces(List<DemandAdspaceDto> demandAdspaces) {
		this.demandAdspaces = demandAdspaces;
	}

	public Integer getAdType() {
		return adType;
	}

	public void setAdType(Integer adType) {
		this.adType = adType;
	}

	public String getAdTypeName() {
		return adTypeName;
	}

	public void setAdTypeName(String adTypeName) {
		this.adTypeName = adTypeName;
	}

	public String getWh() {
		return wh;
	}

	public void setWh(String wh) {
		this.wh = wh;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

    public Boolean getSupportHttps() {
        return supportHttps;
    }

    public void setSupportHttps(Boolean supportHttps) {
        this.supportHttps = supportHttps;
    }

	public String getCreativeType() {
		return creativeType;
	}

	public void setCreativeType(String creativeType) {
		this.creativeType = creativeType;
	}

	public int getMaxCreativeSize() {
		return maxCreativeSize;
	}

	public void setMaxCreativeSize(int maxCreativeSize) {
		this.maxCreativeSize = maxCreativeSize;
	}

	public int getTitleLength() {
		return titleLength;
	}

	public void setTitleLength(int titleLength) {
		this.titleLength = titleLength;
	}

	public int getDescLength() {
		return descLength;
	}

	public void setDescLength(int descLength) {
		this.descLength = descLength;
	}
}
