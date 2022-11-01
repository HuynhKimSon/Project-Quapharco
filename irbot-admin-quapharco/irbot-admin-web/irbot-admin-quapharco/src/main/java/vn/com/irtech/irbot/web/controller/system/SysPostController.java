package vn.com.irtech.irbot.web.controller.system;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.core.common.annotation.Log;
import vn.com.irtech.core.common.constant.UserConstants;
import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.enums.BusinessType;
import vn.com.irtech.core.common.page.TableDataInfo;
import vn.com.irtech.core.common.utils.poi.ExcelUtil;
import vn.com.irtech.core.framework.util.ShiroUtils;
import vn.com.irtech.irbot.system.domain.SysPost;
import vn.com.irtech.irbot.system.service.ISysPostService;
import vn.com.irtech.irbot.web.constant.LogTitleConstant;

/**
 * Post controller
 * 
 * @author admin
 */
@Controller
@RequestMapping("/system/post")
public class SysPostController extends BaseController {
	private String prefix = "system/post";

	@Autowired
	private ISysPostService postService;

	@RequiresPermissions("system:post:view")
	@GetMapping()
	public String operlog() {
		return prefix + "/post";
	}

	@RequiresPermissions("system:post:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysPost post) {
		startPage();
		List<SysPost> list = postService.selectPostList(post);
		return getDataTable(list);
	}

	@Log(title = LogTitleConstant.SYS_POST, businessType = BusinessType.EXPORT)
	@RequiresPermissions("system:post:export")
	@PostMapping("/export")
	@ResponseBody
	public AjaxResult export(SysPost post) {
		List<SysPost> list = postService.selectPostList(post);
		ExcelUtil<SysPost> util = new ExcelUtil<SysPost>(SysPost.class);
		return util.exportExcel(list, "Post data");
	}

	@RequiresPermissions("system:post:remove")
	@Log(title = LogTitleConstant.SYS_POST, businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		try {
			return toAjax(postService.deletePostByIds(ids));
		} catch (Exception e) {
			return error(e.getMessage());
		}
	}

	/**
	 * New post
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * Add save new post
	 */
	@RequiresPermissions("system:post:add")
	@Log(title = LogTitleConstant.SYS_POST, businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated SysPost post) {
		if (UserConstants.POST_NAME_NOT_UNIQUE.equals(postService.checkPostNameUnique(post))) {
			return error("Thêm bài viết " + post.getPostName() + " thất bại，tên bài viết đã tồn tại!");
		} else if (UserConstants.POST_CODE_NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) {
			return error("Thêm bài viết " + post.getPostName() + " thất bại，mã bài viết đã tồn tại!");
		}
		post.setCreateBy(ShiroUtils.getLoginName());
		return toAjax(postService.insertPost(post));
	}

	/**
	 * Modify post
	 */
	@GetMapping("/edit/{postId}")
	public String edit(@PathVariable("postId") Long postId, ModelMap mmap) {
		mmap.put("post", postService.selectPostById(postId));
		return prefix + "/edit";
	}

	/**
	 * Modify save post
	 */
	@RequiresPermissions("system:post:edit")
	@Log(title = LogTitleConstant.SYS_POST, businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(@Validated SysPost post) {
		if (UserConstants.POST_NAME_NOT_UNIQUE.equals(postService.checkPostNameUnique(post))) {
			return error("Cập nhật bài viết " + post.getPostName() + " thất bại，tên bài viết đã tồn tại!");
		} else if (UserConstants.POST_CODE_NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) {
			return error("Cập nhật bài viết " + post.getPostName() + " thất bại，mã bài viết đã tồn tại!");
		}
		post.setUpdateBy(ShiroUtils.getLoginName());
		return toAjax(postService.updatePost(post));
	}

	/**
	 * Check post name is unique
	 */
	@PostMapping("/checkPostNameUnique")
	@ResponseBody
	public String checkPostNameUnique(SysPost post) {
		return postService.checkPostNameUnique(post);
	}

	/**
	 * Check post code is unique
	 */
	@PostMapping("/checkPostCodeUnique")
	@ResponseBody
	public String checkPostCodeUnique(SysPost post) {
		return postService.checkPostCodeUnique(post);
	}
}
