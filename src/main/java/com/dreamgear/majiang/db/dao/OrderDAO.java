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

import com.dreamgear.majiang.db.row.DailyOrderDataMapper;
import com.dreamgear.majiang.db.row.DelegateIncomeSQLDataMapper;
import com.dreamgear.majiang.db.row.DelegateIncomeTotalSQLDataMapper;
import com.dreamgear.majiang.db.row.OrderDataMapper;
import com.dreamgear.majiang.delegate.DelegateIncomeSQLData;
import com.dreamgear.majiang.game.order.DailyOrderData;
import com.dreamgear.majiang.game.order.OrderData;

public class OrderDAO extends JdbcDaoSupport{
	private static final String SQL_ORDER_QUERY_FORINT = "SELECT count(*) FROM orderdata WHERE orderId LIKE ";
	private static final String SQL_ORDER_QUERY = "SELECT * FROM orderdata";
	private static final String SQL_ORDER_DELETE = "DELETE FROM orderdata WHERE id = ?  ";
	private static final String SQL_ORDER_UPDATE = "UPDATE orderdata SET "
			+ "result=?"
			+ "WHERE id=?";
	private static final String SQL_ORDER_INSERT = "INSERT INTO orderdata "
			+ "(orderId,orderType,value,appid,"
			+ "result,msg,uid,parentid,"
			+ "trade_type,out_trade_no,mch_id,openid,time"
			+ ") "
			+ "VALUES"
			+ "(?,?,?,?,"
			+ "?,?,?,?,"
			+ "?,?,?,?,?)";
	
	private static final String SQL_ORDER_COUNT = "select COUNT(*) as count,SUM(`value`) as `value` from orderdata where parentid=? and time>=? and time<=?";

	private static final String SQL_ORDER_COUNT_OFDAY = "SELECT FROM_UNIXTIME(`time`/1000,'%Y-%m-%d') as days,COUNT(id) as count,sum(`value`) as `value` FROM orderdata where parentid=? and time>=? and time<=? GROUP BY days";

	private static final String SQL_ORDER_COUNT_OFYEAR = "SELECT FROM_UNIXTIME(`time`/1000,'%Y') as days,COUNT(id) as count,sum(`value`) as `value` FROM orderdata where parentid=? and time>=? and time<=? GROUP BY days";
	
	private static final String SQL_ORDER_ALL_BY_TIME = "select * from orderdata where `time`>=? and `time`<=? order by time desc limit 50";

	private static final String SQL_ORDER_BY_UID = "select * from orderdata where uid=? order by time desc limit 50";

	private static final String SQL_ORDER_COUNT_ALL = "select COUNT(*) as count,SUM(`value`) as `value` from orderdata";
	
	private static final String SQL_ORDER_COUNT_BAYDAY = "SELECT * FROM daily_order WHERE `time`>=? and `time`<=?";

	private OrderDataMapper orderDataMapper = new OrderDataMapper();
	private DelegateIncomeSQLDataMapper delegateIncomeSQLDataMapper = new DelegateIncomeSQLDataMapper();
	private DelegateIncomeTotalSQLDataMapper delegateIncomeTotalSQLDataMapper = new DelegateIncomeTotalSQLDataMapper();
	private DailyOrderDataMapper dailyOrderDataMapper = new DailyOrderDataMapper();
	
	
	public List<DelegateIncomeSQLData> GetDelegateOrderData(long uid,long starttime,long endtime){
		try {
			return getJdbcTemplate().query(SQL_ORDER_COUNT_OFDAY, delegateIncomeSQLDataMapper, uid,starttime,endtime);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	public List<DelegateIncomeSQLData> GetDelegateOrderDataByYear(long uid,long starttime,long endtime){
		try {
			return getJdbcTemplate().query(SQL_ORDER_COUNT_OFYEAR, delegateIncomeSQLDataMapper, uid,starttime,endtime);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	public DelegateIncomeSQLData GetDelegateOrderTotalDate(long uid,long starttime,long endtime){
		try {
			return getJdbcTemplate().queryForObject(SQL_ORDER_COUNT, delegateIncomeTotalSQLDataMapper, uid,starttime,endtime);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	/**
	 * 获取游戏订单统计数据
	 * @param uid
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public DelegateIncomeSQLData GetOrderTotalDate(){
		try {
			return getJdbcTemplate().queryForObject(SQL_ORDER_COUNT_ALL, delegateIncomeTotalSQLDataMapper);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	/**
	 * 获取时间段内的全部数据
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<OrderData> GetOrderDateByTime(long starttime,long endtime){
		try {
			return getJdbcTemplate().query(SQL_ORDER_ALL_BY_TIME, orderDataMapper,starttime,endtime);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	/**
	 * 统计每日订单数据
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<DailyOrderData> GetDailyOrderDataByTime(long starttime,long endtime){
		try {
			return getJdbcTemplate().query(SQL_ORDER_COUNT_BAYDAY, dailyOrderDataMapper,starttime,endtime);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	/**
	 * 获取一个玩家的全部数据
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<OrderData> GetOrderDateByUid(long uid){
		try {
			return getJdbcTemplate().query(SQL_ORDER_BY_UID, orderDataMapper,uid);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public int getOrderCount(String id){
		try {
			return getJdbcTemplate().queryForInt(SQL_ORDER_QUERY_FORINT + "'" + id + "'");
		} catch (EmptyResultDataAccessException e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 获取全部数据
	 * @return
	 */
	public List<OrderData> getOrderDataList(){
		try {
			return getJdbcTemplate().query(SQL_ORDER_QUERY,orderDataMapper);
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
	public void deleteOrder(long id){
		try {
			getJdbcTemplate().update(SQL_ORDER_DELETE,id);
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
	public long addOrder(final OrderData data){
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection conn)
						throws SQLException {
					PreparedStatement ps = conn.prepareStatement(SQL_ORDER_INSERT,
							Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, data.getOrderId());
					ps.setInt(2, data.getOrderType());
					ps.setInt(3, data.getValue());
					ps.setString(4, data.getAppid());
					ps.setByte(5, data.getResult());
					ps.setString(6, data.getMsg());
					ps.setLong(7, data.getUid());
					ps.setLong(8, data.getParentid());
					
					ps.setString(9, data.getTrade_type());
					ps.setString(10, data.getOut_trade_no());
					ps.setString(11, data.getMch_id());
					ps.setString(12, data.getOpenid());
					ps.setLong(13, data.getTime());
					return ps;
				}
			},keyHolder);
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
	public void updateOrder(final OrderData data){
		getJdbcTemplate().update(SQL_ORDER_UPDATE, 
				data.getResult(),
				data.getId());
	}
}
