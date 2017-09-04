package com.dreamgear.majiang.game.server.resp;

import java.util.ArrayList;
import java.util.List;

import com.dreamgear.majiang.game.notice.GameNotice;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class NoticeResp extends BaseMessage{
	public List<GameNotice> noticeList = new ArrayList<GameNotice>();
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.NOTICE;
	}
	
}
