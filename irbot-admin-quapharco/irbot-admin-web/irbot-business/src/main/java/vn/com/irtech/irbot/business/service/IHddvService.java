package vn.com.irtech.irbot.business.service;

import vn.com.irtech.core.common.page.TableDataInfo;
import vn.com.irtech.irbot.business.domain.Hddv;
import vn.com.irtech.irbot.business.dto.request.UpdateEInvoiceNoReq;
import vn.com.irtech.irbot.business.dto.response.HddvMasterRes;

public interface IHddvService {

	public HddvMasterRes selectHddvById(String fastId) throws Exception;

	public TableDataInfo selectHddvList(Hddv hddv) throws Exception;

	public void updateStatus(String ids, Integer status) throws Exception;

	public void updateMode(String ids) throws Exception;

	public void retry(String ids) throws Exception;

	public void updateEInvoiceNo(UpdateEInvoiceNoReq request) throws Exception;

	public TableDataInfo checkModify(String fastId) throws Exception;

	public void confirmModify(String fastIds) throws Exception;
}
