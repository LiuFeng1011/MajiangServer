package com.dreamgear.majiang.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamgear.majiang.common.GameConst;

/**
 * 基于joda-time的工具方法
 * 参考资料 
 * http://blog.csdn.net/dhdhdh0920/article/details/7415359
 * http://persevere.iteye.com/blog/1755237
 * timezone 资料http://joda-time.sourceforge.net/timezones.html
 * @author admin
 *
 */
public class TimeUtils {
//	http://www.date4j.net/javadoc/index.html
	static final Logger logger = LoggerFactory.getLogger(TimeUtils.class);
	public static final DateTimeZone tz =DateTimeZone.forTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));//设置时区为北京时间
	
	private static final DateTimeFormatter FORMAT_MONTH= DateTimeFormat.forPattern("yyyyMM");//自定义日期格式
	private static final DateTimeFormatter FORMAT_YEAR= DateTimeFormat.forPattern("yyyy");//自定义日期格式
	private static final DateTimeFormatter FORMAT_DAY= DateTimeFormat.forPattern("yyyy-MM-dd");//自定义日期格式
	private static final DateTimeFormatter FORMAT_CH_YEAR = DateTimeFormat .forPattern("yyyy-MM-dd HH:mm:ss");  
	private static final DateTimeFormatter FORMAT_EN_YEAR = DateTimeFormat.forPattern("HH:mm:ss yyyy-MM-dd");//自定义日期格式
	private static final DateTimeFormatter FORMAT_DAY_CUSTOM= DateTimeFormat.forPattern("yyyyMMdd");//自定义日期格式
	
	private static final DateTimeFormatter MAIL_DATE = DateTimeFormat.forPattern("yy-MM-dd HH:mm");//自定义日期格式
	public static String formatDay(DateTime dt){
		return dt.toString(FORMAT_DAY);
	}
	public static String formatYear(DateTime dt){
		return dt.toString(FORMAT_CH_YEAR);
	}
	public static String formatMAIL(DateTime dt){
		return dt.toString(MAIL_DATE);
	}
	
	public static int formatDayCustom(DateTime dt){
		return Integer.parseInt(dt.toString(FORMAT_DAY_CUSTOM));
	}
	
