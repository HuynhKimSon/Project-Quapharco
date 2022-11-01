package vn.com.irtech.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import vn.com.irtech.api.dto.request.GetBillDataReq;
import vn.com.irtech.api.dto.request.UpdateInvoiceNoReq;
import vn.com.irtech.api.dto.response.HdbhMasterRes;
import vn.com.irtech.api.mapper.HdbhMapper;

@Service
public class HdbhService {

	@Autowired
	private HdbhMapper hdbhMapper;

	public List<HdbhMasterRes> getList(GetBillDataReq request) {
		List<HdbhMasterRes> listMaster = hdbhMapper.getListMaster(request);

		if (!CollectionUtils.isEmpty(listMaster)) {
			if (request.getIsSelectDetail() != null && request.getIsSelectDetail() == true) {
				for (HdbhMasterRes master : listMaster) {
					if (request.getIsSelectDetail() != null && request.getIsSelectDetail() == true) {
						master.setDetails(hdbhMapper.getDetails(master.getFastId(), request.getNameTableMaster(),
								request.getNameTableDetail()));
					}
					master.setUnitCode("100");
				}
			} else {
				for (HdbhMasterRes master : listMaster) {
					master.setUnitCode("100");
				}
			}
		}

		return listMaster;
	}

	public void updateInvoiceNo(UpdateInvoiceNoReq request) {
		hdbhMapper.updateInvoiceNo(request);
	}

}
