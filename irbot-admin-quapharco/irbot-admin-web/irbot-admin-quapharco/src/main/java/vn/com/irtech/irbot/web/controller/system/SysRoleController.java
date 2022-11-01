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
import vn.com.irtech.irbot.system.domain.SysRole;
import vn.com.irtech.irbot.system.domain.SysUser;
import vn.com.irtech.irbot.system.domain.SysUserRole;
import vn.com.irtech.irbot.system.service.ISysRoleService;
import vn.com.irtech.irbot.system.service.ISysUserService;
import vn.com.irtech.irbot.web.constant.LogTitleConstant;

/**
 * Role controller
 * 
 * @author admin
 */
@Controller
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {
	private String prefix = "system/role";

	@Autowired
	private ISysRoleService roleService;

	@Autowired
	private ISysUserService userService;

	@RequiresPermissions("system:role:view")
	@GetMapping()
	public String role() {
		return prefix + "/role";
	}

	@RequiresPermissions("system:role:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysRole role) {
		startPage();
		List<SysRole> list = roleService.selectRoleList(role);
		return getDataTable(list);
	}

	@Log(title = LogTitleConstant.SYS_ROLE, businessType = BusinessType.EXPORT)
	@RequiresPermissions("system:role:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysRole role) {
		List<SysRole> list = roleService.selectRoleList(role);
		ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
		return util.exportExcel(list, "Role data");
	}

	/**
	 * New role
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * Add save new role
	 */
	@RequiresPermissions("system:role:add")
	@Log(title = LogTitleConstant.SYS_ROLE, businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated SysRole role) {
		if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
			return error("Thêm vai trò " + role.getRoleName() + " thất bại, tên vai trò đã tồn tại!");
		} else if (UserConstants.ROLE_KEY_NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
			return error("Thêm vai trò " + role.getRoleName() + " thất bại, quyền hạn của vai trò đã tồn tại!");
		}
		role.setCreateBy(ShiroUtils.getLoginName());
		ShiroUtils.clearCachedAuthorizationInfo();
		return toAjax(roleService.insertRole(role));

	}

	/**
	 * Modify role
	 */
	@GetMapping("/edit/{roleId}")
	public String edit(@PathVariable("roleId") Long roleId, ModelMap mmap) {
		mmap.put("role", roleService.selectRoleById(roleId));
		return prefix + "/edit";
	}

	/**
	 * Modify save role
	 */
	@RequiresPermissions("system:role:edit")
	@Log(title = LogTitleConstant.SYS_ROLE, businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(@Validated SysRole role) {
		roleService.checkRoleAllowed(role);
		if (UserConstants.ROLE_NAME_NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
			return error("Cập nhật vai trò " + role.getRoleName() + " thất bại, tên vai trò đã thất bại!");
		} else if (UserConstants.ROLE_KEY_NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
			return error("Cập nhật vai trò " + role.getRoleName() + " thất bại, quyền hạn của vai trò đã tồn tại!");
		}
		role.setUpdateBy(ShiroUtils.getLoginName());
		ShiroUtils.clearCachedAuthorizationInfo();
		return toAjax(roleService.updateRole(role));
	}

	/**
	 * Role permissions
	 */
	@GetMapping("/authDataScope/{roleId}")
	public String authDataScope(@PathVariable("roleId") Long roleId, ModelMap mmap) {
		mmap.put("role", roleService.selectRoleById(roleId));
		return prefix + "/dataScope";
	}

	/**
	 * Save role permissions
	 */
	@RequiresPermissions("system:role:edit")
	@Log(title = LogTitleConstant.SYS_ROLE, businessType = BusinessType.UPDATE)
	@PostMapping("/authDataScope")
	@ResponseBody
	public AjaxResult authDataScopeSave(SysRole role) {
		roleService.checkRoleAllowed(role);
		role.setUpdateBy(ShiroUtils.getLoginName());
		if (roleService.authDataScope(role) > 0) {
			ShiroUtils.setLoginUser(userService.selectUserById(ShiroUtils.getLoginUser().getUserId()));
			return success();
		}
		return error();
	}

	@RequiresPermissions("system:role:remove")
	@Log(title = LogTitleConstant.SYS_ROLE, businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		try {
			return toAjax(roleService.deleteRoleByIds(ids));
		} catch (Exception e) {
			return error(e.getMessage());
		}
	}

	/**
	 * Verify role name
	 */
	@PostMapping("/checkRoleNameUnique")
	@ResponseBody
	public String checkRoleNameUnique(SysRole role) {
		return roleService.checkRoleNameUnique(role);
	}

	/**
	 * Verify role permissions
	 */
	@PostMapping("/checkRoleKeyUnique")
	@ResponseBody
	public String checkRoleKeyUnique(SysRole role) {
		return roleService.checkRoleKeyUnique(role);
	}

	/**
	 * Select the menu tree
	 */
	@GetMapping("/selectMenuTree")
	public String selectMenuTree() {
		return prefix + "/tree";
	}

	/**
	 * Modify status
	 */
	@Log(title = LogTitleConstant.SYS_ROLE, businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:role:edit")
	@PostMapping("/changeStatus")
	@ResponseBody
	public AjaxResult changeStatus(SysRole role) {
		roleService.checkRoleAllowed(role);
		return toAjax(roleService.changeStatus(role));
	}

	/**
	 * Assign user
	 */
	@RequiresPermissions("system:role:edit")
	@GetMapping("/authUser/{roleId}")
	public String authUser(@PathVariable("roleId") Long roleId, ModelMap mmap) {
		mmap.put("role", roleService.selectRoleById(roleId));
		return prefix + "/authUser";
	}

	/**
	 * Query the list of assigned user roles
	 */
	@RequiresPermissions("system:role:list")
	@PostMapping("/authUser/allocatedList")
	@ResponseBody
	public TableDataInfo allocatedList(SysUser user) {
		startPage();
		List<SysUser> list = userService.selectAllocatedList(user);
		return getDataTable(list);
	}

	/**
	 * Cancel auth user
	 */
	@Log(title = LogTitleConstant.SYS_ROLE, businessType = BusinessType.GRANT)
	@PostMapping("/authUser/cancel")
	@ResponseBody
	public AjaxResult cancelAuthUser(SysUserRole userRole) {
		return toAjax(roleService.deleteAuthUser(userRole));
	}

	/**
	 * Cancel auth user
	 */
	@Log(title = LogTitleConstant.SYS_ROLE, businessType = BusinessType.GRANT)
	@PostMapping("/authUser/cancelAll")
	@ResponseBody
	public AjaxResult cancelAuthUserAll(Long roleId, String userIds) {
		return toAjax(roleService.deleteAuthUsers(roleId, userIds));
	}

	/**
	 * Select user
	 */
	@GetMapping("/authUser/selectUser/{roleId}")
	public String selectUser(@PathVariable("roleId") Long roleId, ModelMap mmap) {
		mmap.put("role", roleService.selectRoleById(roleId));
		return prefix + "/selectUser";
	}

	/**
	 * Query the list of unassigned user roles
	 */
	@RequiresPermissions("system:role:list")
	@PostMapping("/authUser/unallocatedList")
	@ResponseBody
	public TableDataInfo unallocatedList(SysUser user) {
		startPage();
		List<SysUser> list = userService.selectUnallocatedList(user);
		return getDataTable(list);
	}

	/**
	 * Select user authorization in bulk
	 */
	@Log(title = LogTitleConstant.SYS_ROLE, businessType = BusinessType.GRANT)
	@PostMapping("/authUser/selectAll")
	@ResponseBody
	public AjaxResult selectAuthUserAll(Long roleId, String userIds) {
		return toAjax(roleService.insertAuthUsers(roleId, userIds));
	}
}