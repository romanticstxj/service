package com.madhouse.platform.premiummad.media.weibo.request;

import java.util.List;

public class WeiboAdInfo {
	private List<WeiboBanner> banner;
	private List<WeiboFeedVideo> feed_video;
	private List<WeiboFeed> feed;
	private List<WeiboFeedActivity> feed_activity;

	public List<WeiboFeed> getFeed() {
		return feed;
	}

	public void setFeed(List<WeiboFeed> feed) {
		this.feed = feed;
	}

	public List<WeiboFeedActivity> getFeed_activity() {
		return feed_activity;
	}

	public void setFeed_activity(List<WeiboFeedActivity> feed_activity) {
		this.feed_activity = feed_activity;
	}

	public List<WeiboBanner> getBanner() {
		return banner;
	}

	public void setBanner(List<WeiboBanner> banner) {
		this.banner = banner;
	}

	public List<WeiboFeedVideo> getFeed_video() {
		return feed_video;
	}

	public void setFeed_video(List<WeiboFeedVideo> feed_video) {
		this.feed_video = feed_video;
	}
}
