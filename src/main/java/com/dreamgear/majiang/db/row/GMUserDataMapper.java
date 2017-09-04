package com.dreamgear.majiang.db.row;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dreamgear.gm.GMUserData;

public class GMUserDataMapper implements RowMapper<GMUserData> {
	public GMUserData mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub

		GMUserData pd = new GMUserData();
		pd.setId(rs.getLong("id"));
		pd.setUname(rs.getString("uname"));
		pd.setPw(rs.getString("pw"));
		pd.setPower(rs.getInt("power"));
		
		return pd;
	}
}
