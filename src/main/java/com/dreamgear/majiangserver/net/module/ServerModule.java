package com.dreamgear.majiangserver.net.module;

import com.dreamgear.majiangserver.net.buffer.JoyBuffer;

public interface ServerModule {
	/**
	 * 模块类型
	 * 
	 * @return
	 */
	public int getModuleType();

	/**
	 * 串行化
	 * 
	 * @param out
	 */
	public void serialize(JoyBuffer out);
	public void deserialize(JoyBuffer in);
}
