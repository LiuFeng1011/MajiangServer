package com.dreamgear.majiang.game.server.resp;

import com.dreamgear.majiang.game.game.RoomPlayData;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class RoomResp extends BaseMessage {
	public int type;//type == GameProtocol.ROOM_IN 进入房间失败
	
	public RoomPlayData rp ;

	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.ROOM;
	}
}
