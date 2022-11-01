package vn.com.irtech.irbot.business.domain;

import vn.com.irtech.core.common.domain.BaseEntity;

public class HddvDetail extends BaseEntity {
	private static final long serialVersionUID = 1L;

	  /** $column.columnComment */
    private Long id;

    /** $column.columnComment */
    private Long hddvId;

    /** $column.columnComment */
    private String fastId;

    /** $column.columnComment */
    private String fastDetailId;

    /** $column.columnComment */
    private String productName;

    /** $column.columnComment */
    private String productTaxId;

    /** $column.columnComment */
    private String productTaxAmount;

    /** $column.columnComment */
    private String productSubtotalAmount;

    /** $column.columnComment */
    private String productTotalAmount;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setHddvId(Long hddvId) 
    {
        this.hddvId = hddvId;
    }

    public Long getHddvId() 
    {
        return hddvId;
    }
    public void setFastId(String fastId) 
    {
        this.fastId = fastId;
    }

    public String getFastId() 
    {
        return fastId;
    }
    public void setFastDetailId(String fastDetailId) 
    {
        this.fastDetailId = fastDetailId;
    }

    public String getFastDetailId() 
    {
        return fastDetailId;
    }
    public void setProductName(String productName) 
    {
        this.productName = productName;
    }

    public String getProductName() 
    {
        return productName;
    }
    public void setProductTaxId(String productTaxId) 
    {
        this.productTaxId = productTaxId;
    }

    public String getProductTaxId() 
    {
        return productTaxId;
    }
    public void setProductTaxAmount(String productTaxAmount) 
    {
        this.productTaxAmount = productTaxAmount;
    }

    public String getProductTaxAmount() 
    {
        return productTaxAmount;
    }
    public void setProductSubtotalAmount(String productSubtotalAmount) 
    {
        this.productSubtotalAmount = productSubtotalAmount;
    }

    public String getProductSubtotalAmount() 
    {
        return productSubtotalAmount;
    }
    public void setProductTotalAmount(String productTotalAmount) 
    {
        this.productTotalAmount = productTotalAmount;
    }

    public String getProductTotalAmount() 
    {
        return productTotalAmount;
    }

}