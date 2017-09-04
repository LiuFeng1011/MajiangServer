package com.dreamgear.http.handler.impl.gmfunction;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.domain.GMFunctionRetData;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.majiang.game.game.GameRoomData;
import com.dreamgear.majiang.game.game.RoomManager;
import com.dreamgear.majiang.game.notice.GameNotice;
import com.dreamgear.majiang.game.notice.NoticeMgr;
import com.dreamgear.majiang.game.player.GamePlayerManager;

public class Notice extends BaseFunction{

	@Override
	public GMFunctionRetData handle(HttpRequestMessage request) {
		String noticeId = request.getParameter("noticeId");	/*Y	公告唯一编号*/
		String noticeType = request.getParameter("noticeType");	/*Y	公告类型：1-滚屏公告
		当取值为0时，表示删除当前唯一编号的所有记录。*/
		String noticeTitle = request.getParameter("noticeTitle");	/*Y	公告标题*/
		String noticeContent = request.getParameter("noticeContent");	/*Y	公告内容*/
		String startTime = request.getParameter("startTime");	/*Y	公告开始时间*/
		String endTime = request.getParameter("endTime");	/*Y	公告结束时间。当取值为null时表示永不过期*/
		String timeDelay = request.getParameter("timeDelay");	/*Y	显示间隔时间。单位：秒*/
		
		if("0".equals(noticeType)){
			System.out.println("remove notice : " + noticeId + "\n");
			
			NoticeMgr.getInstance().RemoveNotice(Long.parseLong(noticeId));
			
			GMFunctionRetData ret = new GMFunctionRetData();
			ret.code = 1;
			ret.data = "删除成功";
			return ret;
		}
		

		GameNotice notice = new GameNotice();
		notice.setNoticeType(Integer.parseInt(noticeType));
		notice.setNoticeTitle(noticeTitle);
		notice.setNoticeContent(noticeContent);
		
		long lStartTime;
		if("".equals(startTime )){
			lStartTime = -1;
		}else{
			lStartTime = Timestamp.valueOf(startTime).getTime();
		}
		notice.setStartTime(lStartTime);

		long lEndTime ;
		if("".equals(endTime)){
			lEndTime = -1;
		}else{
			lEndTime = Timestamp.valueOf(endTime).getTime();
		}

		notice.setEndTime(lEndTime);
		
		if(!"".equals(timeDelay)){
			notice.setTimeDelay(Integer.parseInt(timeDelay));
		}

		NoticeMgr.getInstance().AddNotice(notice);

		GMFunctionRetData ret = new GMFunctionRetData();
		ret.code = 1;
		ret.data = "添加成功";
		return ret;
	}
}
