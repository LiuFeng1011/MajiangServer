package com.dreamgear.majiang.game.server;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamgear.majiang.common.GameCommon;
import com.dreamgear.majiang.common.GameTools;
import com.dreamgear.majiang.common.GameCommon.enPlayerRoomState;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.game.game.Room;
import com.dreamgear.majiang.game.game.RoomManager;
import com.dreamgear.majiang.game.game.RoomPlayData;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiang.game.server.request.RoomRequest;
import com.dreamgear.majiang.game.server.resp.RoomResp;
import com.dreamgear.majiangserver.core.annotation.DGMessageService;
import com.dreamgear.majiangserver.net.BaseMessage;

@DGMessageService
public class RoomServer extends GameServer{

	private static final Logger logger = LoggerFactory.getLogger(RoomServer.class);
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.ROOM;
	}

	public BaseMessage GetRequest() {
		// TODO Auto-generated method stub
		return new RoomRequest();
	}
	//
	public BaseMessage handle(IoSession is,BaseMessage request) {
		// TODO Auto-generated method stub
		logger.info("============RoomServer==========");
		super.handle(is, request);
		if(pm == null) return null;
		
		RoomRequest req = (RoomRequest)request;
		logger.info("type : " + req.type);
		switch(req.type){
		case GameProtocol.ROOM_CREATE:
			RoomManager.GetInstance().CreateRoom(pm,req.value,req.ispao,req.findtype);
			break;
		case GameProtocol.ROOM_IN:
			if(req.findtype == GameCommon.enRoomFindType.firend.ordinal()){
				int isin = RoomManager.GetInstance().InRoom(pm, req.roomid);
				
				if(isin == 0){
					RoomPlayData r = DBManager.getInstance().getRoomDAO().getRoomData(req.roomid);
					
					RoomResp resp = new RoomResp();
					resp.type = GameProtocol.ROOM_IN;
					if(r != null && r.playcount > 0){
						resp.rp = r;
					}
					resp.setUserInfo(pm.getUi());
					resp.Send();
				}
			}else{
				RoomManager.GetInstance().InRandomRoom(pm, req.ispao, req.findtype);
			}
			
			break;
		case GameProtocol.ROOM_LEAVE:
			RoomManager.GetInstance().LeaveRoom(pm);
			break;
		case GameProtocol.ROOM_DOWN:
			RoomManager.GetInstance().PlayerDown(pm, req.value);
			break;
		case GameProtocol.ROOM_STARTGAME:
			RoomManager.GetInstance().StartGame(pm);
			break;
		case GameProtocol.ROOM_UP:
			RoomManager.GetInstance().Up(pm);
			break;
		case GameProtocol.ROOM_READY:
			RoomManager.GetInstance().Ready(pm,enPlayerRoomState.enDown.ordinal());
			
			break;
			
		}
		
		return null;
	}
}
