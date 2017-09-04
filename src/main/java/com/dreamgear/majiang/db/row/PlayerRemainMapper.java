package com.dreamgear.majiang.db.row;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dreamgear.majiang.game.player.PlayerRemain;

public class PlayerRemainMapper implements RowMapper<PlayerRemain> {
	//PlayerRemain
	public PlayerRemain mapRow(ResultSet rs, int rowNum) throws SQLException {

		PlayerRemain pd = new PlayerRemain();
		
		pd.setId(rs.getInt("id"));
		pd.setDru(rs.getInt("dru"));
		
		pd.setDay_1(rs.getInt("day_1"));
		pd.setDay_2(rs.getInt("day_2"));
		pd.setDay_3(rs.getInt("day_3"));
		pd.setDay_4(rs.getInt("day_4"));
		pd.setDay_5(rs.getInt("day_5"));
		pd.setDay_6(rs.getInt("day_6"));
		pd.setDay_7(rs.getInt("day_7"));
		pd.setDay_14(rs.getInt("day_14"));
		pd.setDay_30(rs.getInt("day_30"));
		
		pd.setStat_time(rs.getLong("stat_time"));
		pd.setAdd_time(rs.getLong("add_time"));
		
		return pd;
	}
}
