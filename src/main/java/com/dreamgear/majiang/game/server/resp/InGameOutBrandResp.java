package com.dreamgear.majiang.game.server.resp;

import com.dreamgear.majiang.game.game.MajiangUtil;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class InGameOutBrandResp  extends BaseMessage{
	public long uid;//玩家id
	public MajiangUtil brand;
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.GAME_OUT_BRAND;
	}
	public InGameOutBrandResp( long uid,MajiangUtil brand){
		this.uid = uid;
		this.brand = brand;
	}
}
