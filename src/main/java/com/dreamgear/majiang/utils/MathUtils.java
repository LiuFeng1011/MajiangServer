package com.dreamgear.majiang.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class MathUtils {
	static final Logger logger = LoggerFactory.getLogger(MathUtils.class);

	private final static Random RND = new Random();

	@SuppressWarnings("deprecation")
	public static boolean isSameDay(long time1, long time2) {

		Date date1 = new Date();
		date1.setTime(time1);

		Date date2 = new Date();
		date2.setTime(time2);

		return date1.getYear() == date2.getYear()
				&& date1.getMonth() == date2.getMonth()
				&& date1.getDay() == date2.getDay();

	}

	/**
	 * @return a int >= 0 and < max
	 */
	public static int random(int size) {
		return RND.nextInt(size);
	}

	/**
	 * #{@link Random#nextDouble()}
	 * 
	 * @return
	 */
	public static double random() {
		return RND.nextDouble();
	}

	/**
	 * @return a double >= 0 and < 1
	 */
	public static double nextDouble() {
		return RND.nextDouble();
	}

	/**
	 * @return a int >= min and <= max
	 */
	public static int random(int min, int max) {
		if (min == max)
			return min;
		return RND.nextInt(max - min + 1) + min;
	}

	/**
	 * fit the chance, normally used for ratio judge
	 * 
	 * @param chn
	 * @return
	 */
	public static boolean randomLessThan(int chn) {
		return RND.nextInt(100) < chn;
	}

	public static boolean randomLessThan(int chn, int percent) {
		return RND.nextInt(1000) < chn;
	}

	/**
	 * @return a random element of ts
	 */
	public static <T> T randomOne(List<T> ts) {
		if (ts.size() > 0)
			return ts.get(random(ts.size()));
		return null;
	}

	/**
	 * @param scenes
	 * @param maxSceneCount
	 * @return
	 */
	public static <T> List<T> randoms(Set<T> ts, int maxCount) {
		if (ts.size() <= maxCount)
			return new ArrayList<T>(ts);

		return randoms(new ArrayList<T>(ts), maxCount);
	}

	/**
	 * @param <T>
	 * @param ts
	 * @return a group of T of ts, group size equals size, less than size only
	 *         when ts size less than size
	 */
	public static <T> List<T> randoms(List<T> ts, int size) {
		List<T> temps = new LinkedList<T>(ts);
		List<T> result = new ArrayList<T>();

		int randTimes = Math.min(ts.size(), size);
		for (int i = 0; i < randTimes; i++) {
			T randOne = randomOne(temps);
			if (randOne == null) {
				continue;
			}
			result.add(randOne);
			temps.remove(randOne);
		}
		return result;
	}

	/**
	 * @return a random element of ts
	 */
	public static <T> T randomOne(T[] ts) {
		return ts[random(ts.length)];
	}

	/**
	 * @return a random element of ts
	 */
	public static int randomOne(int[] ts) {
		return ts[random(ts.length)];
	}

	/**
	 * @param reqPage
	 *           
	 * @param numOfOnePage
	 *           
	 * @param ts
	 */
	public static <T> List<T> getListOfThePage(int reqPage, int numOfOnePage,
			List<T> ts) {
		int size = ts.size();
		int totalPage = (size - 1) / numOfOnePage + 1;
		if (reqPage > totalPage || reqPage < 1) {
			return Collections.emptyList();
		}
		return ts.subList((reqPage - 1) * numOfOnePage,
				Math.min(reqPage * numOfOnePage, size));
	}

	/**
	 * @param <E>
	 * @param e
	 * @param list
	 * @param cpr
	 * @return
	 */
	public static <E> int binarySearch(E e, List<E> list, Comparator<E> cpr) {
		int low = 0;
		int high = list.size() - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			int cmp = cpr.compare(list.get(mid), e);

			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return mid; // same e found
		}
		return -(low + 1); // not found same e.
	}

	/**
	 * @param <E>
	 * @param e
	 * @param list
	 * @param cpr
	 */
	public static <E> void binaryInsert(E e, List<E> list, Comparator<E> cpr) {
		int idx = binarySearch(e, list, cpr);
		if (idx >= 0) {
			list.add(idx, e);
		} else {
			list.add(-idx - 1, e);
		}
	}

	public static final int[] NUM_PER_PAGE = { 5 };

	/**
	 * 
	 * @param clientFormType
	 *            {@link Const#CLIENT_FORM_TYPE_176_220} and so on.
	 * */
	public static int getDataMaxNumOfPage(byte clientFormType) {
		return NUM_PER_PAGE[clientFormType];
	}










