package com.madhouse.platform.smartexchange.model;

import com.madhouse.platform.smartexchange.entity.Targeting;

import java.util.List;

/**
 * Created by zhujiajun
 * 15/8/10 16:38
 */
public class DemandAdspaceModel {

    private transient Integer id;
    private Integer demandId;
    private String demandUrl;
    private Integer timeout;
    private Integer mediaId;
    private Long expectImp;
    private Long realyTimeImp;
    private Integer demandAdspaceId;
    private String demandSecretKey;
    private String demandAdspaceKey;
    private String pkgName;
	private String appName;
    private String osType;
    private String demandType;
	private Double price;
	private String priceType;

    private Integer adType;//supplierAdspace的广告位类型
    
    /****
     * 提供给媒体的接口中对应的size相关字段
     ****/
    private int width;
    private int height;

    private Integer weight;
    private String comments;

    private boolean projectFlg;

    private boolean supportHttps;
    private String mimes;
    private Integer maxDuration; //视频的最大长度
	private Integer minDuration; //视频的最小长度
    
    public Integer getMaxDuration() {
		return maxDuration;
	}

	public void setMaxDuration(Integer maxDuration) {
		this.maxDuration = maxDuration;
	}

	public Integer getMinDuration() {
		return minDuration;
	}

	public void setMinDuration(Integer minDuration) {
		this.minDuration = minDuration;
	}

	public String getMimes() {
		return mimes;
	}

	public void setMimes(String mimes) {
		this.mimes = mimes;
	}

	public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    private List<Targeting> targetings;

    public Long getExpectImp() {
        return expectImp;
    }

    public void setExpectImp(Long expectImp) {
        this.expectImp = expectImp;
    }

    public Long getRealyTimeImp() {
        return realyTimeImp;
    }

    public void setRealyTimeImp(Long realyTimeImp) {
        this.realyTimeImp = realyTimeImp;
    }

    public Integer getDemandId() {
        return demandId;
    }

    public void setDemandId(Integer demandId) {
        this.demandId = demandId;
    }

    public String getDemandUrl() {
        return demandUrl;
    }

    public void setDemandUrl(String demandUrl) {
        this.demandUrl = demandUrl;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getDemandAdspaceId() {
        return demandAdspaceId;
    }

    public void setDemandAdspaceId(Integer demandAdspaceId) {
        this.demandAdspaceId = demandAdspaceId;
    }

    public String getDemandSecretKey() {
        return demandSecretKey;
    }

    public void setDemandSecretKey(String demandSecretKey) {
        this.demandSecretKey = demandSecretKey;
    }

    public String getDemandAdspaceKey() {
        return demandAdspaceKey;
    }

    public void setDemandAdspaceKey(String demandAdspaceKey) {
        this.demandAdspaceKey = demandAdspaceKey;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getDemandType() {
        return demandType;
    }

    public void setDemandType(String demandType) {
        this.demandType = demandType;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<Targeting> getTargetings() {
        return targetings;
    }

    public void setTargetings(List<Targeting> targetings) {
        this.targetings = targetings;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

	public Integer getAdType() {
		return adType;
	}

	public void setAdType(Integer adType) {
		this.adType = adType;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

    public boolean isProjectFlg() {
        return projectFlg;
    }

    public void setProjectFlg(boolean projectFlg) {
        this.projectFlg = projectFlg;
    }

    public boolean isSupportHttps() {
        return supportHttps;
    }

    public void setSupportHttps(boolean supportHttps) {
        this.supportHttps = supportHttps;
    }

	public String getPkgName() {
		return pkgName;
	}

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
    
}
