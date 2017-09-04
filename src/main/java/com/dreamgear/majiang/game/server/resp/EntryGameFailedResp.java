package com.dreamgear.majiang.game.server.resp;

import com.dreamgear.majiang.common.GameConfig;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class EntryGameFailedResp  extends BaseMessage{
	public String url = "http://"+GameConfig.game_address+"/majiang/baishan/wxlogin.php";
	
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.ENTRY_GAME_FAILED;
	}
}
