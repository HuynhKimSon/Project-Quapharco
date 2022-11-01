package vn.com.irtech.irbot.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import vn.com.irtech.core.common.constant.Constants;
import vn.com.irtech.core.common.constant.ShiroConstants;
import vn.com.irtech.core.common.constant.UserConstants;
import vn.com.irtech.core.common.enums.UserStatus;
import vn.com.irtech.core.common.exception.user.CaptchaException;
import vn.com.irtech.core.common.exception.user.UserBlockedException;
import vn.com.irtech.core.common.exception.user.UserDeleteException;
import vn.com.irtech.core.common.exception.user.UserNotExistsException;
import vn.com.irtech.core.common.exception.user.UserPasswordNotMatchException;
import vn.com.irtech.core.common.utils.DateUtils;
import vn.com.irtech.core.common.utils.MessageUtils;
import vn.com.irtech.core.common.utils.ServletUtils;
import vn.com.irtech.core.framework.manager.AsyncManager;
import vn.com.irtech.core.framework.manager.factory.AsyncFactory;
import vn.com.irtech.core.framework.util.ShiroUtils;
import vn.com.irtech.irbot.system.domain.SysUser;
import vn.com.irtech.irbot.system.service.ISysUserService;

/**
 * Login verification method
 * 
 * @author admin
 */
@Component
public class SysLoginService {
	@Autowired
	private SysPasswordService passwordService;

	@Autowired
	private ISysUserService userService;

	/**
	 * log in
	 */
	public SysUser login(String username, String password) {
		// Verification code verification
		if (!StringUtils.isEmpty(ServletUtils.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA))) {
			AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
					MessageUtils.message("user.jcaptcha.error")));
			throw new CaptchaException();
		}
		// Username or password is empty error
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			AsyncManager.me().execute(
					AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
			throw new UserNotExistsException();
		}
		// If the password is not in the specified range, error
//		if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
//				|| password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
//			AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
//					MessageUtils.message("user.password.not.match")));
//			throw new UserPasswordNotMatchException();
//		}

		// Username is not in the specified range error
		if (username.length() < UserConstants.USERNAME_MIN_LENGTH
				|| username.length() > UserConstants.USERNAME_MAX_LENGTH) {
			AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
					MessageUtils.message("user.password.not.match")));
			throw new UserPasswordNotMatchException();
		}

		// Query user information
		SysUser user = userService.selectUserByLoginName(username);

		if (user == null && maybeMobilePhoneNumber(username)) {
			user = userService.selectUserByPhoneNumber(username);
		}

		if (user == null && maybeEmail(username)) {
			user = userService.selectUserByEmail(username);
		}

		if (user == null) {
			AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
					MessageUtils.message("user.not.exists")));
			throw new UserNotExistsException();
		}

		if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
			AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
					MessageUtils.message("user.password.delete")));
			throw new UserDeleteException();
		}

		if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
			AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
					MessageUtils.message("user.blocked", user.getRemark())));
			throw new UserBlockedException();
		}

		passwordService.validate(user, password);

		AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS,
				MessageUtils.message("user.login.success")));
		recordLoginInfo(user);
		return user;
	}

	private boolean maybeEmail(String username) {
		if (!username.matches(UserConstants.EMAIL_PATTERN)) {
			return false;
		}
		return true;
	}

	private boolean maybeMobilePhoneNumber(String username) {
		if (!username.matches(UserConstants.MOBILE_PHONE_NUMBER_PATTERN)) {
			return false;
		}
		return true;
	}

	/**
	 * Record login information
	 */
	public void recordLoginInfo(SysUser user) {
		user.setLoginIp(ShiroUtils.getIp());
		user.setLoginDate(DateUtils.getNowDate());
		userService.updateUserInfo(user);
	}
}
