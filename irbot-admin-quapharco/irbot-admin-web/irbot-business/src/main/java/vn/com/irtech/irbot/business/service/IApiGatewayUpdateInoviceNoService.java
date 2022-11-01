package vn.com.irtech.irbot.business.service;

import vn.com.irtech.irbot.business.dto.request.ApiUpdateInvoiceNoReq;

public interface IApiGatewayUpdateInoviceNoService {

	public void updateInvoiceNoHdbhMaster(ApiUpdateInvoiceNoReq request) throws Exception;

	public void updateInvoiceNoHdbhChiNhanh(ApiUpdateInvoiceNoReq request) throws Exception;

	public void updateInvoiceNoHdDichVu(ApiUpdateInvoiceNoReq request) throws Exception;

	public void updateInvoiceNoHdbhNoiBo(ApiUpdateInvoiceNoReq request) throws Exception;

	public void updateInvoiceNoHdxNhaCungCap(ApiUpdateInvoiceNoReq request) throws Exception;

}
