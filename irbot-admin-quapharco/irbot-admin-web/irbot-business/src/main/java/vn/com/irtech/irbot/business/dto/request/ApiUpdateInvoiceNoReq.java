package vn.com.irtech.irbot.business.dto.request;

import java.io.Serializable;

public class ApiUpdateInvoiceNoReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fastId;

	private String fastInvoiceNo;

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

}
