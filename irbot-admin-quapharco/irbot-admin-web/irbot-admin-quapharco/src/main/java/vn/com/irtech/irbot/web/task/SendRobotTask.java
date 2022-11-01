package vn.com.irtech.irbot.web.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import vn.com.irtech.core.common.utils.DateUtils;
import vn.com.irtech.irbot.business.domain.Robot;
import vn.com.irtech.irbot.business.domain.WorkProcess;
import vn.com.irtech.irbot.business.mapper.RobotMapper;
import vn.com.irtech.irbot.business.mapper.WorkProcessMapper;
import vn.com.irtech.irbot.business.service.IHdbhModifyService;
import vn.com.irtech.irbot.business.service.IHdbhNbModifyService;
import vn.com.irtech.irbot.business.service.IHdbhNbService;
import vn.com.irtech.irbot.business.service.IHdbhService;
import vn.com.irtech.irbot.business.type.ProcessStatus;
import vn.com.irtech.irbot.business.type.RobotServiceType;
import vn.com.irtech.irbot.business.type.RobotStatusType;

@Component("sendRobotTask")
public class SendRobotTask {

	private static final Logger logger = LoggerFactory.getLogger(SendRobotTask.class);

	@Autowired
	private RobotMapper robotMapper;

	@Autowired
	private WorkProcessMapper workProcessMapper;

	@Autowired
	private IHdbhService hdbhService;

	@Autowired
	private IHdbhNbService hdbhNbService;

	@Autowired
	private IHdbhModifyService hdbhModifyService;
	
	@Autowired
	private IHdbhNbModifyService hdbhNbModifyService;

	public void executeTask() {
		logger.info(">>>>>>>>>>>>>> SEND ROBOT TASK - START!");

		try {

			hoaDonGtgtGoc();
			xuatNoiBoGoc();
			hoaDonGtgtDieuChinh();
			xuatNoiBoDieuChinh();

		} catch (Exception e) {

			logger.error(">>>>>> Error: " + e.getMessage());
		}

		logger.info(">>>>>>>>>>>>>> SEND ROBOT TASK - FINISH!");
	}

