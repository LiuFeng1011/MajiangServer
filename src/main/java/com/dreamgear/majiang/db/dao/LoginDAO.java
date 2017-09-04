package com.dreamgear.majiang.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.alibaba.fastjson.JSON;
import com.dreamgear.http.handler.domain.LoginData;
import com.dreamgear.http.handler.domain.WXPlayerInfo;
import com.dreamgear.majiang.db.row.LoginDataMapper;
import com.dreamgear.majiang.game.player.PlayerData;

public class LoginDAO extends JdbcDaoSupport{

	private static final String SQL_LOGIN_QUERY = "SELECT * FROM login";
	private static final String SQL_LOGIN_QUERYBYPAGE = "SELECT * FROM login LIMIT ?,?";
	private static final String SQL_LOGIN_DELETE = "DELETE FROM login WHERE openid = ?  ";
	private static final String SQL_LOGIN_UPDATE = "UPDATE login SET nickname=?,head=?,lastLoginTime=? WHERE openid=?";
	private static final String SQL_LOGIN_INSERT = "INSERT INTO login "
			+ "(openid,createTime,lastLoginTime,nickname,sex,province,country,city,head) "
			+ "VALUES"
			+ "(?,?,?,?,?,?,?,?,?)";
	private static final String SQL_LOGIN_QUERYONE = "SELECT * FROM login WHERE openid=?";
	private static final String SQL_LOGIN_QUERYONE_ID = "SELECT * FROM login WHERE id=?";
	
	private LoginDataMapper loginMapper = new LoginDataMapper();

	//查询语句
	public int GetPlayerCount(String sql){
		try {
			return getJdbcTemplate().queryForInt(sql);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}
	}
	/**
	 * 模糊查找玩家
	 * @param key
	 * @return
	 * @throws DataAccessException
	 */
	public List<LoginData> getRoleByKey(String key) {
		try {
			String sql = "SELECT * FROM login WHERE BINARY nickname LIKE ?";

			return getJdbcTemplate().query(sql, loginMapper, "%"+java.net.URLEncoder.encode(key,"utf-8")+"%");
		} catch (Exception e) {

			return null;
		}
	}

	/**
	 * 分页查找
	 * @param page
	 * @return
	 */
	public List<LoginData> getPlayerListByPage(int start,int end){
		try {
			return getJdbcTemplate().query(SQL_LOGIN_QUERYBYPAGE,loginMapper,start,end);
		} catch (EmptyResultDataAccessException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取一条数据
	 * @param uid
	 * @return
	 * @throws DataAccessException
	 */
	public LoginData getLoginData(String openid)  {
		try {
			return getJdbcTemplate().queryForObject(SQL_LOGIN_QUERYONE, loginMapper, openid);
		} catch (EmptyResultDataAccessException e) {
			 logger.error("no player with uname : " + openid);
			return null;
		}
	}
	public LoginData getLoginData(long uid)  {
		try {
			return getJdbcTemplate().queryForObject(SQL_LOGIN_QUERYONE_ID, loginMapper, uid);
		} catch (EmptyResultDataAccessException e) {
			 logger.error("no player with uname : " + uid);
			return null;
		}
	}

	/**
	 * 添加
	 * @param data
	 * @return
	 */
	public long addLoginData(final LoginData data){
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn)
						throws SQLException {
					PreparedStatement ps = conn.prepareStatement(SQL_LOGIN_INSERT,
							Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, data.getOpenid());
					ps.setLong(2,data.getCreateTime());
					ps.setLong(3,data.getLastLoginTime());
					
					try{
						ps.setString(4, java.net.URLEncoder.encode(data.nickname,"utf-8"));
					}catch(Exception e){
						e.printStackTrace();
						ps.setString(4, "");
					}
					
					try{
						ps.setInt(5,Integer.parseInt(data.sex));
					}catch(Exception e){
						ps.setInt(5,0);
					}
					
					ps.setString(6, data.province);
					ps.setString(7, data.country);
					ps.setString(8, data.city);
					ps.setString(9, data.head);
					
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
	 * uname=?,lv=?,head=?,country=?,lvstar=?,item_list=? 
	 */
	public void updateLoginData(final LoginData data){
		logger.info(" data : " + JSON.toJSONString(data));
		String nickname = "";
		try{
			nickname = java.net.URLEncoder.encode(data.nickname,"utf-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		getJdbcTemplate().update(SQL_LOGIN_UPDATE,
				nickname,
				data.head,
				data.getLastLoginTime(),
				data.getOpenid());
	}
}
