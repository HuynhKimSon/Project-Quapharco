package vn.com.irtech.irbot.business.service;

import vn.com.irtech.core.common.page.TableDataInfo;
import vn.com.irtech.irbot.business.domain.HdxNcc;
import vn.com.irtech.irbot.business.dto.request.UpdateEInvoiceNoReq;
import vn.com.irtech.irbot.business.dto.response.HdxNccMasterRes;

public interface IHdxNccService {

	public HdxNccMasterRes selectHdxNccById(String id) throws Exception;

	public TableDataInfo selectHdxNccList(HdxNcc hdxNcc) throws Exception;

	public void updateStatus(String ids, Integer status) throws Exception;

	public void updateMode(String ids) throws Exception;

	public void retry(String ids) throws Exception;

	public void updateEInvoiceNo(UpdateEInvoiceNoReq request) throws Exception;

	public TableDataInfo checkModify(String fastId) throws Exception;

	public void confirmModify(String fastIds) throws Exception;
}