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
import vn.com.irtech.core.common.enums.BusinessType;
import vn.com.irtech.core.common.utils.StringUtils;
import vn.com.irtech.core.framework.util.ShiroUtils;
import vn.com.irtech.irbot.system.domain.SysDept;
import vn.com.irtech.irbot.system.domain.SysRole;
import vn.com.irtech.irbot.system.service.ISysDeptService;
import vn.com.irtech.irbot.web.constant.LogTitleConstant;

/**
 * Department Controller
 * 
 * @author admin
 */
@Controller
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {
	private String prefix = "system/dept";

	@Autowired
	private ISysDeptService deptService;

	@RequiresPermissions("system:dept:view")
	@GetMapping()
	public String dept() {
		return prefix + "/dept";
	}

	@RequiresPermissions("system:dept:list")
	@PostMapping("/list")
	@ResponseBody
	public List<SysDept> list(SysDept dept) {
		List<SysDept> deptList = deptService.selectDeptList(dept);
		return deptList;
	}

	/**
	 * New department
	 */
	@GetMapping("/add/{parentId}")
	public String add(@PathVariable("parentId") Long parentId, ModelMap mmap) {
		mmap.put("dept", deptService.selectDeptById(parentId));
		return prefix + "/add";
	}

	/**
	 * New preservation department
	 */
	@Log(title = LogTitleConstant.SYS_DEPT, businessType = BusinessType.INSERT)
	@RequiresPermissions("system:dept:add")
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated SysDept dept) {
		if (UserConstants.DEPT_NAME_NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
			return error("Thêm bộ phận mới " + dept.getDeptName() + " thất bại, tên bộ phận đã tồn tại!");
		}
		dept.setCreateBy(ShiroUtils.getLoginName());
		return toAjax(deptService.insertDept(dept));
	}

	/**
	 * Edit
	 */
	@GetMapping("/edit/{deptId}")
	public String edit(@PathVariable("deptId") Long deptId, ModelMap mmap) {
		SysDept dept = deptService.selectDeptById(deptId);
		if (StringUtils.isNotNull(dept) && 100L == deptId) {
			dept.setParentName("Root");
		}
		mmap.put("dept", dept);
		return prefix + "/edit";
	}

	/**
	 * Edit
	 */
	@Log(title = LogTitleConstant.SYS_DEPT, businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:dept:edit")
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(@Validated SysDept dept) {
		if (UserConstants.DEPT_NAME_NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
			return error("Cập nhật bộ phận " + dept.getDeptName() + " thất bại, tên bộ phận đã tồn tại!");
		} else if (dept.getParentId().equals(dept.getDeptId())) {
			return error(
					"Cập nhật bộ phận " + dept.getDeptName() + " thất bại，bộ phận cấp trên không thể là chính nó!");
		}
		dept.setUpdateBy(ShiroUtils.getLoginName());
		return toAjax(deptService.updateDept(dept));
	}

	/**
	 * Delete
	 */
	@Log(title = LogTitleConstant.SYS_DEPT, businessType = BusinessType.DELETE)
	@RequiresPermissions("system:dept:remove")
	@GetMapping("/remove/{deptId}")
	@ResponseBody
	public AjaxResult remove(@PathVariable("deptId") Long deptId) {
		if (deptService.selectDeptCount(deptId) > 0) {
			return AjaxResult.warn("Có một bộ phận trực thuộc và nó không được phép xóa");
		}
		if (deptService.checkDeptExistUser(deptId)) {
			return AjaxResult.warn("Có một người dùng trong bộ phận và nó không được phép xóa");
		}
		return toAjax(deptService.deleteDeptById(deptId));
	}

	/**
	 * Check department name is unique
	 */
	@PostMapping("/checkDeptNameUnique")
	@ResponseBody
	public String checkDeptNameUnique(SysDept dept) {
		return deptService.checkDeptNameUnique(dept);
	}

	/**
	 * Select department tree
	 */
	@GetMapping("/selectDeptTree/{deptId}")
	public String selectDeptTree(@PathVariable("deptId") Long deptId, ModelMap mmap) {
		mmap.put("dept", deptService.selectDeptById(deptId));
		return prefix + "/tree";
	}

	/**
	 * Load the department list tree
	 */
	@GetMapping("/treeData")
	@ResponseBody
	public List<Ztree> treeData() {
		List<Ztree> ztrees = deptService.selectDeptTree(new SysDept());
		return ztrees;
	}

	/**
	 * Load the role department (data permission) list tree
	 */
	@GetMapping("/roleDeptTreeData")
	@ResponseBody
	public List<Ztree> deptTreeData(SysRole role) {
		List<Ztree> ztrees = deptService.roleDeptTreeData(role);
		return ztrees;
	}
}
