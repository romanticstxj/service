package com.madhouse.platform.premiummad.dto;

import java.io.Serializable;
import java.util.List;

import com.madhouse.platform.premiummad.annotation.NotNull;

public class AdvertiserMediaDto implements Serializable {
	
	private static final long serialVersionUID = 6193135169642528287L;
	
	/**
	 * DSP定义 的广告主 
	 */
	@NotNull
	private String id;
	
	/**
	 * DSP定义的广告主名称
	 */
	@NotNull
	private String name;
	
	/**
	 * 广告主所属行业
	 */
	@NotNull
	private Integer industry;
	
	/**
	 *广告主 web 主页 
	 */
	@NotNull
	private String webSite;
	
	/**
	 * PremiumMAD 平台定义的媒体 ID，可同 时指定 多个 媒体 ；
	 */
	@NotNull
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
	private String lience;

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

	public String getLience() {
		return lience;
	}

	public void setLience(String lience) {
		this.lience = lience;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
 