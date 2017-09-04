package com.dreamgear.http.handler.impl.gmfunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.domain.GMFunctionRetData;
import com.dreamgear.http.handler.domain.LoginData;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.game.player.GamePlayerManager;
import com.dreamgear.majiang.game.player.LoginManager;
import com.dreamgear.majiang.game.player.PlayerManager;
import com.dreamgear.majiang.utils.TimeUtils;

public class GetPlayerInfoByKey extends BaseFunction{
	@Override
	public GMFunctionRetData handle(HttpRequestMessage request) {
		// TODO Auto-generated method stub
		
		String key = request.getParameter("key");

		if(key == null || key == ""){
			return ReturnError("发送消息不能为空");
		}
		
		List<LoginData> datalist = DBManager.getInstance().getLoginDAO().getRoleByKey(key);
		
		List<Map<String , String>> retdata = new ArrayList<Map<String , String>>();
		
		for(int i = 0 ; i < datalist.size() ; i ++){
			LoginData ld = datalist.get(i);
			
			PlayerManager pm = GamePlayerManager.GetInstance().GetPlayer(ld.getId());

			Map<String , String> userdata = new HashMap<String , String>();
			userdata.put("uid", ld.getId()+"");
			userdata.put("nickname", ld.nickname);
			userdata.put("sex", ld.sex);
			userdata.put("country", ld.country);
			userdata.put("province", ld.province);
			userdata.put("city", ld.city);
			if(pm != null){
				userdata.put("lastOnLineTime", TimeUtils.chDate(pm.getPd().getLastLoginTime()).toString());
				userdata.put("createTime", TimeUtils.chDate(pm.getPd().getCreateTime()).toString());
				userdata.put("parent", pm.getPd().getParent()+"");
			}
			
			retdata.add(userdata);
		}
		
		GMFunctionRetData ret = new GMFunctionRetData();

		ret.code = 1;
		ret.data = JSON.toJSONString(retdata);
		System.out.println("data : " + ret.data);
		return ret;
	}
}
