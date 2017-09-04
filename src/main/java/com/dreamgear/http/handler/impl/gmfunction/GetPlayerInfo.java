package com.dreamgear.http.handler.impl.gmfunction;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.domain.GMFunctionRetData;
import com.dreamgear.http.handler.domain.LoginData;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.majiang.game.player.GamePlayerManager;
import com.dreamgear.majiang.game.player.LoginManager;
import com.dreamgear.majiang.game.player.PlayerManager;
import com.dreamgear.majiang.utils.TimeUtils;

public class GetPlayerInfo extends BaseFunction {

	@Override
	public GMFunctionRetData handle(HttpRequestMessage request) {
		// TODO Auto-generated method stub
		
		String suid = request.getParameter("uid");

		if(suid == null || suid == ""){
			return ReturnError("发送消息不能为空");
		}
		long uid = -1;
		try{
			uid = Long.parseLong(suid);
		}catch(Exception e){
			return ReturnError("玩家id错误:"+suid);
		}
		
		PlayerManager pm = GamePlayerManager.GetInstance().GetPlayer(uid);
		if(pm == null){
			return ReturnError("玩家不存在:"+suid);
		}
		
		LoginData ld = LoginManager.GetInstance().GetLoginData(uid);
		if(ld != null){
			pm.nickname = ld.nickname;
			pm.sex = ld.sex;
			pm.country = ld.country;
			pm.province = ld.province;
			pm.city = ld.city;
		}else{
			pm.nickname = suid;
		}
		
		Map<String , String> userdata = new HashMap<String , String>();
		
		userdata.put("uid", suid);
		userdata.put("nickname", pm.nickname);
		userdata.put("sex", pm.sex);
		userdata.put("country", pm.country);
		userdata.put("province", pm.province);
		userdata.put("city", pm.city);
		userdata.put("lastOnLineTime", TimeUtils.chDate(pm.getPd().getLastLoginTime()).toString());
		userdata.put("createTime", TimeUtils.chDate(pm.getPd().getCreateTime()).toString());
		userdata.put("room", pm.GetRoomListData());
		userdata.put("parent", pm.getPd().getParent()+"");
		
		GMFunctionRetData ret = new GMFunctionRetData();

		ret.code = 1;
		ret.data = JSON.toJSONString(userdata);
		return ret;
	}

}
