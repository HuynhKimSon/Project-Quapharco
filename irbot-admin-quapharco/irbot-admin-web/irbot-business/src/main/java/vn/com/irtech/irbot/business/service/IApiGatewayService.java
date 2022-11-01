package vn.com.irtech.irbot.business.service;

import vn.com.irtech.irbot.business.dto.request.ApiGetDataReq;
import vn.com.irtech.irbot.business.dto.response.ApiGetDataHdbhCnRes;
import vn.com.irtech.irbot.business.dto.response.ApiGetDataHdbhNbRes;
import vn.com.irtech.irbot.business.dto.response.ApiGetDataHdbhRes;
import vn.com.irtech.irbot.business.dto.response.ApiGetDataHddvRes;
import vn.com.irtech.irbot.business.dto.response.ApiGetDataHdxNccRes;

public interface IApiGatewayService {

	public ApiGetDataHdbhRes getListHdbhMaster(ApiGetDataReq request) throws Exception;

	public ApiGetDataHdbhCnRes getHdbhChiNhanh(ApiGetDataReq request) throws Exception;

	public ApiGetDataHddvRes getHdDichVu(ApiGetDataReq request) throws Exception;

	public ApiGetDataHdbhNbRes getHdbhNoiBo(ApiGetDataReq request) throws Exception;
	
	public ApiGetDataHdxNccRes getHdxNhaCungCap(ApiGetDataReq request) throws Exception;
}
