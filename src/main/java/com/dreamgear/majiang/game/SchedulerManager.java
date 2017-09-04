package com.dreamgear.majiang.game;

import org.quartz.JobKey;
import org.quartz.SchedulerException;

import com.dreamgear.majiang.common.GameConfig;
import com.dreamgear.majiang.core.SchedulerServer;
import com.dreamgear.majiang.game.job.OnlineCountLogJob;
import com.dreamgear.majiang.game.job.RankJob;
import com.dreamgear.majiang.game.job.RoomDataJob;


/**
 * 计划任务管理器
 * @author mingming
 * @date 2017年2月8日-下午4:16:12
 * @desc:
 */
public class SchedulerManager {

	public static SchedulerServer scheduler;
	
	public static void schedule(SchedulerServer schedulerServer) {
		scheduler = schedulerServer;
		
		if(GameConfig.test_server == 1) return;
		scheduler.addJob(RoomDataJob.class, "0 0 0 * * ?"); //每天0点触发一次
		scheduler.addJob(OnlineCountLogJob.class, "0 0 * * * ?"); //每小时触发一次 统计在线人数
		scheduler.addJob(RankJob.class, "0 0 0 * * MON"); //每个周一结算排行榜奖励
		
		try {
			scheduler.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加一个定时消息任务规则
	 */
	public static void addCrontabRule(Class c,String rule){
		if(SchedulerManager.scheduler != null){
			SchedulerManager.scheduler.addJob(c, rule);
		}
	}
	/**
	 * 删除一条消息任务
	 * @return
	 * @throws SchedulerException 
	 */
	public static boolean removeCrontabRule(JobKey jobkey) throws SchedulerException{
		boolean result = false;
		if(SchedulerManager.scheduler != null){
			SchedulerManager.scheduler.del(jobkey);
		}
		
		return result;
	}
	
	public static void main(){
		
	}
}