//	/**
//	 * 检测开始日期和结束日期，判断是否需要处理该条记录
//	 * @param weekArray
//	 * @param begin_date
//	 * @param end_date
//	 * @param now
//	 * @return
//	 */
//	public static boolean checkTime(String[] weekArray, String begin_date,
//			String end_date, DateTime now) {
//		if (weekArray == null || weekArray.length == 0) {//不需要判断星期
//			if (begin_date == null) {
//				// 跳出
//				return false;
//			} else {
//				// 按照日期处理
//				DateTime sDate = new DateTime(begin_date);
//				DateTime eDate = new DateTime(end_date);
//				if (now.lteq(eDate) && sDate.lteq(now)) {
//					// 在当前日期中判断是否在开始和结束时间内
//					return true;
//				}
//			}
//		} else {
//			// 按照星期处理
//			int day = now.getWeekDay();
//			for (int i = 0; i < weekArray.length; i++) {
//				logger.debug("week i==" + i + " ===" + weekArray[i]+" now day= "+day);
//				if (day == Integer.parseInt(weekArray[i])) {
//					// 在当前星期中
//					return true;
//				}
//			}
//			//system.out.println("@@@@@@@day===" + now.toString());
//			return false;
//		}
//		return false;
//	}
	/**
	 * 检测是否是同一天
	 * @param time
	 * @return
	 */
	public static boolean isSameDay(long start,long end){
		DateTime startDate =TimeUtils.getTime(start);
		DateTime endDate = TimeUtils.getTime(end); 
		String d1= formatDay(startDate);
		String d2= formatDay(endDate);
		if(d1.equals(d2)){
			return true;
		}else{
			return false;
		}
	}
	
	//从N点开始，判断是否在同一天
	public static boolean isSameDayToReset(int hour,long start,long end){
		
		long dtime = (long) (hour * GameConst.TIME_HOUR_MILLIS);

		DateTime startDate =TimeUtils.getTime(start - dtime);
		DateTime endDate = TimeUtils.getTime(end - dtime); 
		String d1= formatDay(startDate);
		String d2= formatDay(endDate);

		if(d1.equals(d2)){
			return true;
		}else{
			return false;
		}
	}
	
	
	/**
	 * 检测是否是同一天
	 * @param time
	 * @return
	 */
	public static boolean isSameDay(long time){
		DateTime now =TimeUtils.now();//获得当前时间
		return isSameDay(now.getMillis(),time);
	}
	public static boolean isSameDayToReset(int hour,long time){
		DateTime now =TimeUtils.now();//获得当前时间
		return isSameDayToReset(hour,now.getMillis(),time);
	}
	/**
	 * 检测是否是同一天
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean isSameWeek(long start,long end){
		DateTime startDate =TimeUtils.getTime(start);
		DateTime endDate = TimeUtils.getTime(end); 
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTimeInMillis(start);
		cal2.setTimeInMillis(end);
		
		//判断是否同年同月
		if(startDate.toString(FORMAT_MONTH).equals(endDate.toString(FORMAT_MONTH))){
			if(cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)){
				return true;
			}
			return false;
		}else{
			return false;
		}
		
	}
	
	/**
	 * 获取日期时间戳
	 * @param date
	 * @return
	 */
	public static long getTimeByDate(Date date){
		long times = date.getTime();  
		return times;
	}
	
	/**
	 * 根据时间戳获取日期
	 * @param time
	 * @return
	 */
	public static Date getDateByTime(long time){
		Date date = new Date(time);  
		return date;
	}
	
	/**
	 * 根据long获得时间
	 * @param time
	 * @return
	 */
	public static DateTime getTime(long time){
		return new DateTime(time);
	}
	/**
	 * 根据字符串获得时间
	 * @param str
	 * @return
	 */
	public static DateTime getTime(String str){
		return  DateTime.parse(str, FORMAT_CH_YEAR);  
	}
	/**
	 * 获得当前时间
	 * @return
	 */
	public static DateTime now(){
		return DateTime.now(tz);
	}
	
	/**
	 * 获得当前时间
	 * @return
	 */
	public static long nowLong(){
		return now().getMillis();
	}
	
	/**
	 * 返回时间字符类型
	 * @param format
	 * @return
	 */
	public static String now(String format){
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat(format);
		Date date = now().toDate();
		returnStr = f.format(date);
		return returnStr;
	}
//	/**
//	 * 返回当前的日期，如2012-07-23
//	 * @return
//	 */
//	public static DateTime today(){
//		return DateTime.today(tz);
//	}
	/**
	 * 返回2个日期的时间差
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int getDays(DateTime d1,DateTime d2){
		Days _days=Days.daysBetween(d1, d2);
		int num=_days.getDays();
		return Math.abs(num);
	}
	/**
	 * 返回两个日期之间的全部日期
	 * @param start
	 * @param end
	 * @return
	 */
	public static String[] getDays(String start,String end){
		
		DateTime startDate=TimeUtils.getTime(start);
		DateTime endDate=TimeUtils.getTime(end);
		//返回两个日期之间的时间差,如2012-11-1 2012-11-20  差值=19
		Days _days=Days.daysBetween(startDate, endDate);
		int num=_days.getDays();
		String days[]=new String[num+1];
		days[0]=start;
		for(int i=1;i<days.length;i++){
			DateTime d=startDate.plusDays(i);
			days[i]=d.toString(FORMAT_DAY);
		}
		return days;
	}
	
	/**
	 * 将字符串时间转化为long
	 * @param time
	 * @return
	 */
	public static long getTimes(String time){
		if(time == null || "".equals(time) || "0".equals(time)){
			return 0;
		}
		return getTime(time).getMillis();
	}
	
	/**
	 * 英文时间显示
	 * @param time
	 * @return
	 */
	public static synchronized String enDate(long time) {
		return getTime(time).toString(FORMAT_EN_YEAR);
	}
	/**
	 * 中文时间显示
	 * @param time
	 * @return
	 */
	public static synchronized String chDate(long time) {
		return getTime(time).toString(FORMAT_CH_YEAR);
	}
	/**
	 * 争夺战开始结束时间
	 * @param time
	 * @return
	 */
	public static int getWarTime(String hour){
		String day = now().toString(FORMAT_DAY);
		String all = day + hour;
		return (int)(getTime(all).getMillis()/1000);
	}
	
	/**
	 * 设置时间
	 * @param yearAdd  年     加的数据
	 * @param monthAdd 月     加的数据
	 * @param dayAdd 日     加的数据
	 * @param hour 小时
	 * @param min 分钟
	 * @return
	 */
	public static int setLegeueTime(int yearAdd,int monthAdd,int dayAdd,int hour,int min){
		DateTime now = now();
		now = now.plusYears(yearAdd);
		now = now.plusMonths(monthAdd);
		now = now.plusDays(dayAdd);
		DateTime dateTime = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), hour, min, 0, 0);
