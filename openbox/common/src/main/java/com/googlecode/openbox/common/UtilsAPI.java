package com.googlecode.openbox.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class UtilsAPI {

	/**
	 * find a special string whether it is in a string array
	 * 
	 * @param strs
	 * @param str
	 * @return
	 */
	public static boolean findString(String[] strs, String str) {
		boolean isFind = false;

		for (int i = 0; i < strs.length; i++) {
			if (str.equals(strs[i])) {
				isFind = true;
				break;
			}
		}
		return isFind;
	}

	public static boolean findString(List<String> strs, String str) {
		boolean isFind = false;
		for (String s : strs) {
			if (s.equals(str)) {
				isFind = true;
				break;
			}
		}
		return isFind;
	}

	/**
	 * find a special string whether it is in a string array (IgnoreCase)
	 * 
	 * @param strs
	 * @param str
	 * @return
	 */
	public static boolean findStringIgnoreCase(String[] strs, String str) {
		boolean isFind = false;

		for (int i = 0; i < strs.length; i++) {
			if (str.equalsIgnoreCase(strs[i])) {
				isFind = true;
				break;
			}
		}

		return isFind;
	}

	public static boolean findStringByFuzzyMode(String[] strs, String str) {
		boolean isFind = false;

		for (int i = 0; i < strs.length; i++) {
			if (-1 != strs[i].indexOf(str)) {
				isFind = true;
				break;
			}
		}

		return isFind;
	}

	public static String trimString(String string) {
		return string.replaceAll("\\s", "");
	}

	public static String trimWords(String content) {
		return content.replaceAll("\\s{2,}", " ");
	}

	public static String getParentPath(String path) {
		return getParentPath(path, "/");
	}

	public static String getParentPath(String path, String pathIndex) {
		if (path == null)
			return null;
		return path.substring(0, path.lastIndexOf(pathIndex));
	}

	public static String getParentPath(String path, int level) {
		String pathIndex = "/";
		return getParentPath(path, pathIndex, level);
	}

	public static String getParentPath(String path, String pathIndex, int level) {
		String[] paths = path.split(pathIndex);
		String parentPath = "";
		for (int i = 0; i <= level; i++) {
			parentPath = parentPath + paths[i] + pathIndex;
		}
		parentPath = parentPath.substring(0, parentPath.length() - 1);
		return parentPath;
	}

	public static String getLastPath(String path) {
		return getLastPath(path, "/");
	}

	public static String getLastPath(String path, String pathIndex) {
		if (path == null)
			return null;
		return path.substring(path.lastIndexOf(pathIndex) + 1);
	}

	public static String getSubPath(String path, int level) {
		return getSubPath(path, "/", level);
	}

	public static String getSubPath(String path, String pathIndex, int level) {
		String[] paths = path.split(pathIndex);
		String subPath = "";
		for (int i = level; i < paths.length; i++) {
			subPath = subPath + pathIndex + paths[i];
		}
		return subPath;
	}

	/**
	 * filter all of itmes of strArrays base on keys
	 * 
	 * @param strArrays
	 * @param keys
	 * @return
	 */
	public static String[] filterStringArray(String[] strArrays, String[] keys) {

		List<String> filtedList = new LinkedList<String>();
		for (int i = 0; i < strArrays.length; i++) {
			for (int j = 0; j < keys.length; j++) {
				if (!strArrays[i].equals(keys[j])) {
					filtedList.add(strArrays[i]);
				}
			}
		}
		String[] filtedArray = new String[filtedList.size()];
		filtedList.toArray(filtedArray);
		return filtedArray;
	}

	/**
	 * filter all of itmes of strArrays base on key
	 * 
	 * @param strArrays
	 * @param key
	 * @return
	 */
	public static String[] filterStringArray(String[] strArrays, String key) {
		List<String> filtedList = new LinkedList<String>();
		for (int i = 0; i < strArrays.length; i++) {
			if (!strArrays[i].equals(key)) {
				filtedList.add(strArrays[i]);
			}
		}
		String[] filtedArray = new String[filtedList.size()];
		filtedList.toArray(filtedArray);
		return filtedArray;
	}

	/**
	 * count the sub string existed how many times in one string
	 * 
	 * @param str
	 * @param key
	 * @return
	 */
	public static int countKeyInString(String str, String key) {
		int num = 0;
		int fromIndex = 0;
		while (fromIndex < str.length()) {
			fromIndex = str.indexOf(str, fromIndex);
			if (fromIndex != -1) {
				num++;
				fromIndex = fromIndex + key.length();
			} else {
				break;
			}
		}
		return num;
	}

	public static boolean compareStringArray(String[] str1, String[] str2) {

		int length1 = str1.length;
		int length2 = str2.length;
		if (length1 != length2) {
			return false;
		}
		// Arrays.sort(str1, new Comparator<String>(){
		// public int compare(String o1, String o2) {
		// return o1.compareTo(o2);
		// }
		// });
		Arrays.sort(str1);
		for (int i = 0; i < length1; i++) {
			if (str1[i] != str2[i]) {
				return false;
			}

		}
		return true;
	}

	/**
	 * select a sub string from str where begin with beginKey , and endwith
	 * endKey
	 * 
	 * eg: selectString("aaabbbccc","aaa","ccc") ==> "bbb"
	 * 
	 * @param str
	 * @param beginKey
	 * @param endKey
	 * @return
	 */
	public static String selectString(String str, String beginKey, String endKey) {
		int begin = str.indexOf(beginKey);
		if (-1 == begin) {
			return null;
		}
		begin = begin + beginKey.length();
		int end = str.indexOf(endKey, begin);
		if (-1 == end) {
			return null;
		}

		return str.substring(begin, end);
	}

	/**
	 * update the list's all item by String.replaceFirst(regex, replacement)
	 * 
	 * @param list
	 * @param regex
	 * @param replacement
	 * @return replaced list
	 */
	public static LinkedList<String> replaceAll(List<String> list,
			String regex, String replacement) {
		LinkedList<String> replacedList = new LinkedList<String>();
		Iterator<String> listIter = list.iterator();
		while (listIter.hasNext()) {
			String item = listIter.next();
			String replacedItem = item.replaceFirst(regex, replacement);
			replacedList.add(replacedItem);
		}
		return replacedList;
	}

	/**
	 * update the list's all item by String.replaceFirst(regex, replacement)
	 * 
	 * @param list
	 * @param regex
	 * @param replacement
	 * @return replaced list
	 */
	public static String[] replaceAll(String[] list, String regex,
			String replacement) {
		String[] replacedList = new String[list.length];

		for (int i = 0; i < list.length; i++) {
			replacedList[i] = list[i].replaceFirst(regex, replacement);
		}
		return replacedList;
	}

	/**
	 * update the key by key.replaceFirst(regex, replacement)
	 * 
	 * @param aMap
	 * @param regex
	 * @param replacement
	 */
	public static void updateAllKeys(Map<String, Object> aMap, String regex,
			String replacement) {
		Iterator<String> keys = aMap.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			String newKey = key.replaceFirst(regex, replacement);
			if (!key.equals(newKey)) {
				aMap.put(newKey, aMap.get(key));
				aMap.remove(key);
			}
		}
	}

	/**
	 * Get current called API name
	 * 
	 * @return current API name
	 */
	public static String getName() {
		// return Thread.currentThread().getStackTrace()[2].getMethodName();
		return Thread.currentThread().getStackTrace()[2].getMethodName()
				+ "_Threads_" + Thread.currentThread().getId();
	}

	/**
	 * format the time to string
	 * 
	 * @param date
	 * @param format
	 *            , such as : "yyyyMMddHHmmssSSS"
	 * @return
	 */
	public static String formatDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String currentDate = sdf.format(date);
		return currentDate;
	}

	/**
	 * format the current time to string
	 * 
	 * @param format
	 *            such as : "MM/dd/yyyy HH:mm:ss"
	 * @return
	 */
	public static String formatCurrentDate(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String currentDate = sdf.format(date);
		return currentDate;
	}

	public static String getGMTCurrentDay() {
		Date date = new Date();
		@SuppressWarnings("deprecation")
		String currentDate = date.toGMTString();
		String[] dates = currentDate.split(" ");
		return dates[0] + " " + dates[1] + " " + dates[2];
	}

	public static Date parseLinuxLogTimeToDate(String logTime) {
		// "29 Sep 2010 08:59:22" "dd MMM yyyy HH:mm:ss"
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss",
				Locale.US);
		Date date = null;
		try {
			date = sdf.parse(logTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// config.infoPrintln("Parse Linux logTime=["+logTime+"] to ["+date.toString()+"] success");
		return date;
	}

	public long parseLinuxLogTimeToLong(String logTime) {
		// "29 Sep 2010 08:59:22" "dd MMM yyyy HH:mm:ss"
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss",
				Locale.US);
		Date date = null;
		long longtime = -1L;
		try {
			date = sdf.parse(logTime);
			longtime = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// config.infoPrintln("Parse Linux logTime=["+logTime+"] to ["+longtime+"] success");
		return longtime;
	}

	public String covertDateToGMT(Date date) {
		// Thu, 30 Sep 2010 05:32:10 GMT
		SimpleDateFormat sdf = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss", Locale.US);
		return sdf.format(date) + " GMT";
	}

	public static String arrayToString(Object[] array) {
		if (array == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i].toString() + ",");
		}
		sb.append("]");
		return sb.toString();
	}

	public static int randomInt() {
		Random random = new Random();
		return Math.abs(random.nextInt());
	}

	public static long randomLong() {
		Random random = new Random();
		return Math.abs(random.nextLong());
	}

	public static String getCurrentWorkingPath() {
		return System.getProperty("user.dir");
	}

	public static String getFixLenthString(int strLength) {
		Random rm = new Random();
		double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
		String fixLenthString = String.valueOf(pross);
		return fixLenthString.substring(1, strLength + 1);
	}

	public static String getCallApiName(int level) {
		return Thread.currentThread().getStackTrace()[level].getMethodName()
				+ "_Threads_" + Thread.currentThread().getId();
	}

	public static String arrayToStr(String[] strings) {
		String str = "";
		for (String s : strings) {
			str = str + s + "   ";
		}
		return str;
	}

	public static String appendPostfix(String str, String postfix, int length) {
		StringBuffer sb = new StringBuffer();
		sb.append(str);
		for (int i = 0; i < length - str.length(); i++) {
			sb.append(postfix);
		}
		return sb.toString();
	}

	public static String[] appendPrefix(String prefix, String[] strings) {
		int length = strings.length;
		String[] s = new String[length];
		for (int i = 0; i < length; i++) {
			s[i] = prefix + strings[i];
		}
		return s;
	}

	public static String[] joinStringArray(String[] strArray1,
			String[] strArray2) {
		String[] joinedStrs = new String[strArray1.length + strArray2.length];

		int i = 0;
		for (String s : strArray1) {
			joinedStrs[i++] = s;
		}
		for (String s : strArray2) {
			joinedStrs[i++] = s;
		}
		return joinedStrs;
	}

	public static String[] mergeStringArrays(String[]... strings) {

		List<String> strs = new LinkedList<String>();
		for (String[] stringArray : strings) {
			for (String str : stringArray) {
				strs.add(str);
			}
		}
		return strs.toArray(new String[1]);
	}

	public static String[] unionStringArray(String[]... strArrays) {
		List<String> unionStringList = new LinkedList<String>();
		for (String[] stringArray : strArrays) {
			for (String str : stringArray) {
				if (!unionStringList.contains(str)) {
					unionStringList.add(str);
				}
			}
		}
		return unionStringList.toArray(new String[1]);

	}

	public static String getArrayStr(String[] strs) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i =0 ; i< strs.length ; i++) {
			sb.append(strs[i]);
			if(i != strs.length -1){
				sb.append(" ,");
			}
		}
		
		sb.append("]");
		return sb.toString();
	}

	public static String getRepeatString(String x, int num) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < num; i++) {
			sb.append(x);
		}
		return sb.toString();
	}

	public static String getFixedColumn(int fixWidth, String fullString,
			String item) {
		int appendLength = (fixWidth - item.length()) / 2;
		String prefix = UtilsAPI.getRepeatString(fullString, appendLength);
		String postfix = UtilsAPI.getRepeatString(fullString,
				fixWidth - item.length() - appendLength);
		return prefix + item + postfix + "|";
	}

	// test for each API here
	public static void main(String[] args) {
		// System.out.println(getGMTCurrentDay());
		// String path = "type:";
		// System.out.println(path.length());
		// getParentPath(path, "/", 2);
		// System.out.println("after  trim =[" + getParentPath(path, "/", 2) +
		// "]");

		String[] s1 = { "aa", "bb", "cc", };
		String[] s2 = { "as", "aa", "mm" };
		String[] s3 = { "aa", "as", "LL", "::" };
		String[] strs = UtilsAPI.unionStringArray(s1, s2, s3);
		for (String s : strs) {
			System.out.print(s + "  ,");
		}
	}
}
