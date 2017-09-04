package com.dreamgear.majiang.game.server.request;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class WXSignRequest extends BaseMessage{
	public String url;
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.WX_SIGN;
	}
	
}
