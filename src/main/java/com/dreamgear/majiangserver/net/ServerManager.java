package com.dreamgear.majiangserver.net;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamgear.majiang.game.player.GamePlayerManager;


public class ServerManager {
	private static ServerManager instance;
	private static final Logger logger = LoggerFactory.getLogger(ServerManager.class);
	
	/** 
	 * 保存游戏中的所有服务
	 */
	private Map<Integer,BaseServer> serverList = new ConcurrentHashMap<Integer,BaseServer>();
	/**
	 * 保存所有登录用户的session
	 */
	private Map<Long, UserInfo> sessionMap = new ConcurrentHashMap<Long, UserInfo>();

    // 服务器监听端口  
	private static int PORT = 9998;
	
	public ServerManager(int port){
		instance = this;
		PORT = port;
	}
	public static ServerManager GetInstance(){
		return instance;
	}
	
	public void Start(){
		// 服务器端的主要对象  
		NioSocketAcceptor acceptor = new NioSocketAcceptor();  
		acceptor.setReuseAddress(true);  
        
        // 设置Filter链   
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());  
        
        //取得协议拦截器
        acceptor.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new MinaFilterFactory()));
        
        // 设置消息处理类（创建、关闭Session，可读可写等等，继承自接口IoHandler）  
        acceptor.setHandler(new ServerHandler() );
        
        // 设置接收缓存区大小  
        acceptor.getSessionConfig().setReadBufferSize(2048);
        //acceptor.getSessionConfig().setSendBufferSize(2048);
        //acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);  
        
        logger.info("PORT : " + PORT);
        try {  
            // 服务器开始监听  
            acceptor.bind( new InetSocketAddress(PORT) );
        }catch(Exception e){  
            e.printStackTrace();  
        }  
	}
	
	public Map<Integer, BaseServer> getServerList() {
		return serverList;
	}

	public void setServerList(Map<Integer, BaseServer> serverList) {
		this.serverList = serverList;
	}
	
	public void BindUserInfo(UserInfo userInfo) {
		RemovePlayer(userInfo);
		sessionMap.put(userInfo.getSession().getId(), userInfo);
		System.out.println("bind player session : " + userInfo.uid);
	}
	
	public void RemovePlayer(UserInfo userInfo) {		
		for (Entry<Long, UserInfo> it : sessionMap.entrySet()) {
			if(it.getValue().getUid() == userInfo.getUid()) {
				try {
					RemovePlayerBySId(it.getKey());
				} catch (Exception e) {
					// TODO: handle exception
					logger.error("ServerManager RemovePlayer:",e);
				}
			}
		}
	}
	
	public void RemovePlayerBySId(long sessionId) {
		if(sessionMap.containsKey(sessionId)) {
			UserInfo userInfo = sessionMap.get(sessionId);
			GamePlayerManager.GetInstance().PlayerLeave(userInfo.uid);
			sessionMap.remove(sessionId);
			logger.info("RemovePlayerBySId : " + sessionId);
		}
	}
	
	public IoSession GetSession(UserInfo userInfo){
		if(!sessionMap.containsKey(userInfo.getSession().getId())) {
			return null;
		}
		return sessionMap.get(userInfo.getSession().getId()).getSession();
	}
	
	public void SendMessage(BaseMessage msg){
		if(msg.getUserInfo() == null){
			System.out.println("userinfo is null!");
			return;
		}
		
		IoSession is = GetSession(msg.getUserInfo());
		if(is == null){
			System.out.println("cant find session,uid : " + msg.getUserInfo().getUid());
			msg.setUserInfo(null);
			return;
		}
		if(is.isClosing()) {
			msg.setUserInfo(null);
			return;
		}
		msg.setUserInfo(null);
		is.write(msg);
	}
	
	public UserInfo getUserInfo(IoSession session) {
		if(sessionMap.containsKey(session.getId())) {
			return sessionMap.get(session.getId());
		}
		return null;
	}
	
	public Map<Long, UserInfo> getSessionMap() {
		return sessionMap;
	}
	
	public void setSessionMap(Map<Long, UserInfo> sessionMap) {
		this.sessionMap = sessionMap;
	}
	
	
}
