package vn.com.irtech.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import vn.com.irtech.api.dto.request.GetBillDataReq;
import vn.com.irtech.api.dto.request.UpdateInvoiceNoReq;
import vn.com.irtech.api.dto.response.HdbhDetailRes;
import vn.com.irtech.api.dto.response.HdbhMasterRes;

@Mapper
public interface HdbhMapper {

	public List<HdbhMasterRes> getListMaster(GetBillDataReq request);

	public List<HdbhDetailRes> getDetails(String fastId, String nameTableMaster, String nameTableDetail);

	public void updateInvoiceNo(UpdateInvoiceNoReq request);
}
