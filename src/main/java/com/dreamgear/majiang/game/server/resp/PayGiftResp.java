package com.dreamgear.majiang.game.server.resp;

import java.util.List;

import com.dreamgear.majiang.game.payGift.PayGiftData;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiangserver.net.BaseMessage;

public class PayGiftResp extends BaseMessage {
	public List<PayGiftData> data;
	public int GetProtocol() {
		// TODO Auto-generated method stub
		return GameProtocol.PAYGIFT;
	}
}
