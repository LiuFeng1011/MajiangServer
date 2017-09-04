package com.dreamgear.majiang.game.server.resp;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class TestResp extends BaseMessage{

	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.TEST;
	}
	
	int test1 = 0;
	public int test2 = 99;
	
}
