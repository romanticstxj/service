package com.madhouse.platform.premiummad.dto;

public class PolicyDspDto {
	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_policy_dsp.id
     *
     * @mbggenerated Wed Jul 19 17:15:49 CST 2017
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_policy_dsp.policy_id
     *
     * @mbggenerated Wed Jul 19 17:15:49 CST 2017
     */
    private Integer policyId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_policy_dsp.dsp_id
     *
     * @mbggenerated Wed Jul 19 17:15:49 CST 2017
     */
    private Integer dspId;
    
    private String dspName; //返回给前端用于显示

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_policy_dsp.weight
     *
     * @mbggenerated Wed Jul 19 17:15:49 CST 2017
     */
    private Integer weight;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_policy_dsp.status
     *
     * @mbggenerated Wed Jul 19 17:15:49 CST 2017
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

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
    
}
