package com.dreamgear.majiang.game.server.request;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class HeartRequest extends BaseMessage{

	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.HEART;
	}
	
}
