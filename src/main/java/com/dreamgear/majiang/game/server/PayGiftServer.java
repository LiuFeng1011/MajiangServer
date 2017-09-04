package com.dreamgear.majiang.game.server;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamgear.majiang.game.game.Room;
import com.dreamgear.majiang.game.payGift.PayGiftManager;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiang.game.server.request.PayGiftRequest;
import com.dreamgear.majiang.game.server.resp.PayGiftResp;
import com.dreamgear.majiangserver.core.annotation.DGMessageService;
import com.dreamgear.majiangserver.net.BaseMessage;

@DGMessageService
public class PayGiftServer extends GameServer {
	private static final Logger logger = LoggerFactory.getLogger(PayGiftServer.class);
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.PAYGIFT;
	}


	public PayGiftRequest GetRequest() {
		// TODO Auto-generated method stub
		return new PayGiftRequest();
	}
	
	public BaseMessage handle(IoSession is,BaseMessage request) {
		// TODO Auto-generated method stub
		logger.info("============ReinroomServer==========");

		super.handle(is, request);
		if(pm == null) return null;
		
		PayGiftResp resp = new PayGiftResp();
		resp.data = PayGiftManager.GetInstance().getGiftList();
		resp.setUserInfo(pm.getUi());
		return resp;
	}
}
