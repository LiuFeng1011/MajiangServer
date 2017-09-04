package com.dreamgear.majiang.game.server.request;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class UserDataRequest extends BaseMessage{
	private byte type = 0;//0 上传 1下载

	private String uname;
	private int lv = 1;
	private String head = "";
	private String country = "";
	private String lvstar = "";
	private String item_list = "";
	private int allStarCount = 0;
	
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.USER_DATA_UPDATE;
	}
	
	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLvstar() {
		return lvstar;
	}

	public void setLvstar(String lvstar) {
		this.lvstar = lvstar;
	}

	public String getItem_list() {
		return item_list;
	}

	public void setItem_list(String item_list) {
		this.item_list = item_list;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getAllStarCount() {
		return allStarCount;
	}

	public void setAllStarCount(int allStarCount) {
		this.allStarCount = allStarCount;
	}
	
	
}
