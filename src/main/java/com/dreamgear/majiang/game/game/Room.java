package com.dreamgear.majiang.game.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.dreamgear.majiang.common.GameCommon.enPlayerRoomState;
import com.dreamgear.majiang.common.GameCommon.enRoomFindType;
import com.dreamgear.majiang.common.GameCommon.enRoomPaoType;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.common.GameCommon;
import com.dreamgear.majiang.common.GameConst;
import com.dreamgear.majiang.common.GameTools;
import com.dreamgear.majiang.game.data.GameDataManager;
import com.dreamgear.majiang.game.data.RoomData;
import com.dreamgear.majiang.game.player.GamePlayerManager;
import com.dreamgear.majiang.game.player.PlayerManager;
import com.dreamgear.majiang.game.player.PlayerSendData;
import com.dreamgear.majiang.game.rank.RankManager;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiang.game.server.resp.GameReinroomResp;
import com.dreamgear.majiang.game.server.resp.InGameStartInfo;
import com.dreamgear.majiang.game.server.resp.MessageResp;
import com.dreamgear.majiang.game.server.resp.RoomPlayerInfo;
import com.dreamgear.majiang.game.server.resp.RoomResp;
import com.dreamgear.majiang.utils.GetLog;
import com.dreamgear.majiang.utils.TimeUtils;
import com.dreamgear.majiangserver.net.BaseMessage;

public class Room {
	private Logger logger ;
	
	List<Long> otherPlayer = new ArrayList<Long>();
	
	//玩家列表
	InGamePlayer[] playerList = {null,null,null,null};
	ConcurrentHashMap<Long,Long> leavePlayerList = new ConcurrentHashMap<Long,Long>();
	//玩家分数 key：uid val：scores
//	Map<String , Map<String,String>> playerScores = new HashMap<String , Map<String,String>>();
	List<Map<String,String>> playerScores = new ArrayList<Map<String,String>>();
	//付钱的玩家
	List<Long> payPlayer = new ArrayList<Long>();
	List<Long> downPlayer = new ArrayList<Long>();//坐下过的玩家
	//房主
	long master = -1;
	//房间号
	int roomId = 0;
	//游戏逻辑
	BSGameLogic gameLogic = null;
	
	//庄家
	int bossid = -1;
	
	//创建时间
	long createTime;
	long startTime = -1;
	
	//房间类型（时长）单位：秒
	int roomtype;
	
	int roundcount = 0;
	int playcount = 0;
	int maxplaycount = -1;
	
	long createRoomPlayer = -1;
	
	RoomPlayData rd = null;
	
	enRoomPaoType ispao = enRoomPaoType.pao;//点炮包3家
	enRoomFindType findtype = enRoomFindType.firend;
	public Room(){
		
	}
	//创建房间 
	//timelong分钟
	public Room(PlayerManager pm,int roomtype,int ispao,int findtype){
		this.ispao = enRoomPaoType.values()[ispao];
		this.findtype = enRoomFindType.values()[findtype];
		RoomData rd = GameDataManager.roomManager.GetRoomData(roomtype);
		
		maxplaycount = rd.getTime();
		
		master = pm.getPd().getUid();
		createRoomPlayer = pm.getPd().getUid();
		
		this.roomtype = roomtype;
		
		//这里使用Math.rangdom在阿里云服务器上无法生产随机数 ，所以简单的获取时间戳后两位作为随机数
		float rand = (float)(TimeUtils.nowLong() % 100);
		bossid = 0;//(int) (rand / 100.0f * 4.0f);
		
		createTime = TimeUtils.nowLong();
		
		CreateRoomPlayData(pm);
		this.roomId = (int) this.rd.roomid;
		logger = GetLog.getLoggerByName(roomId + "", "room");
		
	}
	
	public void Update(){
		if(this.gameLogic == null) return;
		

		synchronized (leavePlayerList) {
			for (Entry<Long, Long> entry : leavePlayerList.entrySet()) {
				if(entry.getValue() == -1) continue;
				if(TimeUtils.nowLong() - entry.getValue() > 5*1000){
					entry.setValue((long)-1);
					this.gameLogic.PlayerAuto(entry.getKey());
				}
			}
		}
	}
	
	public boolean IsOpen(){

		if( IsTimeOutClose()){
			//20分钟没有开始游戏，关闭房间
			return false;
		}else if(this.roundcount >= maxplaycount && this.gameLogic == null){
			return false;
		}
		return true;
	}
	
