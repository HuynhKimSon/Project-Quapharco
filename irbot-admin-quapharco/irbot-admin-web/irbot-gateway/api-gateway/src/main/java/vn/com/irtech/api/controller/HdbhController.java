package vn.com.irtech.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.api.common.page.TableDataInfo;
import vn.com.irtech.api.common.utils.R;
import vn.com.irtech.api.dto.request.GetBillDataReq;
import vn.com.irtech.api.dto.request.UpdateInvoiceNoReq;
import vn.com.irtech.api.dto.response.HdbhMasterRes;
import vn.com.irtech.api.service.HdbhService;

@Controller
@RequestMapping("/api/hdbh")
public class HdbhController extends BaseController {

	@Autowired
	private HdbhService hdbhService;

	@PostMapping("/getListMaster")
	@ResponseBody
	public R getDataMaster(@RequestBody GetBillDataReq request) {

		if (request != null) {
			startPage(request.getPageNum(), request.getPageSize(), null);
		}

		Map<String, Object> responseData = new HashMap<>();

		// Lay du lieu table hoa don ban hang
		request.setNameTableMaster("ph81");
		request.setNameTableDetail("ct81");

		List<HdbhMasterRes> result = hdbhService.getList(request);

		TableDataInfo dataTable = getDataTable(result);

		responseData.put("data", dataTable.getRows());
		responseData.put("total", dataTable.getTotal());

		return R.ok(responseData);
	}

	@PostMapping("/getListBranch")
	@ResponseBody
	public R getDataChiNhanh(@RequestBody GetBillDataReq request) {

		if (request != null) {
			startPage(request.getPageNum(), request.getPageSize(), null);
		}

		Map<String, Object> responseData = new HashMap<>();

		// Lay du lieu table hoa don ban hang chi nhanh
		request.setNameTableMaster("ph52");
		request.setNameTableDetail("ct52");

		List<HdbhMasterRes> result = hdbhService.getList(request);

		TableDataInfo dataTable = getDataTable(result);

		responseData.put("data", dataTable.getRows());
		responseData.put("total", dataTable.getTotal());

		return R.ok(responseData);
	}

	@PostMapping("/master/updateInvoiceNo")
	@ResponseBody
	public R updateInvoiceNoMaster(@RequestBody UpdateInvoiceNoReq request) {

		// Update so hoa don table hoa don ban hang
		request.setNameTableMaster("ph81");

		hdbhService.updateInvoiceNo(request);

		return R.ok();
	}

	@PostMapping("/branch/updateInvoiceNo")
	@ResponseBody
	public R updateInvoiceNoChiNhanh(@RequestBody UpdateInvoiceNoReq request) {

		// Lay du lieu table hoa don ban hang chi nhanh
		request.setNameTableMaster("ph52");

		hdbhService.updateInvoiceNo(request);

		return R.ok();
	}
}
