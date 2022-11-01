package vn.com.irtech.api.dto.request;

public class UpdateInvoiceNoReq {

	private String nameTableMaster;

	private String fastInvoiceNo;

	private String fastId;

	public String getNameTableMaster() {
		return nameTableMaster;
	}

	public void setNameTableMaster(String nameTableMaster) {
		this.nameTableMaster = nameTableMaster;
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
}
