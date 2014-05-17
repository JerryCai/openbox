package com.googlecode.openbox.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IOUtils {
	private static final Logger logger = LogManager.getLogger();
	public static final String PATH_SPLIT = System.getProperties().getProperty(
			"file.separator");

	public static InputStream getInputStreamFromString(String s) {
		return new ByteArrayInputStream(s.getBytes());
	}

	public static String getFileStringContent(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new RuntimeException("the file [" + filePath + "] can't find");
		}
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			return getStringFromStream(fis);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("the file [" + filePath + "] can't find");
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static String getStringFromStream(InputStream is) {
		if (null == is) {
			throw new RuntimeException(
					"Convert Stream to String failed as input stream is null");
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		char[] buffer = new char[2048];
		try {
			int len = -1;
			while (-1 != (len = br.read(buffer))) {
				sb.append(buffer, 0, len);
			}
		} catch (IOException e) {
			throw new RuntimeException("Convert Stream to String failed !", e);
		}
		String content = sb.toString();
		return content;
	}

	public static String getFileMD5(File file) {
		if (!file.isFile()) {
			logger.error("computer file=[" + file
					+ "]'s MD5 failed as it's not a file , return null");
			return null;
		}
		FileInputStream in = getFileInputStream(file);
		return getInputStreamMD5(in);
	}

	public static String getInputStreamMD5(InputStream in) {
		if (null == in) {
			logger.error("computer file md5 failed as its input stream is null,return null");
			return null;
		}
		MessageDigest digest = null;
		byte buffer[] = new byte[2048];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			logger.error("computer file MD5 encounter error", e);
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}

	public static Map<String, String> getDirMD5(File directory,
			boolean listChild) {
		if (!directory.isDirectory()) {
			logger.info("get directory=["
					+ directory
					+ "]'s md5 failed caused by it's not a directory,return null");
			return null;
		}
		// <filepath,md5>
		Map<String, String> map = new HashMap<String, String>();
		String md5;
		File files[] = directory.listFiles();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory() && listChild) {
				map.putAll(getDirMD5(f, listChild));
			} else {
				md5 = getFileMD5(f);
				if (md5 != null) {
					map.put(f.getPath(), md5);
				}
			}
		}
		return map;
	}

	public static boolean isSameContent(String filePath1, String filePath2) {
		File file1 = new File(filePath1);
		File file2 = new File(filePath2);
		return getFileMD5(file1).equals(getFileMD5(file2));
	}

	public static boolean isSameInputStream(InputStream stream1,
			InputStream stream2) {
		return getInputStreamMD5(stream1).equals(getInputStreamMD5(stream2));
	}

	public static void autoCreateParentDirectory(File file) {
		File parentFolder = new File(file.getParent());
		parentFolder.mkdirs();
		parentFolder.setReadable(true);
		parentFolder.setWritable(true);
	}

	public static boolean createFile(String filePath, InputStream inputStream) {
		logger.info("create local file path = [" + filePath + "]");
		boolean createSuccess = false;
		File file = new File(filePath);
		autoCreateParentDirectory(file);
		FileOutputStream fos = null;
		try {
			file.createNewFile();
			file.setReadable(true);
			file.setWritable(true);
			fos = new FileOutputStream(file);
			int buffersize = 1024 * 8;
			byte[] buffer = new byte[buffersize];
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			int len = -1;
			while (true) {
				len = bis.read(buffer);
				if (len == -1) {
					createSuccess = true;
					if (logger.isInfoEnabled()) {
						logger.info("Create local file=[" + filePath
								+ "] successfully ");
					}
					break;
				}
				fos.write(buffer, 0, len);
				fos.flush();
			}
		} catch (IOException e) {
			logger.error("Create local file=[" + filePath
					+ "]failed !!! error as:", e);
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e1) {
				logger.error("Close Stream encounter error as below:", e1);
			}
		}
		return createSuccess;
	}

	public static void writeInfoToFile(String logFilePath, String info) {
		PrintWriter PW = null;
		try {
			PW = new PrintWriter(new FileWriter(logFilePath), true);
		} catch (IOException e) {
			logger.error("local file :[" + logFilePath + "]can not be found", e);
		}
		try {
			PW.println(info);
			PW.flush();
			if (logger.isDebugEnabled()) {
				logger.debug(info);
			}
		} catch (Exception ex) {
			logger.error("Write info:[" + info + "] to file:[" + logFilePath
					+ "] failed!", ex);
		} finally {
			PW.close();
		}
	}

	public static void appendContentToFile(String filePath, String content) {
		filePath = filePath.replaceAll("\\\\", "/");
		mkdirs(filePath.substring(0, filePath.lastIndexOf("/")));
		FileWriter writer = null;
		try {
			writer = new FileWriter(filePath, true);
			writer.write(content);
			if (logger.isDebugEnabled()) {
				logger.debug("append content=[" + content + "] to file=["
						+ filePath + "] success");
			}
		} catch (IOException e) {
			logger.error("append content=[" + content + "] to file=["
					+ filePath + "] failed", e);
		} finally {
			if (null != writer) {
				try {
					writer.close();
				} catch (IOException e) {
					logger.error("writer.close() failed !!!", e);
				}
			}
		}
	}

	public static void mkdirs(String localPath) {
		File downloadFolder = new File(localPath);
		downloadFolder.mkdirs();
		downloadFolder.setReadable(true);
		downloadFolder.setWritable(true);
	}

	public static boolean copyFile(String srcPath, String destPath) {
		boolean copySuccess = false;
		FileChannel srcChannel = null;
		FileChannel destChannel = null;
		try {
			srcChannel = new FileInputStream(srcPath).getChannel();
			destChannel = new FileOutputStream(destPath).getChannel();
			destChannel.transferFrom(srcChannel, 0, srcChannel.size());
			if (logger.isInfoEnabled()) {
				logger.info("copy file from srcPath=[" + srcPath
						+ "] to destPath=[" + destPath + "] success !");
			}
			copySuccess = true;
		} catch (Exception e) {
			logger.error("copy file from srcPath=[" + srcPath
					+ "] to destPath=[" + destPath + "] failed !", e);
		} finally {
			closeFileChannel(srcChannel);
			closeFileChannel(destChannel);
		}
		return copySuccess;
	}

	public static void closeFileChannel(FileChannel fileChannel) {
		if (null != fileChannel) {
			try {
				fileChannel.close();
			} catch (IOException e) {
				logger.error("close file channel failed!!!", e);
			}
		}
	}

	public static boolean copyFolder(String srcFolderPath, String destFolderPath) {
		return copyFolder(srcFolderPath, destFolderPath, true);
	}

	public static boolean copyFolder(String srcFolderPath,
			String destFolderPath, boolean isOverwrite) {
		File srcFolder = new File(srcFolderPath);
		File destFolder = new File(destFolderPath);
		if (!srcFolder.isDirectory()) {
			if (logger.isInfoEnabled()) {
				logger.info("the srcFolderPath=[" + srcFolderPath
						+ "] is not a directory path , copy file directly");
			}
			if (destFolder.exists() && !isOverwrite) {
				if (logger.isInfoEnabled()) {
					logger.info("the copy file from ["
							+ srcFolderPath
							+ "] to ["
							+ destFolderPath
							+ "]cancel , since it has alreay existed and overwrite=["
							+ isOverwrite + "] ");
				}
				return false;
			}
			mkdirs(destFolder.getParent());
			copyFile(srcFolderPath, destFolderPath);
			return true;
		}
		if (destFolder.exists()) {
			if (isOverwrite) {
				// first step , delete the dest older files and folers
				File[] olderFiles = destFolder.listFiles();
				for (int i = 0; i < olderFiles.length; i++) {
					olderFiles[i].delete();
				}
			} else {
				if (logger.isInfoEnabled()) {
					logger.info("copy folder from["
							+ srcFolderPath
							+ "]to["
							+ srcFolderPath
							+ "]cancel since it has already existed with overwrite=["
							+ isOverwrite + "]");
				}
				return false;
			}
		} else {
			mkdirs(destFolder.getPath());
		}
		// second step , copy from src
		File[] files = srcFolder.listFiles();
		for (int i = 0; i < files.length; i++) {
			String srcSubPath = files[i].getPath();
			String destSubPath = srcSubPath.replace(srcFolderPath,
					destFolderPath);
			if (files[i].isDirectory()) {
				copyFolder(srcSubPath, destSubPath, isOverwrite);
			} else {
				copyFile(srcSubPath, destSubPath);
			}
		}
		return true;
	}

	public static void closeInputStream(InputStream instream) {
		if (instream != null) {
			try {
				instream.close();
			} catch (IOException e) {
				logger.error("close input stream failed!!!", e);
			}
		}
	}

	public static InputStream getStreamFromString(String str) {
		InputStream is = new ByteArrayInputStream(str.getBytes());
		return is;
	}

	public static String getStringFromFile(String localFilePath) {
		InputStream is = null;
		try {
			is = new FileInputStream(localFilePath);
		} catch (FileNotFoundException e) {
			logger.error("getStringFromFile from localFilePath=["
					+ localFilePath + "] failed as below:", e);
		}
		return getStringFromStream(is);
	}

	public static FileInputStream getFileInputStream(File file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			String message = "get input stream from file=[" + file
					+ "] failed!!!";
			logger.error(message, e);
			throw new RuntimeException(message, e);
		}
		return fis;
	}

	public static FileInputStream getFileInputStream(String filePath) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			logger.error("get input stream from file=[" + filePath
					+ "] failed!!!", e);
		}
		return fis;
	}

	/**
	 * Prints this property list out to the specified output stream. This method
	 * is useful for debugging.
	 * 
	 * @param out
	 *            an output stream.
	 * @throws ClassCastException
	 *             if any key in this property list is not a string.
	 */
	public static void outputProperties(Properties properties, PrintStream out) {
		out.println("# This properties is updated on "
				+ DateHelper.getTimeString());
		Enumeration<Object> keys = properties.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String val = properties.getProperty(key);
			out.println(key + "=" + val);
		}
		out.flush();
	}

	public static void outputProperties(Properties properties, String filePath) {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(URLDecoder.decode(filePath,
					"UTF-8"));
			boolean autoFlush = true;
			PrintStream out = new PrintStream(fileOutputStream, autoFlush);
			outputProperties(properties, out);
			out.close();
		} catch (FileNotFoundException e) {
			String message = "save properties to [" + filePath
					+ "] failed, file can't find!";
			logger.error(message, e);
			throw new RuntimeException(message, e);
		} catch (UnsupportedEncodingException e) {
			String message = "save properties to [" + filePath
					+ "] failed with UnsupportedEncodingException";
			logger.error(message, e);
			throw new RuntimeException(message, e);
		} catch (Exception e) {
			String message = "save properties to [" + filePath
					+ "] failed with unknow error";
			logger.error(message, e);
			throw new RuntimeException(message, e);
		} finally {
			if (null != fileOutputStream) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					// ignore this error
				}
			}
		}
	}

	public static InputStream getInputStreamByProjectRelativePath(
			String projectRelativePath) {
		return IOUtils.class.getClassLoader().getResourceAsStream(
				projectRelativePath);
	}

	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
			if (logger.isInfoEnabled())
				logger.info("delete file [" + filePath + "] success");
		}

	}
}