	private void hoaDonGtgtGoc() {
		Date now = new Date();
		// Hoá đơn GTGT gốc
		WorkProcess workProcessSelect = new WorkProcess();
		workProcessSelect.setServiceId(RobotServiceType.HOA_DON_GTGT_GOC.value());
		workProcessSelect.setStatus(ProcessStatus.WAIT.value());
		workProcessSelect.getParams().put("createTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now));
		List<WorkProcess> listWorkProcessExist = workProcessMapper.selectWorkProcessList(workProcessSelect);

		if (CollectionUtils.isEmpty(listWorkProcessExist)) {
			logger.info(">>>> SEND ROBOT TASK - KHONG CO LENH HOA DON GTGT GOC");
			return;
		}

		List<Robot> robots = robotMapper.selectRobotByService(RobotServiceType.HOA_DON_GTGT_GOC.value(),
				RobotStatusType.AVAILABLE.value());

		// Case if have not any robot available
		if (CollectionUtils.isEmpty(robots)) {
			logger.info(">>> SEND ROBOT TASK - KHONG TIM THAY ROBOT KHA DUNG LAM LENH!");
			return;
		}

		Map<Robot, WorkProcess> requestMap = new HashMap<Robot, WorkProcess>();
		for (WorkProcess workProcess : listWorkProcessExist) {
			if (CollectionUtils.isEmpty(robots)) {
				break;
			}

			requestMap.put(robots.get(0), workProcess);

			robots.remove(0);
		}

		try {
			hdbhService.sendRobot(requestMap);
		} catch (Exception e) {
			logger.warn(">>> ERROR SEND ROBOT GTGT GOC - " + e.getMessage());
		}
	}

	private void xuatNoiBoGoc() {
		Date now = new Date();
		// Xuất nội bộ gốc
		WorkProcess workProcessSelect = new WorkProcess();
		workProcessSelect.setServiceId(RobotServiceType.XUAT_NOI_BO_GOC.value());
		workProcessSelect.setStatus(ProcessStatus.WAIT.value());
		workProcessSelect.getParams().put("createTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now));
		List<WorkProcess> listWorkProcessExist = workProcessMapper.selectWorkProcessList(workProcessSelect);

		if (CollectionUtils.isEmpty(listWorkProcessExist)) {
			logger.info(">>>> SEND ROBOT TASK - KHONG CO LENH XUAT NOI BO GOC");
			return;
		}

		List<Robot> robots = robotMapper.selectRobotByService(RobotServiceType.XUAT_NOI_BO_GOC.value(),
				RobotStatusType.AVAILABLE.value());

		// Case if have not any robot available
		if (CollectionUtils.isEmpty(robots)) {
			logger.info(">>> SEND ROBOT TASK - KHONG TIM THAY ROBOT KHA DUNG LAM LENH!");
			return;
		}

		Map<Robot, WorkProcess> requestMap = new HashMap<Robot, WorkProcess>();
		for (WorkProcess workProcess : listWorkProcessExist) {
			if (CollectionUtils.isEmpty(robots)) {
				break;
			}

			requestMap.put(robots.get(0), workProcess);

			robots.remove(0);
		}

		try {
			hdbhNbService.sendRobot(requestMap);
		} catch (Exception e) {
			logger.warn(">>> ERROR SEND ROBOT NOI BO GOC - " + e.getMessage());
		}
	}

	private void hoaDonGtgtDieuChinh() {
		Date now = new Date();
		// Hoá đơn GTGT dieu chinh
		WorkProcess workProcessSelect = new WorkProcess();
		workProcessSelect.setServiceId(RobotServiceType.HOA_DON_GTGT_DIEU_CHINH.value());
		workProcessSelect.setStatus(ProcessStatus.WAIT.value());
		workProcessSelect.getParams().put("createTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now));
		List<WorkProcess> listWorkProcessExist = workProcessMapper.selectWorkProcessList(workProcessSelect);

		if (CollectionUtils.isEmpty(listWorkProcessExist)) {
			logger.info(">>>> SEND ROBOT TASK - KHONG CO LENH HOA DON GTGT DIEU CHINH");
			return;
		}

		List<Robot> robots = robotMapper.selectRobotByService(RobotServiceType.HOA_DON_GTGT_DIEU_CHINH.value(),
				RobotStatusType.AVAILABLE.value());

		// Case if have not any robot available
		if (CollectionUtils.isEmpty(robots)) {
			logger.info(">>> SEND ROBOT TASK - KHONG TIM THAY ROBOT KHA DUNG LAM LENH!");
			return;
		}

		Map<Robot, WorkProcess> requestMap = new HashMap<Robot, WorkProcess>();
		for (WorkProcess workProcess : listWorkProcessExist) {
			if (CollectionUtils.isEmpty(robots)) {
				break;
			}

			requestMap.put(robots.get(0), workProcess);

			robots.remove(0);
		}

		try {
			hdbhModifyService.sendRobot(requestMap);
		} catch (Exception e) {
			logger.warn(">>> ERROR SEND ROBOT GTGT DIEU CHINH - " + e.getMessage());
		}
	}

	private void xuatNoiBoDieuChinh() {
		Date now = new Date();
		// Hoá đơn noi bo dieu chinh
		WorkProcess workProcessSelect = new WorkProcess();
		workProcessSelect.setServiceId(RobotServiceType.XUAT_NOI_BO_DIEU_CHINH.value());
		workProcessSelect.setStatus(ProcessStatus.WAIT.value());
		workProcessSelect.getParams().put("createTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now));
		List<WorkProcess> listWorkProcessExist = workProcessMapper.selectWorkProcessList(workProcessSelect);

		if (CollectionUtils.isEmpty(listWorkProcessExist)) {
			logger.info(">>>> SEND ROBOT TASK - KHONG CO LENH HOA DON NOI BO DIEU CHINH");
			return;
		}

		List<Robot> robots = robotMapper.selectRobotByService(RobotServiceType.XUAT_NOI_BO_DIEU_CHINH.value(),
				RobotStatusType.AVAILABLE.value());

		// Case if have not any robot available
		if (CollectionUtils.isEmpty(robots)) {
			logger.info(">>> SEND ROBOT TASK - KHONG TIM THAY ROBOT KHA DUNG LAM LENH!");
			return;
		}

		Map<Robot, WorkProcess> requestMap = new HashMap<Robot, WorkProcess>();
		for (WorkProcess workProcess : listWorkProcessExist) {
			if (CollectionUtils.isEmpty(robots)) {
				break;
			}

			requestMap.put(robots.get(0), workProcess);

			robots.remove(0);
		}

		try {
			hdbhNbModifyService.sendRobot(requestMap);
		} catch (Exception e) {
			logger.warn(">>> ERROR SEND ROBOT NOI BO DIEU CHINH - " + e.getMessage());
		}
	}
}
