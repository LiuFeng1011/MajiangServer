package com.dreamgear.majiang.db;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dreamgear.majiang.db.dao.DelegateDAO;
import com.dreamgear.majiang.db.dao.GMUserDAO;
import com.dreamgear.majiang.db.dao.LogDAO;
import com.dreamgear.majiang.db.dao.LoginDAO;
import com.dreamgear.majiang.db.dao.NoticeDAO;
import com.dreamgear.majiang.db.dao.OrderDAO;
import com.dreamgear.majiang.db.dao.PayGiftDAO;
import com.dreamgear.majiang.db.dao.PlayerDAO;
import com.dreamgear.majiang.db.dao.RankDAO;
import com.dreamgear.majiang.db.dao.RoomDAO;




public class DBManager {
	private static DBManager dbManager = null;
	private ApplicationContext context = null;
	private ApplicationContext common_context = null;

	private PlayerDAO playerDAO;
	private LoginDAO loginDAO;
	private LogDAO logDAO;
	private GMUserDAO gmUserDAO;
	private NoticeDAO noticeDAO;
	private RoomDAO roomDAO;
	private OrderDAO orderDAO;
	private DelegateDAO delegateDAO;
	private PayGiftDAO payGiftDAO;
	private RankDAO rankDAO;
	
	public static DBManager getInstance() {
		if (dbManager == null) {
			dbManager = new DBManager();
		}
		return dbManager;
	}


	public void init() {
		context = new ClassPathXmlApplicationContext("applicationContext-game.xml");
		common_context = new ClassPathXmlApplicationContext("applicationContext-game_common.xml");

		loginDAO = (LoginDAO) common_context.getBean("loginDAO");
		
		gmUserDAO = (GMUserDAO) common_context.getBean("gmUserDAO");
		
		playerDAO = (PlayerDAO) context.getBean("playerDAO");
		
		logDAO = (LogDAO) context.getBean("logDAO");
		
		noticeDAO = (NoticeDAO) context.getBean("noticeDAO");

		roomDAO = (RoomDAO) context.getBean("roomDAO");

		orderDAO = (OrderDAO) context.getBean("orderDAO");
		
		delegateDAO = (DelegateDAO) context.getBean("delegateDAO");
		
		payGiftDAO = (PayGiftDAO) context.getBean("payGiftDAO");
		
		rankDAO = (RankDAO) context.getBean("rankDAO");
	}

	public ApplicationContext getContext() {
		return context;
	}


	public void setContext(ApplicationContext context) {
		this.context = context;
	}


	public PlayerDAO getPlayerDAO() {
		return playerDAO;
	}


	public void setPlayerDAO(PlayerDAO playerDAO) {
		this.playerDAO = playerDAO;
	}


	public LoginDAO getLoginDAO() {
		return loginDAO;
	}


	public void setLoginDAO(LoginDAO loginDAO) {
		this.loginDAO = loginDAO;
	}


	public LogDAO getLogDAO() {
		return logDAO;
	}


	public void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}


	public GMUserDAO getGmUserDAO() {
		return gmUserDAO;
	}


	public void setGmUserDAO(GMUserDAO gmUserDAO) {
		this.gmUserDAO = gmUserDAO;
	}


	public NoticeDAO getNoticeDAO() {
		return noticeDAO;
	}


	public void setNoticeDAO(NoticeDAO noticeDAO) {
		this.noticeDAO = noticeDAO;
	}


	public RoomDAO getRoomDAO() {
		return roomDAO;
	}


	public void setRoomDAO(RoomDAO roomDAO) {
		this.roomDAO = roomDAO;
	}


	public OrderDAO getOrderDAO() {
		return orderDAO;
	}


	public void setOrderDAO(OrderDAO orderDAO) {
		this.orderDAO = orderDAO;
	}


	public DelegateDAO getDelegateDAO() {
		return delegateDAO;
	}


	public void setDelegateDAO(DelegateDAO delegateDAO) {
		this.delegateDAO = delegateDAO;
	}


	public PayGiftDAO getPayGiftDAO() {
		return payGiftDAO;
	}


	public void setPayGiftDAO(PayGiftDAO payGiftDAO) {
		this.payGiftDAO = payGiftDAO;
	}


	public RankDAO getRankDAO() {
		return rankDAO;
	}


	public void setRankDAO(RankDAO rankDAO) {
		this.rankDAO = rankDAO;
	}

	
}
