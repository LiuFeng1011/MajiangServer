package com.dreamgear.majiang.game.notice;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.game.GameThread.GameThread;
import com.dreamgear.majiang.game.GameThread.ThreadWork;
import com.dreamgear.majiang.game.player.GamePlayerManager;
import com.dreamgear.majiang.game.player.PlayerManager;
import com.dreamgear.majiang.game.server.resp.NoticeResp;

public class NoticeMgr implements ThreadWork{

	public static final int NOTICE_TYEP_BOARD = 1; //滚屏公告
	
	
	static final Logger logger = LoggerFactory.getLogger(NoticeMgr.class);

	private Map<Long,GameNotice> noticeList = new HashMap<Long,GameNotice>();

	private static NoticeMgr instance;
	public static NoticeMgr getInstance() {
		if (instance == null) {
			instance = new NoticeMgr();
		}
		return instance;
	}

	public static GameNotice sendNotice;
	private NoticeMgr() {
		List<GameNotice> list = DBManager.getInstance().getNoticeDAO().getNoticeDataList();

		for(int i = 0 ; i < list.size() ; i ++){
			GameNotice notice = list.get(i);
			noticeList.put(notice.getId(), notice);
		}
	}
	
	public void RemoveNotice(long nid){
		if(noticeList.containsKey(nid)){
			sendNotice = new GameNotice();
			GameNotice remnotice = noticeList.get(nid);
			
			sendNotice.setId(remnotice.getId());
			sendNotice.setNoticeType(0);
			
			noticeList.remove(nid);
			DBManager.getInstance().getNoticeDAO().deleteNotice(nid);
		}
		
		SendNotice(sendNotice);
		return;
	}
	public void AddNotice(GameNotice notice){

		if(notice.noticeType == 0){
			if(noticeList.containsKey(notice.getId())){
				noticeList.remove(notice.getId());
				DBManager.getInstance().getNoticeDAO().deleteNotice(notice.getId());
			}
			return;
		}
		
		
		if(noticeList.containsKey(notice.getId())){
			//update
			DBManager.getInstance().getNoticeDAO().updateNotice(notice);
		}else{
			//add
			DBManager.getInstance().getNoticeDAO().addNotice(notice);
		}
		
		SendNotice(notice);
		
		noticeList.put(notice.getId(), notice);
		
	}
	
	public void SendNotice(GameNotice notice){

		sendNotice = notice;
		GameThread gt = new GameThread();
		gt.setWork(this);
		gt.start();
	}
	
	
	public Map<Long, GameNotice> getNoticeList() {
		return noticeList;
	}
	public void setNoticeList(Map<Long, GameNotice> noticeList) {
		this.noticeList = noticeList;
	}
	
	public void doWork() {
		// TODO Auto-generated method stub
		
        if(sendNotice == null){
        	return;
        }

		NoticeResp resp = new NoticeResp();
		resp.noticeList = new ArrayList<GameNotice>();
		resp.noticeList.add(sendNotice);
		
		Iterator<Long> it = GamePlayerManager.GetInstance().getPlayerMap().keySet().iterator();
		while (it.hasNext()) {
			Long key = it.next();
			PlayerManager p = GamePlayerManager.GetInstance().getPlayerMap().get(key);
			if (p != null) {
				//SendMsgToPlayer(p.getUi(),msg);
				resp.setUserInfo(p.getUi());
				resp.Send();
			}
			try {
				// 线程发送时，暂停10ms
				Thread.sleep(10L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void Test(){
		GameNotice notice = new GameNotice();
		notice.setId(12);
		notice.setNoticeType(0);//删除
		notice.setNoticeTitle("bbbb");
		notice.setNoticeContent("aaaaaaaaaaaaa");
		
		notice.setStartTime(33333);

		notice.setEndTime(44444);
		
		notice.setTimeDelay(5);
		
		this.AddNotice(notice);
	}

	
	
}
