package vn.com.irtech.irbot.business.dto.response;

import java.io.Serializable;

public class RobotProcessDataRes implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String invoiceNo;

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

}
