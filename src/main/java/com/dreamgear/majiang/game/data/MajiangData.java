package com.dreamgear.majiang.game.data;

public class MajiangData {
	String id;
	int	index;//索引，用于判断顺子
	String name;//名字
	int type;//类型 1万 2条 3饼 4风 5箭
	int islink;//是否可以串
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getIslink() {
		return islink;
	}
	public void setIslink(int islink) {
		this.islink = islink;
	}
	
}
