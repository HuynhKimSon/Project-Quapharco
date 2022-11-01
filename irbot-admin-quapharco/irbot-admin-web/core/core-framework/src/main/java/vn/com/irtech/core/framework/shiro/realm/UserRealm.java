package vn.com.irtech.core.framework.shiro.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.com.irtech.core.common.domain.LoginUser;
import vn.com.irtech.core.common.exception.user.CaptchaException;
import vn.com.irtech.core.common.exception.user.RoleBlockedException;
import vn.com.irtech.core.common.exception.user.UserBlockedException;
import vn.com.irtech.core.common.exception.user.UserNotExistsException;
import vn.com.irtech.core.common.exception.user.UserPasswordNotMatchException;
import vn.com.irtech.core.common.exception.user.UserPasswordRetryLimitExceedException;

/**
 * 
 * @author admin
 */
public abstract class UserRealm extends AuthorizingRealm {

	private static final Logger log = LoggerFactory.getLogger(UserRealm.class);

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();
		String password = "";
		if (upToken.getPassword() != null) {
			password = new String(upToken.getPassword());
		}

		LoginUser user = null;

		try {
			user = login(username, password);
		} catch (CaptchaException e) {
			throw new AuthenticationException(e.getMessage(), e);
		} catch (UserNotExistsException e) {
			throw new UnknownAccountException(e.getMessage(), e);
		} catch (UserPasswordNotMatchException e) {
			throw new IncorrectCredentialsException(e.getMessage(), e);
		} catch (UserPasswordRetryLimitExceedException e) {
			throw new ExcessiveAttemptsException(e.getMessage(), e);
		} catch (UserBlockedException e) {
			throw new LockedAccountException(e.getMessage(), e);
		} catch (RoleBlockedException e) {
			throw new LockedAccountException(e.getMessage(), e);
		} catch (Exception e) {
			log.info("Login verification for user [" + username + "] failed: {}", e.getMessage());
			throw new AuthenticationException(e.getMessage(), e);
		}
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
		return info;
	}

	protected abstract LoginUser login(String username, String password);

	public void clearCachedAuthorizationInfo() {
		this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
	}

	public void clearCachedAuthorizationInfo(Object principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		this.clearCachedAuthorizationInfo(principals);
	}

	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}
}
