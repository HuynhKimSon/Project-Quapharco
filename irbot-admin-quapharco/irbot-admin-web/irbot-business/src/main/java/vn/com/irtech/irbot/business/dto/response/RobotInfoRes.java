package vn.com.irtech.irbot.business.dto.response;

import java.util.List;

import vn.com.irtech.core.common.utils.bean.BeanUtils;
import vn.com.irtech.irbot.business.domain.Robot;
import vn.com.irtech.irbot.business.domain.Service;

public class RobotInfoRes extends Robot {

	private static final long serialVersionUID = 1L;
	
	public RobotInfoRes() {
	}
	
	public RobotInfoRes(Robot robot) {
		BeanUtils.copyBeanProp(this, robot);
	}

	private List<Service> services;

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

}
