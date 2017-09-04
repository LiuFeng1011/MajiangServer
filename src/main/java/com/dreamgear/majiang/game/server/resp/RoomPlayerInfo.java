package com.dreamgear.majiang.game.server.resp;

import java.util.List;
import java.util.Map;

import com.dreamgear.majiang.game.player.PlayerSendData;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class RoomPlayerInfo extends BaseMessage {
	public int roomid;
	public long masterid;
	public long createtime;//房间创建时间
	public long starttime;//开始时间
	public int timelong;
	public int roomtype;//房间配置表id
	public int playcount;//玩了几次
	
	public List<PlayerSendData> playerList;
	public List<Map<String,String>> playerScores;
	public List<PlayerSendData> otherList;
	public List<Long> downPlayer;//坐下过的玩家
	
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.ROOM_GET_PLAYER_INFO;
	}
	
	public RoomPlayerInfo(int roomid,long masterid,List<PlayerSendData> l,List<PlayerSendData> ol){
		this.roomid = roomid;
		this.masterid = masterid;
		playerList = l;
		otherList = ol;
	}
}
