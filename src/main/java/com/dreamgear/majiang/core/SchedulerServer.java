package com.dreamgear.majiang.core;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.List;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamgear.majiang.common.GameConst;

/**
 * 计划任务服务器
 * @author mingming
 * @date 2017年2月8日-下午4:17:29
 * @desc:
 */
public class SchedulerServer {
	static final Logger logger = LoggerFactory.getLogger(SchedulerServer.class);
	static Scheduler sched;
	
	public SchedulerServer() {
		try {
			SchedulerFactory sf = new StdSchedulerFactory(GameConst.SCHEDULE_PROPERTIES);
			sched = sf.getScheduler();
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("scheduler run  error");
			e.printStackTrace();
		}
	}
	
	public void del(JobKey jobkey) throws SchedulerException{
		sched.deleteJob(jobkey);
	}
	
	/**
	 * 运行任务服务器
	 * 
	 * @throws Exception
	 */
	public void run() throws Exception {
		sched.start();
	}

	/**
	 * 加入计划任务
	 * @param c
	 * @param groupName
	 * @param cronString
	 */
	public void addJob(Class c,String cronString) {
		try {
			JobDetail job = null;
			CronTrigger trigger = null;
			job = newJob(c).withIdentity("job_"+c.getName(), "group_"+c.getName()).build();
			trigger = newTrigger().withIdentity("trigger_"+c.getName(),"group_"+c.getName())
					.withSchedule(cronSchedule(cronString)).build();
			sched.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 停止计划任务
	 */
	public void stop(){
		try {
			sched.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	public List<String> getGroupNames(){
		List<String> names=null;
		try {
			names = sched.getJobGroupNames();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return names;
	}
}
//0 0/30 * * * ? 每30分钟一次
//附表：
//"0 0 12 * * ?" 每天中午12点触发
//"0 15 10 ? * *" 每天上午10:15触发
//"0 15 10 * * ?" 每天上午10:15触发
//"0 15 10 * * ? *" 每天上午10:15触发
//"0 15 10 * * ? 2005" 2005年的每天上午10:15触发
//"0 * 14 * * ?" 在每天下午2点到下午2:59期间的每1分钟触发
//"0 0/5 14 * * ?" 在每天下午2点到下午2:55期间的每5分钟触发
//"0 0/5 14,18 * * ?" 在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发
//"0 0-5 14 * * ?" 在每天下午2点到下午2:05期间的每1分钟触发
//"0 10,44 14 ? 3 WED" 每年三月的星期三的下午2:10和2:44触发
//"0 15 10 ? * MON-FRI" 周一至周五的上午10:15触发
//"0 15 10 15 * ?" 每月15日上午10:15触发
//"0 15 10 L * ?" 每月最后一日的上午10:15触发
//"0 15 10 ? * 6L" 每月的最后一个星期五上午10:15触发
//"0 15 10 ? * 6L 2002-2005" 2002年至2005年的每月的最后一个星期五上午10:15触发
//"0 15 10 ? * 6#3" 每月的第三个星期五上午10:15触发 