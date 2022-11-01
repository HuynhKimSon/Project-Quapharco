package vn.com.irtech.core.common.utils;

import java.util.Collection;
import java.util.Map;

import vn.com.irtech.core.common.text.StrFormatter;

/**
 * String tools
 * 
 * @author admin
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils
{
    /** Empty string */
    private static final String NULLSTR = "";

    /** Underscore */
    private static final char SEPARATOR = '_';

    /**
     * Get parameter is not null
     * 
     * @param value defaultValue the value to be judged
     * @return value return value
     */
    public static <T> T nvl(T value, T defaultValue)
    {
        return value != null ? value : defaultValue;
    }

    /**
     * * Determine whether a Collection is empty, including List, Set, Queue
     * 
     * @param coll Collection to be judged
     * @return true: empty false: not empty
     */
    public static boolean isEmpty(Collection<?> coll)
    {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * * Determine whether a Collection is not empty, including List, Set, Queue
     * 
     * @param coll Collection to be judged
     * @return true: not empty false: empty
     */
    public static boolean isNotEmpty(Collection<?> coll)
    {
        return !isEmpty(coll);
    }

    /**
     * * 判断一个对象数组是否为空
     * 
     * @param objects 要判断的对象数组
     ** @return true：为空 false：非空
     */
    public static boolean isEmpty(Object[] objects)
    {
        return isNull(objects) || (objects.length == 0);
    }

    /**
     * * 判断一个对象数组是否非空
     * 
     * @param objects 要判断的对象数组
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Object[] objects)
    {
        return !isEmpty(objects);
    }

    /**
     * * 判断一个Map是否为空
     * 
     * @param map 要判断的Map
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Map<?, ?> map)
    {
        return isNull(map) || map.isEmpty();
    }

    /**
     * * 判断一个Map是否为空
     * 
     * @param map 要判断的Map
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Map<?, ?> map)
    {
        return !isEmpty(map);
    }

    /**
     * * 判断一 ký tự串是否为空串
     * 
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str)
    {
        return isNull(str) || NULLSTR.equals(str.trim());
    }

    /**
     * * 判断一 ký tự串是否为非空串
     * 
     * @param str String
     * @return true：非空串 false：空串
     */
    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    /**
     * * Determine whether an object is empty
     * 
     * @param object Object
     * @return true: empty false: not empty
     */
    public static boolean isNull(Object object)
    {
        return object == null;
    }

    /**
     * * Determine whether an object is not empty
     * 
     * @param object Object
     * @return true: not empty false: empty
     */
    public static boolean isNotNull(Object object)
    {
        return !isNull(object);
    }

    /**
     * * Determine whether an object is an array type (array of basic Java types)
     * 
     * @param object Object
     * @return true: is an array false: not an array
     */
    public static boolean isArray(Object object)
    {
        return isNotNull(object) && object.getClass().isArray();
    }

    /**
     * Trim
     */
    public static String trim(String str)
    {
        return (str == null ? "" : str.trim());
    }

    /**
     * Intercept string
     * 
     * @param str String
     * @param start start
     * @return result
     */
    public static String substring(final String str, int start)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = str.length() + start;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (start > str.length())
        {
            return NULLSTR;
        }

        return str.substring(start);
    }

    /**
     * Intercept string
     * 
     * @param str String
     * @param start Start
     * @param end End
     * @return result
     */
    public static String substring(final String str, int start, int end)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (end < 0)
        {
            end = str.length() + end;
        }
        if (start < 0)
        {
            start = str.length() + start;
        }

        if (end > str.length())
        {
            end = str.length();
        }

        if (start > end)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (end < 0)
        {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * Formatted text, {} means placeholder<br>
     * This method simply replaces the placeholder {} with parameters in order<br>
     * If you want to output {}, use \\ to escape {, if you want to output the \ before {}, use double escape \\\\<br>
     * example:<br>
     * Usually used:format("this is {} for {}", "a", "b") -> this is a for b<br>
     * Escaping{}:format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
     * Escape \:format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     * 
     * @param template Text template, the replaced part is represented by {}
     * @param params Parameter value
     * @return Formatted text
     */
    public static String format(String template, Object... params)
    {
        if (isEmpty(params) || isEmpty(template))
        {
            return template;
        }
        return StrFormatter.format(template, params);
    }

    /**
     * Underscore to hump naming
     */
    public static String toUnderScoreCase(String str)
    {
        if (str == null)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // Whether the preceding character is capitalized
        boolean preCharIsUpperCase = true;
        // Whether the current character is uppercase
        boolean curreCharIsUpperCase = true;
        // Whether the next character is uppercase
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (i > 0)
            {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            }
            else
            {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1))
            {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase)
            {
                sb.append(SEPARATOR);
            }
            else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase)
            {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * Does it contain a string
     * 
     * @param str Verification string
     * @param strs String group
     * @return Contains returns true
     */
    public static boolean inStringIgnoreCase(String str, String... strs)
    {
        if (str != null && strs != null)
        {
            for (String s : strs)
            {
                if (str.equalsIgnoreCase(trim(s)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Convert a string named in underscore uppercase to camel case. If the string named in underscore capitalization before conversion is empty, an empty string is returned. For example: HELLO_WORLD->HelloWorld
     * 
     * @param name String named in underscore capitalization before conversion
     * @return Converted camel case named string
     */
    public static String convertToCamelCase(String name)
    {
        StringBuilder result = new StringBuilder();
        // Quick check
        if (name == null || name.isEmpty())
        {
            // No need to switch
            return "";
        }
        else if (!name.contains("_"))
        {
            // No underscore, capitalize only the first letter
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        // Split the original string with an underscore
        String[] camels = name.split("_");
        for (String camel : camels)
        {
            // Skip the underline or double underline at the beginning and end of the original string
            if (camel.isEmpty())
            {
                continue;
            }
            // Capitalize the first letter
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     * CamelCase nomenclature For example: user_name->userName
     */
    public static String toCamelCase(String s)
    {
        if (s == null)
        {
            return null;
        }
        if (s.indexOf(SEPARATOR) == -1)
        {
            return s;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);

            if (c == SEPARATOR)
            {
                upperCase = true;
            }
            else if (upperCase)
            {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            }
            else
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj)
    {
        return (T) obj;
    }
}