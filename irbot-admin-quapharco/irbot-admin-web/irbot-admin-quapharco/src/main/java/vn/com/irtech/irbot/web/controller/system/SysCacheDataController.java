package vn.com.irtech.irbot.web.controller.system;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.domain.CacheEntity;
import vn.com.irtech.core.common.page.TableDataInfo;
import vn.com.irtech.core.common.utils.CacheUtils;

/**
 * SysConfig Controller
 * 
 * @author admin
 */
@Controller
@RequestMapping("/system/cache")
public class SysCacheDataController extends BaseController {
	private String prefix = "system/cache";

	@GetMapping()
	public String config() {
		return prefix + "/cache";
	}

	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo getCacheList(CacheEntity cacheEntity) {
		return getDataTable(CacheUtils.getAllCacheData(cacheEntity));
	}

	@DeleteMapping("/remove-all")
	@ResponseBody
	public AjaxResult removeAllCache() {
		List<Map<String, String>> cacheList = CacheUtils.getAllCacheData(new CacheEntity());
		for (Map<String, String> cacheMap : cacheList) {
			CacheUtils.remove(cacheMap.get("keyName"), cacheMap.get("key"));
		}
		return success();
	}

	@DeleteMapping("/{keyName}/{key}/remove")
	@ResponseBody
	public AjaxResult removeAllCache(@PathVariable("key") String key, @PathVariable("keyName") String keyName) {
		CacheUtils.remove(keyName, key);
		return success();
	}
}
