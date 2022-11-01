package vn.com.irtech.irbot.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.core.common.text.Convert;
import vn.com.irtech.irbot.business.domain.WorkProcess;
import vn.com.irtech.irbot.business.mapper.WorkProcessMapper;
import vn.com.irtech.irbot.business.service.IWorkProcessService;

@Service
public class WorkProcessServiceImpl implements IWorkProcessService 
{
    @Autowired
    private WorkProcessMapper workProcessMapper;

    /**
     * Query workProcess
     *
     * @param id ID workProcess
     * @return workProcess
     */
    @Override
    public WorkProcess selectWorkProcessById(Long id)
    {
        return workProcessMapper.selectWorkProcessById(id);
    }

    /**
     * Query list workProcess
     *
     * @param workProcess workProcess
     * @return workProcess
     */
    @Override
    public List<WorkProcess> selectWorkProcessList(WorkProcess workProcess)
    {
        return workProcessMapper.selectWorkProcessList(workProcess);
    }

    /**
     * Added workProcess
     *
     * @param workProcess workProcess
     * @return result
     */
    @Override
    public int insertWorkProcess(WorkProcess workProcess)
    {
        return workProcessMapper.insertWorkProcess(workProcess);
    }

    /**
     * Update workProcess
     *
     * @param workProcess workProcess
     * @return result
     */
    @Override
    public int updateWorkProcess(WorkProcess workProcess)
    {
        return workProcessMapper.updateWorkProcess(workProcess);
    }

    /**
     * Delete object workProcess
     *
     * @param id ID of the data to be deleted
     * @return result
     */
    @Override
    public int deleteWorkProcessByIds(String ids)
    {
        return workProcessMapper.deleteWorkProcessByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete information workProcess
     *
     * @param id ID workProcess
     * @return result
     */
    @Override
    public int deleteWorkProcessById(Long id)
    {
        return workProcessMapper.deleteWorkProcessById(id);
    }

	@Override
	public WorkProcess selectWorkProcessBySyncKey(String syncKey) 
	{
		return workProcessMapper.selectWorkProcessBySyncKey(syncKey);
	}

	@Override
	public int countByStatuses(Integer[] statuses, Date fromDate, Date toDate, Integer serviceId) {
		return workProcessMapper.countByStatuses(statuses, fromDate, toDate, serviceId);
	}
}
