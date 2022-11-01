package vn.com.irtech.core.common.exception.user;

import vn.com.irtech.core.common.exception.base.BaseException;

/**
 * 用户信息异常类
 * 
 * @author admin
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
