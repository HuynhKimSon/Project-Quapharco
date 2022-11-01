package vn.com.irtech.api.controller;

import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import vn.com.irtech.api.common.page.TableDataInfo;
import vn.com.irtech.api.common.utils.StringUtils;

public class BaseController {


	protected void startPage(Integer pageNum, Integer pageSize, String orderBy) {
		if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
			PageHelper.startPage(pageNum, pageSize, orderBy);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected TableDataInfo getDataTable(List<?> list) {


		TableDataInfo rspData = new TableDataInfo();
		rspData.setCode(0);
		rspData.setRows(list);
		rspData.setTotal(new PageInfo(list).getTotal());
		return rspData;
	}
}
