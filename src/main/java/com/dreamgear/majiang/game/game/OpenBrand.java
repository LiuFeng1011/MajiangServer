package com.dreamgear.majiang.game.game;

import java.util.List;

import com.dreamgear.majiang.common.GameCommon.OpenType;


public class OpenBrand {
	public OpenType type;
	public List<MajiangUtil> list;
	public OpenBrand(OpenType type, List<MajiangUtil> list){
		this.type = type;
		this.list = list;
	}
}
