package vn.com.irtech.irbot.web.controller.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import vn.com.irtech.core.common.annotation.Log;
import vn.com.irtech.core.common.config.Global;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.enums.BusinessType;
import vn.com.irtech.core.common.utils.StringUtils;
import vn.com.irtech.core.common.utils.file.FileUploadUtils;
import vn.com.irtech.core.framework.util.ShiroUtils;
import vn.com.irtech.irbot.system.domain.SysUser;
import vn.com.irtech.irbot.system.service.ISysUserService;
import vn.com.irtech.irbot.web.controller.AdminBaseController;
import vn.com.irtech.irbot.web.service.SysPasswordService;

/**
 * Profile Controller
 * 
 * @author admin
 */
@Controller
@RequestMapping("/system/user/profile")
public class SysProfileController extends AdminBaseController {
	private static final Logger log = LoggerFactory.getLogger(SysProfileController.class);

	private String prefix = "system/user/profile";

	@Autowired
	private ISysUserService userService;

	@Autowired
	private SysPasswordService passwordService;

	/**
	 * Profile
	 */
	@GetMapping()
	public String profile(ModelMap mmap) {
		SysUser user = getUser();
		mmap.put("user", user);
		mmap.put("roleGroup", userService.selectUserRoleGroup(user.getUserId()));
		mmap.put("postGroup", userService.selectUserPostGroup(user.getUserId()));
		return prefix + "/profile";
	}

	@GetMapping("/checkPassword")
	@ResponseBody
	public boolean checkPassword(String password) {
		SysUser user = getUser();
		if (passwordService.matches(user, password)) {
			return true;
		}
		return false;
	}

	@GetMapping("/resetPwd")
	public String resetPwd(ModelMap mmap) {
		SysUser user = getUser();
		mmap.put("user", userService.selectUserById(user.getUserId()));
		return prefix + "/resetPwd";
	}

	@Log(title = "Reset password", businessType = BusinessType.UPDATE)
	@PostMapping("/resetPwd")
	@ResponseBody
	public AjaxResult resetPwd(String oldPassword, String newPassword) {
		SysUser user = getUser();
		if (StringUtils.isNotEmpty(newPassword) && passwordService.matches(user, oldPassword)) {
			user.setSalt(ShiroUtils.randomSalt());
			user.setPassword(passwordService.encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
			if (userService.resetUserPwd(user) > 0) {
				ShiroUtils.setLoginUser(userService.selectUserById(user.getUserId()));
				return success();
			}
			return error();
		} else {
			return error("Thay đổi mật khẩu thất bại, mật khẩu cũ sai!");
		}
	}

	/**
	 * Modify
	 */
	@GetMapping("/edit")
	public String edit(ModelMap mmap) {
		SysUser user = getUser();
		mmap.put("user", userService.selectUserById(user.getUserId()));
		return prefix + "/edit";
	}

	/**
	 * Modify avatar
	 */
	@GetMapping("/avatar")
	public String avatar(ModelMap mmap) {
		SysUser user = getUser();
		mmap.put("user", userService.selectUserById(user.getUserId()));
		return prefix + "/avatar";
	}

	/**
	 * Modify user
	 */
	@Log(title = "Profile", businessType = BusinessType.UPDATE)
	@PostMapping("/update")
	@ResponseBody
	public AjaxResult update(SysUser user) {
		SysUser currentUser = getUser();
		currentUser.setUserName(user.getUserName());
		currentUser.setEmail(user.getEmail());
		currentUser.setPhonenumber(user.getPhonenumber());
		currentUser.setSex(user.getSex());
		if (userService.updateUserInfo(currentUser) > 0) {
			ShiroUtils.setLoginUser(userService.selectUserById(currentUser.getUserId()));
			return success();
		}
		return error();
	}

	/**
	 * Save avatar
	 */
	@Log(title = "Profile", businessType = BusinessType.UPDATE)
	@PostMapping("/updateAvatar")
	@ResponseBody
	public AjaxResult updateAvatar(@RequestParam("avatarfile") MultipartFile file) {
		SysUser currentUser = getUser();
		try {
			if (!file.isEmpty()) {
				String avatar = FileUploadUtils.upload(Global.getAvatarPath(), file);
				currentUser.setAvatar(avatar);
				if (userService.updateUserInfo(currentUser) > 0) {
					ShiroUtils.setLoginUser(userService.selectUserById(currentUser.getUserId()));
					return success();
				}
			}
			return error();
		} catch (Exception e) {
			log.error("Cập nhật avatar thất bại!", e);
			return error(e.getMessage());
		}
	}
}
