package com.dreamgear.majiang.game.server.resp;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class RoomListResp extends BaseMessage{
	public String data;
	
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.PLAYER_ROOM_LIST;
	}
}
