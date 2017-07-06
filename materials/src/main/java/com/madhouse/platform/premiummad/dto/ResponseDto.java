package com.madhouse.platform.premiummad.dto;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;
import java.util.List;

@JSONType(serialzeFeatures={SerializerFeature.WriteNullListAsEmpty})
public class ResponseDto<T> implements Serializable {
	private static final long serialVersionUID = -5875343291840334945L;
	
	private Integer code;
	
	private String message;
	
	private List<T> data;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}


}
