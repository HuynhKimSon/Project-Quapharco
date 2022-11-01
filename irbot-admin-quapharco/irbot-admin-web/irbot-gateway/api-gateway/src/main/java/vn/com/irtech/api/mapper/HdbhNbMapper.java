package vn.com.irtech.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import vn.com.irtech.api.dto.request.GetBillDataReq;
import vn.com.irtech.api.dto.request.UpdateInvoiceNoReq;
import vn.com.irtech.api.dto.response.HdbhNbDetailRes;
import vn.com.irtech.api.dto.response.HdbhNbMasterRes;

@Mapper
public interface HdbhNbMapper {
	
	public List<HdbhNbMasterRes> getListMaster(GetBillDataReq request);
	
	public List<HdbhNbDetailRes> getDetails(String fastId);
	
	public void updateInvoiceNo(UpdateInvoiceNoReq request);
}
