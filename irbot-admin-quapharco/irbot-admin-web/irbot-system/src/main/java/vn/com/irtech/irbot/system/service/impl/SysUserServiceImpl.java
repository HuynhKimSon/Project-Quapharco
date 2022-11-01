package vn.com.irtech.irbot.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.irtech.core.common.annotation.DataScope;
import vn.com.irtech.core.common.constant.UserConstants;
import vn.com.irtech.core.common.exception.BusinessException;
import vn.com.irtech.core.common.text.Convert;
import vn.com.irtech.core.common.utils.StringUtils;
import vn.com.irtech.core.common.utils.bean.BeanUtils;
import vn.com.irtech.core.common.utils.security.Md5Utils;
import vn.com.irtech.core.system.service.ISysConfigService;
import vn.com.irtech.irbot.system.domain.Employee;
import vn.com.irtech.irbot.system.domain.SysDept;
import vn.com.irtech.irbot.system.domain.SysPost;
import vn.com.irtech.irbot.system.domain.SysRole;
import vn.com.irtech.irbot.system.domain.SysUser;
import vn.com.irtech.irbot.system.domain.SysUserPost;
import vn.com.irtech.irbot.system.domain.SysUserRole;
import vn.com.irtech.irbot.system.domain.UserDto;
import vn.com.irtech.irbot.system.mapper.SysPostMapper;
import vn.com.irtech.irbot.system.mapper.SysRoleMapper;
import vn.com.irtech.irbot.system.mapper.SysUserMapper;
import vn.com.irtech.irbot.system.mapper.SysUserPostMapper;
import vn.com.irtech.irbot.system.mapper.SysUserRoleMapper;
import vn.com.irtech.irbot.system.service.ISysDeptService;
import vn.com.irtech.irbot.system.service.ISysUserService;

