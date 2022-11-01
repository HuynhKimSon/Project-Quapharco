package vn.com.irtech.irbot.business.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import vn.com.irtech.core.common.domain.BaseEntity;

public class HdbhNbDetail extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** $column.columnComment */
	private Long id;

	/** $column.columnComment */
	private Long hdbhNbId;

	/** $column.columnComment */
	private String fastId;

	/** $column.columnComment */
	private String fastDetailId;

	/** $column.columnComment */
	private String productId;

	/** $column.columnComment */
	private String productUnit;

	/** $column.columnComment */
	private String productName;

	/** $column.columnComment */
	private String productQuantity;

	/** $column.columnComment */
	private String productPrice;

	/** $column.columnComment */
	private String productSubtotalAmount;

	/** $column.columnComment */
	private String productTaxAmount;

	/** $column.columnComment */
	private String productTotalAmount;

	/** $column.columnComment */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date productExpiredDate;

	/** $column.columnComment */
	private String stockOut;
	
	/** $column.columnComment */
	private String stockOutName;

	/** $column.columnComment */
	private String lotNo;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setHdbhNbId(Long hdbhNbId) {
		this.hdbhNbId = hdbhNbId;
	}

	public Long getHdbhNbId() {
		return hdbhNbId;
	}

	public void setFastId(String fastId) {
		this.fastId = fastId;
	}

	public String getFastId() {
		return fastId;
	}

	public void setFastDetailId(String fastDetailId) {
		this.fastDetailId = fastDetailId;
	}

	public String getFastDetailId() {
		return fastDetailId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}

	public String getProductUnit() {
		return productUnit;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductQuantity(String productQuantity) {
		this.productQuantity = productQuantity;
	}

	public String getProductQuantity() {
		return productQuantity;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductSubtotalAmount(String productSubtotalAmount) {
		this.productSubtotalAmount = productSubtotalAmount;
	}

	public String getProductSubtotalAmount() {
		return productSubtotalAmount;
	}

	public void setProductTaxAmount(String productTaxAmount) {
		this.productTaxAmount = productTaxAmount;
	}

	public String getProductTaxAmount() {
		return productTaxAmount;
	}

	public void setProductTotalAmount(String productTotalAmount) {
		this.productTotalAmount = productTotalAmount;
	}

	public String getProductTotalAmount() {
		return productTotalAmount;
	}

	public void setProductExpiredDate(Date productExpiredDate) {
		this.productExpiredDate = productExpiredDate;
	}

	public Date getProductExpiredDate() {
		return productExpiredDate;
	}

	public void setStockOut(String stockOut) {
		this.stockOut = stockOut;
	}

	public String getStockOut() {
		return stockOut;
	}

	public String getStockOutName() {
		return stockOutName;
	}

	public void setStockOutName(String stockOutName) {
		this.stockOutName = stockOutName;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getLotNo() {
		return lotNo;
	}

}
