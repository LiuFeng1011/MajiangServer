package com.dreamgear.majiang.db.row;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dreamgear.majiang.game.player.PlayerData;


public class PlayerDataMapper  implements RowMapper<PlayerData>{

	public PlayerData mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub

		PlayerData pd = new PlayerData();
		pd.setId(rs.getLong("id"));
		pd.setUid(rs.getLong("uid"));
		try{
			pd.setUname(java.net.URLDecoder.decode(rs.getString("uname"),"utf-8")  );
		}catch(Exception e){
			e.printStackTrace();
		}
		pd.setLv(rs.getInt("lv"));
		pd.setHead(rs.getString("head"));
		pd.setGold(rs.getInt("gold"));
		pd.setCreateTime(rs.getLong("createTime"));
		pd.setLastLoginTime(rs.getLong("lastLoginTime"));
		pd.setLastOnlineTime(rs.getLong("lastOnlineTime"));
		pd.setRoomdata(rs.getString("roomdata"));
		pd.setParent(rs.getLong("parent"));
		pd.setPlaycount(rs.getInt("playcount"));
		pd.setWincount(rs.getInt("wincount"));
		
		return pd;
	}

}
