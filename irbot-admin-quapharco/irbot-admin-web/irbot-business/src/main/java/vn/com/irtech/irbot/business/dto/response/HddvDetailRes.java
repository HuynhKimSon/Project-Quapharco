package vn.com.irtech.irbot.business.dto.response;

import java.io.Serializable;

public class HddvDetailRes implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fastId;

	private String fastDetailId;

	private String productName;

	private String productTaxId;

	private String productSubtotalAmount;

	private String productTaxAmount;

	private String productTotalAmount;

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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductTaxId() {
		return productTaxId;
	}

	public void setProductTaxId(String productTaxId) {
		this.productTaxId = productTaxId;
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

}
