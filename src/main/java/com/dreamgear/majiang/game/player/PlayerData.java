package com.dreamgear.majiang.game.player;

import com.dreamgear.majiang.db.DBManager;

public class PlayerData {
	
	private long id;
	private long uid;
	private String uname = "";
	private int lv = 1;
	private String head = "";
	private int gold = 0;
	private long createTime = 0;// 创建时间
	private long lastLoginTime = 0;// 角色上次登录时间
	private long lastOnlineTime = 0;//上次在线时间
	private String roomdata = "";
	private long parent = -1;
	private int playcount = 0;
	private int wincount = 0;
	
	public void SaveData(){
		DBManager.getInstance().getPlayerDAO().updatePlayer(this);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		try{
			this.uname = java.net.URLEncoder.encode(uname,"utf-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public long getLastOnlineTime() {
		return lastOnlineTime;
	}

	public void setLastOnlineTime(long lastOnlineTime) {
		this.lastOnlineTime = lastOnlineTime;
	}

	public String getRoomdata() {
		return roomdata;
	}

	public void setRoomdata(String roomdata) {
		this.roomdata = roomdata;
	}

	public long getParent() {
		return parent;
	}

	public void setParent(long parent) {
		this.parent = parent;
	}

	public int getPlaycount() {
		return playcount;
	}

	public void setPlaycount(int playcount) {
		this.playcount = playcount;
	}

	public int getWincount() {
		return wincount;
	}

	public void setWincount(int wincount) {
		this.wincount = wincount;
	}

}
