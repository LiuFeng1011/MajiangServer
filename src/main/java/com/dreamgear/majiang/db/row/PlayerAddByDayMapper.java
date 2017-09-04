package com.dreamgear.majiang.db.row;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dreamgear.majiang.game.player.PlayerAddByDay;

public class PlayerAddByDayMapper implements RowMapper<PlayerAddByDay> {
	public PlayerAddByDay mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		PlayerAddByDay data = new PlayerAddByDay();
		data.setDay(rs.getString("days"));
		data.setCount(rs.getInt("count"));
		
		return data;
	}
}
