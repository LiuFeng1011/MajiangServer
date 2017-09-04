package com.dreamgear.http.handler.impl;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.HttpHandler;
import com.dreamgear.http.handler.domain.ReturnEntity;
import com.dreamgear.http.handler.domain.WXPlayerInfo;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.http.response.HttpResponseMessage;
import com.dreamgear.majiang.common.GameTools;
import com.dreamgear.majiang.game.player.LoginManager;

public class Login extends HttpHandler{

	@Override
	public String handle(HttpRequestMessage request, HttpResponseMessage response) {

		String openid = request.getParameter("openid");
		
		WXPlayerInfo p = new WXPlayerInfo();
		
		p.openid = openid;
		p.nickname = openid;
		
		LoginManager.GetInstance().Login(p);
		
		
		Map<String,Object> data = new HashMap<String,Object>();
		
		data.put("p",JSON.toJSONString(p));
		
		ReturnEntity ret = ReturnEntity.createSucc(data);
		String body = GameTools.getBASE64(JSON.toJSONString(ret));

		GameTools.Log("WXCallBack : " + "base64 : " + body);
		
		response.appendBody(body);
		response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
		return "";
	}
}
