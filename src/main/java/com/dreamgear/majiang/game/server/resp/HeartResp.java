package com.dreamgear.majiang.game.server.resp;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class HeartResp extends BaseMessage{
	public long time;
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.HEART;
	}
	
}
