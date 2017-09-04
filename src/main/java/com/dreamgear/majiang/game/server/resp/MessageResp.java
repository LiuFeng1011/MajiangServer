package com.dreamgear.majiang.game.server.resp;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class MessageResp  extends BaseMessage{
	String message;
	
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.MESSAGE;
	}
	

	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
