package vn.com.irtech.irbot.business.dto.response;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class HdbhNbDetailRes implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fastId;

	private String fastDetailId;

	private String productId;

	private String productName;

	private String productUnit;

	private String productQuantity;

	private String productPrice;

	private String productSubtotalAmount;

	private String productTaxAmount;

	private String productTotalAmount;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date productExpiredDate;

	private String stockOut;
	
	private String stockOutName;

	private String lotNo;

	public String getFastId() {
		return fastId;
	}

	public void setFastId(String fastId) {
		this.fastId = fastId;
	}

	public String getFastDetailId() {
		return fastDetailId;
	}

	public void setFastDetailId(String fastDetailId) {
		this.fastDetailId = fastDetailId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductUnit() {
		return productUnit;
	}

	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}

	public String getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(String productQuantity) {
		this.productQuantity = productQuantity;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductSubtotalAmount() {
		return productSubtotalAmount;
	}

	public void setProductSubtotalAmount(String productSubtotalAmount) {
		this.productSubtotalAmount = productSubtotalAmount;
	}

	public String getProductTaxAmount() {
		return productTaxAmount;
	}

	public void setProductTaxAmount(String productTaxAmount) {
		this.productTaxAmount = productTaxAmount;
	}

	public String getProductTotalAmount() {
		return productTotalAmount;
	}

	public void setProductTotalAmount(String productTotalAmount) {
		this.productTotalAmount = productTotalAmount;
	}

	public Date getProductExpiredDate() {
		return productExpiredDate;
	}

	public void setProductExpiredDate(Date productExpiredDate) {
		this.productExpiredDate = productExpiredDate;
	}

	public String getStockOut() {
		return stockOut;
	}

	public void setStockOut(String stockOut) {
		this.stockOut = stockOut;
	}

	public String getStockOutName() {
		return stockOutName;
	}

	public void setStockOutName(String stockOutName) {
		this.stockOutName = stockOutName;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	
}
