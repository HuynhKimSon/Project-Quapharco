package vn.com.irtech.irbot.system.mapper;

import java.util.List;

import vn.com.irtech.irbot.system.domain.SysUserToken;

/**
 * Notify TokenMapper Interface
 * 
 * @author irtehc
 * @date 2020-08-22
 */
public interface SysUserTokenMapper {
	/**
	 * Get Notify Token
	 * 
	 * @param id Notify TokenID
	 * @return Notify Token
	 */
	public SysUserToken selectSysUserTokenById(Long id);

	/**
	 * Get Notify Token List
	 * 
	 * @param sysUserToken Notify Token
	 * @return Notify Token List
	 */
	public List<SysUserToken> selectSysUserTokenList(SysUserToken sysUserToken);

	/**
	 * Add Notify Token
	 * 
	 * @param sysUserToken Notify Token
	 * @return Result
	 */
	public int insertSysUserToken(SysUserToken sysUserToken);

	/**
	 * Update Notify Token
	 * 
	 * @param sysUserToken Notify Token
	 * @return Result
	 */
	public int updateSysUserToken(SysUserToken sysUserToken);

	/**
	 * Delete Notify Token
	 * 
	 * @param id Notify TokenID
	 * @return result
	 */
	public int deleteSysUserTokenById(Long id);

	/**
	 * Batch Delete Notify Token
	 * 
	 * @param ids IDs
	 * @return result
	 */
	public int deleteSysUserTokenByIds(String[] ids);

	/**
	 * Delete User Token By User Token
	 * 
	 * @param userLoginToken
	 * @return int
	 */
	public int deleteUserTokenByUserToken(String userLoginToken);

	/**
	 * Get list device token by user id
	 * 
	 * @param userId
	 * @return List<String>
	 */
	public List<String> getListDeviceTokenByUserId(Long userId);

}
