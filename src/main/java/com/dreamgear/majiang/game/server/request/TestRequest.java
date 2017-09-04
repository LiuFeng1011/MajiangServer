package com.dreamgear.majiang.game.server.request;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;


public class TestRequest extends BaseMessage{

	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.TEST;
	}
	
	public int test1 = 0;
	public String test2 = "";
	
}
