package com.dreamgear.majiang.game.server.request;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class InGameEatRequest extends BaseMessage{
	public String bid1;
	public String bid2;
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.GAME_EAT;
	}
	
}
