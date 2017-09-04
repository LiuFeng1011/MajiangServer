package com.dreamgear.majiang.game.order;

public class DailyOrderData {
	long time;
	int ucount;//充值用户数
	int ordercount;//订单数量
	int value;//充值总额
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getUcount() {
		return ucount;
	}
	public void setUcount(int ucount) {
		this.ucount = ucount;
	}
	
	public int getOrdercount() {
		return ordercount;
	}
	public void setOrdercount(int ordercount) {
		this.ordercount = ordercount;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
}
