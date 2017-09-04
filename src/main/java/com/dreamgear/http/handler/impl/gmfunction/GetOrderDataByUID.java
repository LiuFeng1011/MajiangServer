package com.dreamgear.http.handler.impl.gmfunction;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.domain.GMFunctionRetData;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.game.order.OrderData;

public class GetOrderDataByUID extends BaseFunction{

	@Override
	public GMFunctionRetData handle(HttpRequestMessage request) {
		String suid = request.getParameter("uid");
		
		if(suid == null || suid == ""){
			return ReturnError("uid不能为空");
		}
		long uid = -1;
		try{
			uid = Long.parseLong(suid);
		}catch(Exception e){
			return ReturnError("玩家id错误:"+suid);
		}
		List<OrderData> list = DBManager.getInstance().getOrderDAO().GetOrderDateByUid(uid);
		
		GMFunctionRetData ret = new GMFunctionRetData();
		ret.code = 1;
		ret.data = JSON.toJSONString(list);
		
		return ret;
	}
}
