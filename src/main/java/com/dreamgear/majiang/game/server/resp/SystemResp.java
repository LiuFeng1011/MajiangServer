package com.dreamgear.majiang.game.server.resp;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class SystemResp extends BaseMessage{
	public int type;

	//type == GameProtocol.SYSTEM_PAY  value:pay id 配置表对应的id
	//type == GameProtocol.SYSTEM_PLAYERDATA  value: gold count 玩家钻石数
	public String value;
	
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.SYSTEM;
	}
	
}
