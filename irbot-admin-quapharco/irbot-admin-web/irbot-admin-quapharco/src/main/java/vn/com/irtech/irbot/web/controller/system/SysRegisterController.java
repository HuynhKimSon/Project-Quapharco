package vn.com.irtech.irbot.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.system.service.ISysConfigService;
import vn.com.irtech.irbot.system.domain.SysUser;
import vn.com.irtech.irbot.web.service.SysRegisterService;

/**
 * Register Controller
 * 
 * @author admin
 */
@Controller
public class SysRegisterController extends BaseController {
	@Autowired
	private SysRegisterService registerService;

	@Autowired
	private ISysConfigService configService;

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@PostMapping("/register")
	@ResponseBody
	public AjaxResult ajaxRegister(SysUser user) {
		if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
			return error("Chức năng đăng ký không được kích hoạt trong hệ thống hiện tại!");
		}
		String msg = registerService.register(user);
		return StringUtils.isEmpty(msg) ? success() : error(msg);
	}
}
