package vn.com.irtech.irbot.web.controller.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.utils.ServletUtils;
import vn.com.irtech.core.common.utils.StringUtils;
import vn.com.irtech.core.framework.util.ShiroUtils;

/**
 * Login controller
 * 
 * @author admin
 */
@Controller
public class SysLoginController extends BaseController {
	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		// If it is an Ajax request, return the Json string。
		if (ServletUtils.isAjaxRequest(request)) {
			return ServletUtils.renderString(response,
					"{\"code\":\"1\",\"msg\":\"Không đăng nhập hoặc hết thời gian. Xin vui lòng đăng nhập lại.\"}");
		}

		return "login";
	}

	@PostMapping("/login")
	@ResponseBody
	public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe) {
		UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			return success();
		} catch (AuthenticationException e) {
			String msg = "Sai tài khoản hoặc mật khẩu";
			if (StringUtils.isNotEmpty(e.getMessage())) {
				msg = e.getMessage();
			}
			return error(msg);
		}
	}

	@GetMapping("/unauth")
	@ResponseBody
	public String unauth() {
		return "error/unauth";
	}

	@PostMapping("/app/login")
	@ResponseBody
	public AjaxResult ajaxAppLogin(String username, String password) {
		UsernamePasswordToken token = new UsernamePasswordToken(username, password, false);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			AjaxResult ajaxResult = AjaxResult.success();
			ajaxResult.put("userInfo", ShiroUtils.getLoginUser());
			return ajaxResult;
		} catch (AuthenticationException e) {
			String msg = "Sai tài khoản hoặc mật khẩu";
			if (StringUtils.isNotEmpty(e.getMessage())) {
				msg = e.getMessage();
			}
			return error(msg);
		}
	}

	@GetMapping("/app/login-qr")
	public String loginByQrCode(@RequestParam String loginName, @RequestParam String securityCode) {
		UsernamePasswordToken token = new UsernamePasswordToken(loginName, securityCode, false);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			AjaxResult ajaxResult = AjaxResult.success();
			ajaxResult.put("userInfo", ShiroUtils.getLoginUser());
			return "redirect:/index";
		} catch (AuthenticationException e) {
			return "login";
		}
	}
}
