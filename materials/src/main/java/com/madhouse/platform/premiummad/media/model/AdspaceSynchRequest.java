package com.madhouse.platform.premiummad.media.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 广告位信息同步请求参数
 */
public class AdspaceSynchRequest extends TencentRequest {

	// 可以为空 该参数 2012-05-18 号开始支持
	private Date date;

	public String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
