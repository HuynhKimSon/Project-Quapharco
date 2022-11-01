package vn.com.irtech.irbot.business.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import vn.com.irtech.core.common.domain.BaseEntity;

public class HdbhNb extends BaseEntity {
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
	private String transporter;

	/** $column.columnComment */
	private String vehicle;

	/** $column.columnComment */
	private String stockIn;
	
	/** $column.columnComment */
	private String stockInName;

	/** $column.columnComment */
	private String orderNo;

	/** $column.columnComment */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date orderDate;

	/** $column.columnComment */
	private Long processId;

	/** $column.columnComment */
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
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

	public String geteInvoiceNo() {
		return eInvoiceNo;
	}

	public void seteInvoiceNo(String eInvoiceNo) {
		this.eInvoiceNo = eInvoiceNo;
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

	public Date getInvoiceModifyDate() {
		return invoiceModifyDate;
	}

	public void setInvoiceModifyDate(Date invoiceModifyDate) {
		this.invoiceModifyDate = invoiceModifyDate;
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

	public String getTransporter() {
		return transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public String getVehicle() {
		return vehicle;
	}

	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}

	public String getStockIn() {
		return stockIn;
	}

	public void setStockIn(String stockIn) {
		this.stockIn = stockIn;
	}

	public String getStockInName() {
		return stockInName;
	}

	public void setStockInName(String stockInName) {
		this.stockInName = stockInName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
