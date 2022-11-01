package vn.com.irtech.irbot.system.mapper;

import java.util.List;

import vn.com.irtech.irbot.system.domain.SysNotification;

/**
 * NotificationMapper Interface
 * 
 * @author Irtech
 * @date 2020-08-22
 */
public interface SysNotificationMapper {
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
	 * @return Result
	 */
	public int insertSysNotification(SysNotification sysNotification);

	/**
	 * Update Notification
	 * 
	 * @param sysNotification Notification
	 * @return Result
	 */
	public int updateSysNotification(SysNotification sysNotification);

	/**
	 * Delete Notification
	 * 
	 * @param id NotificationID
	 * @return result
	 */
	public int deleteSysNotificationById(Long id);

	/**
	 * Batch Delete Notification
	 * 
	 * @param ids IDs
	 * @return result
	 */
	public int deleteSysNotificationByIds(String[] ids);
}
