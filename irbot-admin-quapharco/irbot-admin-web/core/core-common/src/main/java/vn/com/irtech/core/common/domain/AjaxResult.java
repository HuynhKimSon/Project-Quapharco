package vn.com.irtech.core.common.domain;

import java.util.HashMap;

import vn.com.irtech.core.common.utils.StringUtils;

/**
 * Operation message reminder
 * 
 * @author admin
 */
public class AjaxResult extends HashMap<String, Object>
{
    private static final long serialVersionUID = 1L;

    /** status code */
    public static final String CODE_TAG = "code";

    /** Return content */
    public static final String MSG_TAG = "msg";

    /** Data object */
    public static final String DATA_TAG = "data";

    /**
     * State type
     */
    public enum Type
    {
        /** success */
        SUCCESS(0),
        /** warning */
        WARN(301),
        /** error */
        ERROR(500);
        private final int value;

        Type(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }

    /**
     * Initialize a newly created AjaxResult object so that it represents an empty message.
     */
    public AjaxResult()
    {
    }

    /**
     * Initialize a newly created AjaxResult object
     * 
     * @param type State type
     * @param msg Return content
     */
    public AjaxResult(Type type, String msg)
    {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
    }

    /**
     * Initialize a newly created AjaxResult object
     * 
     * @param type State type
     * @param msg Return content
     * @param data Data object
     */
    public AjaxResult(Type type, String msg, Object data)
    {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        if (StringUtils.isNotNull(data))
        {
            super.put(DATA_TAG, data);
        }
    }
    
    /**
     * 方便链式调用
     *
     * @param key 键
     * @param value 值
     * @return 数据对象
     */
    @Override
    public AjaxResult put(String key, Object value)
    {
        super.put(key, value);
        return this;
    }

    /**
     * Return success message
     * 
     * @return Success message
     */
    public static AjaxResult success()
    {
        return AjaxResult.success("Thành công");
    }

    /**
     * Return success data
     * 
     * @return Success message
     */
    public static AjaxResult success(Object data)
    {
        return AjaxResult.success("Thành công", data);
    }

    /**
     * Return success message
     * 
     * @param msg Return content
     * @return Success message
     */
    public static AjaxResult success(String msg)
    {
        return AjaxResult.success(msg, null);
    }

    /**
     * Return success message
     * 
     * @param msg Return content
     * @param data Data object
     * @return Success message
     */
    public static AjaxResult success(String msg, Object data)
    {
        return new AjaxResult(Type.SUCCESS, msg, data);
    }

    /**
     * Return warning message
     * 
     * @param msg Return content
     * @return Warning message
     */
    public static AjaxResult warn(String msg)
    {
        return AjaxResult.warn(msg, null);
    }

    /**
     * Return warning message
     * 
     * @param msg Return content
     * @param data Data object
     * @return Warning message
     */
    public static AjaxResult warn(String msg, Object data)
    {
        return new AjaxResult(Type.WARN, msg, data);
    }

    /**
     * Return error message
     * 
     * @return
     */
    public static AjaxResult error()
    {
        return AjaxResult.error("Thất bại");
    }

    /**
     * Return error message
     * 
     * @param msg Return content
     * @return Warning message
     */
    public static AjaxResult error(String msg)
    {
        return AjaxResult.error(msg, null);
    }

    /**
     * Return error message
     * 
     * @param msg Return content
     * @param data Data object
     * @return Warning message
     */
    public static AjaxResult error(String msg, Object data)
    {
        return new AjaxResult(Type.ERROR, msg, data);
    }
}
