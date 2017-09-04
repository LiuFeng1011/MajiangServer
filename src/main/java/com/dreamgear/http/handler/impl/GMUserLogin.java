package com.dreamgear.http.handler.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dreamgear.gm.GMLoginData;
import com.dreamgear.gm.GMUserManager;
import com.dreamgear.http.handler.HttpHandler;
import com.dreamgear.http.handler.domain.ReturnEntity;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.http.response.HttpResponseMessage;
import com.dreamgear.majiang.utils.JsonUtil;

public class GMUserLogin  extends HttpHandler{
	private static final Logger logger = LoggerFactory.getLogger(GMUserLogin.class);

	@Override
	public String handle(HttpRequestMessage request, HttpResponseMessage response) {

		String uname = request.getParameter("uname");
		String pw = request.getParameter("pw");
		
		logger.info("GMUserLogin : " + uname + " pw : " + pw);
		
		GMLoginData logindata = GMUserManager.GetInstance().Login(uname, pw);

		Map<String,Object> data = new HashMap<String,Object>();
		if(logindata == null){
			data.put("code", 0);
		}else{
			data.put("code", 1);
			data.put("data", JSON.toJSONString(logindata));
		}
		
		ReturnEntity ret = ReturnEntity.createSucc(data);
		response.appendBody(JsonUtil.ObjectToJsonString(ret));
		response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
		
		return "";
	}
}
