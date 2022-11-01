package vn.com.irtech.irbot.web.controller.monitor;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.core.common.annotation.Log;
import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.enums.BusinessType;
import vn.com.irtech.core.common.enums.OnlineStatus;
import vn.com.irtech.core.common.page.TableDataInfo;
import vn.com.irtech.core.framework.shiro.session.OnlineSession;
import vn.com.irtech.core.framework.shiro.session.OnlineSessionDAO;
import vn.com.irtech.core.framework.util.ShiroUtils;
import vn.com.irtech.core.system.domain.SysUserOnline;
import vn.com.irtech.core.system.service.ISysUserOnlineService;
import vn.com.irtech.irbot.web.constant.LogTitleConstant;

/**
 * Online user controller
 * 
 * @author admin
 */
@Controller
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController {
	private String prefix = "monitor/online";

	@Autowired
	private ISysUserOnlineService userOnlineService;

	@Autowired
	private OnlineSessionDAO onlineSessionDAO;

	@RequiresPermissions("monitor:online:view")
	@GetMapping()
	public String online() {
		return prefix + "/online";
	}

	@RequiresPermissions("monitor:online:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysUserOnline userOnline) {
		startPage();
		List<SysUserOnline> list = userOnlineService.selectUserOnlineList(userOnline);
		return getDataTable(list);
	}

	@RequiresPermissions("monitor:online:batchForceLogout")
	@Log(title = LogTitleConstant.SYS_USER_ONLINE, businessType = BusinessType.FORCE)
	@PostMapping("/batchForceLogout")
	@ResponseBody
	public AjaxResult batchForceLogout(@RequestParam("ids[]") String[] ids) {
		for (String sessionId : ids) {
			SysUserOnline online = userOnlineService.selectOnlineById(sessionId);
			if (online == null) {
				return error("Người dùng đang ngoại tuyến!");
			}
			OnlineSession onlineSession = (OnlineSession) onlineSessionDAO.readSession(online.getSessionId());
			if (onlineSession == null) {
				return error("Người dùng đang ngoại tuyến!");
			}
			if (sessionId.equals(ShiroUtils.getSessionId())) {
				return error("Người dùng đang đăng nhập hiện tại không thể tự động đăng xuất!");
			}
			onlineSession.setStatus(OnlineStatus.off_line);
			onlineSessionDAO.update(onlineSession);
			online.setStatus(OnlineStatus.off_line);
			userOnlineService.saveOnline(online);
		}
		return success();
	}

	@RequiresPermissions("monitor:online:forceLogout")
	@Log(title = LogTitleConstant.SYS_USER_ONLINE, businessType = BusinessType.FORCE)
	@PostMapping("/forceLogout")
	@ResponseBody
	public AjaxResult forceLogout(String sessionId) {
		SysUserOnline online = userOnlineService.selectOnlineById(sessionId);
		if (sessionId.equals(ShiroUtils.getSessionId())) {
			return error("Người dùng đang đăng nhập hiện tại không thể tự động đăng xuất!");
		}
		if (online == null) {
			return error("Người dùng đang ngoại tuyến!");
		}
		OnlineSession onlineSession = (OnlineSession) onlineSessionDAO.readSession(online.getSessionId());
		if (onlineSession == null) {
			return error("Người dùng đang ngoại tuyến!");
		}
		onlineSession.setStatus(OnlineStatus.off_line);
		onlineSessionDAO.update(onlineSession);
		online.setStatus(OnlineStatus.off_line);
		userOnlineService.saveOnline(online);
		return success();
	}
}
