package com.madhouse.platform.premiummad.media.sohu.response;

/**
 * desc:对应的表中其实广告主，并非客户表
 */
public class SohuCustomerListDetail {

	private String customer_key;

	private String customer_name;

	private String customer_website;

	private String company_address;

	private String capital;

	private String reg_address;

	private String contact;

	private String phone_number;

	private String publish_category;

	private String oganization_code;

	private String oganization_license;

	private String business_license;

	private String legalperson_identity;

	private String tax_cert;

	private String taxreg_cert;

	private String ext_license;

	private String deadline;

	private Integer modified;

	/* 审核状态（0 为未审核，1 为审核通过，2 为拒绝） */
	private Integer status;

	private String audit_info;

	private Integer tv_status;

	private String tv_audit_info;

	public String getCustomer_key() {
		return customer_key;
	}

	public void setCustomer_key(String customer_key) {
		this.customer_key = customer_key;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getOganization_code() {
		return oganization_code;
	}

	public void setOganization_code(String oganization_code) {
		this.oganization_code = oganization_code;
	}

	public String getBusiness_license() {
		return business_license;
	}

	public void setBusiness_license(String business_license) {
		this.business_license = business_license;
	}

	public String getLegalperson_identity() {
		return legalperson_identity;
	}

	public void setLegalperson_identity(String legalperson_identity) {
		this.legalperson_identity = legalperson_identity;
	}

	public String getTaxreg_cert() {
		return taxreg_cert;
	}

	public void setTaxreg_cert(String taxreg_cert) {
		this.taxreg_cert = taxreg_cert;
	}

	public Integer getModified() {
		return modified;
	}

	public void setModified(Integer modified) {
		this.modified = modified;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCustomer_website() {
		return customer_website;
	}

	public void setCustomer_website(String customer_website) {
		this.customer_website = customer_website;
	}

	public String getCompany_address() {
		return company_address;
	}

	public void setCompany_address(String company_address) {
		this.company_address = company_address;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public String getReg_address() {
		return reg_address;
	}

	public void setReg_address(String reg_address) {
		this.reg_address = reg_address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getPublish_category() {
		return publish_category;
	}

	public void setPublish_category(String publish_category) {
		this.publish_category = publish_category;
	}

	public String getTax_cert() {
		return tax_cert;
	}

	public void setTax_cert(String tax_cert) {
		this.tax_cert = tax_cert;
	}

	public String getExt_license() {
		return ext_license;
	}

	public void setExt_license(String ext_license) {
		this.ext_license = ext_license;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getAudit_info() {
		return audit_info;
	}

	public void setAudit_info(String audit_info) {
		this.audit_info = audit_info;
	}

	public Integer getTv_status() {
		return tv_status;
	}

	public void setTv_status(Integer tv_status) {
		this.tv_status = tv_status;
	}

	public String getTv_audit_info() {
		return tv_audit_info;
	}

	public void setTv_audit_info(String tv_audit_info) {
		this.tv_audit_info = tv_audit_info;
	}

	public String getOganization_license() {
		return oganization_license;
	}

	public void setOganization_license(String oganization_license) {
		this.oganization_license = oganization_license;
	}
}
