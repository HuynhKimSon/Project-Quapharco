package vn.com.irtech.irbot.web.controller.system;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.core.common.annotation.Log;
import vn.com.irtech.core.common.constant.UserConstants;
import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.enums.BusinessType;
import vn.com.irtech.core.common.page.TableDataInfo;
import vn.com.irtech.core.common.utils.poi.ExcelUtil;
import vn.com.irtech.core.framework.util.ShiroUtils;
import vn.com.irtech.core.system.domain.SysConfig;
import vn.com.irtech.core.system.service.ISysConfigService;
import vn.com.irtech.irbot.web.constant.LogTitleConstant;

/**
 * SysConfig Controller
 * 
 * @author admin
 */
@Controller
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {
	private String prefix = "system/config";

	@Autowired
	private ISysConfigService configService;

	@RequiresPermissions("system:config:view")
	@GetMapping()
	public String config() {
		return prefix + "/config";
	}

	/**
	 * 查询参数配置列表
	 */
	@RequiresPermissions("system:config:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysConfig config) {
		startPage();
		List<SysConfig> list = configService.selectConfigList(config);
		return getDataTable(list);
	}

	@Log(title = LogTitleConstant.SYS_CONFIG, businessType = BusinessType.EXPORT)
	@RequiresPermissions("system:config:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysConfig config) {
		List<SysConfig> list = configService.selectConfigList(config);
		ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
		return util.exportExcel(list, "参数数据");
	}

	/**
	 * 新增参数配置
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * 新增保存参数配置
	 */
	@RequiresPermissions("system:config:add")
	@Log(title = LogTitleConstant.SYS_CONFIG, businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated SysConfig config) {
		if (UserConstants.CONFIG_KEY_NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
			return error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
		}
		config.setCreateBy(ShiroUtils.getLoginName());
		return toAjax(configService.insertConfig(config));
	}

	/**
	 * 修改参数配置
	 */
	@GetMapping("/edit/{configId}")
	public String edit(@PathVariable("configId") Long configId, ModelMap mmap) {
		mmap.put("config", configService.selectConfigById(configId));
		return prefix + "/edit";
	}

	/**
	 * 修改保存参数配置
	 */
	@RequiresPermissions("system:config:edit")
	@Log(title = LogTitleConstant.SYS_CONFIG, businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(@Validated SysConfig config) {
		if (UserConstants.CONFIG_KEY_NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
			return error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
		}
		config.setUpdateBy(ShiroUtils.getLoginName());
		return toAjax(configService.updateConfig(config));
	}

	/**
	 * 删除参数配置
	 */
	@RequiresPermissions("system:config:remove")
	@Log(title = LogTitleConstant.SYS_CONFIG, businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		configService.deleteConfigByIds(ids);
		return success();
	}

	/**
	 * 刷新参数缓存
	 */
	@RequiresPermissions("system:config:remove")
	@Log(title = LogTitleConstant.SYS_CONFIG, businessType = BusinessType.CLEAN)
	@GetMapping("/refreshCache")
	@ResponseBody
	public AjaxResult refreshCache() {
		configService.resetConfigCache();
		return success();
	}

	/**
	 * 校验参数键名
	 */
	@PostMapping("/checkConfigKeyUnique")
	@ResponseBody
	public String checkConfigKeyUnique(SysConfig config) {
		return configService.checkConfigKeyUnique(config);
	}
}
