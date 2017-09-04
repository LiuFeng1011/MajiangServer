package com.dreamgear.majiang.game.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IapDataManager {
	static final Logger logger = LoggerFactory.getLogger(IapDataManager.class);

	private List<IapData> dataList = new ArrayList<IapData>();
	private Map<Integer,IapData> dataMap = new HashMap<Integer,IapData>();
	
	/**
	 * 载入固化数据
	 * @param path
	 * @throws Exception
	 */
	public void load(String path) throws Exception {
		
		List<Object> list = GameDataManager.loadData(path, IapData.class,"iap");
		
		for (Object obj : list) {
			IapData data = (IapData) obj;
			dataList.add(data);
			dataMap.put(data.getId(), data);
		}
	}

	public List<IapData> getDataList() {
		return dataList;
	}

	public void setDataList(List<IapData> dataList) {
		this.dataList = dataList;
	}
	
	public IapData GetDataById(int id){
		return dataMap.get(id);
	}
	
	public IapData GetDataByPrice(int price){
		for(int i = 0 ; i < dataList.size() ; i ++){
			if(dataList.get(i).getPrice() == price){
				return dataList.get(i);
			}
		}
		return null;
	}
	
}
