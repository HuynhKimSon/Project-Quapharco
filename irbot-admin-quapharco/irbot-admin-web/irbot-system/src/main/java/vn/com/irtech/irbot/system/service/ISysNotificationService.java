package vn.com.irtech.irbot.system.service;

import java.util.List;

import vn.com.irtech.irbot.system.domain.SysNotification;

/**
 * NotificationService Interface
 * 
 * @author Irtech
 * @date 2020-08-22
 */
public interface ISysNotificationService {
	/**
	 * Get Notification
	 * 
	 * @param id NotificationID
	 * @return Notification
	 */
	public SysNotification selectSysNotificationById(Long id);

	/**
	 * Get Notification List
	 * 
	 * @param sysNotification Notification
	 * @return Notification List
	 */
	public List<SysNotification> selectSysNotificationList(SysNotification sysNotification);

	/**
	 * Add Notification
	 * 
	 * @param sysNotification Notification
	 * @return result
	 */
	public int insertSysNotification(SysNotification sysNotification);

	/**
	 * Update Notification
	 * 
	 * @param sysNotification Notification
	 * @return result
	 */
	public int updateSysNotification(SysNotification sysNotification);

	/**
	 * Batch Delete Notification
	 * 
	 * @param ids Entity Ids
	 * @return result
	 */
	public int deleteSysNotificationByIds(String ids);

	/**
	 * Delete Notification
	 * 
	 * @param id NotificationID
	 * @return result
	 */
	public int deleteSysNotificationById(Long id);
}
