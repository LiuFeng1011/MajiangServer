package com.dreamgear.http.handler.impl.gmfunction;

import com.dreamgear.gm.GMUserData;
import com.dreamgear.http.handler.domain.GMFunctionRetData;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.majiang.db.DBManager;

public class AddGmUser extends BaseFunction {

	@Override
	public GMFunctionRetData handle(HttpRequestMessage request) {
		String uname = request.getParameter("u");
		String pw = request.getParameter("p");
		String power = request.getParameter("power");
		
		if(uname == null || uname == ""){
			return ReturnError("uname不能为空");
		}

		if(pw == null || pw == ""){
			return ReturnError("pw不能为空");
		}
		if(power == null || power == ""){
			return ReturnError("power不能为空");
		}
		
		int ipower = -1;
		try{
			ipower = Integer.parseInt(power);
		}catch(Exception e){
			return ReturnError("错误的权限:"+power);
		}
		

		String SQL_LOGIN_QUERYONE = "SELECT COUNT(*) FROM gmuser WHERE uname='"+uname+"'";
		int count = DBManager.getInstance().getGmUserDAO().GetPlayerCount(SQL_LOGIN_QUERYONE);
		if(count > 0){
			return ReturnError("用户已存在");
		}
		
		GMUserData udata = new GMUserData();

		udata.setUname(uname);
		udata.setPw(pw);
		udata.setPower(ipower);
		
		DBManager.getInstance().getGmUserDAO().addLoginData(udata);
		
		GMFunctionRetData ret = new GMFunctionRetData();
		ret.code = 1;
		ret.data = "添加成功";
		return ret;
	}
	
	public int FunctionPower (){
		return 10;
	}
}
