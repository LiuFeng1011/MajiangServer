package com.dreamgear.http.handler.impl.gmfunction;

import com.dreamgear.http.handler.domain.GMFunctionRetData;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.majiang.game.player.GamePlayerManager;
import com.dreamgear.majiang.game.player.PlayerManager;

public class ChangGoldCount extends BaseFunction{

	@Override
	public GMFunctionRetData handle(HttpRequestMessage request) {
		String suid = request.getParameter("uid");
		String scount = request.getParameter("count");

		if(suid == null || suid == ""){
			return ReturnError("uid不能为空");
		}
		if(scount == null || scount == ""){
			return ReturnError("scount不能为空");
		}
		long uid = -1;
		try{
			uid = Long.parseLong(suid);
		}catch(Exception e){
			return ReturnError("玩家id错误:"+suid);
		}
		int count = 0;
		try{
			count = Integer.parseInt(scount);
		}catch(Exception e){
			return ReturnError("数量格式错误:"+scount);
		}
		PlayerManager pm = GamePlayerManager.GetInstance().GetPlayer(uid);
		
		if(pm == null){
			return ReturnError("玩家不存在:" + uid);
		}
		
		pm.AddGold(count);
		
		GMFunctionRetData ret = new GMFunctionRetData();
		ret.code = 1;
		ret.data = "玩家:"+uid+"钻石添加成功，钻石总数为:"+pm.getPd().getGold();
		return ret;
	}

}
