package com.dreamgear.majiangserver.net;

import org.apache.mina.core.session.IoSession;

public interface BaseServer {
	public int GetProtocol();
	public BaseMessage GetRequest();
    public BaseMessage handle(IoSession is,BaseMessage request) ;
    
}
