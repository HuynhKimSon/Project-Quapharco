package vn.com.irtech.irbot.web.controller.monitor;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.core.common.annotation.Log;
import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.enums.BusinessType;
import vn.com.irtech.core.common.page.TableDataInfo;
import vn.com.irtech.core.common.utils.poi.ExcelUtil;
import vn.com.irtech.core.system.domain.SysLogininfor;
import vn.com.irtech.core.system.service.ISysLogininforService;
import vn.com.irtech.irbot.web.constant.LogTitleConstant;
import vn.com.irtech.irbot.web.service.SysPasswordService;

/**
 * SysLoginin for Controller
 * 
 * @author admin
 */
@Controller
@RequestMapping("/monitor/logininfor")
public class SysLogininforController extends BaseController {
	private String prefix = "monitor/logininfor";

	@Autowired
	private ISysLogininforService logininforService;

	@Autowired
	private SysPasswordService passwordService;

	@RequiresPermissions("monitor:logininfor:view")
	@GetMapping()
	public String logininfor() {
		return prefix + "/logininfor";
	}

	@RequiresPermissions("monitor:logininfor:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysLogininfor logininfor) {
		startPage();
		List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
		return getDataTable(list);
	}

	@Log(title = LogTitleConstant.SYS_LOGIN_INFO, businessType = BusinessType.EXPORT)
	@RequiresPermissions("monitor:logininfor:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysLogininfor logininfor) {
		List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
		ExcelUtil<SysLogininfor> util = new ExcelUtil<SysLogininfor>(SysLogininfor.class);
		return util.exportExcel(list, "Log in");
	}

	@RequiresPermissions("monitor:logininfor:remove")
	@Log(title = LogTitleConstant.SYS_LOGIN_INFO, businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(logininforService.deleteLogininforByIds(ids));
	}

	@RequiresPermissions("monitor:logininfor:remove")
	@Log(title = LogTitleConstant.SYS_LOGIN_INFO, businessType = BusinessType.CLEAN)
	@PostMapping("/clean")
	@ResponseBody
	public AjaxResult clean() {
		logininforService.cleanLogininfor();
		return success();
	}

	@RequiresPermissions("monitor:logininfor:unlock")
	@Log(title = "Unlock account", businessType = BusinessType.OTHER)
	@PostMapping("/unlock")
	@ResponseBody
	public AjaxResult unlock(String loginName) {
		passwordService.unlock(loginName);
		return success();
	}
}
