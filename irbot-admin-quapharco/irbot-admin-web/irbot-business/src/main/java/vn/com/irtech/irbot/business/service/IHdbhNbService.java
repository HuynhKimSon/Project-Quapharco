package vn.com.irtech.irbot.business.service;

import java.util.Map;

import vn.com.irtech.core.common.page.TableDataInfo;
import vn.com.irtech.irbot.business.domain.HdbhNb;
import vn.com.irtech.irbot.business.domain.Robot;
import vn.com.irtech.irbot.business.domain.WorkProcess;
import vn.com.irtech.irbot.business.dto.request.UpdateEInvoiceNoReq;
import vn.com.irtech.irbot.business.dto.response.HdbhNbMasterRes;

public interface IHdbhNbService {

	public HdbhNbMasterRes selectHdbhNbById(String id) throws Exception;

	public TableDataInfo selectHdbhNbList(HdbhNb hdbhNb) throws Exception;

	public void updateStatus(String ids, Integer status) throws Exception;

	public void updateMode(String ids) throws Exception;

	public void retry(String ids) throws Exception;

	public void updateEInvoiceNo(UpdateEInvoiceNoReq request) throws Exception;

	public void sendRobot(Map<Robot, WorkProcess> processList) throws Exception;

	public TableDataInfo checkModify(String fastId) throws Exception;

	public void confirmModify(String fastIds) throws Exception;
}