package vn.com.irtech.irbot.business.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import vn.com.irtech.irbot.business.domain.WorkProcess;

public interface WorkProcessMapper 
{
    /**
     * Query workProcess
     *
     * @param id ID workProcess
     * @return workProcess
     */
    public WorkProcess selectWorkProcessById(Long id);
    
	public WorkProcess selectWorkProcessBySyncKey(String syncKey);

    /**
     * Query list workProcess
     *
     * @param workProcess workProcess
     * Collection @return workProcess
     */
    public List<WorkProcess> selectWorkProcessList(WorkProcess workProcess);

    /**
     * Added workProcess
     *
     * @param workProcess workProcess
     * @return result
     */
    public int insertWorkProcess(WorkProcess workProcess);

    /**
     * Update workProcess
     *
     * @param workProcess workProcess
     * @return result
     */
    public int updateWorkProcess(WorkProcess workProcess);
    
    public int updateWorkProcessSyncKey(WorkProcess workProcess);

    /**
     * Delete workProcess
     *
     * @param id ID workProcess
     * @return result
     */
    public int deleteWorkProcessById(Long id);

    /**
     * Bulk delete workProcess
     *
     * @param id The ID of the data will be deleted
     * @return result
     */
    public int deleteWorkProcessByIds(String[] ids);
    
    public int countByStatuses(@Param("statuses") Integer[] statuses, @Param("fromDate") Date fromDate,
			@Param("toDate") Date toDate, @Param("serviceId") Integer serviceId);

}
