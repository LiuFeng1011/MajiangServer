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
import com.dreamgear.majiang.db.row.RankDataMapper;
import com.dreamgear.majiang.db.row.RankGiftDataMapper;
import com.dreamgear.majiang.game.rank.RankData;
import com.dreamgear.majiang.game.rank.RankGiftData;
import com.dreamgear.majiang.utils.TimeUtils;

public class RankDAO extends JdbcDaoSupport {

	private static final String SQL_RANK_QUERY = "SELECT * FROM rank WHERE datatime>? ORDER BY scores DESC";
	private static final String SQL_RANK_DELETE = "DELETE FROM rank WHERE uid=?  ";
	private static final String SQL_RANK_DELETE_ALL = "DELETE FROM rank ";
	private static final String SQL_RANK_UPDATE = "UPDATE rank SET "
			+ "scores=? WHERE id=?";
	
	private static final String SQL_RANK_INSERT = "INSERT INTO rank "
			+ "( uid,scores,datatime ) "
			+ "VALUES"
			+ "(?,?,?)";
	

	private RankDataMapper rankMapper = new RankDataMapper();
	
	/**
	 * 获取全部数据
	 * @return
	 */
	public List<RankData> getRankDataList(long time){

		try {
			return getJdbcTemplate().query(SQL_RANK_QUERY,rankMapper,time);
		} catch (EmptyResultDataAccessException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 删除
	 * @param uid
	 */
	public void deleteRank(long uid){
		try {
			getJdbcTemplate().update(SQL_RANK_DELETE,uid);
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void clearRank(){
		try {
			getJdbcTemplate().update(SQL_RANK_DELETE_ALL);
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * 更新一个数据
	 * @param data
	 */
	public void updateRank(final RankData data){
		getJdbcTemplate().update(SQL_RANK_UPDATE, 
				data.getScores(),
				data.getId());
	}

	/**
	 * 添加
	 * @param data
	 * @return
	 */
	public long addRank(final RankData data){
		logger.info("addRank data: " + JSON.toJSONString(data));
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn)
						throws SQLException {
					PreparedStatement ps = conn.prepareStatement(SQL_RANK_INSERT,
							Statement.RETURN_GENERATED_KEYS);
					ps.setLong(1, data.getUid());
					ps.setInt(2, data.getScores());
					ps.setLong(3, TimeUtils.nowLong());
					return ps;
				}
			},keyHolder);
			data.setId(keyHolder.getKey().longValue());
			return keyHolder.getKey().longValue();
		}catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}
	
	//********************************排行奖励************************************//

	private static final String SQL_RANKGIFT_QUERY = "SELECT * FROM rank_gift ORDER BY rank_min ASC";
	private static final String SQL_RANKGIFT_DELETE = "DELETE FROM rank_gift WHERE id=?  ";
	private static final String SQL_RANKGIFT_UPDATE = "UPDATE rank_gift SET "
			+ "rank_min=?,rank_max=?,count=?"
			+ " WHERE id=?";
	
	private static final String SQL_RANKGIFT_INSERT = "INSERT INTO rank_gift "
			+ "( rank_min,rank_max,count ) "
			+ "VALUES"
			+ "(?,?,?)";


	private RankGiftDataMapper rankGiftDataMapper = new RankGiftDataMapper();
	
	
	/**
	 * 获取全部数据
	 * @return
	 */
	public List<RankGiftData> getRankGiftDataList(){
		try {
			return getJdbcTemplate().query(SQL_RANKGIFT_QUERY,rankGiftDataMapper);
		} catch (EmptyResultDataAccessException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 删除
	 * @param uid
	 */
	public void deleteRankGift(long id){
		try {
			getJdbcTemplate().update(SQL_RANKGIFT_DELETE,id);
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * 更新一个数据
	 * @param data
	 */
	public void updateRankGift(final RankGiftData data){
		getJdbcTemplate().update(SQL_RANKGIFT_UPDATE, 
				data.getRank_min(),
				data.getRank_max(),
				data.getCount(),
				data.getId());
	}

	/**
	 * 添加
	 * @param data
	 * @return
	 */
	public long addRankGift(final RankGiftData data){
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn)
						throws SQLException {
					PreparedStatement ps = conn.prepareStatement(SQL_RANKGIFT_INSERT,
							Statement.RETURN_GENERATED_KEYS);
					ps.setInt(1, data.getRank_min());
					ps.setInt(2, data.getRank_max());
					ps.setInt(3, data.getCount());
					return ps;
				}
			},keyHolder);
			data.setId(keyHolder.getKey().longValue());
			return keyHolder.getKey().longValue();
		}catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}
}
