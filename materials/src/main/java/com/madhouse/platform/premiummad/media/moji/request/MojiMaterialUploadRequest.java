package com.madhouse.platform.premiummad.media.moji.request;

public class MojiMaterialUploadRequest {
	private String source;
	private String sign;
	private int time_stamp;
	private String position_ids;
	private int ad_type;
	private int show_type;
	private String redirect_url;
	private String image_url;
	private String desc;
	private String title;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public int getTime_stamp() {
		return time_stamp;
	}

	public void setTime_stamp(int time_stamp) {
		this.time_stamp = time_stamp;
	}

	public String getPosition_ids() {
		return position_ids;
	}

	public void setPosition_ids(String position_ids) {
		this.position_ids = position_ids;
	}

	public int getAd_type() {
		return ad_type;
	}

	public void setAd_type(int ad_type) {
		this.ad_type = ad_type;
	}

	public int getShow_type() {
		return show_type;
	}

	public void setShow_type(int show_type) {
		this.show_type = show_type;
	}

	public String getRedirect_url() {
		return redirect_url;
	}

	public void setRedirect_url(String redirect_url) {
		this.redirect_url = redirect_url;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
