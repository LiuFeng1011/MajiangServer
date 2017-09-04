package com.dreamgear.majiang.game.server.resp;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class InGameFlushInfoResp extends BaseMessage {
	public int brandCount;//游戏剩余牌数量
	public int[] playerBCount = {0,0,0,0};
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.GMAE_FLUSH_INFO;
	}
}
