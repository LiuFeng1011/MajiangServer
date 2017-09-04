package com.dreamgear.majiang.game.server.request;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class InGameOutBrandRequest extends BaseMessage{
	public String bid;

	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.GAME_OUT_BRAND;
	}
}
