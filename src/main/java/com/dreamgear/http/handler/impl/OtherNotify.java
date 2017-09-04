package com.dreamgear.http.handler.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dreamgear.http.handler.HttpHandler;
import com.dreamgear.http.handler.domain.ReturnEntity;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.http.response.HttpResponseMessage;
import com.dreamgear.majiang.common.GameTools;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.game.data.GameDataManager;
import com.dreamgear.majiang.game.data.IapData;
import com.dreamgear.majiang.game.order.OrderData;
import com.dreamgear.majiang.game.order.OtherOrderData;
import com.dreamgear.majiang.game.payGift.PayGiftManager;
import com.dreamgear.majiang.game.player.GamePlayerManager;
import com.dreamgear.majiang.game.player.PlayerManager;
import com.dreamgear.majiang.utils.JsonUtil;
import com.dreamgear.majiang.utils.TimeUtils;

public class OtherNotify extends HttpHandler {
	private static final Logger logger = LoggerFactory.getLogger(OtherNotify.class);

	public String handle(HttpRequestMessage request, HttpResponseMessage response) {
		

		String paydata = request.getParameter("data");

		String decodedata = "";
		
		try{
			decodedata = java.net.URLDecoder.decode(paydata,"utf-8");
		}catch(Exception e){
			e.printStackTrace();
			decodedata = "Cant decode paydata!";
		}

		GameTools.Log("decodedata : " + decodedata);
		

		OtherOrderData otherOrderData = JsonUtil.JsonToObject(decodedata, OtherOrderData.class);
		
		if(otherOrderData.paySt != 2){
			Map<String,Object> data = new HashMap<String,Object>();
			ReturnEntity ret = ReturnEntity.createSucc(data);
			String body = JSON.toJSONString(ret);
			response.appendBody(body);
			response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
			return "";
		}
		
		int count = DBManager.getInstance().getOrderDAO().getOrderCount(otherOrderData.orderNo);
		if(count > 0){
			logger.info("count > 0 : " + count);
			return "";
		}
		
		long uid = Long.parseLong(otherOrderData.extra);
		PlayerManager pm = GamePlayerManager.GetInstance().GetPlayer(uid);
		logger.info("uid : " + uid);
		
		OrderData orderdata = new OrderData();
		int price = -1;
		try{
			price = otherOrderData.amount;
		}catch(Exception e){
			e.printStackTrace();
		}
		orderdata.setTime(TimeUtils.nowLong());
		orderdata.setOrderId(otherOrderData.orderNo);
		orderdata.setOrderType(1);
		orderdata.setValue(price);
		orderdata.setAppid(otherOrderData.appid);
		orderdata.setResult((byte)1);//微信支付只有成功时才进入此接口 所以直接设置为1
		orderdata.setMsg(otherOrderData.extra);
		orderdata.setUid(uid);
		if(pm != null) orderdata.setParentid(pm.getPd().getParent());
		
		orderdata.setTrade_type("");
		orderdata.setOut_trade_no(otherOrderData.outTransactionId);
		orderdata.setMch_id("");
		orderdata.setOpenid(otherOrderData.openId);
		
		DBManager.getInstance().getOrderDAO().addOrder(orderdata);
		
		IapData iapdata= GameDataManager.iapDataManager.GetDataByPrice(price/100);
		
		if(iapdata != null){
			logger.info("iapdata : " + JSON.toJSONString(iapdata));

			int giftcount = PayGiftManager.GetInstance().GetGiftByID(iapdata.getId());
			if(pm != null) pm.AddGold(iapdata.getCount()+giftcount);
		}else{
			logger.info("iapdata == null : " );
		}
		
		Map<String,Object> data = new HashMap<String,Object>();
		ReturnEntity ret = ReturnEntity.createSucc(data);
		String body = JSON.toJSONString(ret);
		response.appendBody(body);
		response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
		return "";
		
	}
}
