package com.dreamgear.http.handler.impl.gmfunction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.domain.GMFunctionRetData;
import com.dreamgear.http.handler.domain.LoginData;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.majiang.db.DBManager;

public class GetPlayerInfoByPage extends BaseFunction {

	@Override
	public GMFunctionRetData handle(HttpRequestMessage request) {
		String spage = request.getParameter("page");

		GMFunctionRetData ret = new GMFunctionRetData();

		int page = -1;
		try{
			page = Integer.parseInt(spage);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		if(page < 1){
			ret.code = 0;
			ret.errmsg = "页数错误:"+spage;
			return ret;
		}
		
		int pagecount = 30;
		
		int start = (page-1) * pagecount;
		System.out.println("start : " + start);
		List<LoginData> list = DBManager.getInstance().getLoginDAO().getPlayerListByPage(start,pagecount);

		System.out.println("list : " + JSON.toJSONString(list));
		
		String SQL_PLAYER_ALLCOUNT = "SELECT COUNT(*) FROM login";
		int allcount = DBManager.getInstance().getLoginDAO().GetPlayerCount(SQL_PLAYER_ALLCOUNT);
		
		Map<String ,Object> retdata = new HashMap<String ,Object>();
		retdata.put("allcount", allcount);
		retdata.put("data", list);
		retdata.put("page", page);
		
		ret.code = 1;
		ret.data = JSON.toJSONString(retdata);
//		System.out.println("data : " + ret.data);
		return ret;
	}
}
