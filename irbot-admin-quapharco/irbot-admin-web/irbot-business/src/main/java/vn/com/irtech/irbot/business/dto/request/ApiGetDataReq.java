package vn.com.irtech.irbot.business.dto.request;

import java.io.Serializable;

public class ApiGetDataReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private String startDate;

	private String endDate;

	private Integer pageNum;

	private Integer pageSize;

	private String fastId;

	private String fastInvoiceNo;

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

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getFastInvoiceNo() {
		return fastInvoiceNo;
	}

	public void setFastInvoiceNo(String fastInvoiceNo) {
		this.fastInvoiceNo = fastInvoiceNo;
	}

	public String getFastId() {
		return fastId;
	}

	public void setFastId(String fastId) {
		this.fastId = fastId;
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
