package vn.com.irtech.irbot.web.shiro.realm;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import vn.com.irtech.core.common.domain.LoginUser;
import vn.com.irtech.core.framework.shiro.realm.UserRealm;
import vn.com.irtech.core.framework.util.ShiroUtils;
import vn.com.irtech.irbot.system.domain.SysUser;
import vn.com.irtech.irbot.system.service.ISysMenuService;
import vn.com.irtech.irbot.system.service.ISysRoleService;
import vn.com.irtech.irbot.web.service.SysLoginService;

public class AdminUserRealm extends UserRealm {

	@Autowired
	private ISysMenuService menuService;

	@Autowired
	private ISysRoleService roleService;

	@Autowired
	private SysLoginService loginService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		LoginUser user = ShiroUtils.getLoginUser();
		Set<String> roles = new HashSet<String>();
		Set<String> menus = new HashSet<String>();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		if (user.isAdmin()) {
			info.addRole("admin");
			info.addStringPermission("*:*:*");
		} else {
			roles = roleService.selectRoleKeys(user.getUserId());
			menus = menuService.selectPermsByUserId(user.getUserId());
			info.setRoles(roles);
			info.setStringPermissions(menus);
		}
		return info;
	}

	@Override
	protected SysUser login(String username, String password) {
		return loginService.login(username, password);
	}

}
