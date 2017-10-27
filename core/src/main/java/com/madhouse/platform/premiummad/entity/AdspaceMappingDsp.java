package com.madhouse.platform.premiummad.entity;

import java.util.Date;

public class AdspaceMappingDsp {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_adspace_mapping_dsp.id
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_adspace_mapping_dsp.adspace_id
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    private Integer adspaceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_adspace_mapping_dsp.dsp_id
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    private Integer dspId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_adspace_mapping_dsp.dsp_media_id
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    private String dspMediaId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_adspace_mapping_dsp.dsp_adspace_key
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    private String dspAdspaceKey;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_adspace_mapping_dsp.status
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    private Boolean status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_sys_adspace_mapping_dsp.created_time
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    private Date createdTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_sys_adspace_mapping_dsp.id
     *
     * @return the value of mad_sys_adspace_mapping_dsp.id
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_sys_adspace_mapping_dsp.id
     *
     * @param id the value for mad_sys_adspace_mapping_dsp.id
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_sys_adspace_mapping_dsp.adspace_id
     *
     * @return the value of mad_sys_adspace_mapping_dsp.adspace_id
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    public Integer getAdspaceId() {
        return adspaceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_sys_adspace_mapping_dsp.adspace_id
     *
     * @param adspaceId the value for mad_sys_adspace_mapping_dsp.adspace_id
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    public void setAdspaceId(Integer adspaceId) {
        this.adspaceId = adspaceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_sys_adspace_mapping_dsp.dsp_id
     *
     * @return the value of mad_sys_adspace_mapping_dsp.dsp_id
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    public Integer getDspId() {
        return dspId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_sys_adspace_mapping_dsp.dsp_id
     *
     * @param dspId the value for mad_sys_adspace_mapping_dsp.dsp_id
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    public void setDspId(Integer dspId) {
        this.dspId = dspId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_sys_adspace_mapping_dsp.dsp_media_id
     *
     * @return the value of mad_sys_adspace_mapping_dsp.dsp_media_id
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    public String getDspMediaId() {
        return dspMediaId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_sys_adspace_mapping_dsp.dsp_media_id
     *
     * @param dspMediaId the value for mad_sys_adspace_mapping_dsp.dsp_media_id
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    public void setDspMediaId(String dspMediaId) {
        this.dspMediaId = dspMediaId == null ? null : dspMediaId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_sys_adspace_mapping_dsp.dsp_adspace_key
     *
     * @return the value of mad_sys_adspace_mapping_dsp.dsp_adspace_key
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    public String getDspAdspaceKey() {
        return dspAdspaceKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_sys_adspace_mapping_dsp.dsp_adspace_key
     *
     * @param dspAdspaceKey the value for mad_sys_adspace_mapping_dsp.dsp_adspace_key
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    public void setDspAdspaceKey(String dspAdspaceKey) {
        this.dspAdspaceKey = dspAdspaceKey == null ? null : dspAdspaceKey.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_sys_adspace_mapping_dsp.status
     *
     * @return the value of mad_sys_adspace_mapping_dsp.status
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_sys_adspace_mapping_dsp.status
     *
     * @param status the value for mad_sys_adspace_mapping_dsp.status
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_sys_adspace_mapping_dsp.created_time
     *
     * @return the value of mad_sys_adspace_mapping_dsp.created_time
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_sys_adspace_mapping_dsp.created_time
     *
     * @param createdTime the value for mad_sys_adspace_mapping_dsp.created_time
     *
     * @mbggenerated Fri Oct 20 10:27:59 CST 2017
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}