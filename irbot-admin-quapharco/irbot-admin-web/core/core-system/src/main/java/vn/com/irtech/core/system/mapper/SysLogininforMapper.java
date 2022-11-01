package vn.com.irtech.core.system.mapper;

import java.util.List;

import vn.com.irtech.core.system.domain.SysLogininfor;

/**
 * 
 * @author admin
 */
public interface SysLogininforMapper
{
    public void insertLogininfor(SysLogininfor logininfor);

    public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor);

    public int deleteLogininforByIds(String[] ids);

    public int cleanLogininfor();
}
