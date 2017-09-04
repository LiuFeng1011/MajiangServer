package com.dreamgear.majiangserver.net;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderException;

public class ServerHandler extends IoHandlerAdapter  {
	

	Logger logger = Logger.getLogger(ServerHandler.class);
	
	public ServerHandler(){
		logger.info("ServerHandler init");
	} 
	
	/**
     * 信息发送
     */
    @Override
    public void messageSent(IoSession session, Object message) {
        //System.out.println("messageSent");
    }

    /**
     * 消息接受
     */
    @Override
    public void messageReceived(IoSession session, Object message) {
        //System.out.println("messageReceived");
    }


    /**
     * 发生异常
     */
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        System.out.println("exceptionCaught");
        if (cause instanceof IOException) {  
            logger.info("处理请求出现IO异常 " + session, cause);  
            session.close(true);
          //session关闭时删除绑定数据
            ServerManager.GetInstance().RemovePlayerBySId(session.getId());
        }  
        else if (cause instanceof ProtocolDecoderException) {  
            logger.warn("出现编码与解码异常:" + session, cause);  
            session.close(true);
          //session关闭时删除绑定数据
            ServerManager.GetInstance().RemovePlayerBySId(session.getId());
        }  
        else {  
            logger.error("出现其它异常：" + session, cause);  
            session.close(true);
          //session关闭时删除绑定数据
            ServerManager.GetInstance().RemovePlayerBySId(session.getId());
        }
    }

    /**
     * session等待
     */
    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        System.out.println("sessionIdle");
    }

    /**
     * session关闭
     */
    @Override
    public void sessionClosed(IoSession session) {
        System.out.println("sessionClosed " + session.getRemoteAddress().toString());
        //session关闭时删除绑定数据
        ServerManager.GetInstance().RemovePlayerBySId(session.getId());
    }

    /**
     * session打开
     */
    @Override
    public void sessionOpened(IoSession session) {
        System.out.println("sessionOpened " + session.getRemoteAddress().toString());
    }

    /**
     * session创建
     */
    @Override
    public void sessionCreated(IoSession session) {
        System.out.println("sessionCreated : " + session.getId());
    }
}
