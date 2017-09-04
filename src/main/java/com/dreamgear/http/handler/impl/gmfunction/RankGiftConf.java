package com.dreamgear.http.handler.impl.gmfunction;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.domain.GMFunctionRetData;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.majiang.game.rank.RankManager;

public class RankGiftConf extends BaseFunction {

	@Override
	public GMFunctionRetData handle(HttpRequestMessage request) {

		String sf = request.getParameter("f");
		String sid = request.getParameter("id");
		String smin = request.getParameter("min");
		String smax = request.getParameter("max");
		String scount = request.getParameter("count");

		System.out.println("sf : " + sf + " sid : " + sid );
		int id = -1;
		try{
			id = Integer.parseInt(sid);
		}catch(Exception e){

		}
		
		if(sf.equals("0")){
			//获取列表
			
		}else if(sf.equals("1")){
			//修改添加数据
			int min = -1;
			int max = -1;
			int count = -1;
			try{
				min = Integer.parseInt(smin);
				max = Integer.parseInt(smax);
				count = Integer.parseInt(scount);
			}catch(Exception e){
				return ReturnError("数据错误");
			}
			
			RankManager.GetInstance().rankGiftManager.AddData(id,min, max, count);
			
		}else if(sf.equals("2")){
			//删除数据
			RankManager.GetInstance().rankGiftManager.DelData(id);
		}
		
		
		GMFunctionRetData ret = new GMFunctionRetData();
		
		Map<String,Object> data = new HashMap<String,Object>();
		
		data.put("giftlist", RankManager.GetInstance().rankGiftManager.GetDataString());
		ret.code = 1;
		ret.data = JSON.toJSONString(data);
		return ret;
	}
	
	
}
