package com.dreamgear.majiang.game.player;

/**
 * 游戏内下发给客户端的玩家数据
 * @author liufeng
 *
 */
public class PlayerSendData {
	public long uid = -1;
	public int channelid = -1;
	public String nickname = "";
	public String headurl = "";
	public int state = 0; //0 等待 //1 坐下 //2 离开
}
