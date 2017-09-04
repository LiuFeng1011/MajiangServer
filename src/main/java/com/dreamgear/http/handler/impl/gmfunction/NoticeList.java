package com.dreamgear.http.handler.impl.gmfunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.domain.GMFunctionRetData;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.majiang.game.notice.NoticeMgr;
import com.dreamgear.majiang.game.notice.GameNotice;
public class NoticeList extends BaseFunction {

	@Override
	public GMFunctionRetData handle(HttpRequestMessage request) {
		
		//获取公告列表
		
		GMFunctionRetData ret = new GMFunctionRetData();
		ret.code = 1;
		List<GameNotice> list = new ArrayList<GameNotice>();
		Map<Long, GameNotice> map = NoticeMgr.getInstance().getNoticeList();
		
		for (Map.Entry<Long, GameNotice> entry : map.entrySet()) {
			list.add(entry.getValue());
		}
		
		ret.data = JSON.toJSONString(list);
		return ret;
	}
}
