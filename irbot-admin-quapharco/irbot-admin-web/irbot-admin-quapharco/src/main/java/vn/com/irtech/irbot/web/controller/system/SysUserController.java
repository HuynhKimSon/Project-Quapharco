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
import org.springframework.web.multipart.MultipartFile;

import vn.com.irtech.core.common.annotation.Log;
import vn.com.irtech.core.common.constant.UserConstants;
import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.enums.BusinessType;
import vn.com.irtech.core.common.page.TableDataInfo;
import vn.com.irtech.core.common.utils.StringUtils;
import vn.com.irtech.core.common.utils.poi.ExcelUtil;
import vn.com.irtech.core.framework.util.ShiroUtils;
import vn.com.irtech.irbot.system.domain.SysUser;
import vn.com.irtech.irbot.system.domain.SysUserRole;
import vn.com.irtech.irbot.system.domain.UserDto;
import vn.com.irtech.irbot.system.domain.UserExcelDto;
import vn.com.irtech.irbot.system.service.ISysPostService;
import vn.com.irtech.irbot.system.service.ISysRoleService;
import vn.com.irtech.irbot.system.service.ISysUserService;
import vn.com.irtech.irbot.web.constant.LogTitleConstant;
import vn.com.irtech.irbot.web.service.SysPasswordService;

/**
 * User controller
 * 
 * @author admin
 */
