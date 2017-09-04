package com.dreamgear.majiang.game.server.resp;

import java.util.ArrayList;
import java.util.List;

import com.dreamgear.majiang.game.game.GameResult;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class InGameOverResp extends BaseMessage {
	public GameResult result;
	public List<List<String>> playerBrandList = new ArrayList<List<String>>();
	public List<List<List<Integer>>> resultMap = new ArrayList<List<List<Integer>>>(); //胡牌玩家的牌 已分组
	public List<List<Integer>> fanlist;//每个玩家的胡法
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.GAME_OVER;
	}
	
	public InGameOverResp(GameResult result){
		this.result = result;
	}
}
