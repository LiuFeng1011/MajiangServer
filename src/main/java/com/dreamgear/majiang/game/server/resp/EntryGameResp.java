package com.dreamgear.majiang.game.server.resp;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class EntryGameResp extends BaseMessage{
	public String nickname = "";//	用户昵称
	public String sex = "";//	用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	public String province = "";//	用户个人资料填写的省份
	public String city = "";//	普通用户个人资料填写的城市
	public String country = "";//	国家，如中国为CN
	public String headimgurl = "";//	用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。

	public int gold;//用户金币数量
	
	//玩家登陆时返回此数据，游戏结束时不会刷新，客户端自己累加
	public int playcount;//历史局数
	public int wincount;//胜利局数  
	
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.ENTRY_GAME;
	}
	
}
