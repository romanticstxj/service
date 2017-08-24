package com.madhouse.platform.premiummad.model;

import java.io.Serializable;
import java.util.List;

public class AdvertiserModel implements Serializable {

	private static final long serialVersionUID = -5204392238626643882L;
	
	/**
	 * DSP 传过来的ID
	 */
	private String dspId;

	/**
	 * DSP定义 的广告主 
	 */
	private String id;
	
	/**
	 * DSP定义的广告主名称
	 */
	private String name;
	
	/**
	 * 广告主所属行业
	 */
	private Integer industry;
	
	/**
	 *广告主 web 主页 
	 */
	private String webSite;
	
	/**
	 * PremiumMAD 平台定义的媒体 ID，可同 时指定 多个 媒体 ；
	 */
	private List<Integer> mediaId;
	
	/**
	 * 联系人姓名
	 */
	private String contact;
	
	/**
	 * 联系人地址
	 */
	private String address;
	
	/**
	 * 联系人电话
	 */
	private String phone;
	
	/**
	 * 资质文件 （营业执照等）链接
	 */
	private List<String> lience;

	public String getDspId() {
		return dspId;
	}

	public void setDspId(String dspId) {
		this.dspId = dspId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIndustry() {
		return industry;
	}

	public void setIndustry(Integer industry) {
		this.industry = industry;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public List<Integer> getMediaId() {
		return mediaId;
	}

	public void setMediaId(List<Integer> mediaId) {
		this.mediaId = mediaId;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<String> getLience() {
		return lience;
	}

	public void setLience(List<String> lience) {
		this.lience = lience;
	}
}
 