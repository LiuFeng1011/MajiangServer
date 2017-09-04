package com.dreamgear.majiang.game.server;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiang.game.server.request.UserDataRequest;
import com.dreamgear.majiang.game.server.resp.UserDataResp;
import com.dreamgear.majiangserver.core.annotation.DGMessageService;
import com.dreamgear.majiangserver.net.BaseMessage;

@DGMessageService
public class UserDataServer extends GameServer {
	private static final Logger logger = LoggerFactory.getLogger(EntryGameServer.class);
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.USER_DATA_UPDATE;
	}

	public BaseMessage GetRequest() {
		// TODO Auto-generated method stub
		return new UserDataRequest();
		
	}

	public BaseMessage handle(IoSession is,BaseMessage request) {
		logger.info("BaseMessage");
		// TODO Auto-generated method stub
		super.handle(is, request);
		if(pm == null) return null;
		

		UserDataRequest req = (UserDataRequest)request;
		
		UserDataResp respdata = new UserDataResp();
		respdata.setType(req.getType() );
		respdata.setUserInfo(req.getUserInfo());
		
		return respdata;
	}
	
}
