package com.dreamgear.http.handler.impl.gmfunction;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.domain.GMFunctionRetData;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.majiang.game.data.GameDataManager;
import com.dreamgear.majiang.game.payGift.PayGiftManager;
import com.dreamgear.majiang.game.player.GamePlayerManager;

public class PayGiftGetList extends BaseFunction{

	@Override
	public GMFunctionRetData handle(HttpRequestMessage request) {

		GMFunctionRetData ret = new GMFunctionRetData();
		
		Map<String,Object> data = new HashMap<String,Object>();
		
		data.put("giftlist", PayGiftManager.GetInstance().getGiftList());
		data.put("iaplist", GameDataManager.iapDataManager.getDataList());

		ret.code = 1;
		ret.data = JSON.toJSONString(data);
		return ret;
	}
}
