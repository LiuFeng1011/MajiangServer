package com.dreamgear.majiang.game.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoomManager {
	static final Logger logger = LoggerFactory.getLogger(RoomManager.class);

	private List<RoomData> dataList = new ArrayList<RoomData>();
	private Map<Integer,RoomData> dataMap = new HashMap<Integer,RoomData>();
	
	/**
	 * 载入固化数据
	 * @param path
	 * @throws Exception
	 */
	public void load(String path) throws Exception {
		
		List<Object> list = GameDataManager.loadData(path, RoomData.class,"room");
		
		for (Object obj : list) {
			RoomData data = (RoomData) obj;
			dataList.add(data);
			dataMap.put(data.getId(), data);
		}
	}

	public List<RoomData> getDataList() {
		return dataList;
	}

	public void setDataList(List<RoomData> dataList) {
		this.dataList = dataList;
	}
	public RoomData GetRoomData(int type){
		return dataMap.get(type);
	}
	
}
