package com.dreamgear.http.handler.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.HttpHandler;
import com.dreamgear.http.handler.domain.LoginData;
import com.dreamgear.http.handler.domain.ReturnEntity;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.http.response.HttpResponseMessage;
import com.dreamgear.majiang.common.GameCommon.enDelegateState;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.delegate.DelegateData;
import com.dreamgear.majiang.delegate.DelegateIncomeData;
import com.dreamgear.majiang.delegate.DelegateManager;
import com.dreamgear.majiang.game.player.LoginManager;
import com.dreamgear.majiang.utils.JsonUtil;

public class DelegateIMPL extends HttpHandler{

	private static final Logger logger = LoggerFactory.getLogger(DelegateIMPL.class);

	@Override
	public String handle(HttpRequestMessage request, HttpResponseMessage response) {

		String function = request.getParameter("function");
		logger.info("function : " + function);
		Map<String,Object> data = null;
		if(function.equals("login")){
			data = this.login(request);
		}else if(function.equals("reg")){
			data = this.reg(request);
		}else if(function.equals("getdata")){
			data = this.getdata(request);
		}

		/**
		 * code = 0表示失败
		 * errmsg ; 只有在code == 0时有此字段
		 * code = 1请求成功
		 * 其他字段，code=1时不同接口返回不同内容
		 */
		ReturnEntity ret = ReturnEntity.createSucc(data);
		response.appendBody(JsonUtil.ObjectToJsonString(ret));
		response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
		
		return "";
	}
	
	public Map<String,Object> getdata(HttpRequestMessage request){
		String suid = request.getParameter("uid");
		String stype = request.getParameter("type");
		
		logger.info("stype : " + stype);

		Map<String,Object> data = new HashMap<String,Object>();

		long uid = -1;
		try{
			uid = Integer.parseInt(suid);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		if(uid == -1){
			data.put("code", 0);
			data.put("errmsg", "用户id错误");
			return data;
		}
		
		int type = -1;
		try{
			type = Integer.parseInt(stype);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		if(type == -1){
			data.put("code", 0);
			data.put("errmsg", "数据类型错误");
			return data;
		}
		
//		enDelegateState state = DelegateManager.GetInstance().UserDelegateState(uid);
		DelegateData delegateData = DBManager.getInstance().getDelegateDAO().getDataByUID(uid);
		if(delegateData == null){
			data.put("code", 0);
			data.put("errmsg", "无法获取代理数据");
			return data;
		}
		if(delegateData.check != enDelegateState.enYes){
			data.put("code", 0);
			data.put("errmsg", "您还不是代理,状态码:" + delegateData.check);
			return data;
		}
		
		Map<String,Object> list = DelegateManager.GetInstance().GetDelegateIncomeData(uid, type,delegateData.checkouttime);
		
		if(list == null){
			data.put("code", 0);
			data.put("errmsg", "数据类型错误");
			return data;
		}
		data.put("code", 1);
		data.put("data", JSON.toJSONString(list));
		
		return data;
	}

	//代理登录
	public Map<String,Object> login(HttpRequestMessage request){
		String suid = request.getParameter("uid");

		Map<String,Object> data = new HashMap<String,Object>();
		
		long uid = -1;
		try{
			uid = Integer.parseInt(suid);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		if(uid == -1){
			data.put("code", 0);
			data.put("errmsg", "用户id错误");
			return data;
		}
		
		LoginData ld = LoginManager.GetInstance().GetLoginData(uid);
		
		if(ld == null){
			data.put("code", 0);
			data.put("errmsg", "无法获取用户信息");
			return data;
		}
		
		
		DelegateData dbdata = DBManager.getInstance().getDelegateDAO().getDataByUID(uid);
		
		if(dbdata == null){
			dbdata = new DelegateData();
			dbdata.setUid(uid);
			dbdata.setCheck(enDelegateState.enNull);
		}
		data.put("code", 1);
		data.put("uid", dbdata.getUid());
		data.put("check", dbdata.getCheck());
		data.put("checkouttime", dbdata.getCheckouttime());
		data.put("phone", dbdata.getPhone());
		data.put("nickname", ld.nickname);
		data.put("headurl", ld.head);
		data.put("sex", ld.sex);
		data.put("rate", dbdata.getRate());

//		data.put("data", JSON.toJSON(dbdata));
		return data;
	}
	//注册代理
	public Map<String,Object> reg(HttpRequestMessage request){
		String suid = request.getParameter("uid");
		String phone = request.getParameter("phone");
		
		Map<String,Object> data = new HashMap<String,Object>();
		
		if(phone == null || data.equals("")){
			data.put("code", 0);
			data.put("errmsg", "电话不能为空");
			return data;
		}
		long uid = -1;
		try{
			uid = Integer.parseInt(suid);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		
		if(uid == -1){
			data.put("code", 0);
			data.put("errmsg", "用户id错误");
			return data;
		}
		
		// -1 申请成功  0已经是代理了 1	已经申请过了
		int result = DelegateManager.GetInstance().RegDelegate(uid, phone);
		logger.info("result : " + result);
		switch(result){
		case -1:
			break;
		case 0:
			data.put("code", 0);
			data.put("errmsg", "已经是代理了");
			return data;
		case 1:
			data.put("code", 0);
			data.put("errmsg", "您已经提交过申请了");
			return data;
		}
		data.put("code", 1);
		data.put("data", "申请成功");
		return data;
	}
	
	
	
}
