package com.dreamgear.majiang.game.server.resp;

import java.util.ArrayList;
import java.util.List;

import com.dreamgear.majiang.game.game.MajiangUtil;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class InGameStartInfo extends BaseMessage{
	//手中还站着的牌
	public List<String> brandList = new ArrayList<String>() ;
	public int selfIndex;//玩家位置
	public int bossIndex;//庄家位置
	
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.GMAE_PLAYER_BRAND_LIST;
	}
	
	public InGameStartInfo(List<MajiangUtil> brandList, int selfIndex,int bossIndex){
		for(int i = 0 ; i < brandList.size() ; i ++){
			this.brandList.add(brandList.get(i).getBrandid());
		}
		this.selfIndex = selfIndex;
		this.bossIndex = bossIndex;
	}
}
