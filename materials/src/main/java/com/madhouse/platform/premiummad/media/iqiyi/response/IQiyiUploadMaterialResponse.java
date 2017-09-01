package com.madhouse.platform.premiummad.media.iqiyi.response;

public class IQiyiUploadMaterialResponse {
	private String code;// 0:上传成功 1001:认证错误 4001:参数错误 5001：服务端错误 5002：用户上传超过限制 5003：应用请求超过限制
	private String m_id;// 上传素材id，只有code为0即上传成功时才返回该项
	private String desc;// 错误信息，只有code为4001时才返回该项

	public String getCode() {
		return code;
	}

	public String getM_id() {
		return m_id;
	}

	public void setM_id(String m_id) {
		this.m_id = m_id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
