package com.dreamgear.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dreamgear.majiang.common.GameConst;
import com.dreamgear.MajiangApp;
import com.dreamgear.http.handler.impl.WXNotify;
import com.dreamgear.majiang.common.GameCommon;
import com.dreamgear.majiang.common.GameConfig;
import com.dreamgear.majiang.common.GameCommon.enDelegateState;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.delegate.DelegateData;
import com.dreamgear.majiang.delegate.DelegateIncomeData;
import com.dreamgear.majiang.delegate.DelegateManager;
import com.dreamgear.majiang.game.data.GameDataManager;
import com.dreamgear.majiang.game.order.OrderData;
import com.dreamgear.majiang.utils.HttpUtils;
import com.dreamgear.majiang.utils.JsonUtil;
import com.dreamgear.majiang.utils.TimeUtils;

public class test {
private static final Logger logger = LoggerFactory.getLogger(MajiangApp.class);
	
    public static void main( String[] args ) throws Exception
    {
		//数据库
//		DBManager.getInstance().init();

		//游戏配置
//		GameConfig.load();
		
//    	testClass t = new testClass();
//    	logger.info("t : " + JSON.toJSONString(t));
//    	
//    	String ts = "{\"a\":5}";
//    	testClass t1 = JsonUtil.JsonToObject(ts, testClass.class);
//    	logger.info("t1 : " + JSON.toJSONString(t1));
    	
    	
    	logger.info(" week start time : " + TimeUtils.getTimeByDate(TimeUtils.getBeginDayOfWeek()));
    	
    }
}
