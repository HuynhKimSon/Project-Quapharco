package vn.com.irtech.api.dto.response;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class HdxNccMasterRes implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fastId;

	private String fastInvoiceNo;

	private String unitCode;

	private String customerId;

	private String buyerName;

	private String customerName;

	private String customerTaxCode;

	private String address;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date invoiceDate;

	private String description;

	private String totalQuantity;

	private String totalTaxAmount;

	private String subtotalAmount;

	private String totalAmount;

	private String paymentMethod;

	private String transporter;

	private List<HdxNccDetailRes> details;

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

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerTaxCode() {
		return customerTaxCode;
	}

	public void setCustomerTaxCode(String customerTaxCode) {
		this.customerTaxCode = customerTaxCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(String totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public String getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(String totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public String getSubtotalAmount() {
		return subtotalAmount;
	}

	public void setSubtotalAmount(String subtotalAmount) {
		this.subtotalAmount = subtotalAmount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getTransporter() {
		return transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public List<HdxNccDetailRes> getDetails() {
		return details;
	}

	public void setDetails(List<HdxNccDetailRes> details) {
		this.details = details;
	}

}
