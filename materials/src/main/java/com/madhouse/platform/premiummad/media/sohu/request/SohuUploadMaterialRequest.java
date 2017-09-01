package com.madhouse.platform.premiummad.media.sohu.request;

import java.util.List;

public class SohuUploadMaterialRequest {
	private String customer_key;// 客户唯一key
	private String material_name;// 素材名称
	private String file_source;// 素材源地址，不可重复---
	private List<String> imp;// pv监测地址（需对json数组encode），使用json格式添加多个
	private List<String> click_monitor;// 点击监测地址。用户点击广告后，前端会在浏览器打开gotourl的同时，触发click_monitor监测。
	private String gotourl;// 落地页地址---
	private String advertising_type;// 素材所属的广告投放类型。101000：品牌；102101：效果_电商；102102：效果_游戏；102100：效果_其它。
	private String submit_to;// 指定素材提交到哪个媒体审核。1：搜狐门户；2：搜狐视频。如果要同时提交到多个媒体审核，可以用竖线分隔，例如：1|2
	private int delivery_type;// 指定素材将用于何种投放方式。1：RTB；2：PDB；3：PMP；4：Preferred
								// Deal。
	/* 以上是必填项 */
	private String campaign_id;// 执行单ID，指定素材将用于哪一个订单投放。
								// 对于PDB和Preferred Deal的订单投放，Sohu Ad
								// Exchange会提前将对应的执行单ID提供给DSP，以便DSP上传素材时引用。
	private int expire;// 素材有效期（以s计，上限6个月，不传递默认30天）
	private String imp_sendtag;
	private String clk_sendtag;
	private String template;
	private String main_attr;
	private List<SohuSlave> slave;
	private String ad_source;
	private int material_type;

	// private int convert_to_video;

	public String getCustomer_key() {
		return customer_key;
	}

	public int getMaterial_type() {
		return material_type;
	}

	public void setMaterial_type(int material_type) {
		this.material_type = material_type;
	}

	public String getAd_source() {
		return ad_source;
	}

	public void setAd_source(String ad_source) {
		this.ad_source = ad_source;
	}

	public void setCustomer_key(String customer_key) {
		this.customer_key = customer_key;
	}

	public String getMaterial_name() {
		return material_name;
	}

	public void setMaterial_name(String material_name) {
		this.material_name = material_name;
	}

	public String getFile_source() {
		return file_source;
	}

	public void setFile_source(String file_source) {
		this.file_source = file_source;
	}

	public List<String> getImp() {
		return imp;
	}

	public void setImp(List<String> imp) {
		this.imp = imp;
	}

	public List<String> getClick_monitor() {
		return click_monitor;
	}

	public void setClick_monitor(List<String> click_monitor) {
		this.click_monitor = click_monitor;
	}

	public String getGotourl() {
		return gotourl;
	}

	public void setGotourl(String gotourl) {
		this.gotourl = gotourl;
	}

	public String getAdvertising_type() {
		return advertising_type;
	}

	public void setAdvertising_type(String advertising_type) {
		this.advertising_type = advertising_type;
	}

	public String getSubmit_to() {
		return submit_to;
	}

	public void setSubmit_to(String submit_to) {
		this.submit_to = submit_to;
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	public String getImp_sendtag() {
		return imp_sendtag;
	}

	public void setImp_sendtag(String imp_sendtag) {
		this.imp_sendtag = imp_sendtag;
	}

	public String getClk_sendtag() {
		return clk_sendtag;
	}

	public void setClk_sendtag(String clk_sendtag) {
		this.clk_sendtag = clk_sendtag;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getMain_attr() {
		return main_attr;
	}

	public void setMain_attr(String main_attr) {
		this.main_attr = main_attr;
	}

	public List<SohuSlave> getSlave() {
		return slave;
	}

	public void setSlave(List<SohuSlave> slave) {
		this.slave = slave;
	}

	public int getDelivery_type() {
		return delivery_type;
	}

	public void setDelivery_type(int delivery_type) {
		this.delivery_type = delivery_type;
	}

	public String getCampaign_id() {
		return campaign_id;
	}

	public void setCampaign_id(String campaign_id) {
		this.campaign_id = campaign_id;
	}

}
