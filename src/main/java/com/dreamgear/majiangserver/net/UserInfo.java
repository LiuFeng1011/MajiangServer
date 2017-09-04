package com.dreamgear.majiangserver.net;

import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSONObject;

public class UserInfo {
	long uid;
	int channelid;
	IoSession session;
	public void serialize(JSONObject jsonObj)
    {
    }

    public void deserialize(JSONObject jsonObj)
    {
		uid = jsonObj.getLongValue("uid");
		channelid = jsonObj.getIntValue("channelid");
    }

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	} 
	
	
}
