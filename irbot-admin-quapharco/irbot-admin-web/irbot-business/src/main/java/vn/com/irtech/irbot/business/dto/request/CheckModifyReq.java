package vn.com.irtech.irbot.business.dto.request;

import java.io.Serializable;

public class CheckModifyReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fastId;

	private String invoiceDate;

	public String getFastId() {
		return fastId;
	}

	public void setFastId(String fastId) {
		this.fastId = fastId;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
}
