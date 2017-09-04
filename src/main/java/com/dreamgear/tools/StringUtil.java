/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.dreamgear.tools;

import java.io.UnsupportedEncodingException;
import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String utilities optimized for the best performance.<br>
 * <h1>How to Use It</h1> <h2>concat() or append()</h2> If concatenating strings<br>
 * in single call, use StringUtil.concat(), otherwise use StringUtil.append()<br>
 * and its variants.<br>
 * <br>
 * <h2>Minimum Calls</h2><br>
 * Bad:
 * 
 * <pre>
 * final StringBuilder sbString = new StringBuilder();
 * StringUtil.append(sbString, &quot;text 1&quot;, String.valueOf(npcId));
 * StringUtil.append(&quot;text 2&quot;);
 * </pre>
 * 
 * Good:
 * 
 * <pre>
 * final StringBuilder sbString = new StringBuilder();
 * StringUtil.append(sbString, &quot;text 1&quot;, String.valueOf(npcId), &quot;text 2&quot;);
 * </pre>
 * 
 * Why?<br/>
 * Because the less calls you do, the less memory re-allocations have to be done<br>
 * so the whole text fits into the memory and less array copy tasks has to be<br>
 * performed. So if using less calls, less memory is used and string
 * concatenation is faster.<br>
 * <br>
 * <h2>Size Hints for Loops</h2><br>
 * Bad:
 * 
 * <pre>
 * final StringBuilder sbString = new StringBuilder();
 * StringUtil.append(sbString, &quot;header start&quot;, someText, &quot;header end&quot;);
 * for (int i = 0; i &lt; 50; i++) {
 * 	StringUtil.append(sbString, &quot;text 1&quot;, stringArray[i], &quot;text 2&quot;);
 * }
 * </pre>
 * 
 * Good:
 * 
 * <pre>
 * final StringBuilder sbString = StringUtil.startAppend(1300, &quot;header start&quot;,
 * 		someText, &quot;header end&quot;);
 * for (int i = 0; i &lt; 50; i++) {
 * 	StringUtil.append(sbString, &quot;text 1&quot;, stringArray[i], &quot;text 2&quot;);
 * }
 * </pre>
 * 
 * Why?<br/>
 * When using StringUtil.append(), memory is only allocated to fit in the
 * strings in method argument. So on each loop new memory for the string has to
 * be allocated and old string has to be copied to the new string. With size
 * hint, even if the size hint is above the needed memory, memory is saved
 * because new memory has not to be allocated on each cycle. Also it is much
 * faster if no string copy tasks has to be performed. So if concatenating
 * strings in a loop, count approximately the size and set it as the hint for
 * the string builder size. It's better to make the size hint little bit larger
 * rather than smaller.<br/>
 * In case there is no text appended before the cycle, just use <code>new
 * StringBuilder(1300)</code>.<br>
 * <br>
 * <h2>Concatenation and Constants</h2><br>
 * Bad:
 * 
 * <pre>
 * StringUtil.concat(&quot;text 1 &quot;, &quot;text 2&quot;, String.valueOf(npcId));
 * </pre>
 * 
 * Good:
 * 
 * <pre>
 * StringUtil.concat(&quot;text 1 &quot; + &quot;text 2&quot;, String.valueOf(npcId));
 * </pre>
 * 
 * or
 * 
 * <pre>
 * StringUtil.concat(&quot;text 1 text 2&quot;, String.valueOf(npcId));
 * </pre>
 * 
 * Why?<br/>
 * It saves some cycles when determining size of memory that needs to be
 * allocated because less strings are passed to concat() method. But do not use
 * + for concatenation of non-constant strings, that degrades performance and
 * makes extra memory allocations needed.<br>
 * <h2>Concatenation and Constant Variables</h2> Bad:
 * 
 * <pre>
 * String glue = &quot;some glue&quot;;
 * StringUtil.concat(&quot;text 1&quot;, glue, &quot;text 2&quot;, glue, String.valueOf(npcId));
 * </pre>
 * 
 * Good:
 * 
 * <pre>
 * final String glue = &quot;some glue&quot;;
 * StringUtil.concat(&quot;text 1&quot; + glue + &quot;text2&quot; + glue, String.valueOf(npcId));
 * </pre>
 * 
 * Why? Because when using <code>final</code> keyword, the <code>glue</code> is
 * marked as constant string and compiler treats it as a constant string so it
 * is able to create string "text1some gluetext2some glue" during the
 * compilation. But this only works in case the value is known at compilation
 * time, so this cannot be used for cases like
 * <code>final String objectIdString =
 * String.valueOf(getObjectId)</code>.<br>
 * <br>
 * <h2>StringBuilder Reuse</h2><br>
 * Bad:
 * 
 * <pre>
 * final StringBuilder sbString1 = new StringBuilder();
 * StringUtil.append(sbString1, &quot;text 1&quot;, String.valueOf(npcId), &quot;text 2&quot;);
 * ... // output of sbString1, it is no more needed
 * final StringBuilder sbString2 = new StringBuilder();
 * StringUtil.append(sbString2, &quot;text 3&quot;, String.valueOf(npcId), &quot;text 4&quot;);
 * </pre>
 * 
 * Good:
 * 
 * <pre>
 * final StringBuilder sbString = new StringBuilder();
 * StringUtil.append(sbString, &quot;text 1&quot;, String.valueOf(npcId), &quot;text 2&quot;);
 * ... // output of sbString, it is no more needed
 * sbString.setLength(0);
 * StringUtil.append(sbString, &quot;text 3&quot;, String.valueOf(npcId), &quot;text 4&quot;);
 * </pre>
 * 
 * Why?</br> In first case, new memory has to be allocated for the second
 * string. In second case already allocated memory is reused, but only in case
 * the new string is not longer than the previously allocated string. Anyway,
 * the second way is better because the string either fits in the memory and
 * some memory is saved, or it does not fit in the memory, and in that case it
 * works as in the first case. <h2>Primitives to Strings</h2> To convert
 * primitives to string, use String.valueOf().<br>
 * <br>
 * <h2>How much faster is it?</h2><br>
 * Here are some results of my tests. Count is number of strings concatenated.
 * Don't take the numbers as 100% true as the numbers are affected by other
 * programs running on my computer at the same time. Anyway, from the results it
 * is obvious that using StringBuilder with predefined size is the fastest (and
 * also most memory efficient) solution. It is about 5 times faster when
 * concatenating 7 strings, compared to TextBuilder. Also, with more strings
 * concatenated, the difference between StringBuilder and TextBuilder gets
 * larger. In code, there are many cases, where there are concatenated 50+
 * strings so the time saving is even greater.<br>
 * 
 * <pre>
 * Count: 2
 * TextBuilder: 1893
 * TextBuilder with size: 1703
 * String: 1033
 * StringBuilder: 993
 * StringBuilder with size: 1024
 * Count: 3
 * TextBuilder: 1973
 * TextBuilder with size: 1872
 * String: 2583
 * StringBuilder: 1633
 * StringBuilder with size: 1156
 * Count: 4
 * TextBuilder: 2188
 * TextBuilder with size: 2229
 * String: 4207
 * StringBuilder: 1816
 * StringBuilder with size: 1444
 * Count: 5
 * TextBuilder: 9185
 * TextBuilder with size: 9464
 * String: 6937
 * StringBuilder: 2745
 * StringBuilder with size: 1882
 * Count: 6
 * TextBuilder: 9785
 * TextBuilder with size: 10082
 * String: 9471
 * StringBuilder: 2889
 * StringBuilder with size: 1857
 * Count: 7
 * TextBuilder: 10169
 * TextBuilder with size: 10528
 * String: 12746
 * StringBuilder: 3081
 * StringBuilder with size: 2139
 * </pre>
 * 
 * @author fordfrog
 */
