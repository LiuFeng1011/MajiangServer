package com.dreamgear.majiang.game.game;

public class MajiangUtil {
	public String brandid;//牌id
	public int getPlayer = -1;//获得此牌的玩家
	public int eatPlayer = -1;//吃或叉此牌的玩家
	public String getBrandid() {
		return brandid;
	}
	public void setBrandid(String brandid) {
		this.brandid = brandid;
	}
	public int getGetPlayer() {
		return getPlayer;
	}
	public void setGetPlayer(int getPlayer) {
		this.getPlayer = getPlayer;
	}
	public int getEatPlayer() {
		return eatPlayer;
	}
	public void setEatPlayer(int eatPlayer) {
		this.eatPlayer = eatPlayer;
	}
	
	
}
