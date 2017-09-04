package com.dreamgear.majiang.game.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.dreamgear.majiang.game.rank.RankManager;

public class RankJob implements Job {
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		RankManager.GetInstance().Accounts();
	}
	
}