	//是否超时关闭
	public boolean IsTimeOutClose(){
		if(playcount == 0 && this.gameLogic == null){
			//20分钟没有开始游戏，关闭房间
			if(TimeUtils.nowLong() - createTime >= 20 * 60 * 1000){
				return true;
			}
		}
		return false;
	}
	
	//是否正在进行游戏
	public boolean IsPlaying(){
		return gameLogic != null;
	}
	public int GetDownPlayerCount(){
		int ret = 0;
		for(int i = 0 ; i < playerList.length ; i ++){
			if(playerList[i] != null) ret ++;
		}
		return ret;
	}
	//进入房间
	public void InRoom(PlayerManager pm){
		
		//是否已经进入房间
		boolean isplay = false;
//		int nullindex = -1;
		
		pm.setRoomid(this.roomId);
		
		for(int i = 0 ; i < playerList.length ; i ++){
			if(playerList[i] == null) {
//				nullindex = i;
				continue;
			}
			if(playerList[i].player == pm.getPd().getUid()){
				this.SetPlayerState(pm, enPlayerRoomState.enDown);
				//playerList[i].state = enPlayerRoomState.enDown;
				isplay = true;
				break;
			}
			
			//玩家是离开状态并且未开局
//			if(nullindex == -1 && playerList[i].state == enPlayerRoomState.enLeave && this.gameLogic == null){
//				nullindex = i;
//			}
		}
		logger.info("playerList : " + JSON.toJSONString(playerList));
		if(!isplay){
			//寻找空位自动坐下
//			if(nullindex != -1){
//				boolean isdown = PlayerDown(pm,nullindex);
//				if(isdown){
//					return;
//				}
//			}
			if(!otherPlayer.contains(pm.getPd().getUid())){
				otherPlayer.add(pm.getPd().getUid());
			}
			SendPlayerInfo();
		}
		
	}
	
	public void Reinroom(PlayerManager pm){
		if(gameLogic != null){
			logger.info("gameLogic != null");
			GameReinroomResp resp = new GameReinroomResp();
			resp.openBrandList = gameLogic.GetAllPlayerOpenBrand();
			resp.selfBrandList = gameLogic.GetPlayerBrandList(pm);
			resp.outBrandList = gameLogic.getOutBrandList();
			this.gameLogic.SetReinInfo(resp);
			resp.setUserInfo(pm.getUi());
			resp.Send();
			this.gameLogic.msg.setUserInfo(pm.getUi());
			this.gameLogic.msg.Send();
		}
		//移除托管状态

		synchronized (leavePlayerList) {
			if(leavePlayerList.containsKey(pm.getPd().getUid())){
				leavePlayerList.remove(pm.getPd().getUid());
				if(gameLogic != null) this.gameLogic.PlayerCancelAuto(pm);
			}
		}
	}
	
	//坐下
	public boolean PlayerDown(PlayerManager pm,int index){
		if(index < 0 || index >= playerList.length){
			return false;
		}
		
		if(playerList[index] != null && playerList[index].state != enPlayerRoomState.enLeave){
			GameTools.SendMessage(pm.getUi(), "此位置已被其他玩家占领");
			return false;
		}

		RoomData rd = GameDataManager.roomManager.GetRoomData(roomtype);
		
		boolean ispay = false;
		for(int i = 0 ; i < payPlayer.size()  ;i ++){
			if(pm.getPd().getUid() == payPlayer.get(i)){
				ispay = true;
				break;
			}
		}
		
		if(!ispay){
			if(pm.getPd().getGold() < rd.getGold()){
				GameTools.SendMessage(pm.getUi(), "钻石不足");
				return false;
			}
		}
		
		for(int i = 0 ; i < playerList.length ; i ++){
			if(playerList[i] == null) continue;
			if(playerList[i].player == pm.getPd().getUid()){
				playerList[i] = null;
				break;
			}
		}
		
		if(otherPlayer.contains(pm.getPd().getUid())){
			otherPlayer.remove(pm.getPd().getUid());
		}
		
		InGamePlayer player = new InGamePlayer(pm.getPd().getUid());
		playerList[index] = player;

		this.SetPlayerState(pm, enPlayerRoomState.enDown);

		return true;
	}
	
	public void AddDownPlayer(PlayerManager pm){
		boolean isdown = false;
		for(int i = 0 ; i < downPlayer.size() ; i ++){
			if(downPlayer.get(i) == pm.getPd().getUid()){
				isdown = true;
			}
		}
		if(!isdown)downPlayer.add(pm.getPd().getUid());
	}

