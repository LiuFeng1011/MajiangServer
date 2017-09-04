package com.dreamgear.majiang.game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.dreamgear.majiang.utils.TimeUtils;

/**
 * 游戏主循环消耗监视
 * 
 * @author ShaoLong Wang
 */
public class WorldMonitor {

	static long MAIN_LOOP_TPS = 5; // 主循环每秒间隔
	static long MAIN_LOOP_INTERVAL = 1000 / MAIN_LOOP_TPS; // 每次主循环的最大固定消耗时间
	public static long PRINT_WORLD_MONITOR_TICK_COUNT = 10000;
	static final int MAX_MONITOR_NUM = 50;
	static List<WorldMonitor> monitors = new LinkedList<WorldMonitor>();

	static WorldMonitor peakMonitor;
	static long avarTimeElapse;
	
	Map<String , Long> consumeTimes = new HashMap<String , Long>();

	long startTime;// 起始时间

	long timeElapse;// 每次循环的实际消耗时间

	/**
	 * 开始计时监视
	 */
	public void start() {
		startTime = TimeUtils.nowLong();
	}

	/**
	 * 停止计时监视
	 */
	public void stop() {
		timeElapse = TimeUtils.nowLong() - startTime;
		
		if(timeElapse <= 0){
			//时间过长
		}
		
		// addMonitor(this);
	}
	
	public void RecordTime(String title,long time){
		consumeTimes.put(title, time);
	}

	/**
	 * 得到计时剩余
	 * 
	 * @return
	 */
	public long getTimeRemain() {
		long remain = MAIN_LOOP_INTERVAL - timeElapse;
		return remain < 0 ? 0 : remain;
	}

	/**
	 * 添加监视
	 * 
	 * @param monitor
	 */
	public static void addMonitor(WorldMonitor monitor) {
		if (monitors.size() > MAX_MONITOR_NUM) {
			monitors.remove(0);
		}

		monitors.add(monitor);

		boolean isPeak = false;
		if (peakMonitor == null) {
			peakMonitor = monitor;
			isPeak = true;
		} else {
			if (monitor.timeElapse > peakMonitor.timeElapse) {

				peakMonitor = monitor;
				isPeak = true;

			}
		}

		if (isPeak) {
			//system.out.println("peak:" + WorldMonitor.peakMonitor);
		}

	}

	@Override
	public String toString() {
		return JSON.toJSONString(consumeTimes);
	}

}
