package vn.com.irtech.irbot.web.task;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.irtech.irbot.business.domain.Robot;
import vn.com.irtech.irbot.business.service.IRobotService;
import vn.com.irtech.irbot.business.type.RobotStatusType;

@Component("healthCheckTask")
public class HealthCheckTask {

	private static final Logger logger = LoggerFactory.getLogger(HealthCheckTask.class);

	@Autowired
	private IRobotService robotService;

	public void executeTask() {
		logger.info(">>>>>>>>>>>>>> HEALTH CHECK TASK - START!");

		try {
			// Check status robot
			List<Robot> robotList = robotService.selectRobotNotPing(3);
			if (CollectionUtils.isNotEmpty(robotList)) {
				for (Robot robot : robotList) {
					Robot robotUpdate = new Robot();
					robotUpdate.setId(robot.getId());
					robotUpdate.setStatus(RobotStatusType.OFFLINE.value());
					robotService.updateRobot(robotUpdate);
				}
			}

		} catch (Exception e) {

			logger.error(">>>>>> Error: " + e.getMessage());
		}

		logger.info(">>>>>>>>>>>>>> HEALTH CHECK TASK - FINISH!");
	}
}
