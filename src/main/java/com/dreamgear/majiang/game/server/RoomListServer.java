package com.dreamgear.majiang.game.server;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiang.game.server.request.RoomListRequest;
import com.dreamgear.majiang.game.server.resp.RoomListResp;
import com.dreamgear.majiangserver.core.annotation.DGMessageService;
import com.dreamgear.majiangserver.net.BaseMessage;

@DGMessageService
public class RoomListServer  extends GameServer{
	private static final Logger logger = LoggerFactory.getLogger(RoomListServer.class);
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.PLAYER_ROOM_LIST;
	}
	public BaseMessage GetRequest() {
		// TODO Auto-generated method stub
		return new RoomListRequest();
	}
	

	public BaseMessage handle(IoSession is,BaseMessage request) {
		// TODO Auto-generated method stub
		logger.info("============RoomListServer==========");
		super.handle(is, request);
		if(pm == null) return null;

		RoomListResp resp = new RoomListResp();
		resp.data = pm.GetRoomListData();
		resp.setUserInfo(pm.getUi());
		return resp;
	}
}
