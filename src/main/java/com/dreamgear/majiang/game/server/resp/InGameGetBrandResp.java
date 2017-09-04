package com.dreamgear.majiang.game.server.resp;

import com.dreamgear.majiang.game.game.MajiangUtil;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class InGameGetBrandResp extends BaseMessage{
	public MajiangUtil brand;
	public InGameGetBrandResp(MajiangUtil b){
		brand = b;
	}
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.GAME_GET_BRAND;
	}
	
}
