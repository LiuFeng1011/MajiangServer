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
import com.dreamgear.majiang.db.row.DelegateMapper;
import com.dreamgear.majiang.delegate.DelegateData;
import com.dreamgear.majiang.game.notice.GameNotice;

public class DelegateDAO extends JdbcDaoSupport{
	private static final String SQL_DELEGATE_QUERY = "SELECT * FROM delegate";
	private static final String SQL_DELEGATE_DELETE = "DELETE FROM delegate WHERE uid = ?  ";
	private static final String SQL_DELEGATE_UPDATE = "UPDATE delegate SET `check`=?,checkouttime=?,phone=?,rate=?,mark=? WHERE uid=?";
	private static final String SQL_DELEGATE_INSERT = "INSERT INTO delegate "
			+ "(uid,`check`,checkouttime,phone,mark) "
			+ "VALUES"
			+ "(?,?,?,?,?)";
	private static final String SQL_DELEGATE_QUERYONE = "SELECT * FROM delegate WHERE uid=?";
	private static final String SQL_DELEGATE_QUERYONE_UID = "SELECT * FROM delegate WHERE uid=?";
	private static final String SQL_DELEGATE_GETBYSTATE = "SELECT * FROM delegate WHERE `check`=?";
	private static final String SQL_DELEGATE_QUERY_NO = "SELECT * FROM delegate WHERE `check`!=2";

	private static final String SQL_DELEGATE_ISDELEGATE = "SELECT `check` FROM delegate WHERE uid=?";
	

	private DelegateMapper delegateMapper = new DelegateMapper();
	/**
	 * 获取一条数据
	 * @param uid
	 * @return
	 * @throws DataAccessException
	 */
	public DelegateData getData(long uid)  {
		try {
			return getJdbcTemplate().queryForObject(SQL_DELEGATE_QUERYONE, delegateMapper, uid);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	public DelegateData getDataByUID(long uid)  {
		try {
			return getJdbcTemplate().queryForObject(SQL_DELEGATE_QUERYONE_UID, delegateMapper, uid);
		} catch (EmptyResultDataAccessException e) {
			 logger.error("no player with uname : " + uid);
			return null;
		}
	}
//	
//	/**
//	 * 判断一个用户是不是代理
//	 * @param id
//	 * @return 0申请中 1	同意 2拒绝
//	 */
//	public int getOrderState(long uid){
//		try {
//			return getJdbcTemplate().queryForInt(SQL_DELEGATE_ISDELEGATE ,uid);
//		} catch (EmptyResultDataAccessException e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			return 0;
//		}
//	}
//	
	/**
	 * 获取全部数据
	 * @return
	 */
	public List<DelegateData> getDataList(){
		try {
			return getJdbcTemplate().query(SQL_DELEGATE_QUERY,delegateMapper);
		} catch (EmptyResultDataAccessException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 根据状态获取全部数据
	 * @return
	 */
	public List<DelegateData> getDataListByNO(){
		try {
			return getJdbcTemplate().query(SQL_DELEGATE_QUERY_NO,delegateMapper);
		} catch (EmptyResultDataAccessException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 根据状态获取全部数据
	 * @return
	 */
	public List<DelegateData> getDataListByState(String check){
		try {
			return getJdbcTemplate().query(SQL_DELEGATE_GETBYSTATE,delegateMapper,check);
		} catch (EmptyResultDataAccessException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 添加
	 * @param data
	 * @return
	 */
	public long addData(final DelegateData data){
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn)
						throws SQLException {
					PreparedStatement ps = conn.prepareStatement(SQL_DELEGATE_INSERT,
							Statement.RETURN_GENERATED_KEYS);

					ps.setLong(1, data.getUid());
					ps.setInt(2,data.getCheck().ordinal());
					ps.setLong(3,data.getCheckouttime());
					ps.setString(4, data.getPhone());
					ps.setLong(5, data.getMark());
					
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
	public void updateData(final DelegateData data){
		logger.info("data : " + JSON.toJSONString(data));
		getJdbcTemplate().update(SQL_DELEGATE_UPDATE,
				data.getCheck().ordinal(),
				data.getCheckouttime(),
				data.getPhone(),
				data.getRate(),
				data.getMark(),
				data.getUid());
	}
	

	/**
	 * 删除
	 * @param roleId
	 */
	public void delete(long id ){
		try {
			getJdbcTemplate().update(SQL_DELEGATE_DELETE,id);
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return;
		}
	}
	
}
