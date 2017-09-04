package com.dreamgear.http.handler.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamgear.gm.GMLoginData;
import com.dreamgear.gm.GMUserManager;
import com.dreamgear.http.handler.HttpHandler;
import com.dreamgear.http.handler.domain.GMFunctionRetData;
import com.dreamgear.http.handler.domain.ReturnEntity;
import com.dreamgear.http.handler.impl.gmfunction.BaseFunction;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.http.response.HttpResponseMessage;
import com.dreamgear.majiang.core.LogType;
import com.dreamgear.majiang.utils.JsonUtil;

public class GMFunction extends HttpHandler {
	private static final Logger logger = LoggerFactory.getLogger(GMFunction.class);


    @SuppressWarnings("unchecked")
	@Override
	public String handle(HttpRequestMessage request, HttpResponseMessage response) {
		logger.info("=========GMFunction=============");
		String funcion = request.getParameter("funciont");
		String uname = request.getParameter("uname");
		String token = request.getParameter("token");
		logger.info("funciont : " + funcion + "   uname : " + uname + "  token : " + token);
		GMLoginData gmLoginData = GMUserManager.GetInstance().Token(uname, token);
		if(gmLoginData == null){
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("code", 3);
			ReturnEntity ret = ReturnEntity.createSucc(data);
			response.appendBody(JsonUtil.ObjectToJsonString(ret));
			response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
			
			return "";
		}
		LogType.GM_OPERATION.getLogger().info(request.getURI());
		Map<String,Object> data = new HashMap<String,Object>();

        Class<? extends BaseFunction> clazz = null;

		try {
			if("".equals(funcion)){
				data.put("code", 0);
				data.put("errmsg", "funcion is null! ");
			}else{
				clazz = (Class<? extends BaseFunction>)Class.forName("com.dreamgear.http.handler.impl.gmfunction." + funcion);
				BaseFunction handler = clazz.newInstance();
				
				if(handler.FunctionPower() > gmLoginData.getData().getPower()){
					data.put("code", 0);
					data.put("errmsg", "权限不足");
				}else{
					GMFunctionRetData result = handler.handle(request);
					
					if(result.code == 1){
						data.put("code", 1);
						data.put("data", result.data);
					}else{
						data.put("code", 0);
						data.put("errmsg", result.errmsg);
					}
				}
				
				
			}
		}catch(Exception e){
			e.printStackTrace();

			data.put("code", 0);
			data.put("errmsg",e.toString());
		}
		
//		try{
//			//获取方法  
//			Method m = this.getClass().getDeclaredMethod(funciont, HttpRequestMessage.class);
//			//调用方法  
//			GMFunctionRetData result = (GMFunctionRetData) m.invoke(this,request);
//			if(result.code == 1){
//				data.put("code", 1);
//				data.put("data", result.data);
//			}else{
//				data.put("code", 0);
//				data.put("errmsg", result.errmsg);
//			}
//			
//			
//		}catch(Exception e){
//			e.printStackTrace();
//			data.put("code", 0);
//			data.put("errmsg", "funciont error : " + funciont);
//		}
		
		ReturnEntity ret = ReturnEntity.createSucc(data);
		response.appendBody(JsonUtil.ObjectToJsonString(ret));
		response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
		
		return "";
	}
	
}
