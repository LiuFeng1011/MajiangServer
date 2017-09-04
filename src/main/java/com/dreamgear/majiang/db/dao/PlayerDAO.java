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

import com.dreamgear.majiang.db.row.PlayerAddByDayMapper;
import com.dreamgear.majiang.db.row.PlayerDataMapper;
import com.dreamgear.majiang.db.row.PlayerRemainMapper;
import com.dreamgear.majiang.game.player.PlayerAddByDay;
import com.dreamgear.majiang.game.player.PlayerData;
import com.dreamgear.majiang.game.player.PlayerRemain;

public class PlayerDAO extends JdbcDaoSupport  {

	private static final String SQL_PLAYER_QUERY = "SELECT * FROM player";
	private static final String SQL_PLAYER_QUERYBYPAGE = "SELECT * FROM player LIMIT ?,?";
	private static final String SQL_PLAYER_DELETE = "DELETE FROM player WHERE uid = ?  ";
	private static final String SQL_PLAYER_UPDATE = "UPDATE player SET "
			+ "uid=?,uname=?,lv=?,head=?,gold=?,"
			+ "createTime=?,lastLoginTime=?,lastOnlineTime=?,roomdata=?,parent=?,"
			+ "playcount=?,wincount=? "
			+ "WHERE uid=?";
	private static final String SQL_PLAYER_INSERT = "INSERT INTO player "
			+ "(uid,uname,lv,head,gold,createTime,lastLoginTime,lastOnlineTime,roomdata,parent,playcount,wincount) "
			+ "VALUES"
			+ "(?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String SQL_PLAYER_QUERYONE = "SELECT * FROM player WHERE uid=?";

	private static final String SQL_PLAYER_DELEGATECOUNT = "SELECT COUNT(*) FROM player WHERE parent=?";
	
	private static final String SQL_PLAYER_DELEGATE_ADDBYDAY = "SELECT FROM_UNIXTIME(`createTime`/1000,'%Y-%m-%d') as days,COUNT(id) as count FROM player where parent=? and createTime >? and createTime < ? GROUP BY days";

	private static final String SQL_PLAYER_ADDBYYEAR = "SELECT FROM_UNIXTIME(`createTime`/1000,'%Y') as days,COUNT(id) as count FROM player where parent=? and createTime >? and createTime < ? GROUP BY days";

	private static final String SQL_PLAYER_GAME_REMAIN = "SELECT * FROM user_remain WHERE stat_time >= ? and stat_time <= ?";

	
	private PlayerDataMapper pdMapper = new PlayerDataMapper();
	private PlayerAddByDayMapper playerAddByDayMapper = new PlayerAddByDayMapper();
	private PlayerRemainMapper playerRemainMapper = new PlayerRemainMapper();
	
	//查询语句
	public int GetPlayerCount(String sql){
		try {
			return getJdbcTemplate().queryForInt(sql);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}
	}
	
	/**
	 * 获取游戏每日新增用户数
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<PlayerRemain> GetGamePlayerRemain(long starttime,long endtime){
		try {
			return getJdbcTemplate().query(SQL_PLAYER_GAME_REMAIN,playerRemainMapper,starttime,endtime);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	/**
	 * 获取代理用户每日新增用户数
	 * @param uid
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<PlayerAddByDay> GetPlayerAddByDay(long uid,long starttime,long endtime){
		try {
			return getJdbcTemplate().query(SQL_PLAYER_DELEGATE_ADDBYDAY,playerAddByDayMapper,uid,starttime,endtime);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	/**
	 * 代理用户每年新增用户数
	 * @param uid
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<PlayerAddByDay> GetPlayerAddByYear(long uid,long starttime,long endtime){
		try {
			return getJdbcTemplate().query(SQL_PLAYER_ADDBYYEAR,playerAddByDayMapper,uid,starttime,endtime);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	/**
	 * 获取一个代理的玩家数
	 * @param id
	 * @return 0申请中 1	同意 2拒绝
	 */
	public int getDelegateUserCount(long uid){
		try {
			return getJdbcTemplate().queryForInt(SQL_PLAYER_DELEGATECOUNT ,uid);
		} catch (EmptyResultDataAccessException e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 获取一条数据
	 * @param uid
	 * @return
	 * @throws DataAccessException
	 */
	public PlayerData getPlayerData(long uid)  {
		try {
			return getJdbcTemplate().queryForObject(SQL_PLAYER_QUERYONE, pdMapper, uid);
		} catch (EmptyResultDataAccessException e) {
			 logger.error("no player with uname : " + uid);
			return null;
		}
	}

	/**
	 * 获取全部数据
	 * @return
	 */
	public List<PlayerData> getPlayerList(){
		try {
			return getJdbcTemplate().query(SQL_PLAYER_QUERY,pdMapper);
		} catch (EmptyResultDataAccessException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 分页查找
	 * @param page
	 * @return
	 */
	public List<PlayerData> getPlayerListByPage(int start,int end){
		try {
			return getJdbcTemplate().query(SQL_PLAYER_QUERYBYPAGE,pdMapper,start,end);
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
	public void deletePlayer(long uid){
		try {
			getJdbcTemplate().update(SQL_PLAYER_DELETE,uid);
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
	public long addPlayer(final PlayerData data){
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn)
						throws SQLException {
					PreparedStatement ps = conn.prepareStatement(SQL_PLAYER_INSERT,
							Statement.RETURN_GENERATED_KEYS);

					ps.setLong(1, data.getUid());
					ps.setString(2, data.getUname());
					ps.setInt(3, data.getLv());
					ps.setString(4,data.getHead());
					ps.setInt(5,data.getGold());
					
					ps.setLong(6,data.getCreateTime());
					ps.setLong(7,data.getLastLoginTime());
					ps.setLong(8,data.getLastOnlineTime());
					ps.setString(9,data.getRoomdata());
					ps.setLong(10,data.getParent());
					ps.setInt(11, data.getPlaycount());
					ps.setInt(12, data.getWincount());
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
	public void updatePlayer(final PlayerData data){
		//logger.info(" data : " + JSON.toJSONString(data));
		getJdbcTemplate().update(SQL_PLAYER_UPDATE, 
				data.getUid(),
				data.getUname(),
				data.getLv(),
				data.getHead(),
				data.getGold(),
				data.getCreateTime(),
				data.getLastLoginTime(),
				data.getLastOnlineTime(),
				data.getRoomdata(),
				data.getParent(),
				data.getPlaycount(),
				data.getWincount(),
				data.getUid());
	}
	
//	 "uid=?,uname=?,lv=?,head=?,country=?,lvstar=?,item_list=?,"
//		+ "createTime=?,lastLoginTime=?,lastOnlineTime=? "
//		+ "WHERE uname=?";
}
