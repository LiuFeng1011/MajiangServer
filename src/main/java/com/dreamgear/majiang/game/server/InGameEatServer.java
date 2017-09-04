package com.dreamgear.majiang.game.server;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamgear.majiang.game.game.Room;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiang.game.server.request.InGameEatRequest;
import com.dreamgear.majiangserver.core.annotation.DGMessageService;
import com.dreamgear.majiangserver.net.BaseMessage;

@DGMessageService
public class InGameEatServer extends GameServer{
	private static final Logger logger = LoggerFactory.getLogger(InGameEatServer.class);
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.GAME_EAT;
	}

	public BaseMessage GetRequest() {
		// TODO Auto-generated method stub
		return new InGameEatRequest();
	}
	
	//
	public BaseMessage handle(IoSession is,BaseMessage request) {
		// TODO Auto-generated method stub
		logger.info("============InGameHuServer==========");
		super.handle(is, request);
		if(pm == null) return null;
		Room r = pm.getRoom();
		if(r == null) {
			logger.info("no't in room");
			return null;
		}
		
		InGameEatRequest req = (InGameEatRequest)request;
		
		r.getGameLogic().EatBrand(pm.getRoom().getGameLogic().GetPlayerIndex(pm),
				req.bid1,
				req.bid2);
		
		return null;
	}
}
