package com.dreamgear.http.handler.impl;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.HttpHandler;
import com.dreamgear.http.handler.domain.ReturnEntity;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.http.response.HttpResponseMessage;
import com.dreamgear.majiang.common.GameConfig;
import com.dreamgear.majiang.common.GameTools;
import com.dreamgear.majiang.game.player.LoginManager;
import com.dreamgear.majiang.utils.HttpUtils;
import com.dreamgear.majiang.utils.JsonUtil;

public class WXLogin  extends HttpHandler{

	@Override
	public String handle(HttpRequestMessage request, HttpResponseMessage response) {
		String code = request.getParameter("uid");
		
		int uid = -1;
		
		try{
			uid = Integer.parseInt(code);
		}catch(Exception e){
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("islog", 0);
			
			ReturnEntity ret = ReturnEntity.createSucc(data);
			response.appendBody(JsonUtil.ObjectToJsonString(ret));
			response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
			return "";//HttpUtils.GetHttpData(url, "","GET");
		}
		
		boolean islogin = LoginManager.GetInstance().isLogin(uid);
		
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("islog", islogin?1:0);
		
		ReturnEntity ret = ReturnEntity.createSucc(data);
		response.appendBody(JsonUtil.ObjectToJsonString(ret));
		response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
		return "";//HttpUtils.GetHttpData(url, "","GET");
	}
}
