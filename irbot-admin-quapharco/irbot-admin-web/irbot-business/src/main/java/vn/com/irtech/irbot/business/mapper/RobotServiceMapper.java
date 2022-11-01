package vn.com.irtech.irbot.business.mapper;

import java.util.List;

import vn.com.irtech.irbot.business.domain.RobotService;
import vn.com.irtech.irbot.business.domain.Service;

public interface RobotServiceMapper {

	public int deleteRobotServiceByRobotId(Long robotId);

	public int deleteRobotService(String[] ids);

	public int selectCountRobotServiceByServiceId(Long serviceId);

	public int batchRobotService(List<RobotService> robotServiceList);
	
	public List<Service> selectServicesByRobotId(Long robotId);
}
