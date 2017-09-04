package com.dreamgear.majiang.db.row;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dreamgear.majiang.game.order.DailyOrderData;

public class DailyOrderDataMapper  implements RowMapper<DailyOrderData>{

	public DailyOrderData mapRow(ResultSet rs, int rowNum) throws SQLException {
		DailyOrderData pd = new DailyOrderData();
		
		pd.setTime(rs.getLong("time"));
		pd.setUcount(rs.getInt("ucount"));
		pd.setOrdercount(rs.getInt("ordercount"));
		pd.setValue(rs.getInt("value"));
		
		return pd;
		
	}
}
