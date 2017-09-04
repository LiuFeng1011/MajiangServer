package com.dreamgear.http.handler.impl;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.HttpHandler;
import com.dreamgear.http.handler.domain.ReturnEntity;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.http.response.HttpResponseMessage;
import com.dreamgear.majiang.common.GameTools;
import com.dreamgear.majiang.game.player.GamePlayerManager;
import com.dreamgear.majiang.game.player.PlayerManager;
import com.dreamgear.majiang.utils.JsonUtil;

public class Test extends HttpHandler{

	@Override
	public String handle(HttpRequestMessage request, HttpResponseMessage response) {
		String uid = request.getParameter("uid");
		
		PlayerManager pm = GamePlayerManager.GetInstance().GetPlayer(Long.parseLong(uid));
		
		if(pm == null){
			ReturnEntity ret = ReturnEntity.createFail("玩家不存在");
			response.appendBody(JsonUtil.ObjectToJsonString(ret));
			response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
			return JsonUtil.ObjectToJsonString(ret);
		}
		
		Map<String,Object> data = new HashMap<String,Object>();
		
		data.put("p",pm.GetRoomListData());
		
		ReturnEntity ret = ReturnEntity.createSucc(data);
		String body = JSON.toJSONString(ret);
		response.appendBody(body);
		response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
		return null;
	}

}
