package vn.com.irtech.irbot.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.core.common.text.Convert;
import vn.com.irtech.core.common.utils.DateUtils;
import vn.com.irtech.irbot.system.domain.SysUserToken;
import vn.com.irtech.irbot.system.mapper.SysUserTokenMapper;
import vn.com.irtech.irbot.system.service.ISysUserTokenService;

/**
 * Notify TokenService Business Processing
 * 
 * @author irtehc
 * @date 2020-08-22
 */
@Service
public class SysUserTokenServiceImpl implements ISysUserTokenService {
	@Autowired
	private SysUserTokenMapper sysUserTokenMapper;

	/**
	 * Get Notify Token
	 * 
	 * @param id Notify TokenID
	 * @return Notify Token
	 */
	@Override
	public SysUserToken selectSysUserTokenById(Long id) {
		return sysUserTokenMapper.selectSysUserTokenById(id);
	}

	/**
	 * Get Notify Token List
	 * 
	 * @param sysUserToken Notify Token
	 * @return Notify Token
	 */
	@Override
	public List<SysUserToken> selectSysUserTokenList(SysUserToken sysUserToken) {
		return sysUserTokenMapper.selectSysUserTokenList(sysUserToken);
	}

	/**
	 * Add Notify Token
	 * 
	 * @param sysUserToken Notify Token
	 * @return result
	 */
	@Override
	public int insertSysUserToken(SysUserToken sysUserToken) {
		sysUserToken.setCreateTime(DateUtils.getNowDate());
		return sysUserTokenMapper.insertSysUserToken(sysUserToken);
	}

	/**
	 * Update Notify Token
	 * 
	 * @param sysUserToken Notify Token
	 * @return result
	 */
	@Override
	public int updateSysUserToken(SysUserToken sysUserToken) {
		sysUserToken.setUpdateTime(DateUtils.getNowDate());
		return sysUserTokenMapper.updateSysUserToken(sysUserToken);
	}

	/**
	 * Delete Notify Token By ID
	 * 
	 * @param ids Entity ID
	 * @return result
	 */
	@Override
	public int deleteSysUserTokenByIds(String ids) {
		return sysUserTokenMapper.deleteSysUserTokenByIds(Convert.toStrArray(ids));
	}

	/**
	 * Delete Notify Token
	 * 
	 * @param id Notify TokenID
	 * @return result
	 */
	@Override
	public int deleteSysUserTokenById(Long id) {
		return sysUserTokenMapper.deleteSysUserTokenById(id);
	}

	/**
	 * Delete User Token By User Token
	 * 
	 * @param userLoginToken
	 * @return int
	 */
	@Override
	public int deleteUserTokenByUserToken(String userLoginToken) {
		return sysUserTokenMapper.deleteUserTokenByUserToken(userLoginToken);
	}

	/**
	 * Get list device token by user id
	 * 
	 * @param userId
	 * @return List<String>
	 */
	@Override
	public List<String> getListDeviceTokenByUserId(Long userId) {
		return sysUserTokenMapper.getListDeviceTokenByUserId(userId);
	}

}
