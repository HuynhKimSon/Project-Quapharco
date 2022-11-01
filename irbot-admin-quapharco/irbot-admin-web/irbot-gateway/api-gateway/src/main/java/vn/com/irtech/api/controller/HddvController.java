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
import vn.com.irtech.api.dto.response.HddvMasterRes;
import vn.com.irtech.api.service.HddvService;

@Controller
@RequestMapping("/api/hddv")
public class HddvController extends BaseController {

	@Autowired
	private HddvService hddvService;

	@PostMapping("/getListMaster")
	@ResponseBody
	public R getDataMaster(@RequestBody GetBillDataReq request) {
		// Validate request
		if (request != null) {
			startPage(request.getPageNum(), request.getPageSize(), null);
		}

		Map<String, Object> responseData = new HashMap<>();

		// Lay du lieu table hoa don dich vu
		List<HddvMasterRes> result = hddvService.getList(request);

		TableDataInfo dataTable = getDataTable(result);

		responseData.put("data", dataTable.getRows());
		responseData.put("total", dataTable.getTotal());

		return R.ok(responseData);
	}

	@PostMapping("/updateInvoiceNo")
	@ResponseBody
	public R updateInvoiceNoMaster(@RequestBody UpdateInvoiceNoReq request) {

		// Update so hoa don table hoa don dich vu
		hddvService.updateInvoiceNo(request);

		return R.ok();
	}
}
