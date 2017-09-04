package com.dreamgear.majiang.game.server.request;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class RoomRequest  extends BaseMessage{
	public int type;
	public int roomid;
	public int value = 1;	// type == GameProtocol.ROOM_DOWN 坐下的位置
						// type == GameProtocol.ROOM_CREATE 房间类型id
	public int ispao = 1;//点炮包3家 0否 1是
	public int findtype = 0;//0 好友房 1 随机房
	
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.ROOM;
	}
	
}
