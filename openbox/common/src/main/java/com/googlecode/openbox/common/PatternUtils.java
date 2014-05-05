package com.googlecode.openbox.common;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtils {

	public static String[] search(String source, String regex) {
		Pattern pattern = Pattern.compile(regex);
		CharSequence input = source.subSequence(0, source.length());
		Matcher matcher = pattern.matcher(input);
		List<String> resultList = new LinkedList<String>();
		while (matcher.find()) {
			resultList.add(matcher.group());
		}
		Collections.sort(resultList, String.CASE_INSENSITIVE_ORDER);
		return resultList.toArray(new String[0]);
	}
	public static String searchFist(String source, String regex) {
		Pattern pattern = Pattern.compile(regex);
		CharSequence input = source.subSequence(0, source.length());
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}
	
	public static boolean isMattch(String source, String regex) {
		Pattern pattern = Pattern.compile(regex);
		CharSequence input = source.subSequence(0, source.length());
		Matcher matcher = pattern.matcher(input);
		return matcher.find();
	}
	
	public static void main(String... args) {
		String source = "xxx\n\rbbb\n\r-rwxxx\\wbxmacc_info_11262012_1.23404.log-r--r--wbxmacc_info_11262012_0.23404.log\n\r";
		String regex = "wbxmacc_info_[0-9]+_[0-9]+.[0-9]+.log";
		String[] list = PatternUtils.search(source, regex);
		for (String s : list) {
			System.out.println(s);
		}
	}

}
