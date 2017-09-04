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

import com.dreamgear.majiang.db.row.NoticeMapper;
import com.dreamgear.majiang.game.notice.GameNotice;
/**
 * 公告数据库
 * @author liufeng
 *
 */
public class NoticeDAO extends JdbcDaoSupport {
	private static final String SQL_NOTICE_QUERY = "SELECT * FROM notice";
	private static final String SQL_NOTICE_DELETE = "DELETE FROM notice WHERE id = ?  ";
	private static final String SQL_NOTICE_UPDATE = "UPDATE notice SET "
			+ "noticeType=?,"
			+ "noticeTitle=?,noticeContent=?,startTime=?,endTime=?,"
			+ "timeDelay=? "
			+ "WHERE id=?";
	private static final String SQL_NOTICE_INSERT = "INSERT INTO notice "
			+ "(noticeType,"
			+ "noticeTitle,noticeContent,startTime,endTime,"
			+ "timeDelay) "
			+ "VALUES"
			+ "(?"
			+ ",?,?,?,?"
			+ ",?)";

	private NoticeMapper gameNoticeDataMapper = new NoticeMapper();
	/**
	 * 获取全部数据
	 * @return
	 */
	public List<GameNotice> getNoticeDataList(){
		try {
			return getJdbcTemplate().query(SQL_NOTICE_QUERY,gameNoticeDataMapper);
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
	public void deleteNotice(long noticeId){
		try {
			getJdbcTemplate().update(SQL_NOTICE_DELETE,noticeId);
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
	public long addNotice(final GameNotice data){
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn)
						throws SQLException {
					PreparedStatement ps = conn.prepareStatement(SQL_NOTICE_INSERT,
							Statement.RETURN_GENERATED_KEYS);
					ps.setInt(1, data.getNoticeType());
					ps.setString(2, data.getNoticeTitle());
					ps.setString(3, data.getNoticeContent());
					ps.setLong(4, data.getStartTime());
					ps.setLong(5, data.getEndTime());
					ps.setInt(6, data.getTimeDelay());
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
	
	/**
	 * 更新一个数据
	 * @param data
	 */
	public void updateNotice(final GameNotice data){
		getJdbcTemplate().update(SQL_NOTICE_UPDATE, 
				data.getNoticeType(),
				data.getNoticeTitle(),
				data.getNoticeContent(),
				data.getStartTime(),
				data.getEndTime(),
				data.getTimeDelay(),
				data.getId());
	}
}
