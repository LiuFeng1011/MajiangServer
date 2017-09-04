package com.dreamgear.majiang.game.server;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiang.game.server.request.UserDataRequest;
import com.dreamgear.majiang.game.server.request.WXSignRequest;
import com.dreamgear.majiang.game.server.resp.UserDataResp;
import com.dreamgear.majiang.game.server.resp.WXSingResp;
import com.dreamgear.majiangserver.core.annotation.DGMessageService;
import com.dreamgear.majiangserver.net.BaseMessage;
import com.dreamgear.wx.WXTicketManager;

@DGMessageService
public class WXSingServer extends GameServer{
	private static final Logger logger = LoggerFactory.getLogger(WXSingServer.class);
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.WX_SIGN;
	}

	public BaseMessage GetRequest() {
		// TODO Auto-generated method stub
		return new WXSignRequest();
		
	}

	public BaseMessage handle(IoSession is,BaseMessage request) {

		// TODO Auto-generated method stub
		super.handle(is, request);
		if(pm == null) return null;
		
		WXSignRequest req = (WXSignRequest)request;
		
		WXSingResp respdata = new WXSingResp();
		respdata.setUserInfo(req.getUserInfo());
		respdata.maps = WXTicketManager.GetInstance().Sign(req.url);
		
		return respdata;
	}
}
