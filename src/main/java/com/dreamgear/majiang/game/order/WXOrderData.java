package com.dreamgear.majiang.game.order;

/**
 * 微信支付回调数据类
 * 详见https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
 * @author liufeng
 *
 */
public class WXOrderData {
	public String appid;//微信分配的公众账号ID（企业号corpid即为此appId）
	public String attach;//商家数据包，原样返回
	public String bank_type;//银行类型，采用字符串类型的银行标识，银行类型见银行列表
	public String cash_fee;//现金支付金额订单现金支付金额，详见支付金额https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2
	public String fee_type;//货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2
	public String is_subscribe;//用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
	public String mch_id;//微信支付分配的商户号
	public String nonce_str	;//随机字符串，不长于32位
	public String openid;//用户在商户appid下的唯一标识
	public String out_trade_no;//商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
	public String result_code;//SUCCESS/FAIL
	public String return_code;//SUCCESS/FAIL 此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
	public String sign;//签名，详见签名算法https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3
	public String time_end;//支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_2
	public String total_fee	;//订单总金额，单位为分
	public String trade_type;//JSAPI、NATIVE、APP
	public String transaction_id;//微信支付订单号
}
