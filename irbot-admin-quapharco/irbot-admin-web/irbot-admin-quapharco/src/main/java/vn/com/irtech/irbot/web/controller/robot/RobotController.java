package vn.com.irtech.irbot.web.controller.robot;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.core.common.annotation.Log;
import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.enums.BusinessType;
import vn.com.irtech.core.common.page.TableDataInfo;
import vn.com.irtech.core.common.utils.poi.ExcelUtil;
import vn.com.irtech.core.framework.util.ShiroUtils;
import vn.com.irtech.irbot.business.domain.Robot;
import vn.com.irtech.irbot.business.dto.request.RobotCreateReq;
import vn.com.irtech.irbot.business.dto.request.RobotUpdateReq;
import vn.com.irtech.irbot.business.dto.response.RobotInfoRes;
import vn.com.irtech.irbot.business.service.IRobotService;

/**
 * RobotController
 * 
 * @author irtech
 * @date 2021-12-05
 */
@Controller
@RequestMapping("/business/robot")
public class RobotController extends BaseController
{
    private String prefix = "business/robot";

    @Autowired
    private IRobotService robotService;

    @RequiresPermissions("business:robot:view")
    @GetMapping()
    public String robot()
    {
        return prefix + "/robot";
    }

    /**
     * Query list Robot
     */
    @RequiresPermissions("business:robot:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Robot robot)
    {
        startPage();
        List<RobotInfoRes> list = robotService.selectRobotInfoList(robot);
        return getDataTable(list);
    }

    /**
     * Export list Robot
     */
    @RequiresPermissions("business:robot:export")
    @Log(title = "Robot", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Robot robot)
    {
        List<Robot> list = robotService.selectRobotList(robot);
        ExcelUtil<Robot> util = new ExcelUtil<Robot>(Robot.class);
        return util.exportExcel(list, "Robotdữ liệu");
    }


    /**
     * Added Robot
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * Added save Robot
     */
    @RequiresPermissions("business:robot:add")
    @Log(title = "Robot", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated @RequestBody RobotCreateReq request)
    {
        return toAjax(robotService.insertRobot(request));
    }

    /**
     * Edit Robot
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        RobotInfoRes robot = robotService.selectRobotInfoById(id);
        mmap.put("robot", robot);
        return prefix + "/edit";
    }

    /**
     * Edit and save Robot
     */
    @RequiresPermissions("business:robot:edit")
    @Log(title = "Robot", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated @RequestBody RobotUpdateReq request)
    {
        return toAjax(robotService.updateRobotInfo(request));
    }

    /**
     * Remove Robot
     */
    @RequiresPermissions("business:robot:remove")
    @Log(title = "Robot", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(robotService.deleteRobotByIds(ids));
    }
    
    @RequiresPermissions("business:robot:edit")
    @Log(title = "Robot", businessType = BusinessType.UPDATE)
    @PostMapping("/disabled/change")
	@ResponseBody
	public AjaxResult changeDisabledStatus(Robot robot) {
    	robot.setUpdateBy(ShiroUtils.getLoginName());
		return toAjax(robotService.updateRobot(robot));
	}
}
