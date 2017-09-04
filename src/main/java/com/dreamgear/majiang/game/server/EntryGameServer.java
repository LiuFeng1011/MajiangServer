package com.dreamgear.majiang.game.server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamgear.http.handler.domain.WXPlayerInfo;
import com.dreamgear.majiang.common.GameCommon.enDelegateState;
import com.dreamgear.majiang.delegate.DelegateManager;
import com.dreamgear.majiang.game.log.LogData;
import com.dreamgear.majiang.game.log.LogEventID;
import com.dreamgear.majiang.game.notice.GameNotice;
import com.dreamgear.majiang.game.notice.NoticeMgr;
import com.dreamgear.majiang.game.player.GamePlayerManager;
import com.dreamgear.majiang.game.player.LoginManager;
import com.dreamgear.majiang.game.player.PlayerData;
import com.dreamgear.majiang.game.player.PlayerManager;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiang.game.server.request.EntryGameRequest;
import com.dreamgear.majiang.game.server.resp.EntryGameFailedResp;
import com.dreamgear.majiang.game.server.resp.EntryGameResp;
import com.dreamgear.majiang.game.server.resp.NoticeResp;
import com.dreamgear.majiang.utils.TimeUtils;
import com.dreamgear.majiangserver.core.annotation.DGMessageService;
import com.dreamgear.majiangserver.net.BaseMessage;
import com.dreamgear.majiangserver.net.ServerManager;

@DGMessageService
public class EntryGameServer extends GameServer {
	private static final Logger logger = LoggerFactory.getLogger(EntryGameServer.class);
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.ENTRY_GAME;
	}

	public BaseMessage GetRequest() {
		// TODO Auto-generated method stub
		return new EntryGameRequest();
	}

	public BaseMessage handle(IoSession is,BaseMessage request) {
		// TODO Auto-generated method stub
		
		WXPlayerInfo playerInfo = LoginManager.GetInstance().GetPlayerInfo(request.getUserInfo().getUid());
		
		EntryGameRequest req = (EntryGameRequest)request;
		
		if(playerInfo == null){
			if(req.channelid == 1 || request.getUserInfo().getUid() > 20){
				EntryGameFailedResp resp = new EntryGameFailedResp();
				resp.setUserInfo(req.getUserInfo());
				return resp;
			}
			
		}else{
			
		}
		
    	String clientIP = ((InetSocketAddress)is.getRemoteAddress()).getAddress().getHostAddress();
		//添加玩家
		ServerManager.GetInstance().BindUserInfo(req.getUserInfo());
		
		Login(req.getUserInfo().getUid());
		
		super.handle(is, request);
		pm.setUi(req.getUserInfo());
		pm.setAddress(clientIP);
		
		logger.info("req.delegate : " + req.delegate);
		logger.info("pm.getPd().getParent() : " + pm.getPd().getParent());
		
		if(req.delegate != -1 && (pm.getPd().getParent() == -1 || pm.getPd().getParent() == 0)){
			enDelegateState delegatestate = DelegateManager.GetInstance().UserDelegateState(req.delegate);
			
			if(delegatestate == enDelegateState.enYes ){
				pm.SetParent(req.delegate);

			}
		}
		
		if(playerInfo != null){
			pm.nickname = playerInfo.nickname;
			pm.sex = playerInfo.sex;
			pm.province = playerInfo.province;
			pm.city = playerInfo.city;
			pm.country = playerInfo.country;
			pm.headimgurl = playerInfo.headimgurl;
			
			pm.getPd().setUname(playerInfo.nickname);
			pm.getPd().setHead(playerInfo.headimgurl);
		}else{
			pm.nickname = "" + request.getUserInfo().getUid();
		}
		

		EntryGameResp resp = new EntryGameResp();
		resp.setUserInfo(req.getUserInfo());
		resp.gold = pm.getPd().getGold();
		resp.playcount = pm.getPd().getPlaycount();
		resp.wincount = pm.getPd().getWincount();
		
		resp.nickname 	= pm.nickname;
		resp.sex 		= pm.sex;
		resp.province 	= pm.province;
		resp.city 		= pm.city;
		resp.country 	= pm.country;
		resp.headimgurl = pm.headimgurl;
		
		pm.getPd().setLastLoginTime(TimeUtils.nowLong());
		logger.info("[EntryGameServer] entry game : " + request.getUserInfo().getUid());
		
		//公告列表
		NoticeResp noticeResp = new NoticeResp();
		Map<Long, GameNotice>  map = NoticeMgr.getInstance().getNoticeList();
		for (Entry<Long, GameNotice> entry : map.entrySet()) {  
			noticeResp.noticeList.add(entry.getValue());
		}  
		
		noticeResp.setUserInfo(pm.getUi());
		noticeResp.Send();
		
		
		/****ModuleTest****/
//		ServerModuleTest t1 = new ServerModuleTest(2,3,"a");
//		ServerModuleTest t2 = new ServerModuleTest(5,6,"c");
//		
//		RespModuleSet moduleTestResp = new RespModuleSet();
//		moduleTestResp.AddModule(t1);
//		moduleTestResp.AddModule(t2);
//		moduleTestResp.setUserInfo(req.getUserInfo());
//		moduleTestResp.Send();
		/****************/
		
		return resp;
	}
	
	public void Login(long uid){
		PlayerManager pm = GamePlayerManager.GetInstance().GetPlayer(uid);
		
		if(pm == null){
			//新玩家
			PlayerData pd = GamePlayerManager.GetInstance().CreatePlayer(uid);
			
			pm = new PlayerManager(pd);

			LogData.AddLog(pm, LogEventID.EVE_PLAYER_LOGIN, 1, "");
			
			GamePlayerManager.GetInstance().AddPlayer(pm);
		}else{
			LogData.AddLog(pm, LogEventID.EVE_PLAYER_LOGIN, 0, "");
		}

		pm.getPd().setLastLoginTime(TimeUtils.nowLong());

		pm.Login();
	}
}
