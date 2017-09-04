package com.dreamgear.majiang.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dreamgear.majiang.game.player.PlayerData;

public class Test {
	private static final Logger logger = LoggerFactory.getLogger(Test.class);
	 public static void main(String[] args) {

			//数据库
			DBManager.getInstance().init();

	    	PlayerData data = DBManager.getInstance().getPlayerDAO().getPlayerData(1);
	    	logger.info("AccountsData : " + JSON.toJSONString(data));
	 }
}
