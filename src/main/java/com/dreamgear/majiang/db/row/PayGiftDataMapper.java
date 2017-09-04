package com.dreamgear.majiang.db.row;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dreamgear.majiang.game.payGift.PayGiftData;

public class PayGiftDataMapper  implements RowMapper<PayGiftData> {

	public PayGiftData mapRow(ResultSet rs, int rowNum) throws SQLException {
		PayGiftData data = new PayGiftData();
		data.setPayid(rs.getInt("payid"));
		data.setGift(rs.getInt("gift"));
		return data;
	}
}
