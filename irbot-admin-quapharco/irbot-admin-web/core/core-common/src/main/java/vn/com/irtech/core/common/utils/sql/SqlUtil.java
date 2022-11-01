package vn.com.irtech.core.common.utils.sql;

import vn.com.irtech.core.common.exception.base.BaseException;
import vn.com.irtech.core.common.utils.StringUtils;

/**
 * SQL operation utils
 * 
 * @author admin
 */
public class SqlUtil
{
    /**
     * Only supports letters, numbers, underscores, spaces, commas, decimal points (supports multiple field sorting)
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    /**
     * Check characters to prevent injection bypass
     */
    public static String escapeOrderBySql(String value)
    {
        if (StringUtils.isNotEmpty(value) && !isValidOrderBySql(value))
        {
            throw new BaseException("The parameter does not meet the specifications and cannot be queried");
        }
        return value;
    }

    /**
     * Verify that the order by syntax complies with the specification
     */
    public static boolean isValidOrderBySql(String value)
    {
        return value.matches(SQL_PATTERN);
    }
}
