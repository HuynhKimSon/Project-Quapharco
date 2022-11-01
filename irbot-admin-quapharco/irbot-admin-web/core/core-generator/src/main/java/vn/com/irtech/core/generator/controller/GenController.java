package vn.com.irtech.core.generator.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import vn.com.irtech.core.common.annotation.Log;
import vn.com.irtech.core.common.controller.BaseController;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.domain.CxSelect;
import vn.com.irtech.core.common.enums.BusinessType;
import vn.com.irtech.core.common.page.TableDataInfo;
import vn.com.irtech.core.common.text.Convert;
import vn.com.irtech.core.common.utils.StringUtils;
import vn.com.irtech.core.common.utils.security.PermissionUtils;
import vn.com.irtech.core.generator.domain.GenTable;
import vn.com.irtech.core.generator.domain.GenTableColumn;
import vn.com.irtech.core.generator.service.IGenTableColumnService;
import vn.com.irtech.core.generator.service.IGenTableService;

/**
 * Xử lý hoạt động tạo mã
 * 
 * @author admin
 */
@Controller
@RequestMapping("/tool/gen")
public class GenController extends BaseController {
	private String prefix = "tool/gen";

	@Autowired
	private IGenTableService genTableService;

	@Autowired
	private IGenTableColumnService genTableColumnService;

	@RequiresPermissions("tool:gen:view")
	@GetMapping()
	public String gen() {
		return prefix + "/gen";
	}

	/**
	 * Danh sách tạo mã truy vấn
	 */
	@RequiresPermissions("tool:gen:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo genList(GenTable genTable) {
		startPage();
		List<GenTable> list = genTableService.selectGenTableList(genTable);
		return getDataTable(list);
	}

	/**
	 * Danh sách cơ sở dữ liệu truy vấn
	 */
	@RequiresPermissions("tool:gen:list")
	@PostMapping("/db/list")
	@ResponseBody
	public TableDataInfo dataList(GenTable genTable) {
		startPage();
		List<GenTable> list = genTableService.selectDbTableList(genTable);
		return getDataTable(list);
	}

	/**
	 * Truy vấn danh sách các trường của bảng dữ liệu
	 */
	@RequiresPermissions("tool:gen:list")
	@PostMapping("/column/list")
	@ResponseBody
	public TableDataInfo columnList(GenTableColumn genTableColumn) {
		TableDataInfo dataInfo = new TableDataInfo();
		List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(genTableColumn);
		dataInfo.setRows(list);
		dataInfo.setTotal(list.size());
		return dataInfo;
	}

	/**
	 * Nhập cấu trúc bảng
	 */
	@RequiresPermissions("tool:gen:list")
	@GetMapping("/importTable")
	public String importTable() {
		return prefix + "/importTable";
	}

	/**
	 * Nhập cấu trúc bảng (lưu）
	 */
	@RequiresPermissions("tool:gen:list")
	@Log(title = "Tạo mã", businessType = BusinessType.IMPORT)
	@PostMapping("/importTable")
	@ResponseBody
	public AjaxResult importTableSave(String tables) {
		String[] tableNames = Convert.toStrArray(tables);
		// Thông tin biểu mẫu truy vấn
		List<GenTable> tableList = genTableService.selectDbTableListByNames(tableNames);
		String operName = (String) PermissionUtils.getPrincipalProperty("loginName");
		genTableService.importGenTable(tableList, operName);
		return AjaxResult.success();
	}

	/**
	 * Sửa đổi nghiệp vụ tạo mã
	 */
	@GetMapping("/edit/{tableId}")
	public String edit(@PathVariable("tableId") Long tableId, ModelMap mmap) {
		GenTable table = genTableService.selectGenTableById(tableId);
		List<GenTable> genTables = genTableService.selectGenTableAll();
		List<CxSelect> cxSelect = new ArrayList<CxSelect>();
		for (GenTable genTable : genTables) {
			if (!StringUtils.equals(table.getTableName(), genTable.getTableName())) {
				CxSelect cxTable = new CxSelect(genTable.getTableName(),
						genTable.getTableName() + '：' + genTable.getTableComment());
				List<CxSelect> cxColumns = new ArrayList<CxSelect>();
				for (GenTableColumn tableColumn : genTable.getColumns()) {
					cxColumns.add(new CxSelect(tableColumn.getColumnName(),
							tableColumn.getColumnName() + '：' + tableColumn.getColumnComment()));
				}
				cxTable.setS(cxColumns);
				cxSelect.add(cxTable);
			}
		}
		mmap.put("table", table);
		mmap.put("data", JSON.toJSON(cxSelect));
		return prefix + "/edit";
	}

	/**
	 * Sửa đổi và lưu nghiệp vụ tạo mã
	 */
	@RequiresPermissions("tool:gen:edit")
	@Log(title = "Tạo mã", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(@Validated GenTable genTable) {
		genTableService.validateEdit(genTable);
		genTableService.updateGenTable(genTable);
		return AjaxResult.success();
	}

	@RequiresPermissions("tool:gen:remove")
	@Log(title = "Tạo mã", businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		genTableService.deleteGenTableByIds(ids);
		return AjaxResult.success();
	}

	/**
	 * Xem trước mã
	 */
	@RequiresPermissions("tool:gen:preview")
	@GetMapping("/preview/{tableId}")
	@ResponseBody
	public AjaxResult preview(@PathVariable("tableId") Long tableId) throws IOException {
		Map<String, String> dataMap = genTableService.previewCode(tableId);
		return AjaxResult.success(dataMap);
	}

	/**
	 * Tạo mã (phương pháp tải xuống)
	 */
	@RequiresPermissions("tool:gen:code")
	@Log(title = "Tạo mã", businessType = BusinessType.GENCODE)
	@GetMapping("/download/{tableName}")
	public void download(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException {
		byte[] data = genTableService.downloadCode(tableName);
		genCode(response, data);
	}

	/**
	 * Tạo mã (đường dẫn tùy chỉnh)
	 */
	@RequiresPermissions("tool:gen:code")
	@Log(title = "Tạo mã", businessType = BusinessType.GENCODE)
	@GetMapping("/genCode/{tableName}")
	@ResponseBody
	public AjaxResult genCode(@PathVariable("tableName") String tableName) {
		genTableService.generatorCode(tableName);
		return AjaxResult.success();
	}

	/**
	 * Đồng bộ hóa cơ sở dữ liệu
	 */
	@RequiresPermissions("tool:gen:edit")
	@Log(title = "Tạo mã", businessType = BusinessType.UPDATE)
	@GetMapping("/synchDb/{tableName}")
	@ResponseBody
	public AjaxResult synchDb(@PathVariable("tableName") String tableName) {
		genTableService.synchDb(tableName);
		return AjaxResult.success();
	}

	/**
	 * Tạo mã hàng loạt
	 */
	@RequiresPermissions("tool:gen:code")
	@Log(title = "Tạo mã", businessType = BusinessType.GENCODE)
	@GetMapping("/batchGenCode")
	@ResponseBody
	public void batchGenCode(HttpServletResponse response, String tables) throws IOException {
		String[] tableNames = Convert.toStrArray(tables);
		byte[] data = genTableService.downloadCode(tableNames);
		genCode(response, data);
	}

	/**
	 * Tạo tệp zip
	 */
	private void genCode(HttpServletResponse response, byte[] data) throws IOException {
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\"ruoyi.zip\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream; charset=UTF-8");
		IOUtils.write(data, response.getOutputStream());
	}
}