package com.dreamgear.majiang.game.server;

import org.apache.mina.core.session.IoSession;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiang.game.server.request.HeartRequest;
import com.dreamgear.majiang.game.server.resp.HeartResp;
import com.dreamgear.majiang.utils.TimeUtils;
import com.dreamgear.majiangserver.core.annotation.DGMessageService;
import com.dreamgear.majiangserver.net.BaseMessage;

@DGMessageService
public class HeartServer extends GameServer{
	
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.HEART;
	}

	public BaseMessage GetRequest() {
		// TODO Auto-generated method stub
		return new HeartRequest();
	}
	//收到心跳
	public BaseMessage handle(IoSession is,BaseMessage request) {
		// TODO Auto-generated method stub
		super.handle(is, request);
		if(pm == null) return null;
		
		pm.setHeartTime(TimeUtils.nowLong());
		
		HeartResp resp = new HeartResp();
		resp.time = TimeUtils.nowLong();
		
		return resp;
	}

}
