package com.madhouse.platform.premiummad.dto;

import java.io.Serializable;

import com.madhouse.platform.premiummad.annotation.NotNullAndBlank;

public class PolicyAdspaceDto implements Serializable{
	
	private static final long serialVersionUID = -3459984485105827234L;

	private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_policy_adspace.policy_id
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    private Integer policyId;
    
    private PolicyDto policy;

    @NotNullAndBlank
    private Integer adspaceId;
    
    private AdspaceDto adspace; //返回给前端用于显示

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_policy_adspace.media_deal_id
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    private String mediaDealId;

    @NotNullAndBlank
    private Byte bidType; //广告位在某个策略下的售卖方式

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_policy_adspace.bid_floor
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    private Double bidFloor; //广告位在某个策略下的售卖价格（单位元）
    
    private Integer limitReqs; //广告位在某个策略下的请求量限制

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_policy_adspace.status
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    private Byte status;

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

	public Integer getAdspaceId() {
		return adspaceId;
	}

	public void setAdspaceId(Integer adspaceId) {
		this.adspaceId = adspaceId;
	}

	public AdspaceDto getAdspace() {
		return adspace;
	}

	public void setAdspace(AdspaceDto adspace) {
		this.adspace = adspace;
	}

	public String getMediaDealId() {
		return mediaDealId;
	}

	public void setMediaDealId(String mediaDealId) {
		this.mediaDealId = mediaDealId;
	}

	public Byte getBidType() {
		return bidType;
	}

	public void setBidType(Byte bidType) {
		this.bidType = bidType;
	}

	public Double getBidFloor() {
		return bidFloor;
	}

	public void setBidFloor(Double bidFloor) {
		this.bidFloor = bidFloor;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Integer getLimitReqs() {
		return limitReqs;
	}

	public void setLimitReqs(Integer limitReqs) {
		this.limitReqs = limitReqs;
	}

	public PolicyDto getPolicy() {
		return policy;
	}

	public void setPolicy(PolicyDto policy) {
		this.policy = policy;
	}
}
