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

import com.dreamgear.majiang.db.row.PayGiftDataMapper;
import com.dreamgear.majiang.game.payGift.PayGiftData;

public class PayGiftDAO extends JdbcDaoSupport {
	private static final String SQL_PAYGIFT_QUERY = "SELECT * FROM pay_gift_data";
	private static final String SQL_PAYGIFT_INSERT = "INSERT INTO pay_gift_data (payid,gift) VALUES (?,?)";
	private static final String SQL_PAYGIFT_UPDATE = "UPDATE pay_gift_data SET payid=?,gift=? WHERE payid=?";
	private static final String SQL_PAYGIFT_DELETE = "DELETE FROM pay_gift_data WHERE payid = ?  ";
	
	private PayGiftDataMapper payGiftDataMapper = new PayGiftDataMapper();
	
	
	/**
	 * 查询全部数据
	 * @return
	 */
	public List<PayGiftData> getGiftDataList(){
		try {
			return getJdbcTemplate().query(SQL_PAYGIFT_QUERY,payGiftDataMapper);
		} catch (EmptyResultDataAccessException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 删除
	 * @param roleId
	 */
	public void deleteGift(long payid){
		try {
			getJdbcTemplate().update(SQL_PAYGIFT_DELETE,payid);
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * 添加
	 * @param data
	 * @return
	 */
	public void addGift(final PayGiftData data){
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn)
						throws SQLException {
					PreparedStatement ps = conn.prepareStatement(SQL_PAYGIFT_INSERT,
							Statement.RETURN_GENERATED_KEYS);
					ps.setInt(1, data.getPayid());
					ps.setInt(2, data.getGift());
					return ps;
				}
			},keyHolder);
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 更新一个数据
	 * @param data
	 */
	public void updateGift(final PayGiftData data){
		getJdbcTemplate().update(SQL_PAYGIFT_UPDATE, 
				data.getPayid(),
				data.getGift(),
				data.getPayid());
	}
}
