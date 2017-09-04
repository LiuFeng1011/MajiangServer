package com.dreamgear.majiang.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum LogType {
	
	//新加日志
	GM_OPERATION(LoggerFactory.getLogger("gmoperation"));//登陆
	
	Logger logger;

	public Logger getLogger() {
		return logger;
	}

	private LogType(Logger logger) {
		this.logger = logger;
	}

	public static LogType[] values = values();

	public static LogType[] getValues() {
		return values;
	}
}