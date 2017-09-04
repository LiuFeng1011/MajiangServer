package com.dreamgear.majiang.game.game;

import java.util.ArrayList;
import java.util.List;

import com.dreamgear.majiang.common.GameCommon.enCtrlType;

public class GamePlayerControl {
	public int playerIndex;
	public List<Integer> controlType = new ArrayList<Integer>();
	public String chi = "";//吃牌
	public String gang = "";//杠牌
	public String cha = "";//叉牌
	public String hu = "";//胡牌
	public List<List<String>> eatList;
	
	public void AddType(enCtrlType controlType){
		this.controlType.add(controlType.ordinal());
	}
}
