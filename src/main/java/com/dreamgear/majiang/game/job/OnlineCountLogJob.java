package com.dreamgear.majiang.game.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.dreamgear.majiang.game.log.LogData;
import com.dreamgear.majiang.game.log.LogEventID;
import com.dreamgear.majiang.game.player.GamePlayerManager;

/**
 * 定时统计在线人数
 * @author liufeng
 *
 */
public class OnlineCountLogJob implements Job{
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub

		int onlinecount = GamePlayerManager.GetInstance().GetOnlinePlayerCount();
		System.out.println("OnlineCountLogJob onlinecount : " + onlinecount);
		LogData.AddLog(null, LogEventID.EVE_ONLINE_COUNT, onlinecount, "");
	}
}
