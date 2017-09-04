package com.dreamgear.majiang.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.alibaba.fastjson.JSON;
import com.dreamgear.gm.GMUserData;
import com.dreamgear.majiang.db.row.GMUserDataMapper;

public class GMUserDAO extends JdbcDaoSupport{

//	private static final String SQL_GMUSER_QUERY = "SELECT * FROM gmuser";
	private static final String SQL_GMUSER_DELETE = "DELETE FROM gmuser WHERE id = ?  ";
	private static final String SQL_GMUSER_UPDATE_PW = "UPDATE gmuser SET pw=? WHERE id=?";
	private static final String SQL_GMUSER_INSERT = "INSERT INTO gmuser "
			+ "(uname,pw,power) "
			+ "VALUES"
			+ "(?,?,?)";
	private static final String SQL_LOGIN_QUERYONE = "SELECT * FROM gmuser WHERE uname=? and pw=?";

	private GMUserDataMapper gmUserDataMapper = new GMUserDataMapper();
	
	//查询语句
	public int GetPlayerCount(String sql){
		try {
			return getJdbcTemplate().queryForInt(sql);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}
	}	
	/**
	 * 获取一条数据
	 * @param uid
	 * @return
	 * @throws DataAccessException
	 */
	public GMUserData getLoginData(String uname,String pw)  {
		try {
			GMUserData ret = getJdbcTemplate().queryForObject(SQL_LOGIN_QUERYONE, gmUserDataMapper, uname,pw);
			return ret;
		} catch (EmptyResultDataAccessException e) {
			 logger.error("no player with uname : " + uname);
			return null;
		}
	}
	
	/**
	 * 添加
	 * @param data
	 * @return
	 */
	public long addLoginData(final GMUserData data){
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn)
						throws SQLException {
					PreparedStatement ps = conn.prepareStatement(SQL_GMUSER_INSERT,
							Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, data.getUname());
					ps.setString(2,data.getPw());
					ps.setLong(3,data.getPower());
					return ps;
				}
			},keyHolder);
			data.setId(keyHolder.getKey().longValue());
			
			return data.getId();
		}catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 更新一个数据
	 * @param data
	 */
	public void updateLoginData(final GMUserData data){
		getJdbcTemplate().update(SQL_GMUSER_UPDATE_PW, 
				data.getPw(),
				data.getId());
	}
	
	/**
	 * 删除
	 * @param roleId
	 */
	public void deletePlayer(long id){
		try {
			getJdbcTemplate().update(SQL_GMUSER_DELETE,id);
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return;
		}
	}
}
