package com.dreamgear.http.handler.impl.gmfunction;

import com.dreamgear.http.handler.domain.GMFunctionRetData;
import com.dreamgear.http.request.HttpRequestMessage;

public abstract class BaseFunction {
	public abstract GMFunctionRetData handle(HttpRequestMessage request); 
	public int FunctionPower (){
		return 1;
	}
	public GMFunctionRetData ReturnError(String msg){
		GMFunctionRetData ret = new GMFunctionRetData();
		ret.code = 0;
		ret.errmsg = msg;
		return ret;
	}
}
