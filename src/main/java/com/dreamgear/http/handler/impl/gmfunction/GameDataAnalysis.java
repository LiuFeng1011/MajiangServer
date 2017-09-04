package com.dreamgear.http.handler.impl.gmfunction;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.domain.GMFunctionRetData;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.game.log.LogData;
import com.dreamgear.majiang.game.player.PlayerAddByDay;
import com.dreamgear.majiang.game.player.PlayerRemain;

public class GameDataAnalysis extends BaseFunction {
	private static final Logger logger = LoggerFactory.getLogger(GameDataAnalysis.class);

	@Override
	public GMFunctionRetData handle(HttpRequestMessage request) {
		String function = request.getParameter("function");
		GMFunctionRetData ret = new GMFunctionRetData();
		if(function == null || function.equals("")){
			ret.code = 0;
			ret.data = "功能参数错误";
			return ret;
		}
		
		if(function.equals("GetOnlineCount")){
			//查看实时在线人数
			GetOnlineCount(request,ret);
		}else if(function.equals("GetUserRemain")){
			//查看每日留存和新增
			GetUserRemain(request,ret);
		}
		
		return ret;
	}
	//查看实时在线人数
	public void GetOnlineCount(HttpRequestMessage request,GMFunctionRetData ret){
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		long lStartTime;
		if("".equals(startTime )){
			lStartTime = -1;
		}else{
			lStartTime = Timestamp.valueOf(startTime).getTime();
		}
		
		long lEndTime ;
		if("".equals(endTime)){
			lEndTime = -1;
		}else{
			lEndTime = Timestamp.valueOf(endTime).getTime();
		}
		
		if(lStartTime > lEndTime){
			ret.code = 0;
			ret.data = "开始时间小于结束时间";
			return ;
		}

		logger.info("lStartTime : " + startTime);
		logger.info("lEndTime : " + endTime);
		List<LogData> data = DBManager.getInstance().getLogDAO().GetOnlinePlayerCountByDay(lStartTime,lEndTime);
		ret.code = 1;
		ret.data = JSON.toJSONString(data);
	}

	//查看每日留存和新增
	public void GetUserRemain(HttpRequestMessage request,GMFunctionRetData ret){
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		long lStartTime;
		if("".equals(startTime )){
			lStartTime = -1;
		}else{
			lStartTime = Timestamp.valueOf(startTime).getTime();
		}
		
		long lEndTime ;
		if("".equals(endTime)){
			lEndTime = -1;
		}else{
			lEndTime = Timestamp.valueOf(endTime).getTime();
		}
		
		if(lStartTime > lEndTime){
			ret.code = 0;
			ret.data = "开始时间小于结束时间";
			return ;
		}
		
		List<PlayerRemain> data = DBManager.getInstance().getPlayerDAO().GetGamePlayerRemain(lStartTime,lEndTime);
		
		ret.code = 1;
		ret.data = JSON.toJSONString(data);
	}
}
