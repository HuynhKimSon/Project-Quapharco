package vn.com.irtech.core.common.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;

import vn.com.irtech.core.common.utils.StringUtils;

/**
 * File processing tools
 * 
 * @author admin
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
	public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

	/**
	 * Output byte array of specified file
	 * 
	 * @param filePath file path
	 * @param os       Output core
	 * @return
	 */
	public static void writeBytes(String filePath, OutputStream os) throws IOException {
		FileInputStream fis = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				throw new FileNotFoundException(filePath);
			}
			fis = new FileInputStream(file);
			byte[] b = new byte[1024];
			int length;
			while ((length = fis.read(b)) > 0) {
				os.write(b, 0, length);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * Delete Files
	 * 
	 * @param filePath file
	 * @return
	 */
	public static boolean deleteFile(String filePath) {
		boolean flag = false;
		File file = new File(filePath);
		// Delete if the path is a file and not empty
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * File name verification
	 * 
	 * @param filename file name
	 * @return true normal false illegal
	 */
	public static boolean isValidFilename(String filename) {
//		return filename.matches(FILENAME_PATTERN);
		return true;
	}

	public static boolean checkAllowDownload(String resource) {
		if (StringUtils.contains(resource, "..")) {
			return false;
		}

		if (ArrayUtils.contains(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, FileTypeUtils.getFileType(resource))) {
			return true;
		}

		return false;
	}

	/**
	 * Download file name re-encoding
	 * 
	 * @param request  Request object
	 * @param fileName file name
	 * @return File name after encoding
	 */
	public static String setFileDownloadHeader(HttpServletRequest request, String fileName)
			throws UnsupportedEncodingException {
		final String agent = request.getHeader("USER-AGENT");
		String filename = fileName;
		if (agent.contains("MSIE")) {
			// IE browser
			filename = URLEncoder.encode(filename, "utf-8");
			filename = filename.replace("+", " ");
		} else if (agent.contains("Firefox")) {
			// Firefox browser
			filename = new String(fileName.getBytes(), "ISO8859-1");
		} else if (agent.contains("Chrome")) {
			// google browser
			filename = URLEncoder.encode(filename, "utf-8");
		} else {
			// Other browsers
			filename = URLEncoder.encode(filename, "utf-8");
		}
		return filename;
	}

	public static void setAttachmentResponseHeader(HttpServletResponse response, String realFileName)
			throws UnsupportedEncodingException {
		String percentEncodedFileName = percentEncode(realFileName);

		StringBuilder contentDispositionValue = new StringBuilder();
		contentDispositionValue.append("attachment; filename=").append(percentEncodedFileName).append(";")
				.append("filename*=").append("utf-8''").append(percentEncodedFileName);

		response.setHeader("Content-disposition", contentDispositionValue.toString());
	}

	public static String percentEncode(String s) throws UnsupportedEncodingException {
		String encode = URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
		return encode.replaceAll("\\+", "%20");
	}
}
