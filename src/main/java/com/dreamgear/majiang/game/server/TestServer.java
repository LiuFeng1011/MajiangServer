package com.dreamgear.majiang.game.server;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiang.game.server.request.TestRequest;
import com.dreamgear.majiang.game.server.resp.TestResp;
import com.dreamgear.majiangserver.core.annotation.DGMessageService;
import com.dreamgear.majiangserver.net.BaseMessage;

@DGMessageService
public class TestServer extends GameServer{
	private static final Logger logger = LoggerFactory.getLogger(TestServer.class);

	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.TEST;
	}

	public TestRequest GetRequest() {
		// TODO Auto-generated method stub
		return new TestRequest();
		
	}

	public BaseMessage handle(IoSession is,BaseMessage request) {
		// TODO Auto-generated method stub

		super.handle(is, request);
		
		logger.info("player test : " + pm.getPd().getUid());
		TestRequest req = (TestRequest)request;
		
		TestResp respdata = new TestResp();
		respdata.setUserInfo(req.getUserInfo());

		return respdata;
	}

}
