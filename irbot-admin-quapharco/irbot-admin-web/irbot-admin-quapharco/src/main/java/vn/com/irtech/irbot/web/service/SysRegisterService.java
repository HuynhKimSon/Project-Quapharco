package vn.com.irtech.irbot.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import vn.com.irtech.core.common.constant.Constants;
import vn.com.irtech.core.common.constant.ShiroConstants;
import vn.com.irtech.core.common.constant.UserConstants;
import vn.com.irtech.core.common.utils.DateUtils;
import vn.com.irtech.core.common.utils.MessageUtils;
import vn.com.irtech.core.common.utils.ServletUtils;
import vn.com.irtech.core.framework.manager.AsyncManager;
import vn.com.irtech.core.framework.manager.factory.AsyncFactory;
import vn.com.irtech.core.framework.util.ShiroUtils;
import vn.com.irtech.irbot.system.domain.SysUser;
import vn.com.irtech.irbot.system.service.ISysUserService;

/**
 * Registration verification method
 * 
 * @author admin
 */
@Component
public class SysRegisterService {
	@Autowired
	private ISysUserService userService;

	@Autowired
	private SysPasswordService passwordService;

	/**
	 * registered
	 */
	public String register(SysUser user) {
		String msg = "", username = user.getLoginName(), password = user.getPassword();

		if (!StringUtils.isEmpty(ServletUtils.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA))) {
			msg = "Captcha error";
		} else if (StringUtils.isEmpty(username)) {
			msg = "Username can not be empty";
		} else if (StringUtils.isEmpty(password)) {
			msg = "User password cannot be empty";
		} else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
				|| password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
			msg = "Password length must be between 5 and 20 characters";
		} else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
				|| username.length() > UserConstants.USERNAME_MAX_LENGTH) {
			msg = "Account length must be between 2 and 20 characters";
		} else if (UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(username))) {
			msg = "Create user '" + username + "' , Registered account already exists";
		} else {
			user.setPwdUpdateDate(DateUtils.getNowDate());
			user.setUserName(username);
			user.setSalt(ShiroUtils.randomSalt());
			user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
			boolean regFlag = userService.registerUser(user);
			if (!regFlag) {
				msg = "Registration failed, please contact system administrator";
			} else {
				AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER,
						MessageUtils.message("user.register.success")));
			}
		}
		return msg;
	}

}
