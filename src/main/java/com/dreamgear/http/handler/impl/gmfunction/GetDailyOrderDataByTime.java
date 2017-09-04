package com.dreamgear.http.handler.impl.gmfunction;

import java.sql.Timestamp;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.domain.GMFunctionRetData;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.game.order.DailyOrderData;

public class GetDailyOrderDataByTime extends BaseFunction {
	@Override
	public GMFunctionRetData handle(HttpRequestMessage request) {
		String startTime = request.getParameter("startTime");	/*Y	开始时间*/
		String endTime = request.getParameter("endTime");	/*Y	结束时间*/

		long lStartTime;
		if("".equals(startTime )){
			lStartTime = -1;
		}else{
			lStartTime = Timestamp.valueOf(startTime).getTime();
		}
		
		long lEndTime ;
		if("".equals(endTime)){
			lEndTime = -1;
		}else{
			lEndTime = Timestamp.valueOf(endTime).getTime();
		}
		
		if(lStartTime > lEndTime){
			return ReturnError("开始时间小于结束时间");
		}

		List<DailyOrderData> list = DBManager.getInstance().getOrderDAO().GetDailyOrderDataByTime(lStartTime, lEndTime);
		GMFunctionRetData ret = new GMFunctionRetData();

		ret.code = 1;
		ret.data = JSON.toJSONString(list);
		
		return ret;
	}
}