	//站起围观
	public void PlayerUp(PlayerManager pm){
		boolean isdownplayer = false;
		for(int i = 0 ; i < playerList.length ; i ++){
			if(playerList[i] == null) continue;
			if(playerList[i].player == pm.getPd().getUid()){
				playerList[i] = null;
				isdownplayer = true;
				break;
			}
		}
		if(isdownplayer){
			for(int i = 0 ; i < playerList.length ; i ++){
				if(playerList[i] == null) {
					continue;
				}
				playerList[i].state = enPlayerRoomState.enDown;
			}
		}
		if(!otherPlayer.contains(pm.getPd().getUid())){
			otherPlayer.add(pm.getPd().getUid());
		}
		
		SendPlayerInfo();
	}
	
	//修改玩家状态
	public void SetPlayerState(PlayerManager pm,enPlayerRoomState state){

		if(!this.IsOpen()){
			RoomResp resp = new RoomResp();
			resp.type = GameProtocol.ROOM_IN ;//进入房间失败
			resp.rp = this.rd;
			resp.setUserInfo(pm.getUi());
			resp.Send();
			
			RoomManager.GetInstance().LeaveRoom(pm);
			
			return;
		}
		
		boolean isready = true;
		boolean isfindmaster = false;//房主是否坐下
		for(int i = 0 ; i < playerList.length ; i ++){
			if(playerList[i] == null) {
				isready = false;
				continue;
			}
			if(playerList[i].player == pm.getPd().getUid()){
				playerList[i].state = state;
			}
			if(playerList[i].player == this.master){
				isfindmaster = true;
			}
			if(playerList[i].state != enPlayerRoomState.enDown){
				isready = false;
			}
		}
		if(state == enPlayerRoomState.enDown){
			AddDownPlayer(pm);
		}

		SendPlayerInfo();
		
		if(!isfindmaster){
			master = pm.getPd().getUid();
			rd.master = this.master;
		}
		if(isready && gameLogic == null){
			StartGame(null);
		}

	}
	public void SetPlayerState(PlayerManager pm,int state){
		SetPlayerState(pm,enPlayerRoomState.GetState(state) );
	}
	
	
	//开始游戏
	public boolean StartGame(PlayerManager pm){
//		if(pm.getPd().getUid() != this.master){
//			GameTools.SendMessage(pm.getUi(), "只有房主可以开始游戏");
//			return false;
//		}
		logger.info("satrt game");
		//超时判断
		if(!this.IsOpen()){
			if(pm!=null)GameTools.SendMessage(pm.getUi(), "此房间已结束");
		}
		
		RoomData rd = GameDataManager.roomManager.GetRoomData(roomtype);
		
		int price = rd.getGold();
		
		//玩家是否全部准备好 扣钱
		boolean isfindmaster = false;//房主是否坐下
		for(int i = 0 ; i < playerList.length ; i ++){
			if(playerList[i] == null || playerList[i].state != enPlayerRoomState.enDown){
				if(pm!=null)GameTools.SendMessage(pm.getUi(), "玩家没有全部准备好");
				return false;
			}
			if(playerList[i] != null && playerList[i].player == master ){
				isfindmaster = true;
			}
		}
		
		if(!isfindmaster){
			master = playerList[0].player;
			this.rd.master = this.master;
		}
		
		//扣钱
		for(int i = 0 ; i < playerList.length ; i ++){

			boolean ispay = false;
			for(int j = 0 ; j < payPlayer.size()  ;j ++){
				if(playerList[i].player == payPlayer.get(j)){
					ispay = true;
					break;
				}
			}
			
			if(ispay) continue;
			payPlayer.add(playerList[i].player);
			PlayerManager _pm = GamePlayerManager.GetInstance().GetPlayerByUid(playerList[i].player);
			_pm.AddGold(-price);
			
			//设置玩家信息
			PlayerSendData data = new PlayerSendData();
			data.uid = playerList[i].player;
			data.nickname = _pm.nickname;
			data.headurl = _pm.headimgurl;
			
			this.rd.plist.add(data);
			
			//设置房间代理
			if(playerList[i].player == createRoomPlayer) {
				this.rd.delegate = _pm.getPd().getParent();
				DBManager.getInstance().getRoomDAO().updateRoom(this.rd);
			}
		}

		if(startTime == -1){
			startTime = TimeUtils.nowLong();
			
			//打乱玩家顺序
			for(int i = 0 ; i < playerList.length-1 ; i ++){
				InGamePlayer p = playerList[i];
				int random =  (int) (i + Math.random() * (playerList.length - i));
				playerList[i] = playerList[random];
				playerList[random] = p;
			}
		}

		SendPlayerInfo();

		InGameStartInfo resp = new InGameStartInfo(new ArrayList<MajiangUtil>(),-1,bossid);
		this.SendMessageToOtherPlayer(resp);
		
		//bossid = 0;
//		bossid++;
//		if(bossid >= GameConst.MAX_PLAYER_COUNT){
//			bossid = 0;
//		}
		//初始化游戏逻辑
		gameLogic = new BSGameLogic(this,bossid,logger);

		for(int i = 0 ; i < playerList.length ; i ++){
			gameLogic.AddPlayer(playerList[i].player);
		}
		gameLogic.Start(this.ispao.ordinal());

		playcount++;
		
		return true;
	}
	
