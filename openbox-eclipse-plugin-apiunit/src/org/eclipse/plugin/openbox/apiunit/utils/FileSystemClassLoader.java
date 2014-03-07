package org.eclipse.plugin.openbox.apiunit.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileSystemClassLoader extends ClassLoader { 
	private static final Log logger = LogFactory.getLog(FileSystemClassLoader.class);
	
    private String rootDir; 

    public FileSystemClassLoader(String rootDir) { 
        this.rootDir = rootDir; 
    } 

    protected Class<?> findClass(String name) throws ClassNotFoundException { 
        byte[] classData = getClassData(name); 
        if (classData == null) { 
            throw new ClassNotFoundException(); 
        } 
        else { 
            return defineClass(name, classData, 0, classData.length); 
        } 
    } 

    private byte[] getClassData(String className) { 
        String path = classNameToPath(className); 
        InputStream ins = null;
        try { 
            ins = new FileInputStream(path); 
            ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
            int bufferSize = 4096; 
            byte[] buffer = new byte[bufferSize]; 
            int bytesNumRead = 0; 
            while ((bytesNumRead = ins.read(buffer)) != -1) { 
                baos.write(buffer, 0, bytesNumRead); 
            } 
            return baos.toByteArray(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } finally{
        	if(null != ins){
        		try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
        return null; 
    } 

    private String classNameToPath(String className) { 
        return rootDir + File.separatorChar 
                + className.replace('.', File.separatorChar) + ".class"; 
    } 
	public Class<?> loadFileClass( String className) {
		FileSystemClassLoader fsc = new FileSystemClassLoader(rootDir);
		Class<?> clss = null;
		try {
			clss = fsc.loadClass(className);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("load file class failed -->classDataRootPath=["
					+ rootDir + "],className=[" + className + "]!", e);
		}
		return clss;
	}
    
	public static Class<?> loadFileClass(String classDataRootPath, String className) {
		FileSystemClassLoader fsc = new FileSystemClassLoader(classDataRootPath);
		Class<?> clss = null;
		try {
			clss = fsc.loadClass(className);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("load file class failed -->classDataRootPath=["
					+ classDataRootPath + "],className=[" + className + "]!", e);
		}
		return clss;
	}
 }