package vn.com.irtech.irbot.web.controller;

import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.LoginUser;
import vn.com.irtech.core.common.utils.bean.BeanUtils;
import vn.com.irtech.core.framework.util.ShiroUtils;
import vn.com.irtech.irbot.system.domain.SysUser;

public class AdminBaseController extends BaseController {

	public SysUser getUser() {
		LoginUser loginUser = ShiroUtils.getLoginUser();
		SysUser user = null;
		if (loginUser instanceof SysUser) {
			user = (SysUser) loginUser;
		} else {
			user = new SysUser();
			BeanUtils.copyBeanProp(user, loginUser);
		}
		return user;
	}

	public Long getUserId() {
		return ShiroUtils.getLoginUser().getUserId();
	}

}