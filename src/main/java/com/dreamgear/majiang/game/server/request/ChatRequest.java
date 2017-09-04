package com.dreamgear.majiang.game.server.request;

import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class ChatRequest extends BaseMessage{
	public int type;//消息类型 0 普通文字内容 1 表情，msg为表情id 2 语音 msg为语音id
	public int sender;//发送者
	public int receiver;//接受方
	public String msg;//消息内容
	
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.CHAT;
	}
}
