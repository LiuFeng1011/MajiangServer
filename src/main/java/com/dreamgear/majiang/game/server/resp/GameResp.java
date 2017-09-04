package com.dreamgear.majiang.game.server.resp;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class GameResp extends BaseMessage {
	
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.GAME_CAN_CHA;
	}
	
}
