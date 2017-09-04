package com.dreamgear.majiang.game.server.request;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

//用户登录请求
public class EntryGameRequest  extends BaseMessage{

	public int uid;
	public int channelid;//0:模拟器  1:手机
	public String token;
	public int delegate;//代理id
	
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.ENTRY_GAME;
	}
}
