package vn.com.irtech.core.common.domain;

import java.io.Serializable;
import java.util.List;

/**
 * CxSelect tree structure entity class
 * 
 * @author irtech
 */
public class CxSelect implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * Data value field name
     */
    private String v;

    /**
     * Data header field name
     */
    private String n;

    /**
     * Subset data field name
     */
    private List<CxSelect> s;

    public CxSelect()
    {
    }

    public CxSelect(String v, String n)
    {
        this.v = v;
        this.n = n;
    }

    public List<CxSelect> getS()
    {
        return s;
    }

    public void setN(String n)
    {
        this.n = n;
    }

    public String getN()
    {
        return n;
    }

    public void setS(List<CxSelect> s)
    {
        this.s = s;
    }

    public String getV()
    {
        return v;
    }

    public void setV(String v)
    {
        this.v = v;
    }
}
