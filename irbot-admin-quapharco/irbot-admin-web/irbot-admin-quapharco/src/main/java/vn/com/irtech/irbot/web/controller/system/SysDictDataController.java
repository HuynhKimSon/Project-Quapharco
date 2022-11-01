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
import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.domain.entity.SysDictData;
import vn.com.irtech.core.common.enums.BusinessType;
import vn.com.irtech.core.common.page.TableDataInfo;
import vn.com.irtech.core.common.utils.poi.ExcelUtil;
import vn.com.irtech.core.framework.util.ShiroUtils;
import vn.com.irtech.core.system.service.ISysDictDataService;
import vn.com.irtech.irbot.web.constant.LogTitleConstant;

/**
 * DataDict Controller
 * 
 * @author admin
 */
@Controller
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {
	private String prefix = "system/dict/data";

	@Autowired
	private ISysDictDataService dictDataService;

	@RequiresPermissions("system:dict:view")
	@GetMapping()
	public String dictData() {
		return prefix + "/data";
	}

	@PostMapping("/list")
	@RequiresPermissions("system:dict:list")
	@ResponseBody
	public TableDataInfo list(SysDictData dictData) {
		startPage();
		List<SysDictData> list = dictDataService.selectDictDataList(dictData);
		return getDataTable(list);
	}

	@Log(title = LogTitleConstant.SYS_DICT_DATA, businessType = BusinessType.EXPORT)
	@RequiresPermissions("system:dict:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysDictData dictData) {
		List<SysDictData> list = dictDataService.selectDictDataList(dictData);
		ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
		return util.exportExcel(list, "Dictionary data");
	}

	/**
	 * New dictionary type
	 */
	@GetMapping("/add/{dictType}")
	public String add(@PathVariable("dictType") String dictType, ModelMap mmap) {
		mmap.put("dictType", dictType);
		return prefix + "/add";
	}

	/**
	 * Save new dictionary type
	 */
	@Log(title = LogTitleConstant.SYS_DICT_DATA, businessType = BusinessType.INSERT)
	@RequiresPermissions("system:dict:add")
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated SysDictData dict) {
		dict.setCreateBy(ShiroUtils.getLoginName());
		return toAjax(dictDataService.insertDictData(dict));
	}

	/**
	 * Modify dictionary type
	 */
	@GetMapping("/edit/{dictCode}")
	public String edit(@PathVariable("dictCode") Long dictCode, ModelMap mmap) {
		mmap.put("dict", dictDataService.selectDictDataById(dictCode));
		return prefix + "/edit";
	}

	/**
	 * Modify save dictionary type
	 */
	@Log(title = LogTitleConstant.SYS_DICT_DATA, businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:dict:edit")
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(@Validated SysDictData dict) {
		dict.setUpdateBy(ShiroUtils.getLoginName());
		return toAjax(dictDataService.updateDictData(dict));
	}

	@Log(title = LogTitleConstant.SYS_DICT_DATA, businessType = BusinessType.DELETE)
	@RequiresPermissions("system:dict:remove")
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(dictDataService.deleteDictDataByIds(ids));
	}
}
