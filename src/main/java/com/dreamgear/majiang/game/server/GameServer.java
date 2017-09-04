package com.dreamgear.majiang.game.server;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamgear.majiang.game.player.GamePlayerManager;
import com.dreamgear.majiang.game.player.PlayerManager;
import com.dreamgear.majiang.utils.TimeUtils;
import com.dreamgear.majiangserver.net.BaseMessage;
import com.dreamgear.majiangserver.net.BaseServer;

public abstract class GameServer implements BaseServer{
	private static final Logger logger = LoggerFactory.getLogger(GameServer.class);
	protected PlayerManager pm;
	public BaseMessage handle(IoSession is,BaseMessage request) {
		// TODO Auto-generated method stub
		//添加玩家
		pm = GamePlayerManager.GetInstance().GetPlayerByUid(request.getUserInfo().getUid());
		if(pm != null){
			pm.setHeartTime(TimeUtils.nowLong());
		}else{
			logger.info("can't find player : " + request.getUserInfo().getUid());
		}
		
		return null;
	}
}
