package com.madhouse.platform.premiummad.entity;

public class AdspaceInfo {
    private long id;
    private int status;
    private int bidType;
    private int bidFloor;
    private String dealId;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getBidType() {
        return bidType;
    }
    public void setBidType(int bidType) {
        this.bidType = bidType;
    }
    public int getBidFloor() {
        return bidFloor;
    }
    public void setBidFloor(int bidFloor) {
        this.bidFloor = bidFloor;
    }
    public String getDealId() {
        return dealId;
    }
    public void setDealId(String dealId) {
        this.dealId = dealId;
    }
    
    
}
