package com.madhouse.platform.premiummad.media.model;

public class SohutvStatusDetailResponse {
	private String customer_key;// 客户唯一key
	private String material_name;// 素材名称
	private String file_source;// 素材源地址
	private String file_size;// 素材大小
	private int width;// 素材宽
	private int height;// 素材高
	private String imp;// pv 监测地址:json
	private String click_monitor;// 点击监测地址
	private String gotourl;// 落地页地址

	private String advertising_type;// 素材所属的广告投放类型
	private int submit_to;// 送审媒体
	private int delivery_type;// 投放方式
	private String campaign_id;// 所属执行单ID

	private String expire;// 素材有效期（以s 计，上限6个月，不传递默认30 天）

	private String template;// 特型广告模板
	private String main_attr;// 主素材属性

	private String slave;// 副素材信息
	private int status;// 审核状态（0 为未审核，1 为审核通过，2 为拒绝）
	private String audit_info;// 审核意见

	public String getCustomer_key() {
		return customer_key;
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

	public String getFile_size() {
		return file_size;
	}

	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getImp() {
		return imp;
	}

	public void setImp(String imp) {
		this.imp = imp;
	}

	public String getClick_monitor() {
		return click_monitor;
	}

	public void setClick_monitor(String click_monitor) {
		this.click_monitor = click_monitor;
	}

	public String getGotourl() {
		return gotourl;
	}

	public void setGotourl(String gotourl) {
		this.gotourl = gotourl;
	}

	public String getExpire() {
		return expire;
	}

	public void setExpire(String expire) {
		this.expire = expire;
	}

	public String getSlave() {
		return slave;
	}

	public void setSlave(String slave) {
		this.slave = slave;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAudit_info() {
		return audit_info;
	}

	public void setAudit_info(String audit_info) {
		this.audit_info = audit_info;
	}

	public String getAdvertising_type() {
		return advertising_type;
	}

	public void setAdvertising_type(String advertising_type) {
		this.advertising_type = advertising_type;
	}

	public int getSubmit_to() {
		return submit_to;
	}

	public void setSubmit_to(int submit_to) {
		this.submit_to = submit_to;
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

}
