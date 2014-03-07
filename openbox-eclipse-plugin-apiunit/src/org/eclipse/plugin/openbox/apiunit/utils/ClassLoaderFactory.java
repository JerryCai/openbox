package org.eclipse.plugin.openbox.apiunit.utils;

import java.util.HashMap;
import java.util.Map;

public class ClassLoaderFactory {

	private static final Map<String , FileSystemClassLoader> cache = new HashMap<String,FileSystemClassLoader>();
	
	public synchronized static FileSystemClassLoader getFileSystemClassLoader(String rootDir){
		if(!cache.containsKey(rootDir)){
			cache.put(rootDir, new FileSystemClassLoader(rootDir));
		}
		return cache.get(rootDir);
	}
}
