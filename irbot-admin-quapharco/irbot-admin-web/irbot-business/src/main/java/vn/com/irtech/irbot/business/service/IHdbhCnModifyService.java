package vn.com.irtech.irbot.business.service;

import java.util.List;

import vn.com.irtech.irbot.business.domain.HdbhCn;
import vn.com.irtech.irbot.business.dto.request.UpdateEInvoiceNoReq;

public interface IHdbhCnModifyService {

	public HdbhCn selectHdbhCnModifyById(Long id);

	public List<HdbhCn> selectHdbhCnModifyList(HdbhCn hdbhCn);

	public int updateStatus(String ids, Integer status);

	public void retry(String ids) throws Exception;

	public void updateEInvoiceNo(UpdateEInvoiceNoReq request) throws Exception;
}