public final class StringUtil {
	private StringUtil() {
	}

	/**
	 * Concatenates strings.
	 * 
	 * @param strings
	 *            strings to be concatenated
	 * @return concatenated string
	 * @see StringUtil
	 */
	// public static String concat(final String... strings)
	// {
	// final TextBuilder sbString = TextBuilder.newInstance();
	//
	// for (final String string : strings)
	// {
	// sbString.append(string);
	// }
	//
	// String result = sbString.toString();
	// TextBuilder.recycle(sbString);
	// return result;
	// }

	/**
	 * Creates new string builder with size initializated to
	 * <code>sizeHint</code>, unless total length of strings is greater than
	 * <code>sizeHint</code>.
	 * 
	 * @param sizeHint
	 *            hint for string builder size allocation
	 * @param strings
	 *            strings to be appended
	 * @return created string builder
	 * @see StringUtil
	 */
	public static StringBuilder startAppend(final int sizeHint,
			final String... strings) {
		final int length = getLength(strings);
		final StringBuilder sbString = new StringBuilder(
				sizeHint > length ? sizeHint : length);

		for (final String string : strings) {
			sbString.append(string);
		}

		return sbString;
	}

	/**
	 * Appends strings to existing string builder.
	 * 
	 * @param sbString
	 *            string builder
	 * @param strings
	 *            strings to be appended
	 * @see StringUtil
	 */
	public static void append(final StringBuilder sbString,
			final String... strings) {
		sbString.ensureCapacity(sbString.length() + getLength(strings));

		for (final String string : strings) {
			sbString.append(string);
		}
	}

