package com.dreamgear.majiang.game.game;

import com.dreamgear.majiang.common.GameCommon.enPlayerRoomState;

public class InGamePlayer {
	
	enPlayerRoomState state = enPlayerRoomState.enWait;
	long player;
	boolean isPay = false; //是否已经付了房费
	
	public InGamePlayer(Long pm){
		player = pm;
	}
}
