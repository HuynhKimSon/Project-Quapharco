package vn.com.irtech.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import vn.com.irtech.api.dto.request.GetBillDataReq;
import vn.com.irtech.api.dto.request.UpdateInvoiceNoReq;
import vn.com.irtech.api.dto.response.HdxNccDetailRes;
import vn.com.irtech.api.dto.response.HdxNccMasterRes;



@Mapper
public interface HdxNccMapper {
	
	public List<HdxNccMasterRes> getListMaster(GetBillDataReq request);
	
	public List<HdxNccDetailRes> getDetails(String fastId);
	
	public void updateInvoiceNo(UpdateInvoiceNoReq request);
}
