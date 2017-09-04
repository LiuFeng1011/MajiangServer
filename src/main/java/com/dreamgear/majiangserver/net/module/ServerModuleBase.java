package com.dreamgear.majiangserver.net.module;

import com.dreamgear.majiangserver.net.buffer.JoyBuffer;

public class ServerModuleBase implements ServerModule {

	public int getModuleType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void serialize(JoyBuffer out) {
		// TODO Auto-generated method stub
		out.putInt(getModuleType());
	}

	public void deserialize(JoyBuffer in) {
		// TODO Auto-generated method stub
		
	}

}
