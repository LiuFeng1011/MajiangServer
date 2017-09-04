package com.dreamgear.majiang.db.row;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dreamgear.majiang.game.log.LogData;

public class LogDataMapper implements RowMapper<LogData> {
	public LogData mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub

		LogData pd = new LogData();
		pd.setUid(rs.getLong("uid"));
		pd.setTime(rs.getLong("time"));
		pd.setGold(rs.getInt("gold"));
		pd.setCreatetime(rs.getLong("createtime"));
		pd.setEventid(rs.getInt("eventid"));
		pd.setEventtype(rs.getInt("eventtype"));
		pd.setEventvalue(rs.getString("eventvalue"));
		
		return pd;
	}
}
