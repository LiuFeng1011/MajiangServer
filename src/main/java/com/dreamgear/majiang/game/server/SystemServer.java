package com.dreamgear.majiang.game.server;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamgear.majiang.common.GameTools;
import com.dreamgear.majiang.game.data.GameDataManager;
import com.dreamgear.majiang.game.data.IapData;
import com.dreamgear.majiang.game.game.RoomManager;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiang.game.server.request.RoomRequest;
import com.dreamgear.majiang.game.server.request.SystemRequest;
import com.dreamgear.majiangserver.core.annotation.DGMessageService;
import com.dreamgear.majiangserver.net.BaseMessage;

@DGMessageService
public class SystemServer extends GameServer{

	private static final Logger logger = LoggerFactory.getLogger(SystemServer.class);
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.SYSTEM;
	}
	

	public BaseMessage GetRequest() {
		// TODO Auto-generated method stub
		return new SystemRequest();
	}
	//
	public BaseMessage handle(IoSession is,BaseMessage request) {
		// TODO Auto-generated method stub
		logger.info("============SystemServer==========");
		super.handle(is, request);
		if(pm == null) return null;
		
		SystemRequest req = (SystemRequest)request;
		logger.info("type : " + req.type);
		switch(req.type){
		case GameProtocol.SYSTEM_PAY:
//			IapData iapdata= GameDataManager.iapDataManager.GetDataById(Integer.parseInt(req.value));
//			if(pm.getPd().getGold() > 100000){
//				GameTools.SendMessage(pm.getUi(), "您的钻石已经够多啦，先去玩一会游戏吧~");
//				return null;
//			}
//			pm.AddGold(iapdata.getCount());
//
//			GameTools.SendMessage(pm.getUi(), "成功购买" + iapdata.getCount() + "钻石");
			GameTools.SendMessage(pm.getUi(), "请使用正式渠道进行购买！");
			break;
		case GameProtocol.SYSTEM_PLAYERDATA:
			
			break;
		}
		
		return null;
	}

}
