package com.dreamgear.majiang.game.rank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.domain.LoginData;
import com.dreamgear.majiang.common.GameConfig;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.game.player.GamePlayerManager;
import com.dreamgear.majiang.game.player.LoginManager;
import com.dreamgear.majiang.game.player.PlayerData;
import com.dreamgear.majiang.game.player.PlayerManager;
import com.dreamgear.majiang.utils.TimeUtils;

public class RankManager {
	private static final Logger logger = LoggerFactory.getLogger(RankManager.class);

	private static RankManager instance;
	
	public static RankManager GetInstance(){
		if(instance == null){
			instance = new RankManager();
		}
		return instance;
	}

	List<RankData> rankList = new Vector<RankData>();
	String rankString = "";
	public final RankGiftManager rankGiftManager = new RankGiftManager();
	
	public static final int max_count = 30;
	
	private RankManager(){
		
		//从数据库取出全部数据
		List<RankData> ranklist = DBManager.getInstance().getRankDAO().getRankDataList(TimeUtils.getTimeByDate(TimeUtils.getBeginDayOfWeek()));
		
		rankList.addAll(ranklist);

		//添加头像和昵称数据
		for(int i = 0 ; i < rankList.size() ; i ++){
			if(i >= GameConfig.rank_max_count) break;
			RankData data = rankList.get(i);
			LoginData ld = LoginManager.GetInstance().GetLoginData(data.uid);
			
			if(ld == null){
				data.setUname(data.uid+"");
				data.setHeadurl("");
			}else{
				data.setUname(ld.nickname);
				data.setHeadurl(ld.head);
			}
		}
		
		rankGiftManager.Init();
		SetRankStringData();
	}
	
	//更新分数
	public void AddScores(PlayerManager pm,int scores){
		if(scores == 0) return;
		
		int rank = GetPlayerRank(pm.getPd().getUid());
		
		if(rank == -1){
			RankData data = new RankData();
			data.setUid(pm.getPd().getUid());
			data.setScores(scores);
			data.setUname(pm.nickname);
			data.setHeadurl(pm.headimgurl);
			
			InsertData(data,true);
		}else{
			RankData data = rankList.get(rank-1);
			data.setScores(data.getScores() + scores);
			rankList.remove(rank-1);
			
			InsertData(data,false);
		}
	}
	
	//获取排行榜数据
	public String GetRankData(long selfuid){
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("data", rankString);
		int selfrank = GetPlayerRank(selfuid);
		data.put("selfrank", selfrank);
		if(selfrank != -1){
			data.put("selfdata", rankList.get(selfrank-1));
		}
		
		return JSON.toJSONString(data);
	}
	
	//获取一个玩家的排名
	public int GetPlayerRank(long uid){
        synchronized (rankList) {
        	for(int i = 0 ; i < rankList.size() ; i ++){
        		if(rankList.get(i).getUid() == uid){
        			return i+1;
        		}
        	}
        }
        return -1;
	}
	
	//结算
	public void Accounts(){
		synchronized (rankList) {
			for(int i = 0 ; i < rankList.size() ; i ++){
				RankData data = rankList.get(i);

				PlayerManager pm = GamePlayerManager.GetInstance().GetPlayer(data.uid);
				
				if(pm == null){
					continue;
				}
				
				int count = this.rankGiftManager.GetGiftCount(i+1);
				if(count == -1) break;
				pm.AddGold(count);
        	}
		}
		rankList.clear();
//		DBManager.getInstance().getRankDAO().clearRank();
		SetRankStringData();
	}
	
	//插入数据
	void InsertData(RankData data,boolean isnew){
		logger.info("InsertData : " + JSON.toJSONString(data));
		boolean isfind = false;
		synchronized (rankList) {
        	for(int i = 0 ; i < rankList.size() ; i ++){
        		if(rankList.get(i).scores < data.scores){
        			rankList.add(i, data);
        			isfind = true;
        			if(i < 30){
        				SetRankStringData();
        			}
        			break;
        		}
        	}
        }
		if(!isfind){
			rankList.add(data);
			if(rankList.size() <= 30){
				SetRankStringData();
			}
		}
		
		
		if(isnew) {
			DBManager.getInstance().getRankDAO().addRank(data);
		}else{
			DBManager.getInstance().getRankDAO().updateRank(data);
		}
	}
	
	//设置排行字符串
	void SetRankStringData(){
		
		List<RankData> data;
		if(rankList.size() > 0){
			data = rankList.subList(0, Math.min(max_count, rankList.size()));
		}else{
			data = new ArrayList<RankData>();
		}
		rankString = JSON.toJSONString(data);
	}

	/**
	 * test
	 * @param args
	 * @throws Exception
	 */
    public static void main( String[] args ) throws Exception
    {
		DBManager.getInstance().init();
		
    	RankManager.GetInstance();
    	logger.info("data : " + RankManager.GetInstance().GetRankData(1));
    	for(int i = 1000 ; i < 1001 ; i ++){
    		PlayerData pd = new PlayerData();
        	pd.setUid(i);
        	PlayerManager pm = new PlayerManager(pd);
        	
        	RankManager.GetInstance().AddScores(pm,(int)(Math.random() * 1000));
    	}
    	logger.info("data : " + RankManager.GetInstance().GetRankData(1000));
    	
    }
}
