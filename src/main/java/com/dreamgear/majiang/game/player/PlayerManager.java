package com.dreamgear.majiang.game.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dreamgear.majiang.common.GameConst;
import com.dreamgear.majiang.game.game.Room;
import com.dreamgear.majiang.game.game.RoomManager;
import com.dreamgear.majiang.game.game.RoomPlayData;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiang.game.server.resp.SystemResp;
import com.dreamgear.majiang.utils.JsonUtil;
import com.dreamgear.majiang.utils.TimeUtils;
import com.dreamgear.majiangserver.net.UserInfo;
import com.dreamgear.majiang.common.GameCommon.enPlayerState;
import com.dreamgear.majiang.db.DBManager;

public class PlayerManager {
	private static final Logger logger = LoggerFactory.getLogger(GamePlayerManager.class);

	PlayerData pd ;
	UserInfo ui;
	String address;
//	
//	String lvstar;
//	String item_list;
//	
//	private Map<String, LevelData> passLevelRecord = new HashMap<String, LevelData>();//过关记录
//	private Map<String, Integer> itemList = new HashMap<String, Integer>();//道具列表
//	
	//上一次有操作的时间 用于判断何时从内存中移除玩家
	long heartTime = -1;
	
	//状态 是否在线
	enPlayerState state = enPlayerState.offline;
	
	long saveTime = 0;
	
	boolean issave = false;
	
	//所在房间
	//Room room = null;
	private int roomid = -1;
	
	public String nickname = "unknow";//	用户昵称
	public String sex = "";//	用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	public String province = "";//	用户个人资料填写的省份
	public String city = "";//	普通用户个人资料填写的城市
	public String country = "";//	国家，如中国为CN
	public String headimgurl = "";//
	
	public Map<Long,Long> roomData;//参加过的房间索引key:房间id  val:创建时间
	public List<RoomPlayData> roomList;//参加过的房间数据 发送给客户端用
	//public String roomListString;
	
	public PlayerManager(PlayerData pd){
		this.pd = pd;
		
//		passLevelRecord = JsonUtil.JsonToObjectMap(pd.getLvstar());
//		itemList = JsonUtil.JsonToObjectMap(pd.getItem_list());
		
		saveTime = TimeUtils.nowLong();
		roomData = JsonUtil.JsonToObjectMap(pd.getRoomdata());
		if(roomData == null){
			roomData = new HashMap<Long,Long>();
		}

        Iterator<Map.Entry<Long,Long>> it = roomData.entrySet().iterator();  
        
        //删除过期数据
		while(it.hasNext()) {
			Map.Entry<Long,Long> entry = it.next();
			if(entry.getValue() < TimeUtils.nowLong() - GameConst.ROOMDATA_OUTTIME){
				it.remove();
			}
		}
		SaveRoomData();
		
		if(roomData.size() > 0){
			roomList = DBManager.getInstance().getRoomDAO().GetPlayerRoomList(roomData);
		}else{
			roomList = new ArrayList<RoomPlayData>();
		}
		
//		roomListString = JSON.toJSONString(roomList);
	}
	
	public void AddRoomPlayData(RoomPlayData r){
		logger.info("r.roomid : " + r.roomid);
		if(!roomData.containsKey(r.roomid)){
			roomData.put(r.roomid, r.createTime);
			SaveRoomData();
		}
		if(!roomList.contains(r)){
			roomList.add(r);
		}
		GamePlayerManager.GetInstance().AddSaveRole(this);
	}

	public void SaveRoomData(){
		pd.setRoomdata(JSON.toJSONString(roomData));
		logger.info("SaveRoomData : " + pd.getRoomdata());
	}
	
	public String GetRoomListData(){
		return JSON.toJSONString(roomList);
	}
	
