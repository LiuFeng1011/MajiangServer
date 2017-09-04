package com.dreamgear.majiang.db.row;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dreamgear.http.handler.domain.LoginData;

public class LoginDataMapper implements RowMapper<LoginData>{
	public LoginData mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub

		LoginData pd = new LoginData();
		pd.setId(rs.getLong("id"));
		pd.setOpenid(rs.getString("openid"));

		pd.setCreateTime(rs.getLong("createTime"));
		pd.setLastLoginTime(rs.getLong("lastLoginTime"));

		try{
			pd.nickname = java.net.URLDecoder.decode(rs.getString("nickname"),"utf-8") ;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		pd.sex = rs.getInt("sex") + "";
		pd.province = rs.getString("province");
		pd.country = rs.getString("country");
		pd.city = rs.getString("city");
		pd.head = rs.getString("head");
		return pd;
	}
}