	//移除玩家
	public void RemovePlayer(PlayerManager pm){
//		boolean isdownplayer = false;
		for(int i = 0 ; i < playerList.length ; i ++){
			if(playerList[i] == null) continue;
			if(playerList[i].player == pm.getPd().getUid()){
				playerList[i] = null;
//				isdownplayer = true;
				this.gameLogic = null;
				break;
			}
		}
//		if(isdownplayer){
//			for(int i = 0 ; i < playerList.length ; i ++){
//				if(playerList[i] == null) {
//					continue;
//				}
//				playerList[i].state = enPlayerRoomState.enDown;
//			}
//		}
		
		if(otherPlayer.contains(pm.getPd().getUid())){
			otherPlayer.remove(pm.getPd().getUid());
		}
		pm.setRoomid(-1);
		
		SendPlayerInfo();
		logger.info("room RemovePlayer uid : " + pm.getPd().getUid());
	}
	
	//玩家离线
	public void PlayerOffline(PlayerManager pm){
//		if(this.gameLogic == null){
//			RemovePlayer(pm);
//			return;
//		}
		boolean ishaveplayer = false;
		for(int i = 0 ; i < playerList.length ; i ++){
			if(playerList[i] == null) continue;
			if(playerList[i].player == pm.getPd().getUid()){
				playerList[i].state = enPlayerRoomState.enLeave;
				leavePlayerList.put(pm.getPd().getUid(),TimeUtils.nowLong());
			}else if(playerList[i].state != enPlayerRoomState.enLeave){
				ishaveplayer =  true;
			}
		}

		if(!ishaveplayer && playcount > 0){
			//玩家全部离开 解散房间
			CloseRoom();
		}else{
			SendPlayerInfo();
		}
		
		logger.info("PlayerOffline uid : " + pm.getPd().getUid());
	}
	
	void CloseRoom(){
		logger.info("=============CloseRoom=============");
		this.roundcount = 100;
		this.gameLogic = null;
	}
	
	void SendPlayerInfo(){

		if(!this.IsOpen() && this.gameLogic == null){
			return;
		}
		
		List<PlayerSendData> list = new ArrayList<PlayerSendData>();
		
		for(int i = 0 ; i < 4 ; i ++){
			PlayerSendData data = new PlayerSendData();
			if(playerList[i] != null){
				data.uid = playerList[i].player;
				PlayerManager pm = GamePlayerManager.GetInstance().GetPlayerByUid(data.uid);
				data.channelid = 111;
				data.nickname = pm.nickname;
				data.headurl = pm.headimgurl;
				//if(playerList[i].state == InGamePlayer.enPlayerState.enLeave) data.state = 1 ;
				data.state = playerList[i].state.ordinal();
			}
			logger.info("playerList data : " + JSON.toJSONString(data));
			list.add(data);
		}

		List<PlayerSendData> otherlist = new ArrayList<PlayerSendData>();
		
		for(int i = 0 ; i < otherPlayer.size() ; i ++){
			PlayerSendData data = new PlayerSendData();
			if(otherPlayer.get(i) != null){
				data.uid = otherPlayer.get(i);
				PlayerManager pm = GamePlayerManager.GetInstance().GetPlayerByUid(data.uid);
				data.channelid = 111;
				data.nickname = pm.nickname;
				data.headurl = pm.headimgurl;
			}
			logger.info("otherPlayer data : " + data);
			otherlist.add(data);
		}
		
		RoomPlayerInfo resp = new RoomPlayerInfo(roomId,master,list,otherlist);
		resp.playerScores = playerScores;
		resp.createtime = this.createTime;
		resp.timelong = this.maxplaycount;
		resp.starttime = this.startTime;
		resp.roomtype = this.roomtype;
		resp.downPlayer = this.downPlayer;
		resp.playcount = this.playcount;
		SendMessageToAllPlayer(resp);
	}
	
