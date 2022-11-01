package vn.com.irtech.irbot.web.controller.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.domain.LoginUser;
import vn.com.irtech.core.common.domain.entity.SysDictData;
import vn.com.irtech.core.common.page.TableDataInfo;
import vn.com.irtech.core.framework.util.ShiroUtils;
import vn.com.irtech.core.system.service.ISysDictDataService;
import vn.com.irtech.irbot.business.domain.Hdbh;
import vn.com.irtech.irbot.business.domain.HdbhDetail;
import vn.com.irtech.irbot.business.domain.WorkProcess;
import vn.com.irtech.irbot.business.dto.request.UpdateEInvoiceNoReq;
import vn.com.irtech.irbot.business.dto.request.UpdateStatusReq;
import vn.com.irtech.irbot.business.service.IHdbhModifyDetailService;
import vn.com.irtech.irbot.business.service.IHdbhModifyService;
import vn.com.irtech.irbot.business.service.IWorkProcessService;
import vn.com.irtech.irbot.business.type.DictType;
import vn.com.irtech.irbot.system.domain.SysDept;
import vn.com.irtech.irbot.system.service.ISysDeptService;

@Controller
@RequestMapping("/business/hdbh-modify")
public class HdbhModifyController extends BaseController {

	private String prefix = "business/hdbh-modify";

	@Autowired
	private IHdbhModifyService hdbhModifyService;

	@Autowired
	private IHdbhModifyDetailService hdbhModifyDetailService;

	@Autowired
	private IWorkProcessService workProcessService;

	@Autowired
	private ISysDictDataService sysDictDataService;

	@Autowired
	private ISysDeptService sysDeptService;

	@GetMapping()
	public String processQuapharco(ModelMap mmap) {
		Map<String, String> branchCompanyOptions = new HashMap<String, String>();
		LoginUser loginUser = ShiroUtils.getLoginUser();
		if (loginUser.getUserId() == 1) {
			SysDictData sysDictDataSelect = new SysDictData();
			sysDictDataSelect.setDictType(DictType.BUSINESS_UNIT_CODE.value());
			List<SysDictData> branchCompanyList = sysDictDataService.selectDictDataList(sysDictDataSelect);

			for (SysDictData branchCompany : branchCompanyList) {
				branchCompanyOptions.put(branchCompany.getDictLabel(), branchCompany.getDictValue());
			}
		} else {
			SysDept sysDept = sysDeptService.selectDeptById(loginUser.getDeptId());
			SysDictData sysDictDataSelect = new SysDictData();
			sysDictDataSelect.setDictType(DictType.BUSINESS_UNIT_CODE.value());
			sysDictDataSelect.setDictValue(sysDept.getDeptName());
			List<SysDictData> branchCompanyList = sysDictDataService.selectDictDataList(sysDictDataSelect);

			for (SysDictData branchCompany : branchCompanyList) {
				branchCompanyOptions.put(branchCompany.getDictLabel(), branchCompany.getDictValue());
			}
		}

		mmap.put("branchCompanyOptions", branchCompanyOptions);
		return prefix + "/hdbh-modify";
	}

	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Hdbh hdbh) {
		startPage();
		List<Hdbh> list = hdbhModifyService.selectHdbhModifyList(hdbh);
		return getDataTable(list);
	}

	@GetMapping("/detail/{id}")
	public String detail(@PathVariable("id") Long id, ModelMap mmap) {
		Hdbh hdbh = hdbhModifyService.selectHdbhModifyById(id);
		mmap.put("hdbhModify", hdbh);

		HdbhDetail hdbhDetail = new HdbhDetail();
		hdbhDetail.setHdbhId(id);
		List<HdbhDetail> details = hdbhModifyDetailService.selectHdbhModifyDetailList(hdbhDetail);
		mmap.put("details", details);

		return prefix + "/detail";
	}

	@GetMapping("/history/{id}")
	public String history(@PathVariable("id") String id, ModelMap mmap) {
		WorkProcess process = new WorkProcess();
		process = workProcessService.selectWorkProcessBySyncKey(id);
		if (process == null) {
			process = new WorkProcess();
		}
		mmap.put("process", process);
		return prefix + "/history";
	}

	@GetMapping("/update-status")
	public String updateStatus() {
		return prefix + "/update-status";
	}

	@PostMapping("/update-status")
	@ResponseBody
	public AjaxResult updateStatus(@Validated @RequestBody UpdateStatusReq request) {
		try {
			hdbhModifyService.updateStatus(request.getIds(), request.getStatus());
			AjaxResult ajaxResult = AjaxResult.success();
			return ajaxResult;
		} catch (Exception e) {
			logger.error(">>>>>> Error: " + e.getMessage());
			return AjaxResult.error(e.getMessage());
		}
	}

	@PostMapping("/updateEInvoiceNo")
	@ResponseBody
	public AjaxResult updateEInvoiceNo(@Validated @RequestBody UpdateEInvoiceNoReq request) {
		try {
			hdbhModifyService.updateEInvoiceNo(request);
			AjaxResult ajaxResult = AjaxResult.success();
			return ajaxResult;
		} catch (Exception e) {
			logger.error(">>>>>> Error: " + e.getMessage());
			return AjaxResult.error(e.getMessage());
		}
	}

	@PostMapping("/retry")
	@ResponseBody
	public AjaxResult retry(String ids) {
		try {
			hdbhModifyService.retry(ids);
			AjaxResult ajaxResult = AjaxResult.success();
			return ajaxResult;
		} catch (Exception e) {
			logger.error(">>>>>> Error: " + e.getMessage());
			return AjaxResult.error(e.getMessage());
		}
	}
}