//	/**
//	 * 
//	 * @return
//	 */
//	public static int getSecondsToClock(int paramClock) {
//		long temp = (CalendarUtil.getTimeInMillisWithoutDay(System
//				.currentTimeMillis())) / 1000;
//		int destSeconds = paramClock * 60 * 60;
//		if (temp < destSeconds) {
//			return (int) (destSeconds - temp);
//		} else {
//			return (int) ((GameConst.TIME_DAY / 1000) - (temp - destSeconds));
//		}
//	}

	/**
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	public static long getSecondesToDayAndClock(int year, int month, int day,
			int hour, int minute, int second) {
		Calendar c = Calendar.getInstance();
		long nowseconds = c.getTimeInMillis();
		c.set(year, month - 1, day, hour, minute, second);
		nowseconds = c.getTimeInMillis() - nowseconds;
		c = Calendar.getInstance();
		return nowseconds / 1000;
	}

//	/**
//	 * @return
//	 */
//	public static int getSecondsToClock(int hour, int minute) {
//		long temp = (CalendarUtil.getTimeInMillisWithoutDay(System
//				.currentTimeMillis())) / 1000;
//		int destSeconds = hour * 60 * 60 + minute * 60;
//		if (temp < destSeconds) {
//			return (int) (destSeconds - temp);
//		} else {
//			return (int) ((GameConst.TIME_DAY / 1000) - (temp - destSeconds));
//		}
//	}

	/**
	 * @return
	 */
	public static int getDayInWeek() {
		Calendar cal = Calendar.getInstance();
		int today = cal.get(Calendar.DAY_OF_WEEK) - 1;

		return today;
	}

	public static int getMinute() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MINUTE);
	}

	public static int getHours() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static long getTimeMillis(int hour, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		return cal.getTimeInMillis();
	}

	/**
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static long getTimeMillis(int year, int month, int day, int hour,
			int minute) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, hour, minute);
		return cal.getTimeInMillis();
	}

	/**
	 * @param basedf
	 * @param power
	 * @return
	 */
	public static long getPowerValue(int base, int power) {
		if (power == 0) {
			return 1;
		}
		long value = base;
		for (int i = 0; i < power - 1; i++) {
			value *= base;
		}
		return value;
	}

	/**
	 * @param basedf
	 * @param power
	 * @return
	 */
	public static long getPowerValue(double base, int power) {
		if (power == 0) {
			return 1;
		}
		double value = base;
		for (int i = 0; i < power - 1; i++) {
			value *= base;
		}
		return (long) value;
	}

	/**
	 * @param inVal
	 * @return
	 */
	public long fromDateStringToLong(String inVal) {
		Date date = null; 
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-mm-dd hh:ss");
		try {
			date = inputFormat.parse(inVal); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date.getTime(); 
	}

	public final static int[] LINGZHU_LEVEL_SYNTHEIS_NEED_MONEY = { 400, 900,
			1600, 2500, 3600, 4900, 6400, 8100, 10000, 12100, 14400, 16900,
			19600, 22500, 25600, 28900, 32400 };

	/**
	 * @param level
	 * @return
	 */
	public static int getLingZhuSynthesisNeedMoneyByLevel(int level) {
		return LINGZHU_LEVEL_SYNTHEIS_NEED_MONEY[level - 1];
	}

	public final static int[] WULINGZHU_LEVEL_SYNTHEIS_SUCC_PROBILITY = { 8823,
			7692, 6521, 5454, 4545, 3797, 3191, 2702, 2307, 1986, 1724, 1507,
			1327, 1176, 1048, 940 };

	/**
	 * @param level
	 * @return
	 */
	public static int getWuLingZhuSynthesisSuccProbilityByLevel(int level) {
		return WULINGZHU_LEVEL_SYNTHEIS_SUCC_PROBILITY[level - 1];
	}

	public final static int[] WULINGZHU_LEVEL_SYNTHEIS_NEED_MONEY = { 2000,
			4500, 8000, 12500, 18000, 24500, 32000, 40500, 50000, 60500, 72000,
			84500, 98000, 112500, 128000, 144500 };

	/**
	 * @param level
	 * @return
	 */
	public static int getWuLingZhuSynthesisNeedMoneyByLevel(int level) {
		return WULINGZHU_LEVEL_SYNTHEIS_NEED_MONEY[level - 1];
	}

	public final static short[][] EQUIP_ENCHANTED_SCUSSESS_PROBABILITY = {
			{ 10000, 5000, 5000, 5000, 5000, 5000, 5000, 5000 },
			{ 9000, 5000, 5000, 5000, 5000, 5000, 5000, 5000 },
			{ 8000, 5000, 5000, 5000, 5000, 5000, 5000, 5000 },
			{ 7000, 5000, 5000, 5000, 5000, 5000, 5000, 5000 },
			{ 6000, 5000, 5000, 5000, 5000, 5000, 5000, 5000 },
			{ 5000, 5000, 5000, 5000, 5000, 5000, 5000, 5000 },
			{ 2000, 5000, 4000, 3000, 3000, 2000, 2000, 2000 },
			{ 1000, 5000, 3000, 2500, 2000, 2000, 1500, 1000 },
			{ 500, 4900, 2400, 1700, 1200, 1000, 800, 700 },
			{ 400, 3300, 1600, 1100, 800, 600, 500, 500 },
			{ 300, 2400, 1200, 800, 600, 500, 400, 300 },
			{ 200, 1600, 800, 500, 400, 300, 200, 200 }

	};

	/**
	 * @param index1
	 * @param index2
	 * @return
	 */
	public static short getEquipEnchantedScussProBability(short index1,
			short index2) {
		return EQUIP_ENCHANTED_SCUSSESS_PROBABILITY[index1][index2];
	}

	/**
	 * @param equipLevel
	 * @return
	 */
	public static int getEquipEnchantedNeedMoney(int equipLevel) {
		return (int) (equipLevel / 10) * 500 + 1000;
	}

	/**
	 * @param equipLevel
	 * @return
	 */
	public static int getEequipSynthesisNeedMoney(int equipLevel) {
		return (int) (equipLevel / 10) * 1000 + 1000;
	}

	/**
	 * @param minLevel
	 * @return
	 */
	public final static int[] REFLUSH_ALLY_DRUG_NUM_BY_LEVEL = { 10, 20, 40, 80 };

	public static int getAllyDrugFlushNumByLevel(byte level) {
		return REFLUSH_ALLY_DRUG_NUM_BY_LEVEL[level - 1];
	}

	/**
	 * @param sex
	 * @param putOnEquips
	 * @return
	 */
	public static int getPlayerMoveAnimationIdx(byte sex, int school) {
		int idx = (sex == 0 ? 0 : 4);
		if (school >= 0)
			idx += school;
		return idx;
	}

	public static int randomEquipExtraAddRate(int highQualityChance) {
		if (random(100) < highQualityChance) {
			return random(81, 100);
		} else {
			return random(1, 80);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Map.Entry> mapValueSortedList(Map m,
			Comparator<Map.Entry> cpr) {
		Set<Map.Entry> set = m.entrySet();

		List<Map.Entry> entries = new ArrayList<Map.Entry>(set);
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		Collections.sort(entries, cpr);

		return entries;
	}

	public static final int[] HONORS = { 201, 501, 1001, 2001, 5001, 10001,
			20001, 50001, 100001, Integer.MAX_VALUE };

	/**
	 * @param honor
	 * @return
	 */
	public static byte getHonorTitileIndex(int honor) {
		for (byte i = 0; i < HONORS.length; i++) {
			if (honor < HONORS[i]) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * @param base
	 * @param relative
	 */
	public static void sortRelativeArr(int[] base, int[][] relative) {
		if (base.length > relative.length) {
			throw new IllegalArgumentException(
					"base length must not bigger than relative length!");
		}
		int len = base.length;
		for (int i = 0; i < len; i++) {
			int minIdx = i;
			for (int j = i; j < len; j++) {
				if (base[j] < base[i]) {
					minIdx = j;
				}
			}
			if (minIdx != i) {
				int temp = base[i];
				base[i] = base[minIdx];
				base[minIdx] = temp;

				int[] tempArr = relative[i];
				relative[i] = relative[minIdx];
				relative[minIdx] = tempArr;
			}
		}
	}

	/**
	 * set the bit of dstValue at pos
	 */
	public static int setBit(int dstValue, int pos, int modifyValue) {
		if (modifyValue == 0) {
			dstValue &= ~(1 << pos);
		} else if (modifyValue == 1) {
			dstValue |= (1 << pos);
		}
		return dstValue;
	}

	/**
	 * get the bit of dstValue at pos
	 * 
	 * @param dstValue
	 * @param pos
	 * @return
	 */
	public static byte getBit(int dstValue, int pos) {
		dstValue &= (1 << pos);
		if (dstValue != 0) {
			return (byte) 1;
		} else {
			return (byte) 0;
		}
	}

	/**
	 * @param length
	 * @param num
	 * @return
	 */
	public static int[] randArray(int length, int num) {
		int[] result = new int[length];
		int temp = 0;
		for (int i = 0; i < length; i++) {
			result[i] = random(0, (num - temp));
			if (i == length - 1) {
				result[i] = num - temp;
			}
			temp += result[i];
		}
		return result;

	}

	/**
	 * @return
	 */
	public static Random getRandomObject() {
		return RND;
	}

	public static final int[] ALLY_FOREIGN_NUM_PER_PAGE = { 5, 8, 8, 6, 8, 8 };

	/**
	 * @param clientFormType
	 *            {@link Const#CLIENT_FORM_TYPE_176_220} and so on.
	 * */
	public static int getAllyForeignDataMaxNumOfPage(byte clientFormType) {
		return ALLY_FOREIGN_NUM_PER_PAGE[clientFormType];
	}

	/**
	 * @param colorDropOdds
	 * @param correction
	 * @return
	 */
	public static int randomIndex(int[] odds, double correction) {
		int chn = random(100);
		if (correction == 0) {
			for (int i = odds.length - 1; i != -1; i--) {
				int odd = odds[i];
				if (chn < odd) {
					return i;
				} else {
					chn -= odd;
				}
			}
		} else {
			for (int i = odds.length - 1; i != -1; i--) {
				int odd = (int) (odds[i] * correction);
				if (chn < odd) {
					return i;
				} else {
					chn -= odd;
				}
			}
		}
		return -1;
	}

	/**
	 * @param colorDropOdds
	 * @param correction
	 * @return
	 */
	public static int randomIndex(int[][] odds_val, int chnIdx, int valIdx,
			double correction) {
		int chn = random(100);
		if (correction == 1) {
			for (int i = odds_val.length - 1; i != -1; i--) {
				int odd = odds_val[i][chnIdx];
				if (chn < odd) {
					return odds_val[i][valIdx];
				} else {
					chn -= odd;
				}
			}
		} else {
			for (int i = odds_val.length - 1; i != -1; i--) {
				int odd = (int) (odds_val[i][chnIdx] * correction);
				if (chn < odds_val[i][chnIdx]) {
					return odds_val[i][valIdx];
				} else {
					chn -= odd;
				}
			}
		}
		return -1;
	}

	public static int calcTotalPage(int allNum, int perNum) {
		int pageNum = allNum / perNum;
		if (allNum % perNum != 0) {
			pageNum += 1;
		}
		if (pageNum <= 0) {
			pageNum = 1;
		}
		return pageNum;
	}

	public static int randomOneDif(int[] probs) {
		int sum = 0;
		for (int prob : probs) {
			sum += prob;
		}
		int chn = MathUtils.random(sum);
		for (int i = probs.length - 1; i != -1; i--) {
			int odd = probs[i];
			if (chn < odd) {
				return i;
			} else {
				chn -= odd;
			}
		}
		return 0;
	}

	public static int[] randomSome(int[] probs, int num) {
		if (num <= 0 || num > probs.length) {
			return null;
		}
		if (num == probs.length) {
			return probs;
		}

		int[] idx = new int[num];
		for (int i = 0; i < num; i++) {
			idx[i] = randomOneDif(probs);
			probs[idx[i]] = 0;

			// int[] probsTmp = new int[probs.length-1];
			// int k = 0;
			// for (int j = 0; j < probs.length; j++) {
			// if (j != idx[i]) {
			// probsTmp[k++] = probs[j];
			// }
			// }
			// probs = probsTmp;
		}

		return idx;
	}
	/**
	 * 随机一个id
	 * @param ids id数组
	 * @param rate 几率数组
	 * @param num 几率上限
	 * @return
	 */
	public static int getRandomId(int ids[],int rate[],int num){
		//随机0-99
		int total=0;
		for(int i=0;i<rate.length;i++){
			total+=rate[i];
		}
		if(total==0){
			return -1;
		}
		int newRate[]=new int[rate.length];
		if(total>num){//重新计算每一个的新的几率.
			for(int i=0;i<newRate.length;i++){
				newRate[i]=rate[i]*num/total;
			}
		}else{
			newRate=rate;
		}
		int random=MathUtils.random(num)+1;
		int r=0;
//		logger.info("getRandomId random="+random);
		for(int i=0;i<ids.length;i++){
//			logger.info("id="+ids[i]+" newRate="+newRate[i]+" r="+r);
			r+=newRate[i];
			if(random<=r){
				logger.info("propsbox return "+ids[i]);
				return ids[i];
			}
		}
		return -1;
	}
	//随机一个min-max之间的数值，不包含 num
	public static int randomNoInclude(int min, int max,int num) {
		
		int n=random(min,max);
		int i=0;
		while(n==num){
			n=random(min,max);
			i++;
			if(i==10){
				if(num==min){
					return max;
				}else if(num==max){
					return min;
				}else{
					return min;
				}
			}
		}
		return n;
	}
	/**
	 * 获得两个数的最大公约数
	 * @param m
	 * @param n
	 * @return
	 */
	 public static int GCD(int m, int n){
        int r;
        int s;
        s = m * n;
        if (m < n) {
            r = m;
            m = n;
            n = r;
        }
        while (n != 0) {
            r = m % n;
            m = n;
            n = r;
        }
        return m;
    }

	/**
	 * a 的 b 次方
	 * @param a
	 * @param b
	 * @return
	 */
	public static double pow(double a,double b){
		return Math.pow(a, b);
	}
	
	/**
	 * 向下取整数 2.x --> 2.0
	 * @param a
	 * @return
	 */
	public static double floor(double a) {
		return Math.floor(a);
	}
	
	/**
	 * 四舍五入
	 * @param a
	 * @return
	 */
	public static double round(double a) {
		return Math.round(a);
	}
	
	/**
	 * 向上取整 2.x --> 3.0
	 * @param a
	 * @return
	 */
	public static double ceil(double a) {
		return Math.ceil(a);
	}
	
	/**
	 * 除法运算
	 * @param dividend
	 * @param divisor
	 * @return
	 */
	public static int divisionInt(int dividend,int divisor){
		if(divisor == 0){
			System.out.println(" divisor = "+ divisor);
			return 0;
		}else{
			return dividend/divisor;
		}
	}
}
