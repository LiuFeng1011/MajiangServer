package com.dreamgear.majiang.game.rank;

import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.game.player.GamePlayerManager;

public class RankGiftManager {
	private static final Logger logger = LoggerFactory.getLogger(RankGiftManager.class);


	List<RankGiftData> rankGiftList = new Vector<RankGiftData>();
	
	
	public void Init(){
		//从数据库取出全部数据
		List<RankGiftData> ranklist = DBManager.getInstance().getRankDAO().getRankGiftDataList();

		rankGiftList.addAll(ranklist);
	}
	
	//添加或修改一个数据
	public void AddData(int id,int min , int max ,int count){
		logger.info("id : " + id + "  min : " + min + "  max : " + max + "  count : " + count  );
		//新增
		if(id == -1){
			RankGiftData data = new RankGiftData();
			data.setRank_min(min);
			data.setRank_max(max);
			data.setCount(count);
			DBManager.getInstance().getRankDAO().addRankGift(data);
			
			synchronized (rankGiftList) {
	        	for(int i = 0 ; i < rankGiftList.size() ; i ++){
	        		if(rankGiftList.get(i).getRank_min() > min){
	        			rankGiftList.add(i, data);
	        			return;
	        		}
	        	}
    			rankGiftList.add( data);
    			return;
	        }
		}
		
		//修改
		synchronized (rankGiftList) {
        	for(int i = 0 ; i < rankGiftList.size() ; i ++){
        		if(rankGiftList.get(i).getId() == id){
        			rankGiftList.get(i).setRank_min(min);
        			rankGiftList.get(i).setRank_max(max);
        			rankGiftList.get(i).setCount(count);
        			DBManager.getInstance().getRankDAO().updateRankGift(rankGiftList.get(i));
        			return;
        		}
        	}
		}
	}
	//删除一个数据
	public void DelData(int id){
		logger.info("del : " + id);
		synchronized (rankGiftList) {
        	for(int i = 0 ; i < rankGiftList.size() ; i ++){
        		if(rankGiftList.get(i).getId() == id){
        			rankGiftList.remove(i);
        			DBManager.getInstance().getRankDAO().deleteRankGift(id);
        			return;
        		}
        	}
		}
	}
	//获取数据
	public String GetDataString(){
		return JSON.toJSONString(rankGiftList);
	}
	
	//根据排名获取奖励
	public int GetGiftCount(int rank){
		synchronized (rankGiftList) {
        	for(int i = 0 ; i < rankGiftList.size() ; i ++){
        		if(rankGiftList.get(i).getRank_min() <= rank && 
        				rankGiftList.get(i).getRank_max() >= rank){
        			return rankGiftList.get(i).getCount();
        		}
        	}
		}
		return -1;
	}
	
}
