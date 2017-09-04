package com.dreamgear.http.handler.impl.gmfunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.domain.GMFunctionRetData;
import com.dreamgear.http.handler.domain.LoginData;
import com.dreamgear.http.request.HttpRequestMessage;
import com.dreamgear.majiang.common.GameCommon.enDelegateState;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.delegate.DelegateData;
import com.dreamgear.majiang.delegate.DelegateIncomeData;
import com.dreamgear.majiang.delegate.DelegateIncomeSQLData;
import com.dreamgear.majiang.delegate.DelegateManager;
import com.dreamgear.majiang.delegate.DelegatePlayerInfo;
import com.dreamgear.majiang.game.GameWord;
import com.dreamgear.majiang.game.player.GamePlayerManager;
import com.dreamgear.majiang.game.player.LoginManager;
import com.dreamgear.majiang.game.player.PlayerManager;
import com.dreamgear.majiang.utils.TimeUtils;

/**
 * 代理审核
 * @author liufeng
 *
 */
public class DelegateFunction  extends BaseFunction{
	private static final Logger logger = LoggerFactory.getLogger(DelegateFunction.class);

	@Override
	public GMFunctionRetData handle(HttpRequestMessage request) {
		String function = request.getParameter("dfunction");

		GMFunctionRetData ret = new GMFunctionRetData();
		if(function == null || function.equals("")){
			ret.code = 0;
			ret.data = "功能参数错误";
			return ret;
		}

		if(function.equals("CheckDelegate")){
			//代理操作
			HandleDelegate(request,ret);
		}else if(function.equals("CancelDelegate")){
			//代理解除
			HandleDelegate(request,ret);
		}else if(function.equals("GetDelegateInfoByState")){
			//查看所有代理
			GetDelegateInfoByState(request,ret);
		}else if(function.equals("GetDelegateData")){
			//查看代理数据
			GetDelegateData(request,ret);
		}else if(function.equals("AddDelegate")){
			//添加代理
			AddDelegate(request,ret);
		}else if(function.equals("ChangeDelegateRate")){
			//修改显示比例
			ChangeDelegateRate(request,ret);
		}else if(function.equals("SetDelegateMark")){
			//修改显示比例
			SetDelegateMark(request,ret);
		}else if(function.equals("Checkout")){
			//结算
			Checkout(request,ret);
		}else{
			ret.code = 0;
			ret.data = "无此功能:"+function;
			return ret;
		}
		logger.info("ret : " + JSON.toJSONString(ret));
		return ret;
	}

