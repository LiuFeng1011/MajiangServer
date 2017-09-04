package com.dreamgear.http.handler.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dreamgear.http.handler.HttpHandler;
import com.dreamgear.http.handler.domain.ReturnEntity;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.http.response.HttpResponseMessage;
import com.dreamgear.majiang.common.GameCommon;
import com.dreamgear.majiang.common.GameConfig;
import com.dreamgear.majiang.common.GameTools;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.game.data.GameDataManager;
import com.dreamgear.majiang.game.data.IapData;
import com.dreamgear.majiang.game.order.OrderData;
import com.dreamgear.majiang.game.order.WXOrderData;
import com.dreamgear.majiang.game.payGift.PayGiftManager;
import com.dreamgear.majiang.game.player.GamePlayerManager;
import com.dreamgear.majiang.game.player.PlayerManager;
import com.dreamgear.majiang.utils.HttpUtils;
import com.dreamgear.majiang.utils.JsonUtil;
import com.dreamgear.majiang.utils.TimeUtils;
import com.dreamgear.majiang.utils.XmlUtils;

public class WXNotify extends HttpHandler{
	private static final Logger logger = LoggerFactory.getLogger(WXNotify.class);

	public String handle(HttpRequestMessage request, HttpResponseMessage response) {
		Map<String,Object> data = new HashMap<String,Object>();
		ReturnEntity ret = ReturnEntity.createSucc(data);
		String body = JSON.toJSONString(ret);
		response.appendBody(body);
		response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
		
		String paydata = request.getParameter("data");

		String decodedata = "";
		
		try{
			decodedata = java.net.URLDecoder.decode(paydata,"utf-8");
		}catch(Exception e){
			e.printStackTrace();
			decodedata = "Cant decode paydata!";
		}

		GameTools.Log("decodedata : " + decodedata);
		
		WXOrderData wxorderdata = JsonUtil.JsonToObject(decodedata, WXOrderData.class);
		int count = DBManager.getInstance().getOrderDAO().getOrderCount(wxorderdata.transaction_id);
		if(count > 0){
			logger.info("count > 0 : " + count);
			return "";
		}
		
		long uid = Long.parseLong(wxorderdata.attach);
		PlayerManager pm = GamePlayerManager.GetInstance().GetPlayer(uid);
		
		OrderData orderdata = new OrderData();
		int price = -1;
		try{
			price = Integer.parseInt(wxorderdata.total_fee);
		}catch(Exception e){
			e.printStackTrace();
		}
		orderdata.setTime(TimeUtils.nowLong());
		orderdata.setOrderId(wxorderdata.transaction_id);
		orderdata.setOrderType(0);
		orderdata.setValue(price);
		orderdata.setAppid(wxorderdata.appid);
		orderdata.setResult((byte)1);//微信支付只有成功时才进入此接口 所以直接设置为1
		orderdata.setMsg(wxorderdata.attach);
		orderdata.setUid(uid);
		if(pm != null) orderdata.setParentid(pm.getPd().getParent());
		
		orderdata.setTrade_type(wxorderdata.trade_type);
		orderdata.setOut_trade_no(wxorderdata.out_trade_no);
		orderdata.setMch_id(wxorderdata.mch_id);
		orderdata.setOpenid(wxorderdata.openid);
		
		DBManager.getInstance().getOrderDAO().addOrder(orderdata);
		
		IapData iapdata= GameDataManager.iapDataManager.GetDataByPrice(price/100);
		
		if(iapdata != null){
			logger.info("iapdata : " + JSON.toJSONString(iapdata));
			int giftcount = PayGiftManager.GetInstance().GetGiftByID(iapdata.getId());
			if(pm != null) pm.AddGold(iapdata.getCount()+giftcount);
		}else{
			logger.info("iapdata == null : " );
		}
		
		return "";
	}
	
	/**
	 * 
	 * 查询订单，WxPayOrderQuery中out_trade_no、transaction_id至少填一个
	 * appid、mchid、spbill_create_ip、nonce_str不需要填入
	 * @param WxPayOrderQuery $inputObj
	 * @param int $timeOut
	 * @throws WxPayException
	 * @return 成功时返回，其他抛异常
	 */
	public static boolean orderQuery(String transaction_id)
	{
		String url = "https://api.mch.weixin.qq.com/pay/orderquery";
		Map<String ,String> datas = new HashMap<String ,String>();
		datas.put("transaction_id", transaction_id);

		datas.put("appid", GameConfig.wx_pay_appid);
		datas.put("mch_id", GameConfig.wx_pay_mchid);
		
		String nonce_str = GameCommon.getNonceStr(32);
		datas.put("nonce_str", nonce_str);
		
		//签名步骤一：按字典序排序参数
		String sign = GameCommon.formatUrlMap(datas, false, false);
		logger.info("sign:"+sign);
		//签名步骤二：在string后加入KEY
		sign = sign + "&key="+GameConfig.wx_pay_key;
		logger.info("sign:"+sign);
		//签名步骤三：MD5加密
		sign = GameCommon.getMd5(sign);
		//签名步骤四：所有字符转为大写
		sign = sign.toUpperCase();

		logger.info("sign:"+sign);
		
		datas.put("sign", sign);
		
		String xml = GameCommon.ToXml(datas);
		
		logger.info("xml:"+xml);
		String result = HttpUtils.GetHttpData(url, xml, "POST");
		
		logger.info("result : " + result);
		try{
			Document doc = DocumentHelper.parseText(result);
			Element root = doc.getRootElement();
			if("SUCCESS".equals(root.elementText("return_code")) && "SUCCESS".equals(root.elementText("result_code"))){
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
}