//		//system.out.println(formatYear(dateTime));
		  return (int)(dateTime.getMillis()/1000);
	}
	
	/**
	 * 设置时间
	 * @param yearAdd  年     加的数据
	 * @param monthAdd 月     加的数据
	 * @param dayAdd 日     加的数据
	 * @param hour 小时
	 * @param min 分钟
	 * @return
	 */
	public static int setLaterTime(int times,int yearAdd,int monthAdd,int dayAdd,int hour,int min){
		DateTime now = getTime(times*1000L);
		now = now.plusYears(yearAdd);
		now = now.plusMonths(monthAdd);
		now = now.plusDays(dayAdd);
		DateTime dateTime = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), hour, min, 0, 0);
//		//system.out.println(formatYear(dateTime));
		  return (int)(dateTime.getMillis()/1000);
	}
	/**
	 * 是否连续天
	 * @return
	 */
	public static boolean continuous(long start,long end){
		DateTime startDate =TimeUtils.getTime(start);
		DateTime endDate = TimeUtils.getTime(end); 
		DateTime addstart =  startDate.plusDays(1);
		DateTime reductionstart = startDate.plusDays(-1);
		if(isSameDay( start, end)){
			return false;
		}else{
			if(formatDay(endDate).equals(formatDay(addstart))){
				return true;
			}else if(formatDay(endDate).equals(formatDay(reductionstart))){
				return true;
			}
		} 
		return false;
	}
	
	/**
	 * 比较两个 字符时间
	 * @param time1 2011-01-10 10:00:00 格式
	 * @param time2
	 * @return
	 */
	public static boolean compareStringTime(String time1,String time2){
		return getTimes(time1) - getTimes(time2) > 0 ;
	}
	/**
	 * 和当前时间比较  字符时间
	 * 当前时间<指定时间=true
	 * 当前时间>指定时间=false
	 * @param time1 2011-01-10 10:00:00 格式
	 * @return
	 */
	public static boolean compareStringTimeNow(String time1){
		return getTimes(time1) - nowLong() > 0 ;
	}
	
	//获取当天的开始时间
	public static java.util.Date getDayBegin() {
	    Calendar cal = new GregorianCalendar();
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    return cal.getTime();
	}
	//获取当天的结束时间
	public static java.util.Date getDayEnd() {
	    Calendar cal = new GregorianCalendar();
	    cal.set(Calendar.HOUR_OF_DAY, 23);
	    cal.set(Calendar.MINUTE, 59);
	    cal.set(Calendar.SECOND, 59);
	    return cal.getTime();
	}
	//获取昨天的开始时间
	public static Date getBeginDayOfYesterday() {
	    Calendar cal = new GregorianCalendar();
	    cal.setTime(getDayBegin());
	    cal.add(Calendar.DAY_OF_MONTH, -1);
	    return cal.getTime();
	}
	//获取昨天的结束时间
	public static Date getEndDayOfYesterDay() {
	    Calendar cal = new GregorianCalendar();
	    cal.setTime(getDayEnd());
	    cal.add(Calendar.DAY_OF_MONTH, -1);
	    return cal.getTime();
	}
	//获取明天的开始时间
	public static Date getBeginDayOfTomorrow() {
	    Calendar cal = new GregorianCalendar();
	    cal.setTime(getDayBegin());
	    cal.add(Calendar.DAY_OF_MONTH, 1);

	    return cal.getTime();
	}
	//获取明天的结束时间
	public static Date getEndDayOfTomorrow() {
	    Calendar cal = new GregorianCalendar();
	    cal.setTime(getDayEnd());
	    cal.add(Calendar.DAY_OF_MONTH, 1);
	    return cal.getTime();
	}
	//获取本周的开始时间
	public static Date getBeginDayOfWeek() {
	    Date date = new Date();

	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
	    if (dayofweek == 1) {
	        dayofweek += 7;
	    }
	    cal.add(Calendar.DATE, 2 - dayofweek);
	    return getDayStartTime(cal.getTime());
	}
	//获取本周的结束时间
	public static Date getEndDayOfWeek(){
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(getBeginDayOfWeek());  
	    cal.add(Calendar.DAY_OF_WEEK, 6); 
	    Date weekEndSta = cal.getTime();
	    return getDayEndTime(weekEndSta);
	}
	//获取本月的开始时间
	 public static Date getBeginDayOfMonth() {
	        Calendar calendar = Calendar.getInstance();
	        calendar.set(getNowYear(), getNowMonth() - 1, 1);
	        return getDayStartTime(calendar.getTime());
	    }
	//获取本月的结束时间
	 public static Date getEndDayOfMonth() {
	        Calendar calendar = Calendar.getInstance();
	        calendar.set(getNowYear(), getNowMonth() - 1, 1);
	        int day = calendar.getActualMaximum(5);
	        calendar.set(getNowYear(), getNowMonth() - 1, day);
	        return getDayEndTime(calendar.getTime());
	    }
	 //获取本年的开始时间
	 public static java.util.Date getBeginDayOfYear() {
	        Calendar cal = Calendar.getInstance();
	        cal.set(Calendar.YEAR, getNowYear());
	        // cal.set
	        cal.set(Calendar.MONTH, Calendar.JANUARY);
	        cal.set(Calendar.DATE, 1);

	        return getDayStartTime(cal.getTime());
	    }
	 //获取本年的结束时间
	 public static java.util.Date getEndDayOfYear() {
	        Calendar cal = Calendar.getInstance();
	        cal.set(Calendar.YEAR, getNowYear());
	        cal.set(Calendar.MONTH, Calendar.DECEMBER);
	        cal.set(Calendar.DATE, 31);
	        return getDayEndTime(cal.getTime());
	    }
	

	 //获取某个日期的开始时间
	 public static Timestamp getDayStartTime(Date d) {
	     Calendar calendar = Calendar.getInstance();
	     if(null != d) calendar.setTime(d);
	     calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
	     calendar.set(Calendar.MILLISECOND, 0);
	     return new Timestamp(calendar.getTimeInMillis());
	 }
	 //获取某个日期的结束时间
	 public static Timestamp getDayEndTime(Date d) {
	     Calendar calendar = Calendar.getInstance();
	     if(null != d) calendar.setTime(d);
	     calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
	     calendar.set(Calendar.MILLISECOND, 999);
	     return new Timestamp(calendar.getTimeInMillis());
	 }
	 //获取今年是哪一年
	  public static Integer getNowYear() {
	          Date date = new Date();
	         GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
	         gc.setTime(date);
	         return Integer.valueOf(gc.get(1));
	     }
	  //获取本月是哪一月
	  public static int getNowMonth() {
	          Date date = new Date();
	         GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
	         gc.setTime(date);
	         return gc.get(2) + 1;
	     }
	  //两个日期相减得到的天数
	  public static int getDiffDays(Date beginDate, Date endDate) {

	         if (beginDate == null || endDate == null) {
	             throw new IllegalArgumentException("getDiffDays param is null!");
	         }

	         long diff = (endDate.getTime() - beginDate.getTime())
	                 / (1000 * 60 * 60 * 24);

	         int days = new Long(diff).intValue();

	         return days;
	     }
	 //两个日期相减得到的毫秒数
	  public static long dateDiff(Date beginDate, Date endDate) {
	         long date1ms = beginDate.getTime();
	         long date2ms = endDate.getTime();
	         return date2ms - date1ms;
	     }
	  //获取两个日期中的最大日期
	  public static Date max(Date beginDate, Date endDate) {
	         if (beginDate == null) {
	             return endDate;
	         }
	         if (endDate == null) {
	             return beginDate;
	         }
	         if (beginDate.after(endDate)) {
	             return beginDate;
	         }
	         return endDate;
	     }
	  //获取两个日期中的最小日期
	  public static Date min(Date beginDate, Date endDate) {
	         if (beginDate == null) {
	             return endDate;
	         }
	         if (endDate == null) {
	             return beginDate;
	         }
	         if (beginDate.after(endDate)) {
	             return endDate;
	         }
	         return beginDate;
	     }
	  //返回某月该季度的第一个月
	  public static Date getFirstSeasonDate(Date date) {
	          final int[] SEASON = { 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4 };
	         Calendar cal = Calendar.getInstance();
	         cal.setTime(date);
	         int sean = SEASON[cal.get(Calendar.MONTH)];
	         cal.set(Calendar.MONTH, sean * 3 - 3);
	         return cal.getTime();
	     }
	  //返回某个日期下几天的日期
	  public static Date getNextDay(Date date, int i) {
	         Calendar cal = new GregorianCalendar();
	         cal.setTime(date);
	         cal.set(Calendar.DATE, cal.get(Calendar.DATE) + i);
	         return cal.getTime();
	     }
	  //返回某个日期前几天的日期
	  public static Date getFrontDay(Date date, int i) {
	         Calendar cal = new GregorianCalendar();
	         cal.setTime(date);
	         cal.set(Calendar.DATE, cal.get(Calendar.DATE) - i);
	         return cal.getTime();
	     }
	  //获取某年某月到某年某月按天的切片日期集合（间隔天数的日期集合）
	  public static List<Object> getTimeList(int beginYear, int beginMonth, int endYear,
	             int endMonth, int k) {
	         List<Object> list = new ArrayList<Object>();
	         if (beginYear == endYear) {
	             for (int j = beginMonth; j <= endMonth; j++) {
	                 list.add(getTimeList(beginYear, j, k));

	             }
	         } else {
	             {
	                 for (int j = beginMonth; j < 12; j++) {
	                     list.add(getTimeList(beginYear, j, k));
	                 }

	                 for (int i = beginYear + 1; i < endYear; i++) {
	                     for (int j = 0; j < 12; j++) {
	                         list.add(getTimeList(i, j, k));
	                     }
	                 }
	                 for (int j = 0; j <= endMonth; j++) {
	                     list.add(getTimeList(endYear, j, k));
	                 }
	             }
	         }
	         return list;
	     }
	  //获取某年某月按天切片日期集合（某个月间隔多少天的日期集合）
	  public static List<Object> getTimeList(int beginYear, int beginMonth, int k) {
	         List<Object> list = new ArrayList<Object>();
	         Calendar begincal = new GregorianCalendar(beginYear, beginMonth, 1);
	         int max = begincal.getActualMaximum(Calendar.DATE);
	         for (int i = 1; i < max; i = i + k) {
	             list.add(begincal.getTime());
	             begincal.add(Calendar.DATE, k);
	         }
	         begincal = new GregorianCalendar(beginYear, beginMonth, max);
	         list.add(begincal.getTime());
	         return list;
	     }
	/**
	 * 增加秒数
	 * @param time
	 * @return
	 */
	public static synchronized Timestamp addSecond(long paramLong,int paramInt) {
		return new Timestamp(paramLong + paramInt*1000);
	}	
	
	public static void main(String[] args) {
		
		System.out.println(TimeUtils.setLegeueTime(0, 0, 120, 0, 0));
		//1395992876
		System.out.println(formatYear(getTime(1411475400*1000L)));
		System.out.println(TimeUtils.setLegeueTime(0, 0, 30-1, 0, 0));
		System.out.println("--------------------------------------");
		System.out.println(getTimes("2014-09-18 10:00:00"));
		System.out.println(getTime("2014-08-05 10:00:00"));
		System.out.println(getTime("2014-10-01 23:51:00").getMillis());
		System.out.println(TimeUtils.isSameDay(getTime("2014-09-24 0:25:00").getMillis(), TimeUtils.nowLong()));
		System.out.println("--------------------------------------");
		System.out.println(TimeUtils.setLaterTime(1407204000,0, 0, 0, 0, 0));
		System.out.println(getTime(1407204000000L).getHourOfDay());
		System.out.println("--------------------------------------");
		int open= TimeUtils.setLaterTime((int)(TimeUtils.getTime("2014-09-15 18:10:00").getMillis()/1000),0, 0, 0, 0, 0);
		int d=TimeUtils.getDays(TimeUtils.now(), TimeUtils.getTime(open*1000L));
		System.out.println(d);
//		String strUtf8 = "\u4e2d\u56fd\u4f01\u4e1a\u5bb6\u6742\u5fd7";   1404489600
//		System.out.println(strUtf8); 
	}
	
}
