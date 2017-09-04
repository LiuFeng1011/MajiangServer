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

import com.dreamgear.majiang.db.row.LogDataMapper;
import com.dreamgear.majiang.game.log.LogData;
import com.dreamgear.majiang.game.player.PlayerAddByDay;

public class LogDAO extends JdbcDaoSupport {

	private static final String SQL_LOG_INSERT = "INSERT INTO gamelog "
			+ "(uid,`time`,gold,createtime,eventid,eventtype,eventvalue) "
			+ "VALUES"
			+ "(?,?,?,?,?,?,?)";
	
	private LogDataMapper logMapper = new LogDataMapper();
	
	/**
	 * 添加
	 * @param data
	 * @return
	 */
	public void addLogData(final LogData data){
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn)
						throws SQLException {
					PreparedStatement ps = conn.prepareStatement(SQL_LOG_INSERT,
							Statement.RETURN_GENERATED_KEYS);

					ps.setLong(1, data.getUid());
					ps.setLong(2,data.getTime());
					ps.setInt(3,data.getGold());
					ps.setLong(4,data.getCreatetime());
					ps.setInt(5,data.getEventid());
					ps.setInt(6,data.getEventtype());
					ps.setString(7,data.getEventvalue());
					return ps;
				}
			},keyHolder);
			
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static final String SQL_LOG_GETONLINECOUNT_BYTIME = "SELECT * FROM gamelog WHERE eventid=20001 and `time`>? and `time`<? order by time desc";
	
	/**
	 * 查询实时在线玩家数量
	 */
	public List<LogData> GetOnlinePlayerCountByDay(long starttime,long endtime){
		try {
			return getJdbcTemplate().query(SQL_LOG_GETONLINECOUNT_BYTIME,logMapper,starttime,endtime);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
