package vn.com.irtech.core.common.utils.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.com.irtech.core.common.config.Global;
import vn.com.irtech.core.common.constant.Constants;
import vn.com.irtech.core.common.utils.StringUtils;

/**
 * Image Utils
 *
 * @author admin
 */
public class ImageUtils {
	private static final Logger log = LoggerFactory.getLogger(ImageUtils.class);

	public static byte[] getImage(String imagePath) {
		InputStream is = getFile(imagePath);
		try {
			return IOUtils.toByteArray(is);
		} catch (Exception e) {
			log.error("Image loading abnormal {}", e);
			return null;
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	public static InputStream getFile(String imagePath) {
		try {
			byte[] result = readFile(imagePath);
			result = Arrays.copyOf(result, result.length);
			return new ByteArrayInputStream(result);
		} catch (Exception e) {
			log.error("Get picture abnormal {}", e);
		}
		return null;
	}

	/**
	 * 读取文件为字节数据
	 * 
	 * @param key 地址
	 * @return 字节数据
	 */
	public static byte[] readFile(String url) {
		InputStream in = null;
		ByteArrayOutputStream baos = null;
		try {
			if (url.startsWith("http")) {
				// 网络地址
				URL urlObj = new URL(url);
				URLConnection urlConnection = urlObj.openConnection();
				urlConnection.setConnectTimeout(30 * 1000);
				urlConnection.setReadTimeout(60 * 1000);
				urlConnection.setDoInput(true);
				in = urlConnection.getInputStream();
			} else {
				// 本机地址
				String localPath = Global.getBasePath();
				String downloadPath = localPath + StringUtils.substringAfter(url, Constants.RESOURCE_PREFIX);
				in = new FileInputStream(downloadPath);
			}
			return IOUtils.toByteArray(in);
		} catch (Exception e) {
			log.error("Abnormal access to file path {}", e);
			return null;
		} finally {
			IOUtils.closeQuietly(baos);
		}
	}
}
