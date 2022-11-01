package vn.com.irtech.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import vn.com.irtech.api.commons.dynamic.datasource.annotation.DataSource;
import vn.com.irtech.api.dto.request.GetBillDataReq;
import vn.com.irtech.api.dto.request.UpdateInvoiceNoReq;
import vn.com.irtech.api.dto.response.HdxNccMasterRes;
import vn.com.irtech.api.mapper.HdxNccMapper;

@Service
public class HdxNccService {

	@Autowired
	private HdxNccMapper hdxNccMapper;

	public List<HdxNccMasterRes> getList(GetBillDataReq request) {
		List<HdxNccMasterRes> listMaster = hdxNccMapper.getListMaster(request);

		if (!CollectionUtils.isEmpty(listMaster)) {
			if (request.getIsSelectDetail() != null && request.getIsSelectDetail() == true) {
				for (HdxNccMasterRes master : listMaster) {
					if (request.getIsSelectDetail() != null && request.getIsSelectDetail() == true) {
						master.setDetails(hdxNccMapper.getDetails(master.getFastId()));
					}
					master.setUnitCode("100");
				}
			} else {
				for (HdxNccMasterRes master : listMaster) {
					master.setUnitCode("100");
				}
			}
		}
		return listMaster;
	}

	public void updateInvoiceNo(UpdateInvoiceNoReq request) {
		hdxNccMapper.updateInvoiceNo(request);
	}

}