	/**
	 * Counts total length of all the strings.
	 * 
	 * @param strings
	 *            array of strings
	 * @return total length of all the strings
	 */
	private static int getLength(final String[] strings) {
		int length = 0;

		for (final String string : strings) {
			if (string == null) {
				length += 4;
			} else {
				length += string.length();
			}
		}

		return length;
	}

	// public static String getTraceString(StackTraceElement[] trace)
	// {
	// final TextBuilder sbString = TextBuilder.newInstance();
	// for (final StackTraceElement element : trace)
	// {
	// sbString.append(element.toString()).append('\n');
	// }
	//
	// String result = sbString.toString();
	// TextBuilder.recycle(sbString);
	// return result;
	// }

	/**
	 * Converts a line of text into an array of lower case words. Words are
	 * delimited by the following characters: , .\r\n:/\+
	 * <p>
	 * In the future, this method should be changed to use a
	 * BreakIterator.wordInstance(). That class offers much more fexibility.
	 * 
	 * @param text
	 *            a String of text to convert into an array of words
	 * @return text broken up into an array of words.
	 */
	public static final String[] toStringArray(String text) {
		if (text == null || text.length() == 0) {
			return new String[0];
		}
		StringTokenizer tokens = new StringTokenizer(text, ",\r\n/\\");
		String[] words = new String[tokens.countTokens()];
		for (int i = 0; i < words.length; i++) {
			words[i] = tokens.nextToken();
		}
		return words;
	}

	/**
	 * * Converts a line of text into an array of lower case words. Words are
	 * delimited by the following characters: , .\r\n:/\+
	 * <p>
	 * In the future, this method should be changed to use a
	 * BreakIterator.wordInstance(). That class offers much more fexibility.
	 * 
	 * @param text
	 *            a String of text to convert into an array of words
	 * @param token
	 *            String
	 * @return String[]broken up into an array of words.
	 */
	public static final String[] toStringArray(String text, String token) {
		if (text == null || text.length() == 0) {
			return new String[0];
		}
		StringTokenizer tokens = new StringTokenizer(text, token);
		String[] words = new String[tokens.countTokens()];
		for (int i = 0; i < words.length; i++) {
			words[i] = tokens.nextToken();
		}
		return words;
	}

