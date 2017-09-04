package com.dreamgear.http.handler;

import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.http.response.HttpResponseMessage;

public abstract class HttpHandler {
	public  abstract String handle(HttpRequestMessage request,HttpResponseMessage response); 
}
