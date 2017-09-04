package com.dreamgear.majiang.delegate;

import com.dreamgear.majiang.common.GameCommon.enDelegateState;

public class DelegateData {
	public long id;
	public long uid;//代理玩家id
	public enDelegateState check;//
	public long checkouttime;//上一次结算的时间
	public String phone;//电话
	public int rate;//收入显示给代理的百分比
	public long mark;//备注
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
	public enDelegateState getCheck() {
		return check;
	}
	public void setCheck(enDelegateState check) {
		this.check = check;
	}
	public long getCheckouttime() {
		return checkouttime;
	}
	public void setCheckouttime(long checkouttime) {
		this.checkouttime = checkouttime;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public long getMark() {
		return mark;
	}
	public void setMark(long mark) {
		this.mark = mark;
	}
	
}
