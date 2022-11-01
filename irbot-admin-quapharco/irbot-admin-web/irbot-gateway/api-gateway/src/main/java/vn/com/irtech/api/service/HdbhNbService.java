package vn.com.irtech.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import vn.com.irtech.api.commons.dynamic.datasource.annotation.DataSource;
import vn.com.irtech.api.dto.request.GetBillDataReq;
import vn.com.irtech.api.dto.request.UpdateInvoiceNoReq;
import vn.com.irtech.api.dto.response.HdbhNbMasterRes;
import vn.com.irtech.api.mapper.HdbhNbMapper;

@Service
public class HdbhNbService {

	@Autowired
	private HdbhNbMapper hdbhNbMapper;

	/**
	 * Lay du lieu hoa don ban hang noi bo don vi 100
	 * @param request
	 * @return
	 */
	public List<HdbhNbMasterRes> getList(GetBillDataReq request) {
		List<HdbhNbMasterRes> listMaster = hdbhNbMapper.getListMaster(request);

		if (!CollectionUtils.isEmpty(listMaster)) {
			if (request.getIsSelectDetail() != null && request.getIsSelectDetail() == true) {
				for (HdbhNbMasterRes master : listMaster) {
					if (request.getIsSelectDetail() != null && request.getIsSelectDetail() == true) {
						master.setDetails(hdbhNbMapper.getDetails(master.getFastId()));
					}
					master.setUnitCode("100");
				}
			} else {
				for (HdbhNbMasterRes master : listMaster) {
					master.setUnitCode("100");
				}
			}
		}
		return listMaster;
	}

	public void updateInvoiceNo(UpdateInvoiceNoReq request) {
		hdbhNbMapper.updateInvoiceNo(request);
	}
	
	/**
	 * Lay du lieu hoa don ban hang noi bo don vi 200 - CN Le Thuy
	 * @param request
	 * @return
	 */
//	@DataSource("slave1")
//	public List<HdbhNbMasterRes> getListChinhNhanh200(GetBillDataReq request) {
//		List<HdbhNbMasterRes> listMaster = hdbhNbMapper.getListMaster(request);
//
//		if (!CollectionUtils.isEmpty(listMaster)) {
//			if (request.getIsSelectDetail() != null && request.getIsSelectDetail() == true) {
//				for (HdbhNbMasterRes master : listMaster) {
//					if (request.getIsSelectDetail() != null && request.getIsSelectDetail() == true) {
//						master.setDetails(hdbhNbMapper.getDetails(master.getFastId()));
//					}
//					master.setUnitCode("200");
//				}
//			} else {
//				for (HdbhNbMasterRes master : listMaster) {
//					master.setUnitCode("200");
//				}
//			}
//		}
//		return listMaster;
//	}
}
