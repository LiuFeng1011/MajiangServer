package com.dreamgear.majiang.game.order;

public class OtherOrderData {

	public int amount;	//订单总金额	 number	单位为分
	
	public String appid	; //交易发起所属 app	 string	 此 appid 必须存在于掌灵科技
	public String body	; //商品  描述	string	商品  描述
	public int fee	; //费率	number	费率
	public String mchntId	; //商户编号	string	此 machntId 必须存在于 sdk 平台
	public String orderNo	; //  掌灵科技订单号	string	平台生成的订单号
	public String description; //	订单附加描述	string	
	public String paidTime; //	订单支付完成时间	string	
	public String orderDt; //	下单日期	string	
	
	public String extra; //	附加数据	 string	
	public String mchntOrderNo; //	商户订单号	string	商户端必须唯一
	
	public int paySt; //	订单支付结果  number; //	0:待支付,1:支付中,2:支付成功,3:支付失败，4：已关闭
	public String openId; //	支付通道付款人id址	string	
	public String signature; //	签名	string	见签名算法
	public String subject; //	商品名称	string	
	public String outTransactionId; //	第三方订单号	string	第三方官方订单号

}
