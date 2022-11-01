package vn.com.irtech.irbot.web.controller.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import vn.com.irtech.core.common.config.Global;
import vn.com.irtech.core.common.config.ServerConfig;
import vn.com.irtech.core.common.constant.Constants;
import vn.com.irtech.core.common.domain.AjaxResult;
import vn.com.irtech.core.common.utils.StringUtils;
import vn.com.irtech.core.common.utils.file.FileUploadUtils;
import vn.com.irtech.core.common.utils.file.FileUtils;

@Controller
public class CommonController {
	private static final Logger log = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private ServerConfig serverConfig;

	@GetMapping("common/download")
	public void fileDownload(String fileName, Boolean delete, HttpServletResponse response,
			HttpServletRequest request) {
		try {
			if (!FileUtils.isValidFilename(fileName)) {
				throw new Exception(StringUtils.format("Tên file ko hợp lệ ! ", fileName));
			}
			String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
			String filePath = Global.getDownloadPath() + fileName;

			response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition",
					"attachment;fileName=" + FileUtils.setFileDownloadHeader(request, realFileName));
			FileUtils.writeBytes(filePath, response.getOutputStream());
			if (delete) {
				FileUtils.deleteFile(filePath);
			}
		} catch (Exception e) {
			log.error("Có lỗi xuất hiện, tải xuống thất bại !", e);
		}
	}

	@PostMapping("/common/upload")
	@ResponseBody
	public AjaxResult uploadFile(MultipartFile file) throws Exception {
		try {
			// Upload file path
			String filePath = Global.getUploadPath();
			// Upload and return the new file name
			String fileName = FileUploadUtils.upload(filePath, file);
			String url = serverConfig.getUrl() + fileName;
			AjaxResult ajax = AjaxResult.success();
			ajax.put("fileName", fileName);
			ajax.put("url", url);
			return ajax;
		} catch (Exception e) {
			return AjaxResult.error(e.getMessage());
		}
	}

	@GetMapping("/common/download/resource")
	public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// Local resource path
		String localPath = Global.getBasePath();
		// Database resource address
		String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
		// download name
		String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition",
				"attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
		FileUtils.writeBytes(downloadPath, response.getOutputStream());
	}
}
