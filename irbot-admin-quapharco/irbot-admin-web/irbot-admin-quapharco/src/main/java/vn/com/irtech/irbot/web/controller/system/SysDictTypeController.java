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
import vn.com.irtech.core.common.domain.Ztree;
import vn.com.irtech.core.common.domain.entity.SysDictType;
import vn.com.irtech.core.common.enums.BusinessType;
import vn.com.irtech.core.common.page.TableDataInfo;
import vn.com.irtech.core.common.utils.poi.ExcelUtil;
import vn.com.irtech.core.framework.util.ShiroUtils;
import vn.com.irtech.core.system.service.ISysDictTypeService;
import vn.com.irtech.irbot.web.constant.LogTitleConstant;

/**
 * Data dictionary type controller
 *
 * @author admin
 */
@Controller
@RequestMapping("/system/dict")
public class SysDictTypeController extends BaseController {
	private String prefix = "system/dict/type";

	@Autowired
	private ISysDictTypeService dictTypeService;

	@RequiresPermissions("system:dict:view")
	@GetMapping()
	public String dictType() {
		return prefix + "/type";
	}

	@PostMapping("/list")
	@RequiresPermissions("system:dict:list")
	@ResponseBody
	public TableDataInfo list(SysDictType dictType) {
		startPage();
		List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
		return getDataTable(list);
	}

	@Log(title = LogTitleConstant.SYS_DICT_TYPE, businessType = BusinessType.EXPORT)
	@RequiresPermissions("system:dict:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysDictType dictType) {

		List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
		ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
		return util.exportExcel(list, "Dictionary type");
	}

	/**
	 * New dictionary type
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * Added save dictionary type
	 */
	@Log(title = LogTitleConstant.SYS_DICT_TYPE, businessType = BusinessType.INSERT)
	@RequiresPermissions("system:dict:add")
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated SysDictType dict) {
		if (UserConstants.DICT_TYPE_NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
			return error("Thêm từ điển mới " + dict.getDictName() + " thất bại, loại từ điển đã tồn tại!");
		}
		dict.setCreateBy(ShiroUtils.getLoginName());
		return toAjax(dictTypeService.insertDictType(dict));
	}

	/**
	 * Modify dictionary type
	 */
	@GetMapping("/edit/{dictId}")
	public String edit(@PathVariable("dictId") Long dictId, ModelMap mmap) {
		mmap.put("dict", dictTypeService.selectDictTypeById(dictId));
		return prefix + "/edit";
	}

	/**
	 * Modify save dictionary type
	 */
	@Log(title = LogTitleConstant.SYS_DICT_TYPE, businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:dict:edit")
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(@Validated SysDictType dict) {
		if (UserConstants.DICT_TYPE_NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
			return error("Cập nhật từ điển " + dict.getDictName() + " thất bại, loại từ điển đã tồn tại!");
		}
		dict.setUpdateBy(ShiroUtils.getLoginName());
		return toAjax(dictTypeService.updateDictType(dict));
	}

	@Log(title = LogTitleConstant.SYS_DICT_TYPE, businessType = BusinessType.DELETE)
	@RequiresPermissions("system:dict:remove")
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(dictTypeService.deleteDictTypeByIds(ids));
	}

	/**
	 * Clear cache
	 */
	@RequiresPermissions("system:dict:remove")
	@Log(title = LogTitleConstant.SYS_DICT_TYPE, businessType = BusinessType.CLEAN)
	@GetMapping("/clearCache")
	@ResponseBody
	public AjaxResult clearCache() {
		dictTypeService.clearCache();
		return success();
	}

	/**
	 * Look up dictionary details
	 */
	@RequiresPermissions("system:dict:list")
	@GetMapping("/detail/{dictId}")
	public String detail(@PathVariable("dictId") Long dictId, ModelMap mmap) {
		mmap.put("dict", dictTypeService.selectDictTypeById(dictId));
		mmap.put("dictList", dictTypeService.selectDictTypeAll());
		return "system/dict/data/data";
	}

	/**
	 * Check dictionary type
	 */
	@PostMapping("/checkDictTypeUnique")
	@ResponseBody
	public String checkDictTypeUnique(SysDictType dictType) {
		return dictTypeService.checkDictTypeUnique(dictType);
	}

	/**
	 * Select dictionary tree
	 */
	@GetMapping("/selectDictTree/{columnId}/{dictType}")
	public String selectDeptTree(@PathVariable("columnId") Long columnId, @PathVariable("dictType") String dictType,
			ModelMap mmap) {
		mmap.put("columnId", columnId);
		mmap.put("dict", dictTypeService.selectDictTypeByType(dictType));
		return prefix + "/tree";
	}

	/**
	 * Load dictionary list tree
	 */
	@GetMapping("/treeData")
	@ResponseBody
	public List<Ztree> treeData() {
		List<Ztree> ztrees = dictTypeService.selectDictTree(new SysDictType());
		return ztrees;
	}
}
