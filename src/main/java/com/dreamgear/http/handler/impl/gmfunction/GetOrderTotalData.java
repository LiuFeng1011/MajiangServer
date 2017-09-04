package com.dreamgear.http.handler.impl.gmfunction;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.domain.GMFunctionRetData;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.delegate.DelegateIncomeSQLData;

public class GetOrderTotalData extends BaseFunction{

	@Override
	public GMFunctionRetData handle(HttpRequestMessage request) {
		
		DelegateIncomeSQLData data = DBManager.getInstance().getOrderDAO().GetOrderTotalDate();
		
		GMFunctionRetData ret = new GMFunctionRetData();
		ret.code = 1;
		ret.data = JSON.toJSONString(data);
		
		return ret;
	}
}
