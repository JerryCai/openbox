package com.google.test.openbox.common;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class DirectoryWalker<T> {

	private List<T> results;

	public DirectoryWalker() {
		results = new LinkedList<T>();
	}

	public List<T> getResults() {
		return results;
	}

	public void walk(File file, Action<T> action) {

		if (!file.exists()) {
			throw new RuntimeException("FILE:[" + file + "] doesn't exist ");
		}
		T echo = action.doAction(file);
		if(null != echo){
			results.add(echo);
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File childFile : files) {
				walk(childFile, action);
			}
		}
	}

	public interface Action<T> {

		public T doAction(File file);
	}

}
