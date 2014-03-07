package com.google.test.openbox.maventools;

import com.google.test.openbox.common.ToolUtils;

public class CommandMain {
	public static void main(String... args) {

		int requiredArgs = 2;

		if (args.length < requiredArgs) {
			ToolUtils.printUsage();
			return;
		}
		String pomPath = args[0].trim();
		String jarsLocation = args[1].trim();
		boolean needDownload = false;
		try {
			if (args.length == requiredArgs + 1) {
				needDownload = Boolean.parseBoolean(args[2].trim());
			}
		} catch (Exception e) {
			ToolUtils.printUsage();
			return;
		}
		String pomFilePath = pomPath;
		String downloadLocation = jarsLocation;
		PomJarsDownloader pomDownloader = new PomJarsDownloader(pomFilePath,
				downloadLocation);
		if (needDownload) {
			pomDownloader.downloadAllJars();
		} else {
			pomDownloader.installJarsBaseOnPomOnly();
		}
	}

}
