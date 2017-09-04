package com.dreamgear.majiang.game.server;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamgear.majiang.game.data.FaceData;
import com.dreamgear.majiang.game.data.GameDataManager;
import com.dreamgear.majiang.game.game.Room;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiang.game.server.request.ChatRequest;
import com.dreamgear.majiang.game.server.resp.ChatResp;
import com.dreamgear.majiangserver.core.annotation.DGMessageService;
import com.dreamgear.majiangserver.net.BaseMessage;

@DGMessageService
public class ChatServer extends GameServer{
	private static final Logger logger = LoggerFactory.getLogger(ChatServer.class);
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.CHAT;
	}

	public BaseMessage GetRequest() {
		// TODO Auto-generated method stub
		return new ChatRequest();
	}

	public BaseMessage handle(IoSession is,BaseMessage request) {
		logger.info("============InGameChaServer==========");
		super.handle(is, request);
		if(pm == null) return null;
		Room r = pm.getRoom();
		if(r == null) {
			logger.info("no't in room");
			return null;
		}
		ChatRequest req = (ChatRequest)request;
		
		if(req.type == 3){//收费表情
			FaceData fd = GameDataManager.faceDataManager.GetDataById(req.msg);
			if(pm.getPd().getGold() < fd.count){
				return null;
			}
			pm.AddGold(-fd.count);
		}
		
		
		ChatResp resp = new ChatResp();
		resp.type = req.type;
		resp.receiver = req.receiver;
		resp.sender = req.sender;
		resp.msg = req.msg;
		
		r.Chat(resp);
		
		return null;
	}
}
