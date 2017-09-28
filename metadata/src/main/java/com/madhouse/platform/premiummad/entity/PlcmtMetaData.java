package com.madhouse.platform.premiummad.entity;

import java.util.List;


public class PlcmtMetaData {
    private long id;
    private long mediaId;
    private int adType;
    private List<Size> sizes;
    private int layout;
    private long blockId;

    private Image banner;
    private Video video;
    private Native natives;

    private int bidFloor;
    private int bidType;

    private boolean enableHttps;
    private String adspaceKey;
    private int status;

    public class Size {
        private int w;
        private int h;

        public int getW() {
            return w;
        }

        public void setW(int w) {
            this.w = w;
        }

        public int getH() {
            return h;
        }

        public void setH(int h) {
            this.h = h;
        }
    }

    public class Image {
        private List<Size> sizes;
        private List<String> mimes;

        public List<Size> getSizes() {
            return sizes;
        }

        public void setSizes(List<Size> sizes) {
            this.sizes = sizes;
        }

        public List<String> getMimes() {
            return mimes;
        }

        public void setMimes(List<String> mimes) {
            this.mimes = mimes;
        }
    }

    public class Video {
        private List<Size> sizes;
        private int minDuraion;
        private int maxDuration;
        private int linearity;
        private int startDelay;
        private List<String> mimes;

        public List<Size> getSizes() {
            return sizes;
        }

        public void setSizes(List<Size> sizes) {
            this.sizes = sizes;
        }

        public int getMinDuraion() {
            return minDuraion;
        }

        public void setMinDuraion(int minDuraion) {
            this.minDuraion = minDuraion;
        }

        public int getMaxDuration() {
            return maxDuration;
        }

        public void setMaxDuration(int maxDuration) {
            this.maxDuration = maxDuration;
        }

        public int getLinearity() {
            return linearity;
        }

        public void setLinearity(int linearity) {
            this.linearity = linearity;
        }

        public int getStartDelay() {
            return startDelay;
        }

        public void setStartDelay(int startDelay) {
            this.startDelay = startDelay;
        }

        public List<String> getMimes() {
            return mimes;
        }

        public void setMimes(List<String> mimes) {
            this.mimes = mimes;
        }
    }

    public class Native {
        private Image icon;
        private Image cover;
        private Image image;
        private Video video;
        private int title;
        private int desc;
        private int content;

        public Image getIcon() {
            return icon;
        }

        public void setIcon(Image icon) {
            this.icon = icon;
        }

        public Image getCover() {
            return cover;
        }

        public void setCover(Image cover) {
            this.cover = cover;
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public Video getVideo() {
            return video;
        }

        public void setVideo(Video video) {
            this.video = video;
        }

        public int getTitle() {
            return title;
        }

        public void setTitle(int title) {
            this.title = title;
        }

        public int getDesc() {
            return desc;
        }

        public void setDesc(int desc) {
            this.desc = desc;
        }

        public int getContent() {
            return content;
        }

        public void setContent(int content) {
            this.content = content;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMediaId() {
        return mediaId;
    }

    public void setMediaId(long mediaId) {
        this.mediaId = mediaId;
    }

    public int getAdType() {
        return adType;
    }

    public void setAdType(int adType) {
        this.adType = adType;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public Image getBanner() {
        return banner;
    }

    public void setBanner(Image banner) {
        this.banner = banner;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Native getNatives() {
        return natives;
    }

    public void setNatives(Native natives) {
        this.natives = natives;
    }

    public int getBidFloor() {
        return bidFloor;
    }

    public void setBidFloor(int bidFloor) {
        this.bidFloor = bidFloor;
    }

    public int getBidType() {
        return bidType;
    }

    public void setBidType(int bidType) {
        this.bidType = bidType;
    }

    public long getBlockId() {
        return blockId;
    }

    public void setBlockId(long blockId) {
        this.blockId = blockId;
    }

    public String getAdspaceKey() {
        return adspaceKey;
    }

    public void setAdspaceKey(String adspaceKey) {
        this.adspaceKey = adspaceKey;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isEnableHttps() {
        return enableHttps;
    }

    public void setEnableHttps(boolean enableHttps) {
        this.enableHttps = enableHttps;
    }

    public List<Size> getSizes() {
        return sizes;
    }

    public void setSizes(List<Size> sizes) {
        this.sizes = sizes;
    }
}
