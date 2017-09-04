package com.dreamgear.majiang.delegate;

import com.dreamgear.majiang.common.GameCommon.enDelegateState;

public class DelegatePlayerInfo {
	public long uid;//代理玩家id
	public enDelegateState check;//状态 -1不是代理 0申请中 1 同意 2 拒绝 3 解除
	public long checkouttime;//上一次结算的时间
	public String phone;//电话
	public int rate;
	public long mark;
	
	public String nickname;
	public String sex;//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	public long createTime;
	public int goldcount;
	
	public int count;//订单数
	public int value;//充值数
	
	public int usercount;
	public int roomcount;
}
