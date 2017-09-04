package com.dreamgear.majiang.utils;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CommonSQL {
	// 默认的SQL过滤字符
	public final static String FILTERSQL = "'|*|^|$|%|_";

	// 特殊字符对应的ASCII编号
	public final static String FILTERSQLCHARCODE = "39|42|94|36|37|95";

	// 默认的SQL过滤字符
	public final static String FILTERSQL0 = "SELECT|UPDATE|DELETE|COUNT|SUM|MASTER|SCRIPT|DECLARE|OR|EXECUTE|ALTER|STATEMENT|EXECUTEQUERY|COUNT|EXECUTEUPDATE|COMMIT";

	// 不过滤@
	public final static String FILTERSQL1 = "SELECT|UPDATE|DELETE|COUNT|*|SUM|MASTER|SCRIPT|'|^|$|%|_|DECLARE|OR|EXECUTE|ALTER|STATEMENT|EXECUTEQUERY|COUNT|EXECUTEUPDATE|COMMIT";
	/**
	 * 过滤SQL文的字段内容中特殊字符
	 * @param sql  SQL 原文 
	 * @return
	 */

	public static String filterSQL(String sql) {
		return filterSQL(sql, true);
	}

	/**
	 * 过滤SQL文的字段内容中特殊字符
	 * @param sql  SQL 原文
	 * @param blFlag true 过滤 “SELECT|UPDATE...” 等操作符合
	 * @return
	 */
	public static String filterSQL(String sql, boolean blFlag) {
		return filterSQL(sql, null, blFlag);
	}

	/**
	 * 过滤SQL文的字段内容中特殊字符
	 * @param sql  SQL 原文
	 * @param blFlag true 过滤 “SELECT|UPDATE...” 等操作符合
	 * @param blReplace 过滤特殊字符“@$%”等
	 * @return
	 */
	public static String filterSQL(String sql, boolean blFlag, boolean blReplace) {
		return filterSQL(sql, null, blFlag, blReplace);
	}

	/**
	 * 过滤SQL文的字段内容中特殊字符
	 * 
	 * @param sql sql文
	 * @param strFilterSQL 自定义的过滤特殊字符，以“|”分割 自定义字符中的“'”必须为首位
	 * @param blFlag true 过滤 “SELECT|UPDATE...” 等操作符合
	 * @return
	 */

	public static String filterSQL(String sql, String strFilterSQL, boolean blFlag) {
		return filterSQL(sql, strFilterSQL, blFlag, false);
	}

	/**
	 * 过滤SQL文的字段内容中特殊字符
	 * 
	 * @param sql sql文
	 * @param strFilterSQL 自定义的过滤特殊字符，以“|”分割 自定义字符中的“'”必须为首位
	 * @param blFlag true 过滤 “SELECT|UPDATE...” 等操作符合
	 * @param blReplace 过滤特殊字符“@$%”等
	 * @return
	 */

	public static String filterSQL(String sql, String strFilterSQL, boolean blFlag, boolean blReplace) {
		if ((sql != null) && !sql.equals("") && !"".equals(strFilterSQL)) {
			if (sql.length() > 0)
				sql = sql.replace("|", "");

			if (strFilterSQL == null || strFilterSQL.length() == 0) {
				strFilterSQL = FILTERSQL;
			}

			String[] data = split(strFilterSQL, "|");
			for (int i = 0; i < data.length; i++) {
				int iOffset = 0;
				iOffset = sql.indexOf(data[i].trim());
				if (iOffset >= 0) {
					char cChar = data[i].trim().charAt(0);
					int iCharCode = (int) cChar;
					if (iCharCode == 37) {
						if (sql.length() == 1) {
							sql = " ";
						} else {
							sql = sql.replace(data[i].trim(), "");
						}
					} else {
						if (blReplace)
							sql = replaceAllIgnoreCase(sql, "\\" + data[i].trim(), "");
						else
							sql = replaceAllIgnoreCase(sql, "\\" + data[i].trim(), "\'||chr(" + iCharCode + ")||\'");
					}
				}
			}
			if (blFlag)
				sql = replaceAllIgnoreCase(sql, FILTERSQL0);
			return sql;
		} else {
			return sql;
		}
	}
	
	public static String filterLikeSQL(String sql) {
		if ((sql != null) && !sql.equals("")) {
			sql.replace("'", "''");
			sql.replace("\\", "\\\\");
			sql.replace("%", "\\%");
			sql.replace("％", "\\％");
			sql.replace("_", "\\_");
			return sql.toString();
		} else {
			return "";
		}
	}
	/**
	 * 分割　str1|str2|str3|str4|str5 格式的数据为一维数组
	 * @param str ：str1|str2|str3|str4|str5
	 * @param sign ：分割符
	 * @return
	 */
	public static String[] split(String str, String sign) {
		String[] strData = null;
		StringTokenizer st1 = new StringTokenizer(str, sign);
		//定义数组长度
		strData = new String[st1.countTokens()];
		int i = 0;
		while (st1.hasMoreTokens()) {
			strData[i] = st1.nextToken().trim();
			i++;
		}
		return strData;
	}
	/**
	 * 批量过滤字符
	 * @param str
	 * @param key
	 * @return
	 */
	public static String replaceAllIgnoreCase(String str, String key) {
		Pattern p = Pattern.compile(key, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		str = m.replaceAll("");
		return str;
	}
	/**
	 * 批量过滤字符
	 * @param str
	 * @param key
	 * @return
	 */
	public static String replaceAllIgnoreCase(String str, String key, String strReplace) {
		Pattern p = Pattern.compile(key, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		str = m.replaceAll(strReplace);
		return str;
	}
	
	public static void main(String[] args) {
		System.out.println(filterSQL("%&\\"));
	}
}
