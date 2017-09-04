package com.dreamgear.majiang.db.row;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dreamgear.majiang.delegate.DelegateIncomeSQLData;

public class DelegateIncomeSQLDataMapper implements RowMapper<DelegateIncomeSQLData>{

	public DelegateIncomeSQLData mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		DelegateIncomeSQLData pd = new DelegateIncomeSQLData();
		pd.setCount(rs.getInt("count"));
		pd.setValue(rs.getInt("value"));
		pd.setTime(rs.getString("days"));
		return pd;
	}
}
