package vn.com.irtech.core.common.domain;

import java.io.Serializable;

/**
 * Ztree tree structure entity class
 * 
 * @author admin
 */
public class Ztree implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** Node ID */
    private Long id;

    /** Node parent ID */
    private Long pId;

    /** Node name */
    private String name;

    /** Node title */
    private String title;

    /** Whether to check */
    private boolean checked = false;

    /** Whether to expand */
    private boolean open = false;

    /** Can you check */
    private boolean nocheck = false;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getpId()
    {
        return pId;
    }

    public void setpId(Long pId)
    {
        this.pId = pId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public boolean isChecked()
    {
        return checked;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

    public boolean isOpen()
    {
        return open;
    }

    public void setOpen(boolean open)
    {
        this.open = open;
    }

    public boolean isNocheck()
    {
        return nocheck;
    }

    public void setNocheck(boolean nocheck)
    {
        this.nocheck = nocheck;
    }
}
