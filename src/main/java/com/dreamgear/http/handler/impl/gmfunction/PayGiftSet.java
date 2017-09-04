package com.dreamgear.http.handler.impl.gmfunction;

import com.dreamgear.http.handler.domain.GMFunctionRetData;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.majiang.game.payGift.PayGiftManager;
import com.dreamgear.majiang.game.player.GamePlayerManager;

public class PayGiftSet extends BaseFunction{

	@Override
	public GMFunctionRetData handle(HttpRequestMessage request) {

		String spid = request.getParameter("pid");
		String scount = request.getParameter("count");

		int pid = -1;
		try{
			pid = Integer.parseInt(spid);
		}catch(Exception e){
			return ReturnError("pid错误:"+spid);
		}

		int count = -1;
		try{
			count = Integer.parseInt(scount);
		}catch(Exception e){
			return ReturnError("scount错误:"+scount);
		}
		
		PayGiftManager.GetInstance().SetGiftCount(pid, count);
		
		GMFunctionRetData ret = new GMFunctionRetData();
		ret.code = 1;
		ret.data = "设置成功";
		return ret;
		
	}
}
