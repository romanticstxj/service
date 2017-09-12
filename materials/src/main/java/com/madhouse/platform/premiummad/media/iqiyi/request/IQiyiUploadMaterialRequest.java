package com.madhouse.platform.premiummad.media.iqiyi.request;

public class IQiyiUploadMaterialRequest {
	// 必填
	private String dsp_token;// 字符串类型，用来唯一标识合作方
	private String click_url;// 字符串类型，点击广告后着陆地址(landing page)
	private String video_id;// 数字或者字符串类型，创意视频或者动画等素材的ID，相同ID的素材在服务端只留存一份。上传创意时若对应素材已有过上传记录，那么只需传该素材ID即可，无需再上传该素材文件

	private String file_name;// 字符串类型，文件名称，必填
	private int platform;// 广告投放平台，必填。1为pc端、2为移动端。
	private int duration;// 创意时长，数字类型。上传视频格式创意时可不填，当创意类型为swf普通贴片、云交互贴片、角标时，该字段必须填写。
	private String dpi;// 素材分辨率，字符串类型。只有上传overlay广告创意且素材格式为swf时，该字段必须填写，且分辨率为728x90、 480x70、468x60、450x50其中之一
	private String position;// 广告展示位置，字符串类型。只有当上传角标或overlay广告创意时，可填写该字段。角标创意备选项为left、right，默认为left；overlay创意备选项为top、bottom，默认为bottom。
	private String end_date;// 创意截止有效时间，字符串类型，格式为yyyyMMdd。默认为3个月，从上传日算最多不超过一年
	private int is_pmp;// 区分素材是属RTB 还是 PMP （PDB+PD）,0 代表 PMP素材， 1代表 RTB素材

	// 非必填
	private int ad_id;// 数字类型，通过广告主上传接口成功上传的广告主ID，可以为空
	private int ad_type;// 上传创意类型，可为空。目前支持四种创意，1为贴片创意、2为暂停创意、3为云交互贴片、4为角标(只支持移动端)、9为overlay。为空时，默认为贴片创意
	private int tag;// 素材标签，数字类型，选填但建议填写，这样方便针对不同类型素材进行相关统计

	public int getIs_pmp() {
		return is_pmp;
	}

	public void setIs_pmp(int is_pmp) {
		this.is_pmp = is_pmp;
	}

	public String getDsp_token() {
		return dsp_token;
	}

	public void setDsp_token(String dsp_token) {
		this.dsp_token = dsp_token;
	}

	public String getClick_url() {
		return click_url;
	}

	public void setClick_url(String click_url) {
		this.click_url = click_url;
	}

	public int getAd_id() {
		return ad_id;
	}

	public void setAd_id(int ad_id) {
		this.ad_id = ad_id;
	}

	public String getVideo_id() {
		return video_id;
	}

	public void setVideo_id(String video_id) {
		this.video_id = video_id;
	}

	public int getAd_type() {
		return ad_type;
	}

	public void setAd_type(int ad_type) {
		this.ad_type = ad_type;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getDpi() {
		return dpi;
	}

	public void setDpi(String dpi) {
		this.dpi = dpi;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

}
