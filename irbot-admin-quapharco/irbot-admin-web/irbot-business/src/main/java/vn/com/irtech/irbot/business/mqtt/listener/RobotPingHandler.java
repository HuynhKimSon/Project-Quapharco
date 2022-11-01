package vn.com.irtech.irbot.business.mqtt.listener;

import java.util.Date;
import java.util.List;

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

import vn.com.irtech.irbot.business.domain.Robot;
import vn.com.irtech.irbot.business.service.IRobotService;

@Component
public class RobotPingHandler implements IMqttMessageListener {

	private static final Logger logger = LoggerFactory.getLogger(RobotPingHandler.class);

	@Autowired
	@Qualifier("threadPoolTaskExecutor")
	private TaskExecutor executor;

	@Autowired
	private IRobotService robotService;

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
		Date now = new Date();
		String messageContent = new String(message.getPayload());
		logger.info(">>>> Receive message topic: {}, content {}", topic, messageContent);

		String robotUuid = topic.split("/")[3];

		Robot robotSelect = new Robot();
		robotSelect.setUuid(robotUuid);
		List<Robot> robotList = robotService.selectRobotList(robotSelect);
		if (CollectionUtils.isNotEmpty(robotList)) {
			for (Robot robot : robotList) {
				Robot robotUpdate = new Robot();
				robotUpdate.setId(robot.getId());
				robotUpdate.setPingTime(now);
				robotService.updateRobot(robotUpdate);
			}
		}
	}
}