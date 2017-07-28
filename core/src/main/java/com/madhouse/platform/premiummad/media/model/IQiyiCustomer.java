package com.madhouse.platform.premiummad.media.model;

public class IQiyiCustomer {
	/* 唯一标识 */
	private String dsp_token;
	/* 广告主id */
	private long ad_id;
	/* 广告主名称 */
	private String name;
	/* 行业信息，可以为空 */
	private String industry;
	/* update 或者 create */
	private String op;
	/* 附件名称为空时，可以为空 文件格式支持：ZIP、RAR、JPG、JPEG、PNG、BMP。 */
	private String file_name;

	public String getDsp_token() {
		return dsp_token;
	}

	public void setDsp_token(String dsp_token) {
		this.dsp_token = dsp_token;
	}

	public long getAd_id() {
		return ad_id;
	}

	public void setAd_id(long ad_id) {
		this.ad_id = ad_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
}