	/**
	 * 是否整形
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		if (!isNull(str, "数字")) {
			Pattern pattern = Pattern.compile("^(-)?\\d+(\\d+)?$");
			return pattern.matcher(str).matches();
		}
		return false;
	}

	/**
	 * 
	 * @param str
	 * @param message
	 * @return
	 */
	public static boolean isNull(String str, String message) {
		if (str == null || str.trim().equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * 将字符串解析成int数组
	 * 
	 * @param str
	 *            需要解析的字符串
	 * @param split
	 *            分隔符号
	 * @return
	 */
	public static int[] changeToInt(String str, String split) {
		if (str == null || str.equals("")) {
			return null;
		}

		int data[] = null;
		try {
			String s[] = str.split(split);
			data = new int[s.length];
			for (int i = 0; i < s.length; i++) {
				data[i] = Integer.parseInt(s[i]);
			}
		} catch (Exception ex) {
			return null;
		}
		return data;
	}

	/**
	 * 从字符串中移除一个
	 * 
	 * @param oldStr
	 *            需要处理的字符串
	 * @param id
	 *            查找的字符串
	 * @param split
	 *            分隔符号
	 * @param newStr
	 *            需要替换的字符
	 * @return
	 */
	public static String removeFromStr(String oldStr, int id, String split,
			String newStr) {
		if (oldStr == null || oldStr.length() == 0) {
			return "";
		}
		String ids[] = oldStr.split(split);
		// 找到相同的id则设置为“”
		for (int i = 0; i < ids.length; i++) {
			if (id == Integer.parseInt(ids[i])) {
				ids[i] = newStr;
				break;
			}
		}
		// 组合为新的字符串
		String str = recoverNewStr(ids, split);
		// String str="";
		// int m=0;
		// for(int i=0;i<ids.length;i++){
		// if(!ids[i].equals("")){
		// if(m==0){
		// str+=ids[i];
		// }else{
		// str+=split+ids[i];
		// }
		// m++;
		// }
		// }
		return str;
	}
	
	/**
	 * 还原数组 为字符串，去掉数组中的空字符串 如 {0,1,2} 为 0；1；2
	 * 
	 * @param oldStr
	 * @param reg
	 * @return
	 */
	public static String recoverNewStrByInteger(List<Integer>oldStr , String reg) {
		String str = "";
		if(oldStr.size() == 0){
			str = "0";
			return str;
		}
		int m = 0;
		for (int i = 0; i < oldStr.size(); i++) {
			if (oldStr.get(i) > 0) {
				if (m == 0) {
					str += "'"+oldStr.get(i)+"'";
				} else {
					str += reg + "'"+oldStr.get(i)+"'";
				}
				m++;
			}
		}
		return str;
	}
	
	/**
	 * 还原数组 为字符串，去掉数组中的空字符串 如 {0,1,2} 为 0；1；2
	 * 
	 * @param oldStr
	 * @param reg
	 * @return
	 */
	public static String recoverNewStrByLong(List<Long>oldStr , String reg) {
		String str = "";
		if(oldStr.size() == 0){
			str = "0";
			return str;
		}
		int m = 0;
		for (int i = 0; i < oldStr.size(); i++) {
			if (oldStr.get(i) > 0) {
				if (m == 0) {
					str += "'"+oldStr.get(i)+"'";
				} else {
					str += reg + "'"+oldStr.get(i)+"'";
				}
				m++;
			}
		}
		return str;
	}

	/**
	 * 还原数组 为字符串，去掉数组中的空字符串 如 {0,1,2} 为 0；1；2
	 * 
	 * @param oldStr
	 * @param reg
	 * @return
	 */
	public static String recoverNewStr(String oldStr[], String reg) {
		String str = "";
		int m = 0;
		for (int i = 0; i < oldStr.length; i++) {
			if (!oldStr[i].equals("") && oldStr[i].indexOf(":0,") == -1) {
				if (m == 0) {
					str += oldStr[i];
				} else {
					str += reg + oldStr[i];
				}
				m++;
			}
		}
		return str;
	}
	
	
	/**
	 * 还原skill数组 为字符串
	 * @param oldStr
	 * @param reg
	 * @return
	 */
	public static String recoverNewSkillStr(String oldStr[], String reg) {
		String str = "";
		int m = 0;
		for (int i = 0; i < oldStr.length; i++) {
			if("0".equals(oldStr[i])){
				continue;
			}
			if (!oldStr[i].equals("") && oldStr[i].indexOf(":0,") == -1) {// 不为空没有0的士兵
				if (m == 0) {
					str += oldStr[i];
				} else {
					str += reg + oldStr[i];
				}
				m++;
			}
		}
		return str;
	}
	
	/**
	 * 将int数组转化为字符串数组
	 * 
	 * @param ids
	 * @return
	 */
	public static String[] changeToString(int ids[]) {
		String str[] = new String[ids.length];
		for (int i = 0; i < ids.length; i++) {
			str[i] = String.valueOf(ids[i]);
		}
		return str;
	}

	/**
	 * 向数组中增加一个新元素
	 * 
	 * @param array
	 * @param id
	 * @return
	 */
	public static int[] addNew(int array[], int id) {
		int[] newArray = new int[array.length + 1];
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[newArray.length - 1] = id;
		return newArray;
	}
	/**
	 * 输入的字符是否是汉字
	 * @param a char
	 * @return boolean
	 */
	public static boolean isChinese(char a) { 
	     int v = (int)a; 
	     return (v >=19968 && v <= 171941); 
	}
	/**
	 * 返回字符串中汉字，字母的数量
	 * @param str
	 * @return
	 */
	public static int[] isChinese(String str){
		int[] len = new int[]{0,0,0};
		if(str == null){
			return len;
		}
		str = str.trim();
		int count = 0;
		String regEx = "[\\u4e00-\\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		while (m.find()) {
			for (int i = 0; i <= m.groupCount(); i++) {
				count = count + 1;
			}
		}
		//system.out.println("共有 " + str.length() + "个字符 ");
		//system.out.println("共有 " + count + "个汉字 ");
		//system.out.println("共有 " + (str.length() - count) + "个字母 ");
		len[0] = str.length();
		len[1] = count;
		len[2] = str.length() - count;
		return len;
	}
	
	/**
	 * 中文升序
	 * @param names
	 */
	public void ascending(String[] names){
		if(names == null){
			return;
		}
		Arrays.sort(names, Collator.getInstance(java.util.Locale.CHINA));//升序;
	}
	
	/**
	 * 中文降序
	 * @param names
	 */
	public void descending(String[] names){
		if(names == null){
			return;
		}
		Arrays.sort(names, Collections.reverseOrder(Collator.getInstance(java.util.Locale.CHINA)));//升序;
		
	}

	public static void main1(String[] args) {
//		List<Integer> aa = new ArrayList<>();
//		aa.add(1);
//		aa.add(2);
//		//system.out.println(recoverNewStrByInteger(aa, ","));
//		byte[] at = "中国人民".getBytes();
//		for(byte b : at){
//			//system.out.println(b);
//		}
		//system.out.println("中国人民".getBytes().length);
	}
	public static class StrObj{
		public StrObj(String string){
//			//system.out.println(string);
			str=string.split("&");
		}
		public  String str[];
	}
	public static int getLength(String str){
		int length=0;
		for(int i=0;i<str.length();i++){
			if(StringUtil.isChinese(str.charAt(i))){
				length+=2;
			}else{
				length+=1;
			}
		}
		return length;
	}
	
	
	/**
	 * 汉字算一个字符
	 * @param str
	 * @return
	 */
	public static int getLengthInName(String str){
		int length=0;
		for(int i=0;i<str.length();i++){
			if(StringUtil.isChinese(str.charAt(i))){
				length+=1;
			}else{
				length+=1;
			}
		}
		return length;
	}
	public static void main(String[] args) throws UnsupportedEncodingException {
	    String arr[] = splitByByteSize("刘成功",4);
	    int byteLen = 0;
	    for(int i=0;i<arr.length;i++){
	        byteLen+=arr[i].getBytes().length;
	    }
	    byte[] bytes = new byte[byteLen];
	    int offset = 0;
	    for(int i=0;i<arr.length;i++){
	        System.arraycopy(arr[i].getBytes("utf-8"), 0, bytes, offset, arr[i].getBytes("utf-8").length);
	        offset += arr[i].getBytes("utf-8").length;
	    }

	    //system.out.println(">>"+new String(bytes));
	}

	public static String[] splitByByteSize(String content, int size) throws UnsupportedEncodingException{
	    byte[] bytes = content.getBytes("utf-8");
	    int totalSize = bytes.length;
	    int partNum = 0;
	    if(totalSize == 0){
	        return new String[0];
	    }
	    if(totalSize % size == 0){
	        partNum = totalSize / size;
	    }else{
	        partNum = totalSize / size + 1;
	    }
	    String[] arr = new String[partNum];
	    int offset = 0;
	    byte newBytes[] = new byte[size];
	    for(int i=0;i<partNum-1;i++){
	        System.arraycopy(bytes, offset, newBytes, 0, size);
	        arr[i] = new String(newBytes,"utf-8");
	        offset += size;
	    }
	    System.arraycopy(bytes, offset, newBytes, 0, totalSize-offset);
	    arr[partNum-1] = new String(newBytes,"utf-8");
	    return arr;
	}
	
	/**
	 * 得到对应的数据
	 * @param s
	 * @return
	 */
	public static int[] getStyles(String s){
		String styles[] = s.split(",");
		if(styles != null){
			int tys[] = new int[styles.length];
			for(int i = 0; i <styles.length;i++ ){
				tys[i] = Integer.parseInt(styles[i]);
			}
			return tys;
		}
		return new int[0];
	}
}
