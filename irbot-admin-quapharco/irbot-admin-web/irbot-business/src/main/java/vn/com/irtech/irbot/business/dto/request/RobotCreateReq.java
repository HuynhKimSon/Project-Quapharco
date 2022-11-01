package vn.com.irtech.irbot.business.dto.request;

import vn.com.irtech.irbot.business.domain.Robot;

/**
 * 
 * @author irtech
 * @date 2021-10-06
 */
public class RobotCreateReq extends Robot {
	private static final long serialVersionUID = 1L;

	/**
	 * Services that robot support
	 */
	private Long[] services;

	public Long[] getServices() {
		return services;
	}

	public void setServices(Long[] services) {
		this.services = services;
	}

}
