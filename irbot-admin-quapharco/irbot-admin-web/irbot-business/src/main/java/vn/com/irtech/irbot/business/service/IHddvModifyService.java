package vn.com.irtech.irbot.business.service;

import java.util.List;

import vn.com.irtech.irbot.business.domain.Hddv;
import vn.com.irtech.irbot.business.dto.request.UpdateEInvoiceNoReq;

public interface IHddvModifyService {

	public Hddv selectHddvModifyById(Long id);

	public List<Hddv> selectHddvModifyList(Hddv hddv);
	
	public int updateStatus(String ids, Integer status);
	
	public void retry(String ids) throws Exception;
	
	public void updateEInvoiceNo(UpdateEInvoiceNoReq request) throws Exception;
	
}