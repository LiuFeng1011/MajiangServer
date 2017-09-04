package com.dreamgear.http.handler.impl.gmfunction;

import com.dreamgear.http.handler.domain.GMFunctionRetData;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.majiang.game.player.GamePlayerManager;

public class SendMsgToAll extends BaseFunction {

	@Override
	public GMFunctionRetData handle(HttpRequestMessage request) {
		// TODO Auto-generated method stub
		String msg = request.getParameter("msg");
		
		GMFunctionRetData ret = new GMFunctionRetData();

		if(msg == null || msg == ""){
			return ReturnError("发送消息不能为空");
		}
		
		ret.code = 1;
		ret.data = GamePlayerManager.GetInstance().SendMsgToAll(msg);
		return ret;
	}

}
