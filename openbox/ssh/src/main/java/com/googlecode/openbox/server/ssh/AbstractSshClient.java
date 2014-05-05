package com.googlecode.openbox.server.ssh;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.openbox.common.IOUtils;

public abstract class AbstractSshClient implements SshClient {
	private static final Logger logger = LogManager.getLogger();
	
	public static final String TEMP_PATH = "/var";
	public static final String LATEEST_EXEC_SHELL_DEBUG = "/var/latest_exec_debug.sh";

	public void uploadFile(OutputStream output, String fileName, File file,
			String serverLocation) {
		logger.info("sftp upload file [" + file + "] to target location ["
				+ serverLocation + "] with file name is [" + fileName + "]");
		InputStream fileContent = null;
		try {
			if (!file.exists()) {
				logger.error("sftp upload local file ["
						+ file + "] can't find !");
			}
			fileContent = new FileInputStream(file);
			uploadFile(output, fileName, fileContent, serverLocation);
		} catch (FileNotFoundException e) {
			String message = "[GSSH-FTP] ERROR as: sftp upload local file ["
					+ file + "] can't find !";
			logger.error(message,e);
			throw new SshException(message, e);
		} catch (Exception e) {
			String message = "[GSSH-FTP] ERROR as with below errors logs:";
			throw new SshException(message, e);
		} finally {
			if (null != fileContent) {
				try {
					fileContent.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public void uploadFile(OutputStream output, String fileName,
			String fileContent, String serverLocation) {
		InputStream bis = new ByteArrayInputStream(fileContent.getBytes());
		uploadFile(output, fileName, bis, serverLocation);
		if (null != bis) {
			try {
				bis.close();
			} catch (IOException e) {
			}
		}
	}

	public void downloadFile(OutputStream output, String remoteFile,
			String localFolder) {
		File rf = new File(remoteFile);
		downloadFile(output, remoteFile, localFolder, rf.getName());
	}

	public void executeShellByFTP(OutputStream output, InputStream shell) {
		Random random = new Random();

		String shellName = "tempshell_" + System.currentTimeMillis()
				+ random.nextInt() + ".sh";
		String shellFile = TEMP_PATH + "/" + shellName;
		try {
			uploadFile(output, shellName, shell, TEMP_PATH);
			chmod(output, 777, shellFile);
			executeCommand(output, ". " + shellFile);
		} finally {
			rm_Rf(output, LATEEST_EXEC_SHELL_DEBUG);
			mv(output, shellFile, LATEEST_EXEC_SHELL_DEBUG);
		}
	}

	public void executeShellByFTP(OutputStream output, String shell) {
		Random random = new Random();
		logger.info("execute shell as : ");
		logger.info(shell);
		String shellName = "tempshell_" + System.currentTimeMillis()
				+ random.nextInt() + ".sh";

		String shellFile = TEMP_PATH + "/" + shellName;
		try {
			uploadFile(output, shellName, shell, TEMP_PATH);
			chmod(output, 777, shellFile);
			executeCommand(output, ". " + shellFile);
		} finally {
			rm_Rf(output, LATEEST_EXEC_SHELL_DEBUG);
			mv(output, shellFile, LATEEST_EXEC_SHELL_DEBUG);
		}
	}

	public void chmod(OutputStream output, int mode, String path) {
		executeCommand(output, "chmod " + mode + " " + path);
	}

	public void chown(OutputStream output, String own, String path) {
		executeCommand(output, "chown " + own + " " + path);
	}

	public void mv(OutputStream output, String source, String dest) {
		executeCommand(output, "mv " + source + " " + dest);
	}

	public void rm_Rf(OutputStream output, String path) {
		executeCommand(output, "rm -rf " + path);
	}

	public void executeCommand(OutputStream output, InputStream command) {
		String content = IOUtils.getStringFromStream(command);
		executeCommand(output, content);
	}
}