@Controller
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
	private String prefix = "system/user";

	@Autowired
	private ISysUserService userService;

	@Autowired
	private ISysRoleService roleService;

	@Autowired
	private ISysPostService postService;

	@Autowired
	private SysPasswordService passwordService;

	@RequiresPermissions("system:user:view")
	@GetMapping()
	public String user() {
		return prefix + "/user";
	}

	@RequiresPermissions("system:user:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysUser user) {
		startPage();
		List<SysUser> list = userService.selectUserList(user);
		return getDataTable(list);
	}

	@Log(title = LogTitleConstant.SYS_USER, businessType = BusinessType.EXPORT)
	@RequiresPermissions("system:user:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysUser user) {
		List<SysUser> list = userService.selectUserList(user);
		ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
		return util.exportExcel(list, "Users");
	}

	@Log(title = LogTitleConstant.SYS_USER, businessType = BusinessType.IMPORT)
	@RequiresPermissions("system:user:import")
	@PostMapping("/importData")
	@ResponseBody
	public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
		ExcelUtil<UserDto> util = new ExcelUtil<UserDto>(UserDto.class);
		List<UserDto> userList = util.importExcel(file.getInputStream());
		String operName = ShiroUtils.getLoginUser().getLoginName();
		String message = userService.importUserDto(userList, updateSupport, operName);
		return AjaxResult.success(message);
	}

	@RequiresPermissions("system:user:view")
	@GetMapping("/importTemplate")
	@ResponseBody
	public AjaxResult importTemplate() {
		ExcelUtil<UserExcelDto> util = new ExcelUtil<UserExcelDto>(UserExcelDto.class);
		return util.importTemplateExcel("Users");
	}

	/**
	 * Add new user
	 */
	@GetMapping("/add")
	public String add(ModelMap mmap) {
		mmap.put("roles", roleService.selectRoleAll());
		mmap.put("posts", postService.selectPostAll());
		return prefix + "/add";
	}

	/**
	 * Add save new user
	 */
	@RequiresPermissions("system:user:add")
	@Log(title = LogTitleConstant.SYS_USER, businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated SysUser user) {
		if (UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(user.getLoginName()))) {
			return error("Thêm người dùng " + user.getLoginName() + " thất bại, tên người dùng đã tồn tại!");
		} else if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user))
				&& StringUtils.isNotEmpty(user.getPhonenumber())) {
			return error("Thêm người dùng " + user.getLoginName() + " thất bại, số điện thoại đã tồn tại!");
		} else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user))
				&& StringUtils.isNotEmpty(user.getEmail())) {
			return error("Thêm người dùng " + user.getLoginName() + " thất bại, email đã tồn tại!");
		}
		user.setSalt(ShiroUtils.randomSalt());
		user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
		user.setCreateBy(ShiroUtils.getLoginName());
		return toAjax(userService.insertUser(user));
	}

	/**
	 * Modify user
	 */
	@GetMapping("/edit/{userId}")
	public String edit(@PathVariable("userId") Long userId, ModelMap mmap) {
		mmap.put("user", userService.selectUserById(userId));
		mmap.put("roles", roleService.selectRolesByUserId(userId));
		mmap.put("posts", postService.selectPostsByUserId(userId));
		return prefix + "/edit";
	}

	/**
	 * Modify save user
	 */
	@RequiresPermissions("system:user:edit")
	@Log(title = LogTitleConstant.SYS_USER, businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(@Validated SysUser user) {
		userService.checkUserAllowed(user);
		if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user))
				&& StringUtils.isNotEmpty(user.getPhonenumber())) {
			return error("Cập nhật người dùng " + user.getLoginName() + " thất bại, số điện thoại đã tồn tại!");
		} else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user))
				&& StringUtils.isNotEmpty(user.getEmail())) {
			return error("Cập nhật người dùng " + user.getLoginName() + " thất bại, email đã tồn tại!");
		}
		user.setUpdateBy(ShiroUtils.getLoginName());
		return toAjax(userService.updateUser(user));
	}

	/**
	 * Modify save user
	 */
	@RequiresPermissions("system:user:edit")
	@Log(title = LogTitleConstant.SYS_USER, businessType = BusinessType.UPDATE)
	@PostMapping("/editNewLoginName")
	@ResponseBody
	public AjaxResult editUserWithNewLoginName(@Validated SysUser user) {
		String defaultPassword = "123456";
		System.out.println("zzzzzzz: " + user);
		userService.checkUserAllowed(user);
		if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user))
				&& StringUtils.isNotEmpty(user.getPhonenumber())) {
			return error("Cập nhật người dùng " + user.getLoginName() + " thất bại, số điện thoại đã tồn tại!");
		} else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user))
				&& StringUtils.isNotEmpty(user.getEmail())) {
			return error("Cập nhật người dùng " + user.getLoginName() + " thất bại, email đã tồn tại!");
		} else if (UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(user))
				&& StringUtils.isNotEmpty(user.getLoginName())) {
			return error("Cập nhật người dùng " + user.getLoginName() + " thất bại, tên đăng nhập đã tồn tại!");
		}
		user.setUpdateBy(ShiroUtils.getLoginName());
		user.setSalt(ShiroUtils.randomSalt());
		user.setPassword(passwordService.encryptPassword(user.getLoginName(), defaultPassword, user.getSalt()));
		return toAjax(userService.updateUser(user));
	}

	@RequiresPermissions("system:user:resetPwd")
	@Log(title = "Reset password", businessType = BusinessType.UPDATE)
	@GetMapping("/resetPwd/{userId}")
	public String resetPwd(@PathVariable("userId") Long userId, ModelMap mmap) {
		mmap.put("user", userService.selectUserById(userId));
		return prefix + "/resetPwd";
	}

	@RequiresPermissions("system:user:resetPwd")
	@Log(title = "Reset password", businessType = BusinessType.UPDATE)
	@PostMapping("/resetPwd")
	@ResponseBody
	public AjaxResult resetPwdSave(SysUser user) {
		userService.checkUserAllowed(user);
		user.setSalt(ShiroUtils.randomSalt());
		user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
		if (userService.resetUserPwd(user) > 0) {
			if (ShiroUtils.getUserId() == user.getUserId()) {
				ShiroUtils.setLoginUser(userService.selectUserById(user.getUserId()));
			}
			return success();
		}
		return error();
	}

	/**
	 * Redirect to Authorization role page
	 */
	@GetMapping("/authRole/{userId}")
	public String authRole(@PathVariable("userId") Long userId, ModelMap mmap) {
		SysUser user = userService.selectUserById(userId);
		// Get the list of roles the user belongs to
		List<SysUserRole> userRoles = userService.selectUserRoleByUserId(userId);
		mmap.put("user", user);
		mmap.put("userRoles", userRoles);
		return prefix + "/authRole";
	}

	/**
	 * Add Auth role
	 */
	@RequiresPermissions("system:user:add")
	@Log(title = LogTitleConstant.SYS_USER, businessType = BusinessType.GRANT)
	@PostMapping("/authRole/insertAuthRole")
	@ResponseBody
	public AjaxResult insertAuthRole(Long userId, Long[] roleIds) {
		userService.insertUserAuth(userId, roleIds);
		return success();
	}

	@RequiresPermissions("system:user:remove")
	@Log(title = LogTitleConstant.SYS_USER, businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		try {
			return toAjax(userService.deleteUserByIds(ids));
		} catch (Exception e) {
			return error(e.getMessage());
		}
	}

	/**
	 * Check LoginName is unique
	 */
	@PostMapping("/checkLoginNameUnique")
	@ResponseBody
	public String checkLoginNameUnique(SysUser user) {
		return userService.checkLoginNameUnique(user);
	}

	/**
	 * Check phone is unique
	 */
	@PostMapping("/checkPhoneUnique")
	@ResponseBody
	public String checkPhoneUnique(SysUser user) {
		System.out.println("check phone");
		return userService.checkPhoneUnique(user);
	}

	/**
	 * Verify email address
	 */
	@PostMapping("/checkEmailUnique")
	@ResponseBody
	public String checkEmailUnique(SysUser user) {
		return userService.checkEmailUnique(user);
	}
	
	/**
	 * Verify user code
	 */
	@PostMapping("/checkUserCodeUnique")
	@ResponseBody
	public String checkUserCodeUnique(SysUser user) {
		return userService.checkUserCodeUnique(user);
	}

	/**
	 * Modify user status
	 */
	@Log(title = LogTitleConstant.SYS_USER, businessType = BusinessType.UPDATE)
	@RequiresPermissions("system:user:edit")
	@PostMapping("/changeStatus")
	@ResponseBody
	public AjaxResult changeStatus(SysUser user) {
		userService.checkUserAllowed(user);
		return toAjax(userService.changeStatus(user));
	}
}