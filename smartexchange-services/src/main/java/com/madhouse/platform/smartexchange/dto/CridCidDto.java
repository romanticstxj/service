package com.madhouse.platform.smartexchange.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * User: Ricky
 * Date: 2016/9/7
 * Time: 15:40
 */
public class CridCidDto implements Serializable {
    private Integer id;

    private String crid;

    private String url;

    private String cid;

    private String projectName;

    private String demandName;

    private Integer demandId;

    private Boolean delFlg;

    private String md5;

    private String latest_md5;

    private Integer checkFlg;

    private Integer cnt;

    private Integer imp;

    private Integer clk;

    private Date createTime;

    private Date startTime;

    private Date endTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCrid() {
        return crid;
    }

    public void setCrid(String crid) {
        this.crid = crid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDemandName() {
        return demandName;
    }

    public void setDemandName(String demandName) {
        this.demandName = demandName;
    }

    public Boolean getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Boolean delFlg) {
        this.delFlg = delFlg;
    }

    public Integer getDemandId() {
        return demandId;
    }

    public void setDemandId(Integer demandId) {
        this.demandId = demandId;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getLatest_md5() {
        return latest_md5;
    }

    public void setLatest_md5(String latest_md5) {
        this.latest_md5 = latest_md5;
    }

    public Integer getCheckFlg() {
        return checkFlg;
    }

    public void setCheckFlg(Integer checkFlg) {
        this.checkFlg = checkFlg;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public Integer getImp() {
        return imp;
    }

    public void setImp(Integer imp) {
        this.imp = imp;
    }

    public Integer getClk() {
        return clk;
    }

    public void setClk(Integer clk) {
        this.clk = clk;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

}
