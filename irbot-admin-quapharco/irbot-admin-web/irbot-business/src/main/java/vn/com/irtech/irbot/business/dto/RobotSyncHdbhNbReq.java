package vn.com.irtech.irbot.business.dto;

import java.io.Serializable;
import java.util.List;

import vn.com.irtech.irbot.business.dto.response.HdbhNbDetailRes;

public class RobotSyncHdbhNbReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private ProcessFastConfig config;

	private Long processId;

	private Integer syncId;

	private Integer serviceType;

	private String serviceName;

	private String fastId;

	private String fastInvoiceNo;

	private String unitCode;

	private String customerId;

	private String customerName;

	private String customerTaxCode;

	private String address;

	private String invoiceDate;
	
	private String invoiceModifyDate;

	private String description;

	private String transporter;

	private String vehicle;

	private String stockIn;
	
	private String stockInName;

	private String orderNo;

	private String orderDate;

	private Integer status;

	private String totalTaxAmount;

	private String subtotalAmount;

	private String totalAmount;

	private Integer orderQuantity;

	private List<HdbhNbDetailRes> processOrder;

	public ProcessFastConfig getConfig() {
		return config;
	}

	public void setConfig(ProcessFastConfig config) {
		this.config = config;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	public Integer getSyncId() {
		return syncId;
	}

	public void setSyncId(Integer syncId) {
		this.syncId = syncId;
	}

	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
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

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceModifyDate() {
		return invoiceModifyDate;
	}

	public void setInvoiceModifyDate(String invoiceModifyDate) {
		this.invoiceModifyDate = invoiceModifyDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getOrderQuantity() {
		return orderQuantity;
	}

	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public List<HdbhNbDetailRes> getProcessOrder() {
		return processOrder;
	}

	public void setProcessOrder(List<HdbhNbDetailRes> processOrder) {
		this.processOrder = processOrder;
	}

}
