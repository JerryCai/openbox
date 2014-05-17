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

}
