package com.dreamgear.majiang.db.row;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dreamgear.http.handler.domain.LoginData;
import com.dreamgear.majiang.game.game.RoomPlayData;
import com.dreamgear.majiang.game.player.LoginManager;
import com.dreamgear.majiang.game.player.PlayerSendData;
import com.dreamgear.majiang.utils.JsonUtil;

public class RoomDataMapper implements RowMapper<RoomPlayData> {
	public RoomPlayData mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub

		RoomPlayData pd = new RoomPlayData();
		
		pd.roomid = rs.getLong("roomid");
		pd.createTime = rs.getLong("createTime");
		pd.roomtype = rs.getInt("roomtype");
		pd.master = rs.getLong("master");
		pd.playcount = rs.getInt("playcount");
		
		pd.data = "";
		try{
			pd.data = java.net.URLDecoder.decode(rs.getString("data"),"utf-8") ;
		}catch(Exception e){
			e.printStackTrace();
		}
		pd.delegate = rs.getLong("delegate");
//		long uid = pd.master;
//		LoginData ld = LoginManager.GetInstance().GetLoginData(uid);
//		PlayerSendData data = new PlayerSendData();
//		
//		if(ld == null){
//			data.uid = uid;
//			data.nickname = uid+"";
//			data.headurl = "";
//		}else{
//			data.uid = ld.getId();
//			data.nickname = ld.nickname;
//			data.headurl = ld.head;
//		}
//		
//		pd.plist.add(data);
		
		if(pd.data == null || pd.data.equals("")){
			return pd;
		}
		System.out.println("pd.data : " + pd.data);
		Map<Long , LoginData> ldmap = new HashMap<Long , LoginData>();
		try{
			List<Map> list =JSON.parseArray(pd.data, Map.class);  
	        for(int i=0;i<list.size();i++){  
	        	Map m = list.get(i);

	    		System.out.println("m : " + JSON.toJSONString(m));
	        	long uid = Long.parseLong(m.get("uid")+"");
				LoginData ld ;
				if(ldmap.containsKey(uid)){
					ld = ldmap.get(uid);
				}else{
					ld = LoginManager.GetInstance().GetLoginData(uid);
				}
				ldmap.put(uid, ld);
				PlayerSendData data = new PlayerSendData();
				if(ld == null){
					data.uid = uid;
					data.nickname = uid+"";
					data.headurl = "";
				}else{
					data.uid = ld.getId();
					data.nickname = ld.nickname;
					data.headurl = ld.head;
				}
				pd.plist.add(data);
	        }  
		}catch(Exception e){
			Map<String, String> playerScores = JsonUtil.JsonToObjectMap(pd.data);
			
			for (Map.Entry<String, String> entry : playerScores.entrySet()) {
				long uid = Long.parseLong(entry.getKey());
				LoginData ld ;
				if(ldmap.containsKey(uid)){
					ld = ldmap.get(uid);
				}else{
					ld = LoginManager.GetInstance().GetLoginData(uid);
				}
				ldmap.put(uid, ld);
				PlayerSendData data = new PlayerSendData();
				if(ld == null){
					data.uid = uid;
					data.nickname = uid+"";
					data.headurl = "";
				}else{
					data.uid = ld.getId();
					data.nickname = ld.nickname;
					data.headurl = ld.head;
				}
				pd.plist.add(data);
			}  
		}
		
		return pd;
	}
}