/**
 * 
 * @author admin
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
	private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

	@Autowired
	private SysUserMapper userMapper;

	@Autowired
	private SysRoleMapper roleMapper;

	@Autowired
	private SysPostMapper postMapper;

	@Autowired
	private SysUserPostMapper userPostMapper;

	@Autowired
	private SysUserRoleMapper userRoleMapper;

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private ISysDeptService deptService;

	@Override
	@DataScope(deptAlias = "d", userAlias = "u")
	public List<SysUser> selectUserList(SysUser user) {
		return userMapper.selectUserList(user);
	}

	@DataScope(deptAlias = "d", userAlias = "u")
	public List<SysUser> selectAllocatedList(SysUser user) {
		return userMapper.selectAllocatedList(user);
	}

	@DataScope(deptAlias = "d", userAlias = "u")
	public List<SysUser> selectUnallocatedList(SysUser user) {
		return userMapper.selectUnallocatedList(user);
	}

	@Override
	public SysUser selectUserByLoginName(String userName) {
		return userMapper.selectUserByLoginName(userName);
	}

	@Override
	public SysUser selectUserByPhoneNumber(String phoneNumber) {
		return userMapper.selectUserByPhoneNumber(phoneNumber);
	}

	@Override
	public SysUser selectUserByEmail(String email) {
		return userMapper.selectUserByEmail(email);
	}

	@Override
	public SysUser selectUserById(Long userId) {
		return userMapper.selectUserById(userId);
	}

	@Override
	public SysUser selectUserByUserCode(String userCode) {
		return userMapper.selectUserByUserCode(userCode);
	}

	public List<SysUserRole> selectUserRoleByUserId(Long userId) {
		return userRoleMapper.selectUserRoleByUserId(userId);
	}

	@Override
	public int deleteUserById(Long userId) {
		// 删除用户与角色关联
		userRoleMapper.deleteUserRoleByUserId(userId);
		// 删除用户与岗位表
		userPostMapper.deleteUserPostByUserId(userId);
		return userMapper.deleteUserById(userId);
	}

	@Override
	public int deleteUserByIds(String ids) throws BusinessException {
		Long[] userIds = Convert.toLongArray(ids);
		for (Long userId : userIds) {
			checkUserAllowed(new SysUser(userId));
		}
		return userMapper.deleteUserByIds(userIds);
	}

	@Override
	@Transactional
	public int insertUser(SysUser user) {
		int rows = userMapper.insertUser(user);
		insertUserPost(user);
		insertUserRole(user.getUserId(), user.getRoleIds());
		return rows;
	}

	public boolean registerUser(SysUser user) {
		user.setUserType(UserConstants.REGISTER_USER_TYPE);
		return userMapper.insertUser(user) > 0;
	}

	@Override
	@Transactional
	public int updateUser(SysUser user) {
		System.out.println("user: " + user);
		Long userId = user.getUserId();
		userRoleMapper.deleteUserRoleByUserId(userId);
		insertUserRole(user.getUserId(), user.getRoleIds());
		userPostMapper.deleteUserPostByUserId(userId);
		insertUserPost(user);
		return userMapper.updateUser(user);
	}

	@Override
	public int updateUserInfo(SysUser user) {
		return userMapper.updateUser(user);
	}

	public void insertUserAuth(Long userId, Long[] roleIds) {
		userRoleMapper.deleteUserRoleByUserId(userId);
		insertUserRole(userId, roleIds);
	}

	@Override
	public int resetUserPwd(SysUser user) {
		return updateUserInfo(user);
	}

	public void insertUserRole(Long userId, Long[] roleIds) {
		if (StringUtils.isNotNull(roleIds)) {
			// 新增用户与角色管理
			List<SysUserRole> list = new ArrayList<SysUserRole>();
			for (Long roleId : roleIds) {
				SysUserRole ur = new SysUserRole();
				ur.setUserId(userId);
				ur.setRoleId(roleId);
				list.add(ur);
			}
			if (list.size() > 0) {
				userRoleMapper.batchUserRole(list);
			}
		}
	}

	public void insertUserPost(SysUser user) {
		Long[] posts = user.getPostIds();
		if (StringUtils.isNotNull(posts)) {
			// 新增用户与岗位管理
			List<SysUserPost> list = new ArrayList<SysUserPost>();
			for (Long postId : posts) {
				SysUserPost up = new SysUserPost();
				up.setUserId(user.getUserId());
				up.setPostId(postId);
				list.add(up);
			}
			if (list.size() > 0) {
				userPostMapper.batchUserPost(list);
			}
		}
	}

	@Override
	public String checkLoginNameUnique(String loginName) {
		int count = userMapper.checkLoginNameUnique(loginName);
		if (count > 0) {
			return UserConstants.USER_NAME_NOT_UNIQUE;
		}
		return UserConstants.USER_NAME_UNIQUE;
	}

	@Override
	public String checkLoginNameUnique(SysUser sysUser) {
		Long userId = StringUtils.isNull(sysUser.getUserId()) ? -1L : sysUser.getUserId();
		SysUser selectUserByLoginName = userMapper.selectUserByLoginName(sysUser.getLoginName());
		if (StringUtils.isNotNull(selectUserByLoginName)
				&& selectUserByLoginName.getUserId().longValue() != userId.longValue()) {
			return UserConstants.USER_NAME_NOT_UNIQUE;
		}
		return UserConstants.USER_NAME_UNIQUE;
	}

	@Override
	public String checkPhoneUnique(SysUser user) {
		if (StringUtils.isEmpty(user.getPhonenumber())) {
			return UserConstants.USER_PHONE_NOT_UNIQUE;
		}
		Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
		SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
		if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
			return UserConstants.USER_PHONE_NOT_UNIQUE;
		}
		return UserConstants.USER_PHONE_UNIQUE;
	}

	@Override
	public String checkEmailUnique(SysUser user) {
		if (StringUtils.isEmpty(user.getEmail())) {
			return UserConstants.USER_EMAIL_NOT_UNIQUE;
		}
		Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
		SysUser info = userMapper.checkEmailUnique(user.getEmail());
		if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
			return UserConstants.USER_EMAIL_NOT_UNIQUE;
		}
		return UserConstants.USER_EMAIL_UNIQUE;
	}

	public void checkUserAllowed(SysUser user) {
		if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin()) {
			throw new BusinessException("Không được chỉnh sửa thông tin quản trị viên");
		}
	}

	@Override
	public String selectUserRoleGroup(Long userId) {
		List<SysRole> list = roleMapper.selectRolesByUserId(userId);
		StringBuffer idsStr = new StringBuffer();
		for (SysRole role : list) {
			idsStr.append(role.getRoleName()).append(",");
		}
		if (StringUtils.isNotEmpty(idsStr.toString())) {
			return idsStr.substring(0, idsStr.length() - 1);
		}
		return idsStr.toString();
	}

	@Override
	public String selectUserPostGroup(Long userId) {
		List<SysPost> list = postMapper.selectPostsByUserId(userId);
		StringBuffer idsStr = new StringBuffer();
		for (SysPost post : list) {
			idsStr.append(post.getPostName()).append(",");
		}
		if (StringUtils.isNotEmpty(idsStr.toString())) {
			return idsStr.substring(0, idsStr.length() - 1);
		}
		return idsStr.toString();
	}

	@Override
	public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
		if (StringUtils.isNull(userList) || userList.size() == 0) {
			throw new BusinessException("Import user data cannot be empty!");
		}
		int successNum = 0;
		int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		String password = configService.selectConfigByKey("sys.user.initPassword");
		for (SysUser user : userList) {
			try {
				// 验证是否存在这个用户
				SysUser u = userMapper.selectUserByLoginName(user.getLoginName());
				if (StringUtils.isNull(u)) {
					user.setPassword(Md5Utils.hash(user.getLoginName() + password));
					user.setCreateBy(operName);
					this.insertUser(user);
					successNum++;
					successMsg.append("<br/>" + successNum + ", Import " + user.getLoginName() + " success");
				} else if (isUpdateSupport) {
					user.setUpdateBy(operName);
					this.updateUser(user);
					successNum++;
					successMsg.append("<br/>" + successNum + ", Update " + user.getLoginName() + "  success");
				} else {
					failureNum++;
					failureMsg.append("<br/>" + failureNum + ", User " + user.getLoginName() + " exist");
				}
			} catch (Exception e) {
				failureNum++;
				String msg = "<br/>" + failureNum + ", Import  " + user.getLoginName() + " failed";
				failureMsg.append(msg + e.getMessage());
				log.error(msg, e);
			}
		}
		if (failureNum > 0) {
			failureMsg.insert(0, "Sorry, the import failed! " + failureNum
					+ " records, the data format is incorrect. The errors are as follows:");
			throw new BusinessException(failureMsg.toString());
		} else {
			successMsg.insert(0, "Congratulations, all the data has been successfully imported (" + successNum
					+ " records )!, Data are as follows:");
		}
		return successMsg.toString();
	}

	@Override
	public int changeStatus(SysUser user) {
		return userMapper.updateUser(user);
	}

	@Override
	public String importUserDto(List<UserDto> userList, Boolean isUpdateSupport, String operName) {
		if (StringUtils.isNull(userList) || userList.size() == 0) {
			throw new BusinessException("Import user data cannot be empty!");
		}
		int successNum = 0;
		int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		String password = configService.selectConfigByKey("sys.user.initPassword");
		for (UserDto user : userList) {
			try {
				SysDept dept = new SysDept();
				dept.setDeptCode(user.getDeptCode());
				List<SysDept> depts = deptService.selectDeptList(dept);
				// System.out.println("depts " + depts.get(0));
				if (CollectionUtils.isEmpty(depts)) {
					failureNum++;
					String msg = "<br/>" + failureNum + ", Import user " + user.getUserCode() + " thất bại.";
					failureMsg.append(msg + "Mã phòng ban này không có trong hệ thống!");
				} else {
					// 验证是否存在这个用户
					
					// Check user status
					if(!user.getStatus().equalsIgnoreCase("đang hoạt động")){
						failureNum++;
						String msg = "<br/>" + failureNum + ", Import user " + user.getUserCode() + " thất bại.";
						failureMsg.append(msg + "Trạng thái hoạt động không hợp lệ! Trạng thái hoạt động có dạng ['Đang hoạt động' hoặc 'Ngừng hoạt động']");
					}else {
						SysUser u = userMapper.selectUserByUserCode(user.getUserCode());
						if (StringUtils.isNull(u)) {
	
							if (user.getStatus().equalsIgnoreCase("đang hoạt động")) {
								user.setStatus("0");
							} else
								user.setStatus("1");
	
							if (user.getStatusWorking().equalsIgnoreCase("đang làm việc tại bidv")) {
								user.setStatusWorking("0");
							} else
								user.setStatusWorking("1");
	
							user.setPassword(Md5Utils.hash(user.getUserCode() + password));
							user.setCreateBy(operName);
							SysUser userCopy = new SysUser();
							BeanUtils.copyBeanProp(userCopy, user);
							userCopy.setLoginName(user.getUserCode());
							userCopy.setDeptId(depts.get(0).getDeptId());
							userCopy.setPassword(Md5Utils.hash(user.getUserCode() + password));
							userCopy.setCreateBy(operName);
	
							this.insertUser(userCopy);
							successNum++;
							successMsg.append("<br/>" + successNum + ", Import user " + user.getUserCode() + " thành công");
						} else if (isUpdateSupport) {
	
							if (user.getStatus().equalsIgnoreCase("đang hoạt động")) {
								user.setStatus("0");
							} else
								user.setStatus("1");
	
							if (user.getStatusWorking().equalsIgnoreCase("đang làm việc tại bidv")) {
								user.setStatusWorking("0");
							} else
								user.setStatusWorking("1");
							user.setUpdateBy(operName);
							user.setUserId(u.getUserId());
							user.setLoginName(user.getUserCode());
							user.setDeptId(depts.get(0).getDeptId());
							user.setPassword(Md5Utils.hash(user.getUserCode() + password));
							user.setCreateBy(operName);
	
							SysUser userUpdate = new SysUser();
							BeanUtils.copyBeanProp(userUpdate, user);
	
							this.updateUser(userUpdate);
							successNum++;
							successMsg.append("<br/>" + successNum + ", Cập nhât user " + user.getUserCode() + "  thành công");
							System.out.println("M" + successMsg.toString());
						} else {
							failureNum++;
							failureMsg.append("<br/>" + failureNum + ", User " + user.getUserCode() + " đã tồn tại");
						}
					}
				}
			} catch (Exception e) {
				failureNum++;
				String msg = "<br/>" + failureNum + ", Import user " + user.getUserCode() + " thất bại";
				failureMsg.append(msg + e.getMessage());
				log.error(msg, e);
			}
		}
		if (failureNum > 0) {
			failureMsg.insert(0, "Xin lỗi, đã xảy ra lỗi trong lúc import dữ liệu!  " + failureNum
					+ " user, dữ liệu không hợp lệ. Những lỗi được thể hiện dưới đây:");
			throw new BusinessException(failureMsg.toString());
		} else {
			successMsg.insert(0, "Chúc mừng, tất cả dữ liệu đã được import thành công (" + successNum
					+ " user )!, Dữ liệu như sau:");
		}

		return successMsg.toString();
	}

	@Override
	public String importEmployee(List<Employee> employees, boolean isUpdateStatus, String operName) {
		if (StringUtils.isNull(employees) || employees.size() == 0) {
			throw new BusinessException("File rỗng!");
		}
		int successNum = 0;
		int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		// String password = configService.selectConfigByKey("sys.user.initPassword");
		for (Employee emp : employees) {
			try {
				SysDept dept = new SysDept();
				dept.setDeptCode(emp.getDeptCode());
				// System.out.println("dept " + dept);
				List<SysDept> depts = deptService.selectDeptList(dept);
				// System.out.println("depts " + depts.get(0));
				if (CollectionUtils.isEmpty(depts)) {
					failureNum++;
					String msg = "<br/>" + failureNum + ", Import cán bộ " + emp.getUserCode() + " thất bại.";
					failureMsg.append(msg + "Mã phòng ban này không có trong hệ thống!");
				} else {
					// 验证是否存在这个用户
					SysUser u = new SysUser();
					u.setUserCode(emp.getUserCode());
					List<SysUser> users = userMapper.selectUserList(u);
					if (CollectionUtils.isEmpty(users)) {
						String password = configService.selectConfigByKey("sys.user.initPassword");
						SysUser userCopy = new SysUser();
						userCopy.setUserName(emp.getUserName());
						userCopy.setUserCode(emp.getUserCode());
						userCopy.setLoginName(emp.getUserCode());
						userCopy.setDeptId(depts.get(0).getDeptId());
						userCopy.setPassword(Md5Utils.hash(emp.getUserCode() + password));
						userCopy.setCreateBy(operName);
						this.insertUser(userCopy);
						successNum++;
						successMsg.append("<br/>" + successNum + ", Import cán bộ " + emp.getUserCode() + " thành công");
					} else if (isUpdateStatus) {

						SysUser userUpdate = users.get(0);
						BeanUtils.copyBeanProp(userUpdate, emp);
						userUpdate.setDeptId(depts.get(0).getDeptId());
						this.updateUser(userUpdate);
						successNum++;
						successMsg.append("<br/>" + successNum + ", Cập nhật cán bộ " + emp.getUserCode() + " thành công");
					} else {
						failureNum++;
						failureMsg.append("<br/>" + failureNum + ", Cán bộ " + emp.getUserCode() + " đã tồn tại");
					}
				}
			} catch (Exception e) {
				failureNum++;
				String msg = "<br/>" + failureNum + ", Import cán bộ " + emp.getUserCode() + " thất bại";
				failureMsg.append(msg + e.getMessage());
				log.error(msg, e);
			}
		}
		if (failureNum > 0) {
			failureMsg.insert(0, "Xin lỗi, đã xảy ra lỗi trong lúc import dữ liệu! " + failureNum
					+ " cán bộ, dữ liệu không hợp lệ. Những lỗi được thể hiện dưới đây:");
			throw new BusinessException(failureMsg.toString());
		} else {
			successMsg.insert(0, "Chúc mừng, tất cả dữ liệu đã được import thành công (" + successNum
					+ " cán bộ )!, Dữ liệu như sau:");
		}
		return successMsg.toString();
	}

	/*
	 * Validate user data import
	 * 
	 */
	@Override
	public String validateUserData(List<UserDto> userList) {
		StringBuilder validMsg = new StringBuilder();

		for (UserDto user : userList) {
			SysDept dept = new SysDept();
			dept.setDeptCode(user.getDeptCode());
			// System.out.println("dept " + user);
			List<SysDept> depts = deptService.selectDeptList(dept);
			// System.out.println("depts " + depts.get(0));
			if (CollectionUtils.isEmpty(depts)) {
				validMsg.append("Mã phòng ban " + user.getDeptCode()
						+ " này không có trong hệ thống! Vui lòng thêm mới hoặc kiểm tra lại thông tin phòng ban này!");
			}
		}

		return validMsg.toString();
	}

	/*
	 * Validate employee data import
	 * 
	 */
	@Override
	public String validateEmployeeData(List<Employee> userList) {
		StringBuilder validMsg = new StringBuilder();

		for (Employee user : userList) {
			SysDept dept = new SysDept();
			dept.setDeptCode(user.getDeptCode());
			// System.out.println("dept " + dept);
			List<SysDept> depts = deptService.selectDeptList(dept);
			// System.out.println("depts " + depts.get(0));
			if (CollectionUtils.isEmpty(depts)) {
				validMsg.append("Mã phòng ban " + user.getDeptCode()
						+ " này không có trong hệ thống! Vui lòng thêm mới hoặc kiểm tra lại thông tin phòng ban này!");
			}
		}

		return validMsg.toString();
	}

	/**
	 * Get infor user from user id and return string url content for login with qr
	 * code
	 * 
	 * @param user
	 * @param domain
	 * @return
	 */
	@Override
	public String getUrlLoginQrcodeByUserId(SysUser user, String domain) {
		if (user == null) {
			return "";
		}
		String pathUrl = "/app/login-qr";
		String loginName = user.getLoginName();
		String password = user.getPassword();
		String salt = user.getSaltQr();
		if (StringUtils.isEmpty(salt)) {
			salt = user.getSalt();
			user.setSaltQr(salt);
			userMapper.updateUser(user);
		}
		String securityCode = new Md5Hash(loginName + password + salt).toHex();

		String param = "?loginName=" + loginName + "&securityCode=" + securityCode;
		return domain + pathUrl + param;
	}

	@Override
	public String checkUserCodeUnique(SysUser sysUser) {
		Long userId = StringUtils.isNull(sysUser.getUserId()) ? -1L : sysUser.getUserId();
		SysUser selectUserByUserCode = userMapper.checkUserCodeUnique(sysUser.getUserCode());
		if (StringUtils.isNotNull(selectUserByUserCode)
				&& selectUserByUserCode.getUserId().longValue() != userId.longValue()) {
			return UserConstants.USER_CODE_NOT_UNIQUE;
		}
		return UserConstants.USER_CODE_UNIQUE;
	}

}
