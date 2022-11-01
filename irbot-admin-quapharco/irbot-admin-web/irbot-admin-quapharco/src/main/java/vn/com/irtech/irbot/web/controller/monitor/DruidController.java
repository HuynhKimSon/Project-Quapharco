package vn.com.irtech.irbot.web.controller.monitor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.com.irtech.core.common.controller.BaseController;

/**
 * druid controller
 * 
 * @author admin
 */
@Controller
@RequestMapping("/monitor/data")
public class DruidController extends BaseController {
	private String prefix = "/druid";

	@RequiresPermissions("monitor:data:view")
	@GetMapping()
	public String index() {
		return redirect(prefix + "/index");
	}
}
