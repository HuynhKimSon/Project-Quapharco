package vn.com.irtech.irbot.business.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import vn.com.irtech.core.common.domain.BaseEntity;

public class HdbhCn extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** $column.columnComment */
	private Long id;

	/** $column.columnComment */
	private Integer mode;

	/** $column.columnComment */
	private String fastId;

	/** $column.columnComment */
	private String fastInvoiceNo;

	private String eInvoiceNo;

	/** $column.columnComment */
	private String unitCode;

	/** $column.columnComment */
	private String customerId;

	/** $column.columnComment */
	private String buyerName;

	/** $column.columnComment */
	private String customerName;

	/** $column.columnComment */
	private String customerTaxCode;

	/** $column.columnComment */
	private String address;

	/** $column.columnComment */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date invoiceDate;
	
	/** $column.columnComment */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date invoiceModifyDate;

	/** $column.columnComment */
	private String description;

	/** $column.columnComment */
	private String totalQuantity;

	/** $column.columnComment */
	private String totalTaxAmount;

	/** $column.columnComment */
	private String subtotalAmount;

	/** $column.columnComment */
	private String totalAmount;

	/** $column.columnComment */
	private String paymentMethod;

	private Long processId;

	/** $column.columnComment */
	private Integer status;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public Integer getMode() {
		return mode;
	}

	public void setFastId(String fastId) {
		this.fastId = fastId;
	}

	public String getFastId() {
		return fastId;
	}

	public void setFastInvoiceNo(String fastInvoiceNo) {
		this.fastInvoiceNo = fastInvoiceNo;
	}

	public String getFastInvoiceNo() {
		return fastInvoiceNo;
	}

	public String geteInvoiceNo() {
		return eInvoiceNo;
	}

	public void seteInvoiceNo(String eInvoiceNo) {
		this.eInvoiceNo = eInvoiceNo;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerTaxCode(String customerTaxCode) {
		this.customerTaxCode = customerTaxCode;
	}

	public String getCustomerTaxCode() {
		return customerTaxCode;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public Date getInvoiceModifyDate() {
		return invoiceModifyDate;
	}

	public void setInvoiceModifyDate(Date invoiceModifyDate) {
		this.invoiceModifyDate = invoiceModifyDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
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

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

}
