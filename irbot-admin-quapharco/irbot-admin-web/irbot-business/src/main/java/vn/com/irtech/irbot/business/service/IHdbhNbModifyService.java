package vn.com.irtech.irbot.business.service;

import java.util.List;
import java.util.Map;

import vn.com.irtech.irbot.business.domain.HdbhNb;
import vn.com.irtech.irbot.business.domain.Robot;
import vn.com.irtech.irbot.business.domain.WorkProcess;
import vn.com.irtech.irbot.business.dto.request.UpdateEInvoiceNoReq;

public interface IHdbhNbModifyService {

	public HdbhNb selectHdbhNbModifyById(Long id);

	public List<HdbhNb> selectHdbhNbModifyList(HdbhNb hdbhNb);
	
	public int updateStatus(String ids, Integer status);
	
	public void retry(String ids) throws Exception;
	
	public void updateEInvoiceNo(UpdateEInvoiceNoReq request) throws Exception;
	
	public void sendRobot(Map<Robot, WorkProcess> processList) throws Exception;
}