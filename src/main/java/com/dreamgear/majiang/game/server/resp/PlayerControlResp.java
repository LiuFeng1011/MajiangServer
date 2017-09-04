package com.dreamgear.majiang.game.server.resp;

import java.util.List;

import com.dreamgear.majiang.game.game.GamePlayerControl;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class PlayerControlResp extends BaseMessage{
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.GAME_PLAYERCTRL;
	}
	public GamePlayerControl pc;
}
