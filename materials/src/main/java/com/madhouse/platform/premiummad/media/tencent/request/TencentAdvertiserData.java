package com.madhouse.platform.premiummad.media.tencent.request;

import java.util.List;

/**
 * PDB/PD：广告主由腾讯内部系统下单时指定，无需新建与修改。因此DSP只需关注查询接口，无需关注其他接口。
 * RTB：DSP上传素材，必须先提交广告主到AdX
 */
public class TencentAdvertiserData {
	/**
	 * 广告主ID
	 */
	private Integer id;

	/**
	 * 广告主名称，需要唯一
	 */
	private String name;

	/**
	 * 广告主主页网址
	 */
	private String homeUrl;

	/**
	 * 广告主QQ号
	 */
	private Integer qq;

	/**
	 * 广告主行业编码
	 */
	private String vocation;

	/**
	 * 基本资质内容
	 */
	private List<TencentQualidata> qualidata;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHomeUrl() {
		return homeUrl;
	}

	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}

	public Integer getQq() {
		return qq;
	}

	public void setQq(Integer qq) {
		this.qq = qq;
	}

	public String getVocation() {
		return vocation;
	}

	public void setVocation(String vocation) {
		this.vocation = vocation;
	}

	public List<TencentQualidata> getQualidata() {
		return qualidata;
	}

	public void setQualidata(List<TencentQualidata> qualidata) {
		this.qualidata = qualidata;
	}
}
