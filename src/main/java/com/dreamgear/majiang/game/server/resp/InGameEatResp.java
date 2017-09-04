package com.dreamgear.majiang.game.server.resp;

import java.util.List;

import com.dreamgear.majiang.game.game.MajiangUtil;
import com.dreamgear.majiangserver.net.BaseMessage;

public class InGameEatResp extends BaseMessage{
	public long index;//玩家id
	public List<MajiangUtil> brand;
	public InGameEatResp( long index,List<MajiangUtil> brand){
		this.index = index;
		this.brand = brand;
	}
}
