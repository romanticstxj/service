package com.madhouse.platform.premiummad.model;

import java.util.List;

public class AdspaceModel {
	private Integer id;
	private Integer mediaId;
	private Integer w;
	private Integer h;
	private Integer adType;
	private Integer layout;
	private Integer blockId;

	private Image banner;
	private Video video;
	private Native natives;

	private boolean enableHttps;
	private String adspaceKey;
	private Integer status;

	public static class Image {
		private Integer w;
		private Integer h;
		private List<String> mimes;

		public Integer getW() {
			return w;
		}

		public void setW(Integer w) {
			this.w = w;
		}

		public Integer getH() {
			return h;
		}

		public void setH(Integer h) {
			this.h = h;
		}

		public List<String> getMimes() {
			return mimes;
		}

		public void setMimes(List<String> mimes) {
			this.mimes = mimes;
		}
	}

	public static class Video {
		private Integer w;
		private Integer h;
		private Integer minDuraion;
		private Integer maxDuration;
		private Integer linearity;
		private Integer startDelay;
		private List<String> mimes;

		public Integer getW() {
			return w;
		}

		public void setW(Integer w) {
			this.w = w;
		}

		public Integer getH() {
			return h;
		}

		public void setH(Integer h) {
			this.h = h;
		}

		public Integer getMinDuraion() {
			return minDuraion;
		}

		public void setMinDuraion(Integer minDuraion) {
			this.minDuraion = minDuraion;
		}

		public Integer getMaxDuration() {
			return maxDuration;
		}

		public void setMaxDuration(Integer maxDuration) {
			this.maxDuration = maxDuration;
		}

		public Integer getLinearity() {
			return linearity;
		}

		public void setLinearity(Integer linearity) {
			this.linearity = linearity;
		}

		public Integer getStartDelay() {
			return startDelay;
		}

		public void setStartDelay(Integer startDelay) {
			this.startDelay = startDelay;
		}

		public List<String> getMimes() {
			return mimes;
		}

		public void setMimes(List<String> mimes) {
			this.mimes = mimes;
		}
	}

	public static class Native {
		private Image icon;
		private Image cover;
		private Image image;
		private Video video;
		private Integer title;
		private Integer desc;
		private Integer content;

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

		public Integer getTitle() {
			return title;
		}

		public void setTitle(Integer title) {
			this.title = title;
		}

		public Integer getDesc() {
			return desc;
		}

		public void setDesc(Integer desc) {
			this.desc = desc;
		}

		public Integer getContent() {
			return content;
		}

		public void setContent(Integer content) {
			this.content = content;
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMediaId() {
		return mediaId;
	}

	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
	}

	public Integer getW() {
		return w;
	}

	public void setW(Integer w) {
		this.w = w;
	}

	public Integer getH() {
		return h;
	}

	public void setH(Integer h) {
		this.h = h;
	}

	public Integer getAdType() {
		return adType;
	}

	public void setAdType(Integer adType) {
		this.adType = adType;
	}

	public Integer getLayout() {
		return layout;
	}

	public void setLayout(Integer layout) {
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

	public Integer getBlockId() {
		return blockId;
	}

	public void setBlockId(Integer blockId) {
		this.blockId = blockId;
	}

	public String getAdspaceKey() {
		return adspaceKey;
	}

	public void setAdspaceKey(String adspaceKey) {
		this.adspaceKey = adspaceKey;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public boolean isEnableHttps() {
		return enableHttps;
	}

	public void setEnableHttps(boolean enableHttps) {
		this.enableHttps = enableHttps;
	}
}
