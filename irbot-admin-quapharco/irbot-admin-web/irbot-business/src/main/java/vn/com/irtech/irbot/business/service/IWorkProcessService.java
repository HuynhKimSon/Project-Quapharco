package vn.com.irtech.irbot.business.service;

import java.util.Date;
import java.util.List;

import vn.com.irtech.irbot.business.domain.WorkProcess;

/**
 * Service interface process
 *
 * @author irtech
 * @date 2022-04-27
 */
public interface IWorkProcessService {

	/**
	 * Query process
	 *
	 * @param id ID process
	 * @return process
	 */
	public WorkProcess selectWorkProcessById(Long id);
	
	public WorkProcess selectWorkProcessBySyncKey(String syncKey);

	/**
	 * Query list process
	 *
	 * @param process process Collection @return process
	 */
	public List<WorkProcess> selectWorkProcessList(WorkProcess process);

	/**
	 * Added process
	 *
	 * @param process process
	 * @return result
	 */
	public int insertWorkProcess(WorkProcess process);

	/**
	 * Update process
	 *
	 * @param process process
	 * @return result
	 */
	public int updateWorkProcess(WorkProcess process);

	/**
	 * Xóa hàng loạt process
	 *
	 * @param id ID của dữ liệu sẽ bị xóa
	 * @return result
	 */
	public int deleteWorkProcessByIds(String ids);

	/**
	 * Delete information process
	 *
	 * @param id ID process
	 * @return result
	 */
	public int deleteWorkProcessById(Long id);
	
	public int countByStatuses(Integer[] statuses, Date fromDate, Date toDate, Integer serviceId); 
}