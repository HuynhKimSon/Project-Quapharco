package vn.com.irtech.irbot.business.service;

import java.util.List;

import vn.com.irtech.irbot.business.domain.Robot;
import vn.com.irtech.irbot.business.dto.request.RobotCreateReq;
import vn.com.irtech.irbot.business.dto.request.RobotUpdateReq;
import vn.com.irtech.irbot.business.dto.response.RobotInfoRes;

/**
 * Service interface Robot
 *
 * @author irtech
 * @date 2021-12-04
 */
public interface IRobotService 
{

    /**
     * Query Robot
     *
     * @param id ID Robot
     * @return Robot
     */
    public Robot selectRobotById(Long id);
    
    /**
     * Query Robot
     *
     * @param id ID Robot
     * @return Robot
     */
    public RobotInfoRes selectRobotInfoById(Long id);

    /**
     * Query list Robot
     *
     * @param robot Robot
     * Collection @return Robot
     */
    public List<Robot> selectRobotList(Robot robot);
    
    /**
     * Query list Robot
     *
     * @param robot Robot
     * Collection @return Robot
     */
    public List<RobotInfoRes> selectRobotInfoList(Robot robot);

    /**
     * Added Robot
     *
     * @param robot Robot
     * @return result
     */
    public int insertRobot(RobotCreateReq request);
    
    /**
     * Update Robot
     *
     * @param robot Robot
     * @return result
     */
    public int updateRobot(Robot robot);

    /**
     * Update Robot
     *
     * @param robot Robot
     * @return result
     */
    public int updateRobotInfo(RobotUpdateReq request);

    /**
     * Xóa hàng loạt Robot
     *
     * @param id ID của dữ liệu sẽ bị xóa
     * @return result
     */
    public int deleteRobotByIds(String ids);

    /**
     * Delete information Robot
     *
     * @param id ID Robot
     * @return result
     */
    public int deleteRobotById(Long id);
    
    public List<Robot> selectRobotNotPing(Integer minutesNotPing);
}
