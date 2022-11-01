package vn.com.irtech.irbot.web.controller.monitor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.framework.web.domain.Server;

/**
 * Server Controller
 * 
 * @author admin
 */
@Controller
@RequestMapping("/monitor/server")
public class ServerController extends BaseController {
	private String prefix = "monitor/server";

	@RequiresPermissions("monitor:server:view")
	@GetMapping()
	public String server(ModelMap mmap) throws Exception {
		Server server = new Server();
		server.copyTo();
		mmap.put("server", server);
		return prefix + "/server";
	}
}
