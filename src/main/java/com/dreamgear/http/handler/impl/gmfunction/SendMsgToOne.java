package com.dreamgear.http.handler.impl.gmfunction;

import com.dreamgear.http.handler.domain.GMFunctionRetData;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.majiang.game.player.GamePlayerManager;

public class SendMsgToOne extends BaseFunction {
	@Override
	public GMFunctionRetData handle(HttpRequestMessage request) {
		String suid = request.getParameter("uid");
		String msg = request.getParameter("msg");

		if(msg == null || msg == ""){
			return ReturnError("发送消息不能为空");
		}
		long uid = -1;
		if(suid == null || suid == ""){
			return ReturnError("玩家id不能为空");
		}else{
			try{
				uid = Long.parseLong(suid);
			}catch(Exception e){
				return ReturnError("玩家id错误:"+suid);
			}
		}

		GMFunctionRetData ret = new GMFunctionRetData();
		ret.code = 1;
		ret.data = GamePlayerManager.GetInstance().SendMsgToPlayer(uid, msg);
		
		return ret;
	}
}
