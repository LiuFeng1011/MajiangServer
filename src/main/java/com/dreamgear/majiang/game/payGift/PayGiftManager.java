package com.dreamgear.majiang.game.payGift;

import java.util.ArrayList;
import java.util.List;

import com.dreamgear.majiang.db.DBManager;

public class PayGiftManager {
	private List<PayGiftData> giftList;
	
	private static PayGiftManager instance;
	public static PayGiftManager GetInstance(){
		if(instance == null){
			instance = new PayGiftManager();
		}
		return instance;
	}
	
	private PayGiftManager(){
		giftList = DBManager.getInstance().getPayGiftDAO().getGiftDataList();
		if(giftList == null){
			giftList = new ArrayList<PayGiftData>();
		}
	}

	PayGiftData GetDataById(int pid){
		for(int i = 0 ; i < giftList.size() ; i ++){
			if(giftList.get(i).getPayid() == pid){
				return giftList.get(i);
			}
		}
		return null;
	}
	
	/**
	 * 获取对应的奖励数量
	 * @param payid
	 * @return
	 */
	public int GetGiftByID(int payid){
		for(int i = 0 ; i < giftList.size() ; i ++){
			PayGiftData data = giftList.get(i) ;
			if(data.getPayid() == payid){
				return data.getGift();
			}
		}
		return 0;
	}
	
	/**
	 * 更新数据
	 * @param pid
	 * @param count
	 */
	public void SetGiftCount(int pid,int count){

		PayGiftData data = GetDataById(pid);
		
		if(data == null){
			data = new PayGiftData();
			data.setPayid(pid);
			data.setGift(count);
			DBManager.getInstance().getPayGiftDAO().addGift(data);
			giftList.add(data);
		}else{
			data.setGift(count);
			DBManager.getInstance().getPayGiftDAO().updateGift(data);
		}
		
	}

	public List<PayGiftData> getGiftList() {
		return giftList;
	}

	public void setGiftList(List<PayGiftData> giftList) {
		this.giftList = giftList;
	}
	
	
}
