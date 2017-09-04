package com.dreamgear.majiang.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {
	/**
	 * 纯数字
	 * @param str
	 * @return
	 */
	 public static boolean isNumeric(String str)
     {
           Pattern pattern = Pattern.compile("[0-9]*");
           Matcher isNum = pattern.matcher(str);
           if( !isNum.matches() )
           {
                return false;
           }
           return true;
     }
	 
 	/**
	 * 纯数字
	 * @param str
	 * @return
	 */
	 public static boolean isNumerics(String str)
     {
           Pattern pattern = Pattern.compile("^[-]{0,1}[0-9]*\\.{0,1}[0-9]*");

           Matcher isNum = pattern.matcher(str);
           if( !isNum.matches() )
           {
                 return false;
           }
           return true;
     }
	 
	 public static void main(String[] args) {
		//system.out.println(isNumerics("-1.1"));
//        String s = ".";
        //system.out.println(s);
	}
}
