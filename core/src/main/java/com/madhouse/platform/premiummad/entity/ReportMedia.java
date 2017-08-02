package com.madhouse.platform.premiummad.entity;

import java.util.Date;

public class ReportMedia extends BaseEntity{
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_report_media.id
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_report_media.media_id
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    private Integer mediaId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_report_media.adspace_id
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    private Integer adspaceId;
    
    private Integer carrier; //运营商code
    
    private Integer conn; //联网方式code
    
    private Integer device; //设备类型code
    
    private String location; //地域code
    
    private String cityName;
    
    private String provinceName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_report_media.date
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    private Date date;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_report_media.hour
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    private Byte hour;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_report_media.reqs
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    private Long reqs;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_report_media.bids
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    private Long bids;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_report_media.errs
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    private Long errs;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_report_media.imps
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    private Long imps;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_report_media.clks
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    private Long clks;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_report_media.vimps
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    private Long vimps;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_report_media.vclks
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    private Long vclks;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mad_report_media.income
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    private Long income;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_report_media.id
     *
     * @return the value of mad_report_media.id
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_report_media.id
     *
     * @param id the value for mad_report_media.id
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_report_media.media_id
     *
     * @return the value of mad_report_media.media_id
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public Integer getMediaId() {
        return mediaId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_report_media.media_id
     *
     * @param mediaId the value for mad_report_media.media_id
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_report_media.adspace_id
     *
     * @return the value of mad_report_media.adspace_id
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public Integer getAdspaceId() {
        return adspaceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_report_media.adspace_id
     *
     * @param adspaceId the value for mad_report_media.adspace_id
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public void setAdspaceId(Integer adspaceId) {
        this.adspaceId = adspaceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_report_media.date
     *
     * @return the value of mad_report_media.date
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public Date getDate() {
        return date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_report_media.date
     *
     * @param date the value for mad_report_media.date
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_report_media.hour
     *
     * @return the value of mad_report_media.hour
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public Byte getHour() {
        return hour;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_report_media.hour
     *
     * @param hour the value for mad_report_media.hour
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public void setHour(Byte hour) {
        this.hour = hour;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_report_media.reqs
     *
     * @return the value of mad_report_media.reqs
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public Long getReqs() {
        return reqs;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_report_media.reqs
     *
     * @param reqs the value for mad_report_media.reqs
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public void setReqs(Long reqs) {
        this.reqs = reqs;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_report_media.bids
     *
     * @return the value of mad_report_media.bids
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public Long getBids() {
        return bids;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_report_media.bids
     *
     * @param bids the value for mad_report_media.bids
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public void setBids(Long bids) {
        this.bids = bids;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_report_media.errs
     *
     * @return the value of mad_report_media.errs
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public Long getErrs() {
        return errs;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_report_media.errs
     *
     * @param errs the value for mad_report_media.errs
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public void setErrs(Long errs) {
        this.errs = errs;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_report_media.imps
     *
     * @return the value of mad_report_media.imps
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public Long getImps() {
        return imps;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_report_media.imps
     *
     * @param imps the value for mad_report_media.imps
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public void setImps(Long imps) {
        this.imps = imps;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_report_media.clks
     *
     * @return the value of mad_report_media.clks
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public Long getClks() {
        return clks;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_report_media.clks
     *
     * @param clks the value for mad_report_media.clks
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public void setClks(Long clks) {
        this.clks = clks;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_report_media.vimps
     *
     * @return the value of mad_report_media.vimps
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public Long getVimps() {
        return vimps;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_report_media.vimps
     *
     * @param vimps the value for mad_report_media.vimps
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public void setVimps(Long vimps) {
        this.vimps = vimps;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_report_media.vclks
     *
     * @return the value of mad_report_media.vclks
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public Long getVclks() {
        return vclks;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_report_media.vclks
     *
     * @param vclks the value for mad_report_media.vclks
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public void setVclks(Long vclks) {
        this.vclks = vclks;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mad_report_media.income
     *
     * @return the value of mad_report_media.income
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public Long getIncome() {
        return income;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mad_report_media.income
     *
     * @param income the value for mad_report_media.income
     *
     * @mbggenerated Tue Aug 01 18:00:00 CST 2017
     */
    public void setIncome(Long income) {
        this.income = income;
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

}