package com.dreamgear.majiang.game.player;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamgear.majiang.common.GameTools;
import com.dreamgear.majiang.common.GameCommon.enPlayerState;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.utils.TimeUtils;
import com.dreamgear.majiangserver.net.UserInfo;

public class GamePlayerManager {
	private static final Logger logger = LoggerFactory.getLogger(GamePlayerManager.class);

	private static GamePlayerManager instance;
	
	public static GamePlayerManager GetInstance(){
		if(instance == null){
			instance = new GamePlayerManager();
		}
		return instance;
	}

	// 玩家保存队列
	private Queue<PlayerManager> saveQueue = new ConcurrentLinkedQueue<PlayerManager>();

	private ConcurrentHashMap<Long,PlayerManager> playerMap = new ConcurrentHashMap<Long,PlayerManager>();
	
	public void Init(){
		
	}
	
	public ConcurrentHashMap<Long, PlayerManager> getPlayerMap() {
		return playerMap;
	}

	public void setPlayerMap(ConcurrentHashMap<Long, PlayerManager> playerMap) {
		this.playerMap = playerMap;
	}

	public PlayerManager GetPlayerByUid(Long uid){
		return playerMap.get(uid);
	}
	
	public void AddPlayer(PlayerManager pm ){
		synchronized (playerMap) {
			if (!playerMap.containsKey(pm.getPd().getUid())) {
				playerMap.put(pm.getPd().getUid(), pm);
			}
		}
	}
	/**
	 * 获取一个玩家 
	 * @param uname
	 * @return
	 */
	public PlayerManager GetPlayer(long uid){
		
		PlayerManager pm = GetPlayerByUid(uid);
		
		//内存中不存在
		if(pm == null){
			//从数据库中获取
			PlayerData pd = DBManager.getInstance().getPlayerDAO().getPlayerData(uid);
			if(pd == null){
				//数据库中也不存在
				return null;
			}
			logger.info("内存中不存在，数据库中存在用户 : " + uid);
			pm = new PlayerManager(pd);
			GamePlayerManager.GetInstance().AddPlayer(pm);
		}else{
			logger.info("内存中存在用户 : " + uid);
		}

		return pm;
	}

	/**
	 * 创建玩家
	 * @param uname
	 * @return
	 */
	public PlayerData CreatePlayer(Long uid) {

		logger.info("创建新用户 : " + uid);
		PlayerData data = new PlayerData();
		data.setUid(uid);
		data.setCreateTime(TimeUtils.nowLong());
		data.setGold(1500);
		
		DBManager.getInstance().getPlayerDAO().addPlayer(data);
		return data;
	}
	
	public void Tick(){
		saveRole();
		Iterator<Long> it = playerMap.keySet().iterator();
		while (it.hasNext()) {
			Long key = it.next();
			PlayerManager p = playerMap.get(key);
			if (p != null) {
				p.Tick();
				if(p.getState() == enPlayerState.offline){
					AddSaveRole(p);
				}
			}
		}
	}

	public boolean saveRole() {
		PlayerManager pc = saveQueue.poll();
		if (pc == null) {
			return true;
		}
		try {
			pc.SaveDataToDB();
			if (pc.getState() == enPlayerState.offline) {
				removeRole(pc);
				//System.out.println("world save role,id=" + pc.getId() + " consume time(毫秒)=" + (TimeUtils.nowLong() - time));
			}
			
		} catch (Exception e) {
			logger.error("SAVE ROLE ERROR : ", e);
		}
		return false;

	}
	
	public boolean AddSaveRole(PlayerManager pm) {
		if (pm != null) {
			try {
				if (!saveQueue.contains(pm)) {
					saveQueue.offer(pm);
					//logger.info("超时保存，uid=" + pm.getPd().getUid() + " saveQueue size=" + saveQueue.size());
				}
				return true;
			} catch (Exception e) {
				// logger.error("save player[" + pc.getId() + "][" +
				// pc.getName() +"] error...");
			}
		}
		return false;
	}
	
	//从内从中删除用户时调用
	public void removeRole(PlayerManager pc) {
		// logger.info("remove role ,uid=" + pc.getId());
		synchronized (playerMap) {
			if (playerMap.containsKey(pc.getPd().getUid())) {
				pc.Leave();
				playerMap.remove(pc.getPd().getUid());// 从游戏世界中删除
			}
		}
	}
	
	//玩家短线时调用
	public void PlayerLeave(long uid){
		PlayerManager pm = GetPlayerByUid(uid);
		if(pm != null) pm.Offline();
	}
	
	public int GetOnlinePlayerCount(){
		return playerMap.size();
	}
	
	public String SendMsgToPlayer(long uid,String msg){
		String ret = "";
		PlayerManager p = this.GetPlayerByUid(uid);
		if(p != null) {
			SendMsgToPlayer(p.getUi(), msg);
			ret = "发送成功!";
		}else{
			ret = "玩家不在线或不存在 : " + uid;
		}
		
		return ret;
	}
	
	public String SendMsgToPlayer(UserInfo ui,String msg){
		String ret = "";
		GameTools.SendMessage(ui, msg);
		return ret;
	}
	
	public String SendMsgToAll(String msg){
		int count = 0;
		Iterator<Long> it = playerMap.keySet().iterator();
		while (it.hasNext()) {
			Long key = it.next();
			PlayerManager p = playerMap.get(key);
			if (p != null) {
				SendMsgToPlayer(p.getUi(),msg);
				count++;
			}
		}
		return "成功向"+count+"位玩家发送了消息";
	}
}
