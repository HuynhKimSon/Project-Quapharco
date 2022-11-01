package vn.com.irtech.irbot.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import vn.com.irtech.irbot.business.domain.Robot;

/**
 * Mapping interface Robot
 *
 * @author irtech
 * @date 2021-12-04
 */
public interface RobotMapper 
{
    /**
     * Query Robot
     *
     * @param id ID Robot
     * @return Robot
     */
    public Robot selectRobotById(Long id);

    /**
     * Query list Robot
     *
     * @param robot Robot
     * Collection @return Robot
     */
    public List<Robot> selectRobotList(Robot robot);

    /**
     * Added Robot
     *
     * @param robot Robot
     * @return result
     */
    public int insertRobot(Robot robot);

    /**
     * Update Robot
     *
     * @param robot Robot
     * @return result
     */
    public int updateRobot(Robot robot);

    /**
     * Delete Robot
     *
     * @param id ID Robot
     * @return result
     */
    public int deleteRobotById(Long id);

    /**
     * Bulk delete Robot
     *
     * @param id The ID of the data will be deleted
     * @return result
     */
    public int deleteRobotByIds(String[] ids);
    
    public List<Robot> selectRobotByService(@Param("serviceId") Integer serviceId,@Param("status")  String status);
    
    public List<Robot> selectRobotByServices(@Param("serviceIds") Integer[] serviceIds,@Param("statuses")  String[] statuses);
    
    public List<Robot> selectRobotNotPing(Integer minutesNotPing);
}
