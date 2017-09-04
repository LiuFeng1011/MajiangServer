package com.dreamgear.majiang.game.server.resp;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class WXSingResp extends BaseMessage {
	public Map<String, String> maps;
	
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.WX_SIGN;
	}

}
