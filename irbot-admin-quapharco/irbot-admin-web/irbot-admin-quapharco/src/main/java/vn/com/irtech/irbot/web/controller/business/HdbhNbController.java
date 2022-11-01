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
import vn.com.irtech.irbot.business.domain.HdbhNb;
import vn.com.irtech.irbot.business.domain.WorkProcess;
import vn.com.irtech.irbot.business.dto.request.UpdateEInvoiceNoReq;
import vn.com.irtech.irbot.business.dto.request.UpdateStatusReq;
import vn.com.irtech.irbot.business.dto.response.HdbhNbMasterRes;
import vn.com.irtech.irbot.business.service.IHdbhNbService;
import vn.com.irtech.irbot.business.service.IWorkProcessService;
import vn.com.irtech.irbot.business.type.DictType;
import vn.com.irtech.irbot.system.domain.SysDept;
import vn.com.irtech.irbot.system.service.ISysDeptService;

@Controller
@RequestMapping("/business/hdbh-nb")
public class HdbhNbController extends BaseController {

	private String prefix = "business/hdbh-nb";

	@Autowired
	private IHdbhNbService hdbhNbService;

	@Autowired
	private IWorkProcessService workProcessService;

	@Autowired
	private ISysDeptService sysDeptService;

	@Autowired
	private ISysDictDataService sysDictDataService;

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
		return prefix + "/hdbh-nb";
	}

	@PostMapping("/list")
	@ResponseBody
	public Object list(HdbhNb hdbhNb) throws Exception {
		try {
			return hdbhNbService.selectHdbhNbList(hdbhNb);
		} catch (Exception e) {
			logger.error(">>>>>> Error: " + e.getMessage());
			return AjaxResult.error(e.getMessage());
		}
	}

	@GetMapping("/detail/{id}")
	public String detail(@PathVariable("id") String id, ModelMap mmap) {
		HdbhNbMasterRes hdbhNb = new HdbhNbMasterRes();
		try {
			hdbhNb = hdbhNbService.selectHdbhNbById(id);
		} catch (Exception e) {
			logger.error(">>>>>> Error: " + e.getMessage());
		}
		mmap.put("hdbhNb", hdbhNb);
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
			hdbhNbService.updateStatus(request.getIds(), request.getStatus());
			AjaxResult ajaxResult = AjaxResult.success();
			return ajaxResult;
		} catch (Exception e) {
			logger.error(">>>>>> Error: " + e.getMessage());
			return AjaxResult.error(e.getMessage());
		}
	}

	@PostMapping("/update-mode")
	@ResponseBody
	public AjaxResult updateMode(@Validated @RequestBody UpdateStatusReq request) {
		try {
			hdbhNbService.updateMode(request.getIds());
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
			hdbhNbService.retry(ids);
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
			hdbhNbService.updateEInvoiceNo(request);
			AjaxResult ajaxResult = AjaxResult.success();
			return ajaxResult;
		} catch (Exception e) {
			logger.error(">>>>>> Error: " + e.getMessage());
			return AjaxResult.error(e.getMessage());
		}
	}

	@GetMapping("/check-modify/{fastId}")
	public String checkModify(@PathVariable("fastId") String fastId, ModelMap mmap) {
		TableDataInfo result = new TableDataInfo();
		try {
			result = hdbhNbService.checkModify(fastId);
		} catch (Exception e) {
			result.setMsg(e.getMessage());
			logger.error(">>>>>> Error: " + e.getMessage());
		}
		mmap.put("result", result);
		return prefix + "/check-modify";
	}

	@GetMapping("/view-modify-detail/{fastId}/{id}")
	public String viewModifyDetail(@PathVariable("fastId") String fastId, @PathVariable("id") int id, ModelMap mmap) {
		try {
			TableDataInfo tableInfo = hdbhNbService.checkModify(fastId);
			mmap.put("result", tableInfo.getRows().get(id));
		} catch (Exception e) {
			logger.error(">>>>>> Error: " + e.getMessage());
		}
		return prefix + "/view-modify-detail";
	}

	@PostMapping("/confirm-modify")
	@ResponseBody
	public AjaxResult confirmModify(String fastIds) {
		try {
			hdbhNbService.confirmModify(fastIds);
			AjaxResult ajaxResult = AjaxResult.success();
			return ajaxResult;
		} catch (Exception e) {
			logger.error(">>>>>> Error: " + e.getMessage());
			return AjaxResult.error(e.getMessage());
		}
	}
}
