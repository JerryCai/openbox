package com.google.test.openbox.common;

import java.io.File;
import java.util.List;

import com.google.test.openbox.common.DirectoryWalker.Action;

public class DirectoryUtils {

	public static List<String> listFileNames(String startPath) {
		return listFileNames(startPath, null);
	}

	public static List<String> listFileNames(String startPath,
			final String fileNameRegex) {
		DirectoryWalker<String> walker = new DirectoryWalker<String>();
		walker.walk(new File(startPath), new Action<String>() {

			@Override
			public String doAction(File file) {
				if (file.isDirectory()) {
					return null;
				}
				String fileName = file.getName();
				if (null == fileNameRegex || fileNameRegex.trim().equals("")) {
					return fileName;
				}
				if (PatternUtils.isMattch(fileName, fileNameRegex)) {
					return fileName;
				}
				return null;
			}

		});
		return walker.getResults();
	}

	public static List<String> listFileNames(String startPath,
			final String prefix, final String postfix) {
		DirectoryWalker<String> walker = new DirectoryWalker<String>();
		walker.walk(new File(startPath), new Action<String>() {

			@Override
			public String doAction(File file) {
				if (file.isDirectory()) {
					return null;
				}
				String fileName = file.getName();
				if (null == prefix && null == postfix) {
					return fileName;
				}
				if (null != prefix) {
					if (!fileName.startsWith(prefix)) {
						return null;
					}
				}
				if (null != postfix) {
					if (!fileName.endsWith(postfix)) {
						return null;
					}
				}
				return fileName;
			}
		});
		return walker.getResults();
	}

	public static List<String> listCuttedFileNames(String startPath,
			final String prefix, final String postfix) {
		DirectoryWalker<String> walker = new DirectoryWalker<String>();
		walker.walk(new File(startPath), new Action<String>() {

			@Override
			public String doAction(File file) {
				if (file.isDirectory()) {
					return null;
				}
				String fileName = file.getName();
				if (null == prefix && null == postfix) {
					return fileName;
				}
				if (null != prefix) {
					if (!fileName.startsWith(prefix)) {
						return null;
					}
				}
				if (null != postfix) {
					if (!fileName.endsWith(postfix)) {
						return null;
					}
				}
				return fileName.replaceFirst(prefix, "")
						.replaceAll(postfix, "");
			}
		});
		return walker.getResults();
	}
}
