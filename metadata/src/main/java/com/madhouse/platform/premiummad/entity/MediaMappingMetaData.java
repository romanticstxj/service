package com.madhouse.platform.premiummad.entity;


public class MediaMappingMetaData {
    private long adspaceId;
    private String adspaceKey;
    private String mappingKey;

    public long getAdspaceId() {
        return adspaceId;
    }

    public void setAdspaceId(long adspaceId) {
        this.adspaceId = adspaceId;
    }

    public String getMappingKey() {
        return mappingKey;
    }

    public void setMappingKey(String mappingKey) {
        this.mappingKey = mappingKey;
    }

    public String getAdspaceKey() {
        return adspaceKey;
    }

    public void setAdspaceKey(String adspaceKey) {
        this.adspaceKey = adspaceKey;
    }
}
