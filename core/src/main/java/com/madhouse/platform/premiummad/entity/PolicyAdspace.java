package com.madhouse.platform.premiummad.entity;

public class PolicyAdspace extends BaseEntity{
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_policy_adspace.id
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_policy_adspace.policy_id
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    private Integer policyId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_policy_adspace.adspace_id
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    private Integer adspaceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_policy_adspace.media_deal_id
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    private String mediaDealId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_policy_adspace.bid_type
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    private Byte bidType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_policy_adspace.bid_floor
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    private Integer bidFloor;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_policy_adspace.status
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    private Byte status;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_sys_policy_adspace.id
     *
     * @return the value of mad_sys_policy_adspace.id
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_sys_policy_adspace.id
     *
     * @param id the value for mad_sys_policy_adspace.id
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_sys_policy_adspace.policy_id
     *
     * @return the value of mad_sys_policy_adspace.policy_id
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    public Integer getPolicyId() {
        return policyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_sys_policy_adspace.policy_id
     *
     * @param policyId the value for mad_sys_policy_adspace.policy_id
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    public void setPolicyId(Integer policyId) {
        this.policyId = policyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_sys_policy_adspace.adspace_id
     *
     * @return the value of mad_sys_policy_adspace.adspace_id
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    public Integer getAdspaceId() {
        return adspaceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_sys_policy_adspace.adspace_id
     *
     * @param adspaceId the value for mad_sys_policy_adspace.adspace_id
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    public void setAdspaceId(Integer adspaceId) {
        this.adspaceId = adspaceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_sys_policy_adspace.media_deal_id
     *
     * @return the value of mad_sys_policy_adspace.media_deal_id
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    public String getMediaDealId() {
        return mediaDealId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_sys_policy_adspace.media_deal_id
     *
     * @param mediaDealId the value for mad_sys_policy_adspace.media_deal_id
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    public void setMediaDealId(String mediaDealId) {
        this.mediaDealId = mediaDealId == null ? null : mediaDealId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_sys_policy_adspace.bid_type
     *
     * @return the value of mad_sys_policy_adspace.bid_type
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    public Byte getBidType() {
        return bidType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_sys_policy_adspace.bid_type
     *
     * @param bidType the value for mad_sys_policy_adspace.bid_type
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    public void setBidType(Byte bidType) {
        this.bidType = bidType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_sys_policy_adspace.bid_floor
     *
     * @return the value of mad_sys_policy_adspace.bid_floor
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    public Integer getBidFloor() {
        return bidFloor;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_sys_policy_adspace.bid_floor
     *
     * @param bidFloor the value for mad_sys_policy_adspace.bid_floor
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    public void setBidFloor(Integer bidFloor) {
        this.bidFloor = bidFloor;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_sys_policy_adspace.status
     *
     * @return the value of mad_sys_policy_adspace.status
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_sys_policy_adspace.status
     *
     * @param status the value for mad_sys_policy_adspace.status
     *
     * @mbggenerated Wed Jul 19 17:12:16 CST 2017
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

}