package vn.com.irtech.irbot.business.service;

import vn.com.irtech.core.common.page.TableDataInfo;
import vn.com.irtech.irbot.business.domain.HdbhCn;
import vn.com.irtech.irbot.business.dto.request.UpdateEInvoiceNoReq;
import vn.com.irtech.irbot.business.dto.response.HdbhCnMasterRes;

public interface IHdbhCnService {

	public HdbhCnMasterRes selectHdbhCnById(String fastId) throws Exception;

	public TableDataInfo selectHdbhCnList(HdbhCn hdbhCn) throws Exception;

	public void updateStatus(String ids, Integer status) throws Exception;
	
	public void updateMode(String ids) throws Exception;

	public void retry(String ids) throws Exception;

	public void updateEInvoiceNo(UpdateEInvoiceNoReq request) throws Exception;

	public TableDataInfo checkModify(String fastId) throws Exception;

	public void confirmModify(String fastIds) throws Exception;

}