	//添加备注
	public void Checkout(HttpRequestMessage request,GMFunctionRetData ret){
		String suid = request.getParameter("uid");
		long uid = -1;
		try{
			uid = Integer.parseInt(suid);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		if(uid == -1){
			ret.code = 0;
			ret.errmsg = "用户id错误";
			return;
		}
		DelegateData dbdata = DBManager.getInstance().getDelegateDAO().getDataByUID(uid);
		
		if(dbdata == null){
			ret.code = 0;
			ret.errmsg = "用户不存在";
			return;
		}else{
			dbdata.setCheckouttime(TimeUtils.nowLong());
			DBManager.getInstance().getDelegateDAO().updateData(dbdata);
		}
		ret.code = 1;
		ret.data = "修改成功";
	}
	//添加备注
	public void SetDelegateMark(HttpRequestMessage request,GMFunctionRetData ret){
		String suid = request.getParameter("uid");
		String smark = request.getParameter("mark");
		long uid = -1;
		try{
			uid = Integer.parseInt(suid);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		if(uid == -1){
			ret.code = 0;
			ret.errmsg = "用户id错误";
			return;
		}
		long mark = -1;
		try{
			mark = Integer.parseInt(smark);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		if(mark == -1){
			ret.code = 0;
			ret.errmsg = "备注只能填写用户id";
			return;
		}
		DelegateData dbdata = DBManager.getInstance().getDelegateDAO().getDataByUID(uid);
		if(dbdata == null){
			ret.code = 0;
			ret.errmsg = "用户不存在";
			return;
		}else{
			dbdata.setMark(mark);
			DBManager.getInstance().getDelegateDAO().updateData(dbdata);
		}

		ret.code = 1;
		ret.data = "修改成功";
		
	}
	//修改显示比例
	public void ChangeDelegateRate(HttpRequestMessage request,GMFunctionRetData ret){
		String suid = request.getParameter("uid");
		String srate = request.getParameter("rate");

		long uid = -1;
		try{
			uid = Integer.parseInt(suid);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		if(uid == -1){
			ret.code = 0;
			ret.errmsg = "用户id错误";
			return;
		}
		
		int rate = -1;
		try{
			rate = Integer.parseInt(srate);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		if(rate < 0 || rate > 100){
			ret.code = 0;
			ret.errmsg = "请输入0-100之间的整数";
			return;
		}
		
		DelegateData dbdata = DBManager.getInstance().getDelegateDAO().getDataByUID(uid);
		if(dbdata == null){
			ret.code = 0;
			ret.errmsg = "用户不存在";
			return;
		}else{
			dbdata.setRate(rate);
			DBManager.getInstance().getDelegateDAO().updateData(dbdata);
		}

		ret.code = 1;
		ret.data = "修改成功";
	}
		
	//添加代理
	public void AddDelegate(HttpRequestMessage request,GMFunctionRetData ret){
		String suid = request.getParameter("uid");
		String phone = request.getParameter("phone");

		long uid = -1;
		try{
			uid = Integer.parseInt(suid);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		if(uid == -1){
			ret.code = 0;
			ret.errmsg = "用户id错误";
			return;
		}
		
		DelegateData dbdata = DBManager.getInstance().getDelegateDAO().getDataByUID(uid);
		if(dbdata != null){
			ret.code = 0;
			ret.errmsg = "用户已存在";
			return;
		}else{
			DelegateData data = new DelegateData();
			data.setUid(uid);
			data.setCheck(enDelegateState.enYes);
			data.setCheckouttime(TimeUtils.nowLong());
			data.setPhone(phone);
			
			DBManager.getInstance().getDelegateDAO().addData(data);
			PlayerManager pm = GamePlayerManager.GetInstance().GetPlayer(uid);
			if(pm != null){
				pm.getPd().setParent(uid);
			}
		}
		
		ret.code = 1;
		ret.data = "添加成功";
	}
	
	//代理审核
	public void HandleDelegate(HttpRequestMessage request,GMFunctionRetData ret){
		String type = request.getParameter("type");
		String suid = request.getParameter("uid");
		
		long uid = -1;
		try{
			uid = Integer.parseInt(suid);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		if(uid == -1){
			ret.code = 0;
			ret.errmsg = "用户id错误";
			return;
		}
		
		DelegateData dbdata = DBManager.getInstance().getDelegateDAO().getDataByUID(uid);
		
		if(dbdata == null){
			ret.code = 0;
			ret.errmsg = "用户没有申请代理";
			return;
		}
		
		if(type.equals("0")){//解除
			if(dbdata.getCheck() != enDelegateState.enYes){
				ret.code = 0;
				ret.errmsg = "该用户还不是代理，无法解除";
				return;
			}
			dbdata.setCheck(enDelegateState.enCancel);
			DBManager.getInstance().getDelegateDAO().updateData(dbdata);
		}else if(type.equals("1")){//通过
			if(dbdata.getCheck() == enDelegateState.enYes){
				ret.code = 0;
				ret.errmsg = "该用户已经是代理了";
				return;
			}
			dbdata.setCheck(enDelegateState.enYes);
			DBManager.getInstance().getDelegateDAO().updateData(dbdata);
			
			PlayerManager pm = GamePlayerManager.GetInstance().GetPlayer(uid);
			if(pm != null){
				pm.getPd().setParent(uid);
			}
		}else if(type.equals("2")){//拒绝
			if(dbdata.getCheck() != enDelegateState.enWait){
				ret.code = 0;
				ret.errmsg = "该用户没有申请代理或请求已审核";
				return;
			}
			dbdata.setCheck(enDelegateState.enNo);
			DBManager.getInstance().getDelegateDAO().updateData(dbdata);
		}
		
		
		ret.code = 1;
		ret.data = "处理成功";
	}
	//查看所有非代理信息
//	public void GetAllNODelegate(HttpRequestMessage request,GMFunctionRetData ret){
//		
//		List<DelegateData> list = DBManager.getInstance().getDelegateDAO().getDataListByNO();
//		List<DelegatePlayerInfo> retlist = new ArrayList<DelegatePlayerInfo>();
//		for(int i = 0 ; i < list.size() ; i ++){
//			DelegateData data = list.get(i);
//			DelegatePlayerInfo info = this.GetDelegatePlayerInfo(data);
//			retlist.add(info);
//		}
//		ret.code = 1;
//		ret.data = JSON.toJSONString(retlist);
//		logger.info("ret.data : " + ret.data);
//		return;
//	}
	//查看所有代理
	public void GetDelegateInfoByState(HttpRequestMessage request,GMFunctionRetData ret){
		String state = request.getParameter("type");
//		if(stype == null || stype.equals("")){
//			ret.code = 0;
//			ret.errmsg = "操作不能为空";
//			return;
//		}
		List<DelegateData> list = DBManager.getInstance().getDelegateDAO().getDataListByState(state+"");
//		List<DelegateData> list = DBManager.getInstance().getDelegateDAO().getDataList();
		List<DelegatePlayerInfo> retlist = new ArrayList<DelegatePlayerInfo>();
		for(int i = 0 ; i < list.size() ; i ++){
			DelegateData data = list.get(i);
			DelegatePlayerInfo info = this.GetDelegatePlayerInfo(data);
			retlist.add(info);
		}
		
		ret.code = 1;
		ret.data = JSON.toJSONString(retlist);
		logger.info("data:"+ret.data);
		return;
	}
	
	DelegatePlayerInfo GetDelegatePlayerInfo(DelegateData data){
		DelegatePlayerInfo info = new DelegatePlayerInfo();
		info.uid = data.getUid();
		logger.info("info.uid : " + info.uid);
		info.check = data.getCheck();
		info.checkouttime = data.getCheckouttime();
		info.phone = data.getPhone();
		info.rate = data.getRate();
		info.mark = data.getMark();
		LoginData ld = LoginManager.GetInstance().GetLoginData(info.uid);
		if(ld != null){
			info.nickname = ld.nickname;
			info.sex = ld.sex;
		}
		PlayerManager pm = GamePlayerManager.GetInstance().GetPlayer(info.uid);
		if(pm != null){
			info.goldcount = pm.getPd().getGold();
			info.createTime = pm.getPd().getCreateTime();
		}
		
		if(info.check == enDelegateState.enYes){
			DelegateIncomeSQLData  orderdata = DBManager.getInstance().getOrderDAO().GetDelegateOrderTotalDate(info.uid, data.checkouttime, TimeUtils.nowLong());
			info.count = orderdata.getCount();
			info.value = orderdata.getValue();
			
			info.usercount = DBManager.getInstance().getPlayerDAO().getDelegateUserCount(info.uid);
			
			info.roomcount = DBManager.getInstance().getRoomDAO().GetDelegateRoomCount(info.uid,0,TimeUtils.nowLong());
		}
		return info;
	}
	//查看代理数据
	public void GetDelegateData(HttpRequestMessage request,GMFunctionRetData ret){
		String suid = request.getParameter("uid");
		String stype = request.getParameter("type");
		
		long uid = -1;
		try{
			uid = Integer.parseInt(suid);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		if(uid == -1){
			ret.code = 0;
			ret.errmsg = "用户id错误";
			return;
		}
		
		int type = -1;
		try{
			type = Integer.parseInt(stype);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		if(type == -1){
			ret.code = 0;
			ret.errmsg = "数据类型错误";
			return;
		}
		DelegateData delegateData = DBManager.getInstance().getDelegateDAO().getDataByUID(uid);
		if(delegateData == null){
			ret.code = 0;
			ret.errmsg = "无法获取代理数据";
			return;
		}
		Map<String,Object> list = DelegateManager.GetInstance().GetDelegateIncomeData(uid, type,delegateData.checkouttime);
		
		ret.code = 1;
		ret.data = JSON.toJSONString(list);
	}
}
