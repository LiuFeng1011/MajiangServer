package com.dreamgear.majiang.game.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamgear.majiang.common.GameCommon;
import com.dreamgear.majiang.common.GameConst;
import com.dreamgear.majiang.common.GameTools;
import com.dreamgear.majiang.game.player.PlayerManager;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiang.game.server.resp.RoomResp;

public class RoomManager {
	private static final Logger logger = LoggerFactory.getLogger(RoomManager.class);

	//房间列表
	private HashMap<Integer,Room> roomMap = new HashMap<Integer,Room>();
	
	//key :玩家id  value : 房间id
	private HashMap<Long,Integer> playerRoom = new HashMap<Long,Integer>();

	public static RoomManager instance = null;
	
	public static RoomManager GetInstance(){
		if(instance == null){
			instance = new RoomManager();
		}
		return instance;
	}
	
	public void Update(){
		for (Entry<Integer, Room> entry : roomMap.entrySet()) {
			entry.getValue().Update();
		}
	}
	
	//创建房间
	public Room CreateRoom(PlayerManager pm,int roomtype,int ispao,int findtype){
//		Room oldr = pm.getRoom();
//		if(oldr != null){
//			if( oldr.IsOpen()) {
//				InRoom(pm,oldr.roomId);
//				return oldr;
//			}
//		}
//		
//		LeaveRoom(pm);
		Room r = new Room(pm,roomtype,ispao,findtype);
		roomMap.put(r.roomId, r);
		InRoom(pm,r.roomId);
		
		return r;
	}
	
	//随机进入房间
	public int InRandomRoom(PlayerManager pm,int ispao,int findtype){
		for (Map.Entry<Integer,Room> entry : roomMap.entrySet()) {  
			Room r = entry.getValue();
			if(!r.IsOpen() || //房间是否开启
					r.IsPlaying() || //房间是否正在游戏
					r.getFindtype() != GameCommon.enRoomFindType.random || //是不是随机房间
					r.getIspao().ordinal() == ispao || //点炮类型匹配
					r.GetDownPlayerCount() < GameConst.MAX_PLAYER_COUNT //是否有空位
					){
				continue;
			}

			if(r.GetDownPlayerCount() < GameConst.MAX_PLAYER_COUNT){
				return InRoom(pm,r.roomId);
			}
		}
		return 0;
	}
	//进入房间，可以查看房间状态
	public int InRoom(PlayerManager pm , int roomid){
		if(roomid == 0){
			Room oldr = pm.getRoom();
			if(oldr != null){
				if( oldr.IsOpen() ) {
					if(roomid != oldr.getRoomId()){
						roomid = oldr.getRoomId();
					}
				}
			}
		}

		Room inr = roomMap.get(roomid);
		
		if(inr == null) return 0;
		
		if(!inr.IsOpen()){
			//GameTools.SendMessage(pm.getUi(), "房间已经关闭");
			if(inr.playcount > 0){
				RoomResp resp = new RoomResp();
				resp.type = GameProtocol.ROOM_IN;
				resp.rp = inr.rd;
				resp.setUserInfo(pm.getUi());
				resp.Send();
				return 1;
			}else{
				return 0;
			}
			
		}

		Room r = pm.getRoom();
		if(r != null){
			if( r.getRoomId() != roomid) {
				//logger.info("InRoom  uid: "+pm.getPd().getUid() + "   roomid : " + roomid);
				LeaveRoom(pm);
				//return InRoom(pm,r.getRoomId());
			}
		}
		
		inr.InRoom(pm);
		playerRoom.put(pm.getPd().getUid(), roomid);
		logger.info("11111");
		return 2;
	}
	
	//坐下
	public boolean PlayerDown(PlayerManager pm,int index){

		Room r = pm.getRoom();
		if(r == null){
			GameTools.SendMessage(pm.getUi(), "您还没有进入房间");
			return false;
		}

		if(!r.IsOpen()){
			GameTools.SendMessage(pm.getUi(), "房间已经关闭");
			return false;
		}
		boolean ret = r.PlayerDown(pm,index);
		
		return ret;
	}
	//开始游戏
	public boolean StartGame(PlayerManager pm){

		Room r = pm.getRoom();
		if(r == null){
			GameTools.SendMessage(pm.getUi(), "您还没有进入房间");
			return false;
		}

		if(!r.IsOpen()){
			GameTools.SendMessage(pm.getUi(), "房间已经关闭");
			return false;
		}
		return r.StartGame(pm);
	}
	
	//离开房间
	public void LeaveRoom(PlayerManager pm){
		Room r = pm.getRoom();
		if(r != null){
			r.RemovePlayer(pm);
			playerRoom.remove(pm.getPd().getUid());
		}
	}
	
	public void Up(PlayerManager pm){
		Room r = pm.getRoom();
		if(r != null){
			r.PlayerUp(pm);
		}
	}
	
	public void Ready(PlayerManager pm,int state){
		if(!playerRoom.containsKey(pm.getPd().getUid())){
			return;
		}
		int roomid = playerRoom.get(pm.getPd().getUid());

		Room r = roomMap.get(roomid);
		if(r != null){
			r.SetPlayerState(pm, state);
		}
	}
	
	public void PlayerOffline(PlayerManager pm){
		Room r = pm.getRoom();
		if(r != null){
			r.PlayerOffline(pm);
		}
	}
	
	public Room GetPlayerRoom(long uid){
		if(!playerRoom.containsKey(uid)){
			return null;
		}
		int roomid = playerRoom.get(uid);

		Room r = roomMap.get(roomid);
		if(!r.IsOpen()) return null;
		return r;
	}
	
	//删除超时房间
	public void DelCloseRoom(){
		List<Integer> dellist = new ArrayList<Integer>();
		for (Entry<Integer, Room> entry : roomMap.entrySet()) {
			if(entry.getValue().IsTimeOutClose()){
				dellist.add(entry.getKey());
			}
		}
		for(int i = 0 ; i < dellist.size() ; i ++){
			roomMap.remove(dellist.get(i));
		}
		
	}
	
	//获取正在进行的游戏牌局
	public GameRoomData GetPlayingCount(){
		GameRoomData data = new GameRoomData();
		for (Map.Entry<Integer,Room> entry : roomMap.entrySet()) {  
			Room r = entry.getValue();
			if(r.IsOpen()){
				data.openRoom++;
			}
			if(r.IsPlaying()){
				data.playingRoom++;
			}
		}
		return data;
	}
}
