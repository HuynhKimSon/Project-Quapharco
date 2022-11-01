package vn.com.irtech.core.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Global configuration class
 * 
 * @author admin
 */
@Component
@ConfigurationProperties(prefix = "app")
public class Global {
	/** Project name */
	private static String name;

	/** version */
	private static String version;

	/** Copyright year */
	private static String copyrightYear;

	/** Example demo switch */
	private static boolean demoEnabled;

	/** Upload path */
	private static String basePath;
	
	/** Get address switch */
	private static boolean addressEnabled;

	public static String getName() {
		return name;
	}

	public void setName(String name) {
		Global.name = name;
	}

	public static String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		Global.version = version;
	}

	public static String getCopyrightYear() {
		return copyrightYear;
	}

	public void setCopyrightYear(String copyrightYear) {
		Global.copyrightYear = copyrightYear;
	}

	public static boolean isDemoEnabled() {
		return demoEnabled;
	}

	public void setDemoEnabled(boolean demoEnabled) {
		Global.demoEnabled = demoEnabled;
	}

	public static String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		Global.basePath = basePath;
	}

	public static boolean isAddressEnabled() {
		return addressEnabled;
	}

	public void setAddressEnabled(boolean addressEnabled) {
		Global.addressEnabled = addressEnabled;
	}

	/**
	 * Get the upload path of the avatar
	 */
	public static String getAvatarPath() {
		return getBasePath() + "/avatar";
	}

	/**
	 * Get download path
	 */
	public static String getDownloadPath() {
		return getBasePath() + "/download/";
	}

	/**
	 * Get upload path
	 */
	public static String getUploadPath() {
		return getBasePath() + "/upload";
	}
}