	public void GameOver(GameResult r){
		leavePlayerList.clear();
		if(r.winPlayer != bossid && r.winPlayer != -1) bossid ++;
		if(bossid >= GameConst.MAX_PLAYER_COUNT){
			bossid = 0;
			roundcount ++;
		}
		
		
		
		for(int i = 0 ; i < playerList.length ; i ++){
			PlayerManager _pm = GamePlayerManager.GetInstance().GetPlayerByUid(playerList[i].player);
			if(!this.IsOpen() && this.findtype == GameCommon.enRoomFindType.random){
				//加入排行榜
				RankManager.GetInstance().AddScores(_pm, r.playerScores[i]);
			}
			_pm.AddRoomPlayData(this.rd);
			_pm.AddPlayCount(r.winPlayer == _pm.getPd().getUid());
			
			if(playerList[i].state == enPlayerRoomState.enDown){
				playerList[i].state = enPlayerRoomState.enWait;
			}
			
			if(r.winPlayer != -1){
				String uid = playerList[i].player+"";
				boolean isfind = false;
				for(int j = 0 ; j < playerScores.size() ; j ++){
					Map<String, String> s = playerScores.get(j);
					if(s.get("uid").equals(uid)){
						isfind = true;
						s.put("scores", (Integer.parseInt(s.get("scores")) + r.playerScores[i])+"");
						break;
					}
				}
				
				if(!isfind){
					Map<String, String> s = new HashMap<String, String>();
					s.put("uid", uid+"");
					s.put("scores", r.playerScores[i]+"");
					s.put("uname",  _pm.nickname);
					playerScores.add(s);
				}
				logger.info("playerScores : " + playerScores);
			}
		}
		logger.info("playerScores : " + playerScores);
		if(r.winPlayer != -1){
			rd.data = JSON.toJSONString(playerScores);
			rd.playcount = this.playcount;
			DBManager.getInstance().getRoomDAO().updateRoom(rd);
		}
		
		gameLogic = null;
		SendPlayerInfo();
		
		if(!this.IsOpen()){
			MessageResp resp = new MessageResp();
			resp.setMessage("房间时间已到，请重新建房间。");
			SendMessageToAllPlayer(resp);
		}
		
	}
	
	public RoomPlayData CreateRoomPlayData(PlayerManager pm){
		RoomPlayData rd = new RoomPlayData();
		rd.roomid = -1;
		
		rd.createTime = this.createTime;
		rd.roomtype = this.roomtype;
		rd.master = this.master;
		rd.playcount = this.playcount;
		rd.delegate = -1;
		//rd.data = JSON.toJSONString(playerScores);
		this.rd = rd;
		DBManager.getInstance().getRoomDAO().addRoom(rd);
		return rd;
	}
	
	public void Chat(BaseMessage msg){
		SendMessageToAllPlayer(msg);
	}
	
	void SendMessageToAllPlayer(BaseMessage msg){
		for(int i = 0 ; i < 4 ; i ++){
			if(playerList[i] != null) SendMessageToPlayer(msg,playerList[i].player);
		}
		//发送给旁观玩家
		for(int i = 0 ; i < otherPlayer.size() ; i ++){
			SendMessageToPlayer(msg,otherPlayer.get(i));
		}
	}
	void SendMessageToOtherPlayer(BaseMessage msg){
		//发送给旁观玩家
		for(int i = 0 ; i < otherPlayer.size() ; i ++){
			SendMessageToPlayer(msg,otherPlayer.get(i));
		}
	}
	void SendMessageToPlayer(BaseMessage msg,long pid){
		logger.info("send msg : " + JSON.toJSONString(msg) + "   to player : " + pid);

		if(!this.IsOpen() && this.gameLogic == null){
			return;
		}
		PlayerManager pm = GamePlayerManager.GetInstance().GetPlayerByUid(pid);
		if(pm == null) return;
		if(pm.getRoomid() != this.roomId) return;
		
		msg.setUserInfo(pm.getUi());
		msg.Send();
	}


	public int getRoomId() {
		return roomId;
	}


	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public BSGameLogic getGameLogic() {
		return gameLogic;
	}

	public void setGameLogic(BSGameLogic gameLogic) {
		this.gameLogic = gameLogic;
	}


	public enRoomFindType getFindtype() {
		return findtype;
	}
	public void setFindtype(enRoomFindType findtype) {
		this.findtype = findtype;
	}
	
	public enRoomPaoType getIspao() {
		return ispao;
	}
	public void setIspao(enRoomPaoType ispao) {
		this.ispao = ispao;
	}
	//***********************test***************************
    public static void main( String[] args ) throws Exception
    {
		// 游戏配置表数据载入
		GameDataManager.load(GameConst.RES_PATH);
		
    }
    
	
}
