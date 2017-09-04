package com.dreamgear.majiang.game.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class MajiangDataManager {
	static final Logger logger = LoggerFactory.getLogger(MajiangDataManager.class);
	
	private List<MajiangData> dataList = new ArrayList<MajiangData>();
	private Map<String,MajiangData> dataMap = new HashMap<String,MajiangData>();

	/**
	 * 载入固化数据
	 * @param path
	 * @throws Exception
	 */
	public void load(String path) throws Exception {
		
		List<Object> list = GameDataManager.loadData(path, MajiangData.class,"majiang");
		
		for (Object obj : list) {
			MajiangData data = (MajiangData) obj;
			dataList.add(data);
			dataMap.put(data.getId(), data);
			
			
			
		}
	}
	
	public List<MajiangData> getDataList() {
		return dataList;
	}
	public void setDataList(List<MajiangData> dataList) {
		this.dataList = dataList;
	}
	
	public MajiangData GetData(String id){
		if(!dataMap.containsKey(id)){
			return null;
		}
		return dataMap.get(id);
	}
	
	public int GetIdByIndex(int index){
		for(int i = 0 ; i < dataList.size() ; i ++){
			if(dataList.get(i).getIndex() == index){
				return Integer.parseInt(dataList.get(i).getId());
			}
		}
		return -1;
	}
}
