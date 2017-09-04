package com.dreamgear.majiang.game.server.resp;

import java.util.List;

import com.dreamgear.majiang.game.game.MajiangUtil;
import com.dreamgear.majiang.game.game.OpenBrand;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class GameReinroomResp extends BaseMessage{

	public List<List<OpenBrand>> openBrandList; //所有玩家已经躺下的牌
	public List<String> selfBrandList;//自己的手牌
	public List<MajiangUtil> outBrandList;//已经打出的牌，客户端根据getPlayer字段来判断是哪个玩家打出的牌

	public int brandCount;//游戏剩余牌数量
	public int[] playerBCount = {0,0,0,0};
	public int[] playerTing = {0,0,0,0};
	
	public int bossid;
	public int index;//操作的玩家
	
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.GMAE_REINROOM;
	}
}
