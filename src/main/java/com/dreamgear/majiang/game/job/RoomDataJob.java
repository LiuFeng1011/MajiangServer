package com.dreamgear.majiang.game.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.dreamgear.majiang.common.GameConst;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.game.game.RoomManager;
import com.dreamgear.majiang.utils.TimeUtils;

/**
 * 每天定时清理过期房间数据
 * @author liufeng
 *
 */
public class RoomDataJob implements Job{

	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		RoomManager.GetInstance().DelCloseRoom();
	}
	
}
