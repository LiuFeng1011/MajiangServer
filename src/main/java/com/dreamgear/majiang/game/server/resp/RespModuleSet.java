package com.dreamgear.majiang.game.server.resp;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;
import com.dreamgear.majiangserver.net.module.ServerModule;


public class RespModuleSet  extends BaseMessage{
	List<ServerModule> moduleList = new ArrayList<ServerModule>();
	
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.RESP_MODULE;
	}
	
	public void AddModule(ServerModule sm){
		if(sm == null) return;
		moduleList.add(sm);
	}
	
}
