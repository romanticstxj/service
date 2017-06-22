package com.madhouse.platform.smartexchange.dto;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;
import java.util.List;

@JSONType(serialzeFeatures={SerializerFeature.WriteNullListAsEmpty})
public class ResponseDto<T> implements Serializable {
	private static final long serialVersionUID = -5875343291840334945L;
	
	private ResponseHeaderDto responseHeaderDto;
	private List<T> results;

	public ResponseHeaderDto getResponseHeaderDto() {
		return responseHeaderDto;
	}

	public void setResponseHeaderDto(ResponseHeaderDto responseHeaderDto) {
		this.responseHeaderDto = responseHeaderDto;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

}
