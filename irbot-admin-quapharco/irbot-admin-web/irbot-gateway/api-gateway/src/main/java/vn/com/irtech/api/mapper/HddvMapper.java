package vn.com.irtech.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import vn.com.irtech.api.dto.request.GetBillDataReq;
import vn.com.irtech.api.dto.request.UpdateInvoiceNoReq;
import vn.com.irtech.api.dto.response.HddvDetailRes;
import vn.com.irtech.api.dto.response.HddvMasterRes;


@Mapper
public interface HddvMapper {
	
	public List<HddvMasterRes> getListMaster(GetBillDataReq request);
	
	public List<HddvDetailRes> getDetails(String fastId);
	
	public void updateInvoiceNo(UpdateInvoiceNoReq request);
}
