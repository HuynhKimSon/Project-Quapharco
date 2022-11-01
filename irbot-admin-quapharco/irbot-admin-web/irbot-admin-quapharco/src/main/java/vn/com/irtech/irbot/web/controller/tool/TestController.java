package vn.com.irtech.irbot.web.controller.tool;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.utils.StringUtils;

/**
 * swagger 用户测试方法
 * 
 * @author ruoyi
 */
@Api("Quản lý thông tin người dùng")
@RestController
@RequestMapping("/test/user")
public class TestController extends BaseController {
	private final static Map<Integer, UserEntity> users = new LinkedHashMap<Integer, UserEntity>();
	{
		users.put(1, new UserEntity(1, "admin", "admin123", "15888888888"));
		users.put(2, new UserEntity(2, "ry", "admin123", "15666666666"));
	}

	@ApiOperation("lấy danh sách người dùng")
	@GetMapping("/list")
	public AjaxResult userList() {
		List<UserEntity> userList = new ArrayList<UserEntity>(users.values());
		return AjaxResult.success(userList);
	}

	@ApiOperation("Lấy thông tin chi tiết về người dùng")
	@ApiImplicitParam(name = "userId", value = "ID người dùng", required = true, dataType = "int", paramType = "path")
	@GetMapping("/{userId}")
	public AjaxResult getUser(@PathVariable Integer userId) {
		if (!users.isEmpty() && users.containsKey(userId)) {
			return AjaxResult.success(users.get(userId));
		} else {
			return error("Người dùng không tồn tại");
		}
	}

	@ApiOperation("Tạo người dùng mới")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "ID người dùng", dataType = "Integer"),
			@ApiImplicitParam(name = "username", value = "Tên đăng nhập", dataType = "String"),
			@ApiImplicitParam(name = "password", value = "Mật khẩu", dataType = "String"),
			@ApiImplicitParam(name = "mobile", value = "Điện thoại", dataType = "String") })
	@PostMapping("/save")
	public AjaxResult save(UserEntity user) {
		if (StringUtils.isNull(user) || StringUtils.isNull(user.getUserId())) {
			return error("ID người dùng không được để trống");
		}
		return AjaxResult.success(users.put(user.getUserId(), user));
	}

	@ApiOperation("Cập nhật người dùng")
	@PutMapping("/update")
	public AjaxResult update(@RequestBody UserEntity user) {
		if (StringUtils.isNull(user) || StringUtils.isNull(user.getUserId())) {
			return error("ID người dùng không được để trống");
		}
		if (users.isEmpty() || !users.containsKey(user.getUserId())) {
			return error("người dùng không tồn tại");
		}
		users.remove(user.getUserId());
		return AjaxResult.success(users.put(user.getUserId(), user));
	}

	@ApiOperation("Xóa thông tin người dùng")
	@ApiImplicitParam(name = "userId", value = "Tên người dùng", required = true, dataType = "int", paramType = "path")
	@DeleteMapping("/{userId}")
	public AjaxResult delete(@PathVariable Integer userId) {
		if (!users.isEmpty() && users.containsKey(userId)) {
			users.remove(userId);
			return success();
		} else {
			return error("người dùng không tồn tại");
		}
	}
}

@ApiModel(value = "UserEntity", description = "Thực thể người dùng")
class UserEntity {
	@ApiModelProperty("user ID")
	private Integer userId;

	@ApiModelProperty("Username")
	private String username;

	@ApiModelProperty("Password")
	private String password;

	@ApiModelProperty("Mobile")
	private String mobile;

	public UserEntity() {

	}

	public UserEntity(Integer userId, String username, String password, String mobile) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.mobile = mobile;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
