package com.dreamgear.majiang.common;

import java.nio.charset.Charset;

public class GameConst {

	public final static long TIME_MINUTE_MILLIS = 60000;//一分钟的毫秒数
	public final static long TIME_HOUR_MILLIS = 3600000;//一小时的毫秒数
	public final static long TIME_DAY_MILLIS = 86400000;//一天的毫秒数
	public final static long ROOMDATA_OUTTIME = 259200000;//三天的毫秒数
	
	public final static long PLAYER_LEAVE_TIME = 600;
	
	public static final String SERVERPACKAGE = "com.football.game.server";
	
	public static final Charset charset=Charset.forName("UTF-8");
	
	public static final String BASEFILENAME = "config/application.properties";
	
	public final static String RES_PATH = "./resources/excel";
	
	public static final String SCHEDULE_PROPERTIES = "config/schedule.properties";

	public static final byte MAX_PLAYER_COUNT = 4;//每局游戏玩家数量
	public static final byte ONE_BRAND_COUNT = 4;//每张牌的数量

	public static final byte MAX_FAN_COUNT = 5;//每张牌的数量

}
