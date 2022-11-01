package vn.com.irtech.api.dto.request;

import vn.com.irtech.api.common.page.PageDomain;

public class GetBillDataReq extends PageDomain {

	private String startDate;

	private String endDate;

	private String nameTableMaster;

	private String nameTableDetail;

	private String fastInvoiceNo;

	private String fastId;

	private Boolean isSelectDetail;

	private String branchCompanyOption;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getNameTableMaster() {
		return nameTableMaster;
	}

	public void setNameTableMaster(String nameTableMaster) {
		this.nameTableMaster = nameTableMaster;
	}

	public String getNameTableDetail() {
		return nameTableDetail;
	}

	public void setNameTableDetail(String nameTableDetail) {
		this.nameTableDetail = nameTableDetail;
	}

	public String getFastId() {
		return fastId;
	}

	public void setFastId(String fastId) {
		this.fastId = fastId;
	}

	public String getFastInvoiceNo() {
		return fastInvoiceNo;
	}

	public void setFastInvoiceNo(String fastInvoiceNo) {
		this.fastInvoiceNo = fastInvoiceNo;
	}

	public Boolean getIsSelectDetail() {
		return isSelectDetail;
	}

	public void setIsSelectDetail(Boolean isSelectDetail) {
		this.isSelectDetail = isSelectDetail;
	}

	public String getBranchCompanyOption() {
		return branchCompanyOption;
	}

	public void setBranchCompanyOption(String branchCompanyOption) {
		this.branchCompanyOption = branchCompanyOption;
	}

}
