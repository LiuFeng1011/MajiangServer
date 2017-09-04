package com.dreamgear.majiang.game.server.protocol;

public class GameProtocol {
	//默认
	public static final int DEFAULT = -1;
	//心跳
	public static final int HEART = 0;

	//[0001 - 000F]系统协议
	public static final int RESP_MODULE = 1;//模块消息

	//[0100 - 02FF]玩家操作
	public static final int ENTRY_GAME = 100;//登录游戏
	public static final int USER_DATA_UPDATE = 101;//更新用户数据
	public static final int ENTRY_GAME_FAILED = 102;//登录游戏失败
	
	//[200-1000]游戏内消息
	public static final int GAME_HU = 200;//胡牌 req
	public static final int GAME_CAN_HU = 201;//可以胡牌 resp 默认

	public static final int GAME_CHA = 211;//叉牌 req resp
	public static final int GAME_CAN_CHA = 213;//可以叉牌resp 默认

	public static final int GAME_EAT = 221;//顺牌req resp
	public static final int GAME_CAN_EAT = 222;//可以顺牌 resp 默认

	public static final int GAME_GANG = 225;//杠牌req resp
	public static final int GAME_CAN_GANG = 226;//可以杠牌 resp 默认
	public static final int GAME_CAN_BACKGANG = 227;//可以杠牌 resp 默认
	
	public static final int GAME_PLAYERCTRL = 228;//等待玩家操作 resp 
	
	public static final int GAME_START_BRAND_LIST = 231;//游戏开局信息resp

	public static final int GAME_OUT_BRAND = 241;//出牌req resp
	public static final int GAME_CAN_OUT_BRAND = 242;//可以出牌resp

	public static final int GAME_GET_BRAND = 251;//抓到的牌resp 

	public static final int GAME_PASS = 261;//过req
	
	public static final int GMAE_PLAYER_BRAND_LIST = 271;//玩家手牌列表 resp

	public static final int GAME_TING = 281;//听牌 req resp
	
	public static final int GMAE_REINROOM = 298;//玩家重新进入游戏 下发游戏信息
	public static final int GMAE_FLUSH_INFO = 299;//下发游戏信息

	public static final int GAME_OVER = 300;//游戏结束resp
	
	public static final int ROOM = 400;//房间
	public static final int ROOM_CREATE = 1;//创建房间
	public static final int ROOM_IN = 2;//进入房间
	public static final int ROOM_LEAVE = 3;//离开房间
	public static final int ROOM_DOWN = 4;//坐下
	public static final int ROOM_STARTGAME = 5;//开始游戏
	public static final int ROOM_UP = 6;//站起围观
	public static final int ROOM_READY = 7;//坐下
	
	public static final int ROOM_GET_PLAYER_INFO = 401;//获取房间玩家信息  resp
	
	public static final int CHAT = 500;//聊天 req resp 
	
	//[1000-1100]游戏内消息
	public static final int WX_SIGN = 1001;//获取微信签名
	
	public static final int SYSTEM = 1000;//游戏消息
	public static final int SYSTEM_PAY = 1;//支付
	public static final int SYSTEM_PLAYERDATA = 2;//刷新玩家信息
	
	public static final int PLAYER_ROOM_LIST = 1010;//获取战绩列表
	
	
	
	//下发消息
	public static final int MESSAGE = 10000;//消息
	public static final int NOTICE = 10001;//公告
	public static final int PAYGIFT = 10002;//获取折扣列表
	
	//test
	public static final int TEST = 0;
}