	public void Tick() {
		if (this.getState() == enPlayerState.online){
			//x秒未操作则离线
			if (TimeUtils.nowLong() - this.getHeartTime() > GameConst.PLAYER_LEAVE_TIME * 1000) {
				//logger.info("uid : " + this.getPd().getUid() + " x 秒未响应 ，自动离线");
				
				
				Room r = this.getRoom();//.getGameLogic().GetPlayerIndex(pm)
				if(r != null&& r.getGameLogic() != null &&r.getGameLogic().GetPlayerIndex(this) != -1){
					this.setHeartTime(this.getHeartTime() + (GameConst.PLAYER_LEAVE_TIME / 2) * 1000);
				}else{
					this.setState(enPlayerState.offline);
					issave = true;
				}
			}
		}
		
		if(issave){
			GamePlayerManager.GetInstance().AddSaveRole(this);
		}
//		if((this.getState() != enPlayerState.offline) && TimeUtils.nowLong() - saveTime > 10000){//10秒保存一次
//			//logger.info("x秒自动保存 uid : " + this.getPd().getUid());
//			saveTime = TimeUtils.nowLong();
//			GamePlayerManager.GetInstance().AddSaveRole(this);
//		}
		
		issave = false;
	}
	
	//离线 暂时保留玩家
	public void Offline(){
//		this.setState(enPlayerState.offline);
//		GamePlayerManager.GetInstance().AddSaveRole(this);
		logger.info("uid : " + this.getPd().getUid() + " Offline");
		RoomManager.GetInstance().PlayerOffline(this);
	}
	
	//离开 删除玩家
	public void Leave(){
		logger.info("uid : " + this.getPd().getUid() + " Leave");
		RoomManager.GetInstance().LeaveRoom(this);
	}
	
	public void AddGold(int count){
		this.getPd().setGold(this.getPd().getGold() + count);
		if(this.getState() == enPlayerState.online){
			SystemResp sr = new SystemResp();
			sr.type = GameProtocol.SYSTEM_PLAYERDATA;
			sr.value = "" + this.getPd().getGold();
			sr.setUserInfo(this.getUi());
			sr.Send();
		}
		issave = true;
	}
	
	public void AddPlayCount(boolean iswin){
		this.getPd().setPlaycount(this.getPd().getPlaycount() + 1);
		if(iswin) this.getPd().setWincount(this.getPd().getWincount() + 1);
		issave = true;
	}
	
	public void Login(){
		this.setRoomid(-1);
		this.setState(enPlayerState.online);
	}
	
	public void SaveDataToDB(){
		this.getPd().SaveData();
	}

	public PlayerData getPd() {
		return pd;
	}

	public void setPd(PlayerData pd) {
		this.pd = pd;
	}

	public void SetParent(long pid){
		getPd().setParent(pid);
		issave = true;
	}

//	public String getLvstar() {
//		return lvstar;
//	}
//
//
//	public void setLvstar(String lvstar) {
//		this.lvstar = lvstar;
//	}
//
//
//	public String getItem_list() {
//		return item_list;
//	}
//
//
//	public void setItem_list(String item_list) {
//		this.item_list = item_list;
//	}
//
//
//	public Map<String, LevelData> getPassLevelRecord() {
//		return passLevelRecord;
//	}
//
//
//	public void setPassLevelRecord(Map<String, LevelData> passLevelRecord) {
//		this.passLevelRecord = passLevelRecord;
//	}
//
//
//	public Map<String, Integer> getItemList() {
//		return itemList;
//	}
//
//
//	public void setItemList(Map<String, Integer> itemList) {
//		this.itemList = itemList;
//	}


	public UserInfo getUi() {
		return ui;
	}


	public void setUi(UserInfo ui) {
		this.ui = ui;
	}

	

	public long getHeartTime() {
		return heartTime;
	}

	public void setHeartTime(long heartTime) {
		this.heartTime = heartTime;
	}

	public enPlayerState getState() {
		return state;
	}

	public void setState(enPlayerState state) {
		this.state = state;
	}

	public Room getRoom() {
		Room room = RoomManager.GetInstance().GetPlayerRoom(this.getPd().getUid());
		return room;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getRoomid() {
		return roomid;
	}

	public void setRoomid(int roomid) {
		this.roomid = roomid;
	}
	
//
//	public void setRoom(Room room) {
//		this.room = room;
//	}

}
