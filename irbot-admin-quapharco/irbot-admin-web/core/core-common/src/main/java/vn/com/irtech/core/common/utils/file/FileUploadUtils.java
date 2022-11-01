package vn.com.irtech.core.common.utils.file;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import vn.com.irtech.core.common.config.Global;
import vn.com.irtech.core.common.constant.Constants;
import vn.com.irtech.core.common.exception.file.FileNameLengthLimitExceededException;
import vn.com.irtech.core.common.exception.file.FileSizeLimitExceededException;
import vn.com.irtech.core.common.exception.file.InvalidExtensionException;
import vn.com.irtech.core.common.utils.DateUtils;
import vn.com.irtech.core.common.utils.StringUtils;
import vn.com.irtech.core.common.utils.security.Md5Utils;

/**
 * File upload tools
 *
 * @author admin
 */
public class FileUploadUtils {
	/**
	 * Default size 50M
	 */
	public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

	/**
	 * Default maximum length of file name 100
	 */
	public static final int DEFAULT_FILE_NAME_LENGTH = 100;

	/**
	 * Default upload address
	 */
	private static String defaultBaseDir = Global.getBasePath();

	private static int counter = 0;

	public static void setDefaultBaseDir(String defaultBaseDir) {
		FileUploadUtils.defaultBaseDir = defaultBaseDir;
	}

	public static String getDefaultBaseDir() {
		return defaultBaseDir;
	}

	/**
	 * File upload with default configuration
	 *
	 * @param file Uploaded file
	 * @return file name
	 * @throws Exception
	 */
	public static final String upload(MultipartFile file) throws IOException {
		try {
			return upload(getDefaultBaseDir(), file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
		} catch (Exception e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	/**
	 * Upload according to file path
	 *
	 * @param baseDir Relative application base directory
	 * @param file    Uploaded file
	 * @return file name
	 * @throws IOException
	 */
	public static final String upload(String baseDir, MultipartFile file) throws IOException {
		try {
			return upload(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
		} catch (Exception e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	/**
	 * Upload according to file path
	 *
	 * @param baseDir          Relative application base directory
	 * @param file             Uploaded file
	 * @param allowedExtension Upload file type
	 * @return file name
	 * @throws IOException
	 */
	public static final String upload(String baseDir, MultipartFile file, String[] allowedExtension)
			throws IOException {
		try {
			return upload(baseDir, null, file, allowedExtension);
		} catch (Exception e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	/**
	 * File Upload
	 *
	 * @param baseDir          Relative application base directory
	 * @param fileName         file name
	 * @param file             Uploaded file
	 * @param allowedExtension Upload file type
	 * @return Return the name of the uploaded file
	 * @throws FileSizeLimitExceededException       If the maximum size is exceeded
	 * @throws FileNameLengthLimitExceededException File name is too long
	 * @throws IOException                          For example, when reading and
	 *                                              writing files are wrong
	 * @throws InvalidExtensionException            File verification exception
	 */
	public static final String upload(String baseDir, String fileName, MultipartFile file, String[] allowedExtension)
			throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
			InvalidExtensionException {
		int fileNamelength = file.getOriginalFilename().length();
		if (fileNamelength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
			throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
		}

		assertAllowed(file, allowedExtension);

		if (StringUtils.isBlank(fileName)) {
			fileName = extractFilename(file);
		}

		File desc = getAbsoluteFile(baseDir, fileName);
		file.transferTo(desc);
		String pathFileName = getPathFileName(baseDir, fileName);
		return pathFileName;
	}

	/**
	 * Encoding file name
	 */
	public static final String extractFilename(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		String extension = getExtension(file);
		fileName = DateUtils.datePath() + "/" + encodingFilename(fileName) + "." + extension;
		return fileName;
	}

	private static final File getAbsoluteFile(String uploadDir, String fileName) throws IOException {
		File desc = new File(uploadDir + File.separator + fileName);

		if (!desc.getParentFile().exists()) {
			desc.getParentFile().mkdirs();
		}
		if (!desc.exists()) {
			desc.createNewFile();
		}
		return desc;
	}

	private static final String getPathFileName(String uploadDir, String fileName) throws IOException {
		int dirLastIndex = Global.getBasePath().length() + 1;
		String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
		String pathFileName = Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
		return pathFileName;
	}

	/**
	 * Encoding file name
	 */
	private static final String encodingFilename(String fileName) {
		fileName = fileName.replace("_", " ");
		fileName = Md5Utils.hash(fileName + System.nanoTime() + counter++);
		return fileName;
	}

	/**
	 * File size check
	 *
	 * @param file Uploaded file
	 * @return
	 * @throws FileSizeLimitExceededException If the maximum size is exceeded
	 * @throws InvalidExtensionException
	 */
	public static final void assertAllowed(MultipartFile file, String[] allowedExtension)
			throws FileSizeLimitExceededException, InvalidExtensionException {
		long size = file.getSize();
		if (DEFAULT_MAX_SIZE != -1 && size > DEFAULT_MAX_SIZE) {
			throw new FileSizeLimitExceededException(DEFAULT_MAX_SIZE / 1024 / 1024);
		}

		String fileName = file.getOriginalFilename();
		String extension = getExtension(file);
		if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {
			if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION) {
				throw new InvalidExtensionException.InvalidImageExtensionException(allowedExtension, extension,
						fileName);
			} else if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION) {
				throw new InvalidExtensionException.InvalidFlashExtensionException(allowedExtension, extension,
						fileName);
			} else if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION) {
				throw new InvalidExtensionException.InvalidMediaExtensionException(allowedExtension, extension,
						fileName);
			} else if (allowedExtension == MimeTypeUtils.VIDEO_EXTENSION) {
				throw new InvalidExtensionException.InvalidVideoExtensionException(allowedExtension, extension,
						fileName);
			} else {
				throw new InvalidExtensionException(allowedExtension, extension, fileName);
			}
		}

	}

	/**
	 * Determine whether the MIME type is an allowed MIME type
	 *
	 * @param extension
	 * @param allowedExtension
	 * @return
	 */
	public static final boolean isAllowedExtension(String extension, String[] allowedExtension) {
		for (String str : allowedExtension) {
			if (str.equalsIgnoreCase(extension)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the suffix of the file name
	 *
	 * @param file Form file
	 * @return Suffix
	 */
	public static final String getExtension(MultipartFile file) {
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		if (StringUtils.isEmpty(extension)) {
			extension = MimeTypeUtils.getExtension(file.getContentType());
		}
		return extension;
	}

	/**
	 * Base dir + file path that replace resource prefix + to get absolute path
	 * 
	 * @param filePath
	 * @return
	 */
	public static final String getAbsoluteFilePath(String filePath) {
		return Global.getBasePath() + filePath.replace(Constants.RESOURCE_PREFIX, "");
	}
}
