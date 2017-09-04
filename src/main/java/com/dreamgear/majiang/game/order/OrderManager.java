package com.dreamgear.majiang.game.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderManager {
	private static final Logger logger = LoggerFactory.getLogger(OrderManager.class);

	private static OrderManager instance;
	
	public static OrderManager GetInstance(){
		if(instance == null){
			instance = new OrderManager();
		}
		return instance;
	}
	
	
}
