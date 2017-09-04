package com.dreamgear.http.handler.impl.gmfunction;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.domain.GMFunctionRetData;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.majiang.game.game.GameRoomData;
import com.dreamgear.majiang.game.game.RoomManager;
import com.dreamgear.majiang.game.player.GamePlayerManager;

public class GetGameRealData extends BaseFunction{

	@Override
	public GMFunctionRetData handle(HttpRequestMessage request) {
		GMFunctionRetData ret = new GMFunctionRetData();
		ret.code = 1;
		
		int onlinecount = GamePlayerManager.GetInstance().GetOnlinePlayerCount();
		GameRoomData roomdata = RoomManager.GetInstance().GetPlayingCount();
		

		Map<String , String> data = new HashMap<String , String>();
		data.put("onlinecount", onlinecount+"");
		data.put("openRoom", roomdata.openRoom+"");
		data.put("playingRoom", roomdata.playingRoom+"");
		
		ret.data = JSON.toJSONString(data);
		return ret;
	}

}
