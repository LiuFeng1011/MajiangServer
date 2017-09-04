package com.dreamgear.majiang.db.row;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dreamgear.majiang.game.order.OrderData;

public class OrderDataMapper implements RowMapper<OrderData> {

	public OrderData mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		OrderData order = new OrderData();
		order.setId(rs.getLong("id"));
		order.setTime(rs.getLong("time"));
		order.setOrderId(rs.getString("orderId"));
		order.setOrderType(rs.getInt("orderType"));
		order.setValue(rs.getInt("value"));
		order.setAppid(rs.getString("appid"));
		
		order.setResult(rs.getByte("result"));
		order.setMsg(rs.getString("msg"));
		order.setUid(rs.getLong("uid"));
		order.setParentid(rs.getLong("parentid"));
		
		order.setTrade_type(rs.getString("trade_type"));
		order.setOut_trade_no(rs.getString("out_trade_no"));
		order.setMch_id(rs.getString("mch_id"));
		order.setOpenid(rs.getString("openid"));
		
		return order;
	}
}
