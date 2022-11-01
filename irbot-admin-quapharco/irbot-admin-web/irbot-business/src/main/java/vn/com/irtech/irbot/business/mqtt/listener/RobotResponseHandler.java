package vn.com.irtech.irbot.business.mqtt.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import vn.com.irtech.core.common.utils.StringUtils;
import vn.com.irtech.irbot.business.domain.Robot;
import vn.com.irtech.irbot.business.domain.RobotService;
import vn.com.irtech.irbot.business.mapper.RobotMapper;
import vn.com.irtech.irbot.business.mapper.RobotServiceMapper;
import vn.com.irtech.irbot.business.type.RobotServiceType;

@Component
public class RobotResponseHandler implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(RobotResponseHandler.class);

	@Autowired
	@Qualifier("threadPoolTaskExecutor")
	private TaskExecutor executor;

	@Autowired
	private RobotMapper robotMapper;

	@Autowired
	private RobotServiceMapper robotServiceMapper;

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					processMessage(topic, message);
				} catch (Exception e) {
					logger.error("Error while process mq message", e);
					e.printStackTrace();
				}
			}
		});
	}

	@Transactional
	private void processMessage(String topic, MqttMessage message) throws Exception {
		String messageContent = new String(message.getPayload());
		logger.info(">>>> Receive message topic: {}, content {}", topic, messageContent);
		Map<String, Object> map = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			map = new Gson().fromJson(messageContent, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		String uuid = getString(map.get("robotName"));
		if (StringUtils.isBlank(uuid)) {
			return;
		}

		String ipAddress = getString(map.get("ipAddress"));
		String status = getString(map.get("status"));

		Robot robotSelect = new Robot();
		robotSelect.setUuid(uuid);
		List<Robot> robots = robotMapper.selectRobotList(robotSelect);
		if (CollectionUtils.isEmpty(robots)) {
			// Insert new robot
			Robot robot = new Robot();
			robot.setUuid(uuid);
			robot.setDisabled("0");
			robot.setIpAddress(ipAddress);
			robot.setPingTime(new Date());
			robot.setStatus(status);
			robotMapper.insertRobot(robot);

			List<RobotService> robotServices = new ArrayList<RobotService>();

			String isGTGTGoc = getString(map.get("isGTGTGoc"));
			if ("1".equals(isGTGTGoc)) {
				RobotService robotService = new RobotService();
				robotService.setRobotId(robot.getId());
				robotService.setServiceId((long) RobotServiceType.HOA_DON_GTGT_GOC.value());
				robotServices.add(robotService);
			}

			String isGTGTDieuChinh = getString(map.get("isGTGTDieuChinh"));
			if ("1".equals(isGTGTDieuChinh)) {
				RobotService robotService = new RobotService();
				robotService.setRobotId(robot.getId());
				robotService.setServiceId((long) RobotServiceType.HOA_DON_GTGT_DIEU_CHINH.value());
				robotServices.add(robotService);
			}

			String isNoiBoGoc = getString(map.get("isNoiBoGoc"));
			if ("1".equals(isNoiBoGoc)) {
				RobotService robotService = new RobotService();
				robotService.setRobotId(robot.getId());
				robotService.setServiceId((long) RobotServiceType.XUAT_NOI_BO_GOC.value());
				robotServices.add(robotService);
			}

			String isNoiBoDieuChinh = getString(map.get("isNoiBoDieuChinh"));
			if ("1".equals(isNoiBoDieuChinh)) {
				RobotService robotService = new RobotService();
				robotService.setRobotId(robot.getId());
				robotService.setServiceId((long) RobotServiceType.XUAT_NOI_BO_DIEU_CHINH.value());
				robotServices.add(robotService);
			}

			if (CollectionUtils.isNotEmpty(robotServices)) {
				robotServiceMapper.batchRobotService(robotServices);
			}
		} else {
			// Update robot
			Robot robot = robots.get(0);

			Robot robotUpdate = new Robot();
			robotUpdate.setId(robot.getId());
			robotUpdate.setStatus(status);
			robotUpdate.setIpAddress(ipAddress);
			robotUpdate.setPingTime(new Date());
			robotMapper.updateRobot(robotUpdate);
		}
	}

	private String getString(Object obj) {
		return obj == null ? null : obj.toString();
	}

}
