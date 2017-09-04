package com.dreamgear.http.handler;

import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamgear.http.handler.domain.ReturnEntity;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.http.response.HttpResponseMessage;
import com.dreamgear.majiang.common.GameTools;
import com.dreamgear.majiang.utils.JsonUtil;

public class HttpServerHandler extends IoHandlerAdapter  {

	static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

	static final String HTTP_BASE_LOGIC_PACKAGE = "com.keyking.coin.service.http.handler.impl";
	
    @Override  
    public void sessionOpened(IoSession session) {  
        // set idle time to 60 seconds  
        session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);  
    }  
  
    @SuppressWarnings("unchecked")
	@Override  
    public void messageReceived(IoSession session, Object message) { 
        HttpRequestMessage request = (HttpRequestMessage) message;
//        String a = request.getPostValue();
        String logicName = request.getParameter("logic");
        Class<? extends HttpHandler> clazz = null;
        HttpResponseMessage response = new HttpResponseMessage();
		try {
			
//			if (logicName.contains(".js")){
//				HttpJs js = new HttpJs(logicName);
//				response.appendBody(js.handle(request,response)); 
//			}else{
//				clazz = (Class<? extends HttpHandler>)Class.forName("com.keyking.http.handler.impl." + logicName);
//				HttpHandler handler = clazz.newInstance();
//				handler.handle(request,response); 
//			}
			System.out.println("logicName : " + logicName);
			if("".equals(logicName)){
				ReturnEntity ret = ReturnEntity.createFail("logicName is null!!!!");
				response.appendBody(JsonUtil.ObjectToJsonString(ret));
			}else{
				clazz = (Class<? extends HttpHandler>)Class.forName("com.dreamgear.http.handler.impl." + logicName);
				HttpHandler handler = clazz.newInstance();
				handler.handle(request,response); 
			}
			
		}catch(Exception e){
//			if (!(e instanceof ClassNotFoundException)){
				GameTools.Log("http handler error"+e);
//			}
		}
		response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
//        response.appendBody("uid="+uid+",name="+name);
		session.write(response).addListener(IoFutureListener.CLOSE);
    }  
  
    @Override  
    public void sessionIdle(IoSession session, IdleStatus status) {  
        session.close(false);  
    }  
  
    @Override  
    public void exceptionCaught(IoSession session, Throwable cause) {  
        session.close(false);  
    }  
}
