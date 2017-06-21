package com.madhouse.platform.smartexchange.dto;

import java.io.Serializable;

public class ResponseHeaderDto implements Serializable {
	private static final long serialVersionUID = 1322198527036344712L;
	
	private Integer responseCode; // 相应类型，正常，验证错误，系统错误
	private Integer errorCode; // 具体的错误代码
	private String errorMsg; // 错误信息
	private Integer resultsSize; // 结果中对象的大小

	private Integer pageSize; // 每页显示的条数
	private Integer currentPageNum; // 当前页
	private Integer totalPageNum; // 总页数

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getCurrentPageNum() {
		return currentPageNum;
	}

	public void setCurrentPageNum(Integer currentPageNum) {
		this.currentPageNum = currentPageNum;
	}

	public Integer getTotalPageNum() {
		return totalPageNum;
	}

	public void setTotalPageNum(Integer totalPageNum) {
		this.totalPageNum = totalPageNum;
	}

	public Integer getResultsSize() {
		return resultsSize;
	}

	public void setResultsSize(Integer resultsSize) {
		this.resultsSize = resultsSize;
	}

}
