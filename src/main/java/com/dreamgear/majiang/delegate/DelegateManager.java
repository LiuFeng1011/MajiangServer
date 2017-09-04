package com.dreamgear.majiang.delegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.domain.LoginData;
import com.dreamgear.majiang.common.GameCommon.enDelegateState;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.game.player.GamePlayerManager;
import com.dreamgear.majiang.game.player.LoginManager;
import com.dreamgear.majiang.game.player.PlayerAddByDay;
import com.dreamgear.majiang.game.player.PlayerManager;
import com.dreamgear.majiang.utils.TimeUtils;

public class DelegateManager {
	private static final Logger logger = LoggerFactory.getLogger(DelegateManager.class);
	
	private static DelegateManager instance;
	
	public static DelegateManager GetInstance(){
		if(instance == null){
			instance = new DelegateManager();
		}
		return instance;
	}
	
	/**
	 * 注册代理
	 * @param uid
	 * @param phone
	 * @return -1 申请成功  0已经是代理了 1	已经申请过了 2
	 */
	public int RegDelegate(long uid,String phone){
		
		DelegateData dbdata = DBManager.getInstance().getDelegateDAO().getDataByUID(uid);
		if(dbdata != null){
			if(dbdata.check == enDelegateState.enYes){
				return 0;
			}else{
				return 1;
			}
		}else{
			DelegateData data = new DelegateData();
			data.setUid(uid);
			data.setCheck(enDelegateState.enWait);
			data.setCheckouttime(TimeUtils.nowLong());
			data.setPhone(phone);
			
			DBManager.getInstance().getDelegateDAO().addData(data);
		}
		
		return -1;
	}
	
	/**
	 * 处理代理请求
	 * @return 0 请求不存在
	 */
	public int HandleReg(int id,int handle){
		
		DelegateData dbdata = DBManager.getInstance().getDelegateDAO().getData(id);
		
		if(dbdata == null){
			return 0;
		}
		
		dbdata.check = enDelegateState.GetState(handle);
		DBManager.getInstance().getDelegateDAO().updateData(dbdata);
		
		return -1;
	}
	
	/**
	 * 
	 * @param state  0 申请中 1 同意 2拒绝
	 * @return
	 */
	public List<DelegateData> GetListByState(int state){
		return DBManager.getInstance().getDelegateDAO().getDataListByState(state+"");
	}
	
	/**
	 * 代理状态
	 * @param uid
	 * @return -1 不是代理	0 申请中	1 同意	2 拒绝
	 */
	public enDelegateState UserDelegateState(long uid){
		DelegateData data = DBManager.getInstance().getDelegateDAO().getDataByUID(uid);
		if(data == null){
			return enDelegateState.enNull;
		}
		return data.check;
	}
	
	//获取代理用户信息
	public DelegatePlayerInfo GetDelegatePlayerInfo(DelegateData data,long uid){
		DelegatePlayerInfo info = new DelegatePlayerInfo();
		info.uid = data.getUid();
		info.check = data.getCheck();
		info.checkouttime = data.getCheckouttime();
		info.phone = data.getPhone();
		
		LoginData ld = LoginManager.GetInstance().GetLoginData(info.uid);
		if(ld != null){
			info.nickname = ld.nickname;
			info.sex = ld.sex;
		}
		PlayerManager pm = GamePlayerManager.GetInstance().GetPlayer(info.uid);
		info.goldcount = pm.getPd().getGold();
		
		return info;
	}
	
	
	/**
	 * 查看代理用户数据
	 * @param type 0 今日 1昨日 2 本月 3 上月 4 今年 5 去年
	 * @return
	 */
	public  Map<String,Object> GetDelegateIncomeData(long uid,int type,long checkouttime){
		logger.info("uid : "+  uid);
		logger.info("type : "+  type);
		DateTime dt = TimeUtils.now();
		long startTime = 0;
		long endTime = 0;
		int datetype = 0;//0日 1年
		switch(type){
		case 0://今日
			DateTime daydt = new DateTime(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth(), 0, 0, 0, 0);
			startTime = daydt.getMillis();
			endTime = TimeUtils.nowLong();
			
			break;
		case 1://昨日
			DateTime lastdaydt = new DateTime(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth(), 0, 0, 0, 0);
			startTime = lastdaydt.plusDays(-1).getMillis();
			endTime = lastdaydt.getMillis();
			
			break;
		case 2://本月
			DateTime monthdt = new DateTime(dt.getYear(), dt.getMonthOfYear(), 1, 0, 0, 0, 0);
			startTime = monthdt.getMillis();
			endTime = TimeUtils.nowLong();
			break;
		case 3://上月
			DateTime lastmonthdt = new DateTime(dt.getYear(), dt.getMonthOfYear(), 1, 0, 0, 0, 0);
			startTime = lastmonthdt.plusMonths(-1).getMillis() ;
			endTime = lastmonthdt.getMillis();
			break;
		case 4://今年
			DateTime yeardt = new DateTime(dt.getYear(), 1, 1, 0, 0, 0, 0);
			startTime = yeardt.getMillis();
			endTime = TimeUtils.nowLong();
			datetype = 1;
			break;
		case 5://去年
			DateTime lastyeardt = new DateTime(dt.getYear(), 1, 1, 0, 0, 0, 0);
			startTime = lastyeardt.plusYears(-1).getMillis();
			endTime = lastyeardt.getMillis();
			datetype = 1;
			break;
			default:
				return null;
		}
		List<PlayerAddByDay> datalist;
		List<DelegateIncomeSQLData> delegateIncomeSQLData;
		logger.info("checkouttime : " + checkouttime);
		logger.info("startTime : " + startTime);
		if(startTime < checkouttime){
			startTime = checkouttime;
		}
		if(startTime > endTime){
			Map<String,Object> ret = new HashMap<String,Object>();
			ret.put("uilist", new ArrayList<Object>());
			ret.put("orderlist",  new ArrayList<Object>());
			ret.put("roomcount", 0);
			return ret;
		}
		
		if(datetype == 0){
			//获取新增用户数
			datalist = DBManager.getInstance().getPlayerDAO().GetPlayerAddByDay(uid,startTime,endTime);
			//获取订单数量 充值数
			delegateIncomeSQLData = DBManager.getInstance().getOrderDAO().GetDelegateOrderData(uid, startTime, endTime);
		}else {
			//获取新增用户数
			datalist = DBManager.getInstance().getPlayerDAO().GetPlayerAddByYear(uid,startTime,endTime);
			//获取订单数量 充值数
			delegateIncomeSQLData = DBManager.getInstance().getOrderDAO().GetDelegateOrderDataByYear(uid, startTime, endTime);
		}
		logger.info("delegateIncomeSQLData : " + JSON.toJSONString(delegateIncomeSQLData));

		int roomcount = DBManager.getInstance().getRoomDAO().GetDelegateRoomCount(uid,startTime,endTime);
		
		Map<String,Object> ret = new HashMap<String,Object>();
		ret.put("uilist", datalist);
		ret.put("orderlist", delegateIncomeSQLData);
		ret.put("roomcount", roomcount);
		return ret;
	}
	
	//test
	public static void main( String[] args ) throws Exception
    {
		DateTime dt = TimeUtils.now();
		DateTime lastdaydt = new DateTime(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth(), 0, 0, 0, 0);
		long startTime = lastdaydt.plusDays(-1).getMillis();
		long endTime = lastdaydt.getMillis();
		logger.info("startTime : " + startTime);
		logger.info("endTime : " + endTime);
    }
}
