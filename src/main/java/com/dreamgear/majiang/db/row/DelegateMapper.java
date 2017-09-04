package com.dreamgear.majiang.db.row;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dreamgear.majiang.common.GameCommon.enDelegateState;
import com.dreamgear.majiang.delegate.DelegateData;

public class DelegateMapper implements RowMapper<DelegateData> {

	public DelegateData mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		DelegateData pd = new DelegateData();
		pd.setId(rs.getLong("id"));
		pd.setUid(rs.getLong("uid"));
		pd.setCheck(enDelegateState.GetState(rs.getInt("check")) );
		pd.setCheckouttime(rs.getLong("checkouttime"));
		pd.setPhone(rs.getString("phone"));
		pd.setRate(rs.getInt("rate"));
		pd.setMark(rs.getLong("mark"));
		return pd;
	}

}
