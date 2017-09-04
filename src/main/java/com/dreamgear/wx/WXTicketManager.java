package com.dreamgear.wx;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dreamgear.majiang.common.GameConfig;
import com.dreamgear.majiang.common.GameConst;
import com.dreamgear.majiang.common.GameTools;
import com.dreamgear.majiang.game.data.GameDataManager;
import com.dreamgear.majiang.game.game.GameLogic;
import com.dreamgear.majiang.utils.HttpUtils;
import com.dreamgear.majiang.utils.SHA1;
import com.dreamgear.majiang.utils.TimeUtils;

public class WXTicketManager {
	private static final Logger logger = LoggerFactory.getLogger(WXTicketManager.class);

	private static WXTicketManager instance;
	
	public static WXTicketManager GetInstance(){
		if(instance == null){
			instance = new WXTicketManager();
		}
		return instance;
	}
	SignatureData data = null;
	
	long getDataTime = -1;
	
	public void Tick(){
		if(getDataTime < TimeUtils.nowLong() - 60 * 1000){//提前60s获取 
			RefreshSignature();
		}
	}
	
	public void RefreshSignature(){
		data = new SignatureData();
		getDataTime = TimeUtils.nowLong();
		
		//获取access token
		String tokenurl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
		tokenurl += "&appid=" + GameConfig.wx_appid;
		tokenurl += "&secret=" + GameConfig.wx_secret;
		String tokendata = HttpUtils.GetHttpData(tokenurl, "", "GET");
		JSONObject tokenJson = JSON.parseObject(tokendata);
		if(tokenJson.containsKey("errcode")){
			logger.error("get token err : " + JSON.toJSONString(tokenJson));
			return;
		}
		
		data.token = tokenJson.getString("access_token");
		data.tokenTime = tokenJson.getLongValue("expires_in");
		
		//获得jsapi_ticket
		String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
				+ data.token
				+ "&type=jsapi";
		String jsapi_ticket_string = HttpUtils.GetHttpData(jsapi_ticket_url, "", "GET");
		JSONObject jsapi_ticket_json = JSON.parseObject(jsapi_ticket_string);
		
		int errcode = jsapi_ticket_json.getIntValue("errcode");
		if(errcode != 0 ){
			logger.error("get jsapi_ticket err : " + jsapi_ticket_json.getString("errmsg"));
			return;
		}
		data.ticket = jsapi_ticket_json.getString("ticket");
		data.ticketTime = jsapi_ticket_json.getLongValue("expires_in");
		
		getDataTime += data.ticketTime*1000;
		
		logger.info("Signature data : " + JSON.toJSONString(data));
	}

    // 注意 URL 一定要动态获取，不能 hardcode
	public Map<String, String> Sign(String url){
		String jsapi_ticket = data.ticket;

        Map<String, String> ret = WXSign.sign(jsapi_ticket, url);
        for (Map.Entry entry : ret.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }
        return ret;
//		return new HashMap<String, String>();
	}
}