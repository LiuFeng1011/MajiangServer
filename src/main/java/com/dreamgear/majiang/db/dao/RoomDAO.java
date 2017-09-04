package com.dreamgear.majiang.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.dreamgear.majiang.db.row.RoomDataMapper;
import com.dreamgear.majiang.game.game.RoomPlayData;

public class RoomDAO extends JdbcDaoSupport {
	private static final String SQL_ROOM_QUERY = "SELECT * FROM room";
	private static final String SQL_ROOM_DELETE = "DELETE FROM room WHERE createTime<?  ";
	private static final String SQL_ROOM_UPDATE = "UPDATE room SET "
			+ "createTime=?,roomtype=?,master=?,playcount=?,data=?,delegate=? "
			+ "WHERE roomid=?";
	
	private static final String SQL_ROOM_INSERT = "INSERT INTO room "
			+ "("
			+ "createTime,roomtype,master,playcount,data,delegate"
			+ ") "
			+ "VALUES"
			+ "(?,?,?,?,?,?"
			+ ")";
	
	private static final String SQL_ROOM_QUERYONE = "SELECT * FROM room WHERE roomid=?";

	private static final String SQL_ROOM_DELEGATE_ROOM_COUNT = "SELECT COUNT(*) FROM room WHERE delegate=? and createTime>? and createTime<?";

	
	private RoomDataMapper roomMapper = new RoomDataMapper();
	
	//查询语句
	public int GetDelegateRoomCount(long delegate,long starttime,long endtime){
		try {
			return getJdbcTemplate().queryForInt(SQL_ROOM_DELEGATE_ROOM_COUNT,delegate,starttime,endtime);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}
	}	

	/**
	 * 获取一个玩家的全部房间
	 * @param uid
	 * @return
	 */
	public List<RoomPlayData> GetPlayerRoomList(Map<Long,Long> roommap){
		String SQL_ROOM_QUERYBYUID = "SELECT * FROM room WHERE ";
		
		for(Map.Entry<Long, Long> entry:roommap.entrySet()){  
			SQL_ROOM_QUERYBYUID += ("roomid="+entry.getKey()+ " or ");
        }  
		SQL_ROOM_QUERYBYUID = SQL_ROOM_QUERYBYUID.substring(0, SQL_ROOM_QUERYBYUID.length()-4);
		
		try {
			return getJdbcTemplate().query(SQL_ROOM_QUERYBYUID, roomMapper);
		} catch (EmptyResultDataAccessException e) {
			 logger.error("no roomid with uname : " + SQL_ROOM_QUERYBYUID);
			return null;
		}
	}
	
	/**
	 * 获取一条数据
	 * @param uid
	 * @return
	 * @throws DataAccessException
	 */
	public RoomPlayData getRoomData(long roomid)  {
		try {
			return getJdbcTemplate().queryForObject(SQL_ROOM_QUERYONE, roomMapper, roomid);
		} catch (EmptyResultDataAccessException e) {
			 logger.error("no roomid with uname : " + roomid);
			return null;
		}
	}
	/**
	 * 删除
	 * @param roleId
	 */
	public void deleteRoom(long outtime){
		try {
			getJdbcTemplate().update(SQL_ROOM_DELETE,outtime);
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
	public long addRoom(final RoomPlayData data){
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn)
						throws SQLException {
					PreparedStatement ps = conn.prepareStatement(SQL_ROOM_INSERT,
							Statement.RETURN_GENERATED_KEYS);

					ps.setLong(1, data.createTime);
					ps.setInt(2, data.roomtype);
					ps.setLong(3, data.master);
					ps.setInt(4,data.playcount);
					ps.setString(5,data.data);
					ps.setLong(6,data.delegate);
					
					return ps;
				}
			},keyHolder);
			data.roomid = (keyHolder.getKey().longValue());
			
			return data.roomid;
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
	public void updateRoom(final RoomPlayData data){
		
		String scores = "";
		try{
			scores = java.net.URLEncoder.encode(data.data,"utf-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//logger.info(" data : " + JSON.toJSONString(data));
		getJdbcTemplate().update(SQL_ROOM_UPDATE, 
				data.createTime,
				data.roomtype,
				data.master,
				data.playcount,
				scores,
				data.delegate,
				data.roomid);
	}
}
