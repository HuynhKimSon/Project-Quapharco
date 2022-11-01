package vn.com.irtech.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import vn.com.irtech.api.commons.dynamic.datasource.annotation.DataSource;
import vn.com.irtech.api.dto.request.GetBillDataReq;
import vn.com.irtech.api.dto.request.UpdateInvoiceNoReq;
import vn.com.irtech.api.dto.response.HddvMasterRes;
import vn.com.irtech.api.mapper.HddvMapper;

@Service
public class HddvService {

	@Autowired
	private HddvMapper hddvMapper;

	public List<HddvMasterRes> getList(GetBillDataReq request) {
		List<HddvMasterRes> listMaster = hddvMapper.getListMaster(request);

		if (!CollectionUtils.isEmpty(listMaster)) {
			if (request.getIsSelectDetail() != null && request.getIsSelectDetail() == true) {
				for (HddvMasterRes master : listMaster) {
					if (request.getIsSelectDetail() != null && request.getIsSelectDetail() == true) {
						master.setDetails(hddvMapper.getDetails(master.getFastId()));
					}
					master.setUnitCode("100");
				}
			} else {
				for (HddvMasterRes master : listMaster) {
					master.setUnitCode("100");
				}
			}
		}
		return listMaster;
	}

	public void updateInvoiceNo(UpdateInvoiceNoReq request) {
		hddvMapper.updateInvoiceNo(request);
	}

}
