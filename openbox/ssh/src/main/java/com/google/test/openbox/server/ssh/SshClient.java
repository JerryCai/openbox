package com.google.test.openbox.server.ssh;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public interface SshClient {

    String executeSingleCommand(OutputStream output, String command);
	
	void executeCommand(OutputStream output, String command);

	void executeShell(OutputStream output, String shell);

	void executeShellByFTP(OutputStream output, String shell);

	void uploadFile(OutputStream output, String fileName, String fileContent,
			String serverLocation);

	void uploadFile(OutputStream output, String fileName,
			InputStream fileContent, String serverLocation);

	void uploadFile(OutputStream output, String fileName, File file,
			String serverLocation);
	
	void downloadFile(OutputStream output, String remoteFile , String localFolder , String fileName);

	void downloadFile(OutputStream output, String remoteFile , String localFolder);

	void chmod(OutputStream output, int mode, String path);

	void chown(OutputStream output, String own, String path);

	void mv(OutputStream output, String source, String dest);

	void rm_Rf(OutputStream output, String path);

	boolean testConnection(OutputStream output);

}