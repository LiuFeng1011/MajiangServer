package com.dreamgear.http.handler.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.HttpHandler;
import com.dreamgear.http.handler.domain.LoginData;
import com.dreamgear.http.handler.domain.ReturnEntity;
import com.dreamgear.http.handler.domain.WXPlayerInfo;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.http.response.HttpResponseMessage;
import com.dreamgear.majiang.common.GameConfig;
import com.dreamgear.majiang.common.GameTools;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.game.player.LoginManager;
import com.dreamgear.majiang.game.player.PlayerData;
import com.dreamgear.majiang.utils.HttpUtils;
import com.dreamgear.majiang.utils.TimeUtils;

public class WXCallBack extends HttpHandler{
	//KEY : CODE
	public static ConcurrentHashMap<String,WXPlayerInfo> codeMap = new ConcurrentHashMap<String,WXPlayerInfo>();
	
	@Override
	public String handle(HttpRequestMessage request, HttpResponseMessage response) {
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		
		GameTools.Log("WXCallBack : " + "code : " + code + "   state:"+state);
		
		WXPlayerInfo p;
		
		if(codeMap.containsKey(code)){
			p = codeMap.get(code);
		}else{
			String url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
					+ "appid="+ GameConfig.wx_appid
					+"&secret="+GameConfig.wx_secret
					+"&code="+code+"&grant_type=authorization_code";
			
			String access_token = HttpUtils.GetHttpData(url, "", "GET");
			GameTools.Log("WXCallBack : " + "access_token : " + access_token);
			WXPlayerInfo access_token_p;
			access_token_p = JSON.parseObject(access_token, WXPlayerInfo.class);
			access_token_p.setCode(code);
			
			//错误时
			if(access_token_p.errcode != -1){
				Map<String,Object> data = new HashMap<String,Object>();

				data.put("uid",-1);
				data.put("errmsg",access_token_p.errmsg);
				
				
				ReturnEntity ret = ReturnEntity.createSucc(data);
				String body = GameTools.getBASE64(JSON.toJSONString(ret));

				response.appendBody(body);
				response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
				return "";
			}
			
			String user_info_url = "https://api.weixin.qq.com/sns/userinfo?"
					+ "access_token=" + access_token_p.access_token
					+ "&openid=" + access_token_p.openid
					+ "&lang=zh_CN" ;

			String user_info = HttpUtils.GetHttpData(user_info_url, "", "GET");
			
			GameTools.Log("WXCallBack : " + "user_info : " + user_info);
			p = JSON.parseObject(user_info, WXPlayerInfo.class);

			//错误时
			if(access_token_p.errcode != -1){
				Map<String,Object> data = new HashMap<String,Object>();

				data.put("uid",-1);
				data.put("errmsg",access_token_p.errmsg);
				
				ReturnEntity ret = ReturnEntity.createSucc(data);
				String body = GameTools.getBASE64(JSON.toJSONString(ret));

				response.appendBody(body);
				response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
				return "";
			}
			p.access_token = access_token_p.access_token;//	网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
			p.expires_in = (TimeUtils.nowLong() + Long.parseLong(access_token_p.expires_in) * 1000) + "";//access_token接口调用凭证超时时间，单位（秒）
			p.refresh_token = access_token_p.refresh_token;//	用户刷新access_token
			p.scope = access_token_p.scope;//	用户授权的作用域，使用逗号（,）分隔
			
			synchronized (codeMap) {
				for (Map.Entry<String,WXPlayerInfo> entry : codeMap.entrySet()) {
				    if(entry.getValue().openid.equals(p.openid)){
				    	codeMap.remove(entry.getKey());
				    	break;
				    }
				}  
				codeMap.put(code, p);
			}
		}
		LoginManager.GetInstance().Login(p);
		
		Map<String,Object> data = new HashMap<String,Object>();
		
		data.put("uid",p.getUid());

		ReturnEntity ret = ReturnEntity.createSucc(data);
		String body = GameTools.getBASE64(JSON.toJSONString(ret));

		response.appendBody(body);
		response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
		return "";
	}
	
}
