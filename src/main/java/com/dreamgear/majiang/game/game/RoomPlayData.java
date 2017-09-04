package com.dreamgear.majiang.game.game;

import java.util.ArrayList;
import java.util.List;

import com.dreamgear.majiang.game.player.PlayerSendData;

public class RoomPlayData {
	public long roomid;
	
	public long createTime;//创建时间
	public int roomtype;//房间类型
	public long master = -1;//房主
	public int playcount;
	
	public String data = "";
	public long delegate ;
	
	public List<PlayerSendData> plist = new ArrayList<PlayerSendData>();
	
}
