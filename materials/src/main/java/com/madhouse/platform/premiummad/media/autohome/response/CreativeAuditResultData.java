package com.madhouse.platform.premiummad.media.autohome.response;

public class CreativeAuditResultData {
	/**
	 * 素材ID
	 */
	private int id;

	/**
	 * 0（未审核）、1（通过）、2（拒绝）
	 */
	private int auditStatus;

	/**
	 * 若未通过，表示审核意见
	 */
	private String auditComment;

	/**
	 * 审核时间，未审核没有此字段
	 */
	private String auditTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(int auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getAuditComment() {
		return auditComment;
	}

	public void setAuditComment(String auditComment) {
		this.auditComment = auditComment;
	}

	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
}
