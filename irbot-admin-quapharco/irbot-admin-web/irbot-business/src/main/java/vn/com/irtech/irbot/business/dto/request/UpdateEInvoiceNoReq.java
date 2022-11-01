package vn.com.irtech.irbot.business.dto.request;

import java.io.Serializable;

public class UpdateEInvoiceNoReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fastId;
	
	private String eInvoiceNo;

	public String getFastId() {
		return fastId;
	}

	public void setFastId(String fastId) {
		this.fastId = fastId;
	}

	public String geteInvoiceNo() {
		return eInvoiceNo;
	}

	public void seteInvoiceNo(String eInvoiceNo) {
		this.eInvoiceNo = eInvoiceNo;
	}
	
	
}
