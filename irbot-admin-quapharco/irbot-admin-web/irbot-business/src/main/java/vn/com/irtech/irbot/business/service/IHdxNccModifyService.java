package vn.com.irtech.irbot.business.service;

import java.util.List;

import vn.com.irtech.irbot.business.domain.HdxNcc;
import vn.com.irtech.irbot.business.dto.request.UpdateEInvoiceNoReq;

public interface IHdxNccModifyService {

	public HdxNcc selectHdxNccModifyById(Long id);

	public List<HdxNcc> selectHdxNccModifyList(HdxNcc hdxNcc);
	
	public int updateStatus(String ids, Integer status);
	
	public void retry(String ids) throws Exception;
	
	public void updateEInvoiceNo(UpdateEInvoiceNoReq request) throws Exception;
}