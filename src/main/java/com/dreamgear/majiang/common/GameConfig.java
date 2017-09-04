package com.dreamgear.majiang.common;


import java.io.FileNotFoundException;
import java.io.IOException;

import com.dreamgear.majiang.utils.PropertyManager;


/**
 * 游戏配置信息
 * 
 * @author admin
 * @date 2012-4-23 TODO
 */
public class GameConfig {
	static PropertyManager pm = null;

	//游戏服务器端口号
	public static int serverPort = 1111;//
	
	//http端口号
	public static int http_port  = 1112;

	public static String FILENAME = "config/game.properties";

	public static String wx_appid = "";
	public static String wx_scope = "";
	public static String wx_secret = "";
	public static String game_address = "";

	public static String wx_pay_appid;
	public static String wx_pay_mchid;
	public static String wx_pay_key;
	public static int test_server;
	public static int rank_max_count;
	public static void load() throws FileNotFoundException, IOException {
		
		pm = new PropertyManager(FILENAME);
		serverPort = Integer.parseInt(pm.getString("serverPort"));
		
		http_port = Integer.parseInt(pm.getString("http_port"));
		
		wx_appid = pm.getString("wx_appid");
		wx_scope = pm.getString("wx_scope");
		wx_secret = pm.getString("wx_secret");
		game_address = pm.getString("game_address");
		
		wx_pay_appid = pm.getString("wx_pay_appid");
		wx_pay_mchid = pm.getString("wx_pay_mchid");
		wx_pay_key = pm.getString("wx_pay_key");
		test_server = Integer.parseInt(pm.getString("test_server"));
		rank_max_count = Integer.parseInt(pm.getString("rank_max_count"));
	}

	public static PropertyManager getPm() {
		return pm;
	}

	public static void setPm(PropertyManager pm) {
		GameConfig.pm = pm;
	}

}
