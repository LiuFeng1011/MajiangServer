package com.dreamgear.majiang.game.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FaceDataManager {
	static final Logger logger = LoggerFactory.getLogger(FaceDataManager.class);

	private List<FaceData> dataList = new ArrayList<FaceData>();
	private Map<String,FaceData> dataMap = new HashMap<String,FaceData>();
	
	/**
	 * 载入固化数据
	 * @param path
	 * @throws Exception
	 */
	public void load(String path) throws Exception {
		
		List<Object> list = GameDataManager.loadData(path, FaceData.class,"biaoqing");
		
		for (Object obj : list) {
			FaceData data = (FaceData) obj;
			dataList.add(data);
			dataMap.put(data.getId(), data);
		}
	}

	public List<FaceData> getDataList() {
		return dataList;
	}

	public void setDataList(List<FaceData> dataList) {
		this.dataList = dataList;
	}
	
	public FaceData GetDataById(String id){
		return dataMap.get(id);
	}
	
}
