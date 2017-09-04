package com.dreamgear.http.handler.impl;

import com.dreamgear.http.handler.HttpHandler;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.http.response.HttpResponseMessage;

public class HttpBasicInfo extends HttpHandler  {

	public String handle(HttpRequestMessage request,
			HttpResponseMessage response) {
		String playerServerId = request.getParameter("playerServerId");
		String playerUid = request.getParameter("playerUid");
		return "";
		
	}
	
}
