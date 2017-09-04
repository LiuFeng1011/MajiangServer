package com.dreamgear.majiang.game;

import com.dreamgear.majiangserver.net.ServerManager;
import com.dreamgear.wx.WXTicketManager;
import com.alibaba.fastjson.JSON;
import com.dreamgear.http.HttpServer;
import com.dreamgear.http.handler.domain.LoginData;
import com.dreamgear.majiang.common.GameConfig;
import com.dreamgear.majiang.common.GameConst;
import com.dreamgear.majiang.common.GameTools;
import com.dreamgear.majiang.core.SchedulerServer;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.game.data.GameDataManager;
import com.dreamgear.majiang.game.game.RoomManager;
import com.dreamgear.majiang.game.notice.NoticeMgr;
import com.dreamgear.majiang.game.player.GamePlayerManager;
import com.dreamgear.majiang.game.player.PlayerData;
import com.dreamgear.majiang.utils.PropertyManager;
import com.dreamgear.majiang.utils.TimeUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameWord {
	private static final Logger logger = LoggerFactory.getLogger(GameWord.class);

	WorldMonitor monitor = new WorldMonitor();

	private static GameWord instance;
	private ServerManager server;

	public static GameWord GetInstance(){
		if(instance == null){
			instance = new GameWord();
		}
		return instance;
	}
	
	public void StartGame(){
		
		try{
			//游戏配置
			GameConfig.load();
			// 游戏配置表数据载入
			
			GameDataManager.load(GameConst.RES_PATH);
			
			//数据库
			DBManager.getInstance().init();
			
			//加载配置文件
			PropertyManager pm = new PropertyManager(GameConst.BASEFILENAME);
			//String str = pm.getString("LanguageTips");
			
			//网络服务
			server = new ServerManager(GameConfig.serverPort);
			server.Start();

			logger.info("注册游戏业务服务...");
			//注册游戏业务服务
			server.setServerList(GameTools.GetServerMap());
			logger.info("玩家管理器初始化...");
			//玩家管理
			GamePlayerManager.GetInstance().Init();

			logger.info("游戏公告系统初始化...");
			//游戏公告
			NoticeMgr.getInstance();

			logger.info("http headle");
			HttpServer.getInstance().run(GameConfig.http_port); 
			
			//启动计划任务管理器
			logger.info("启动计划任务管理器...");
			SchedulerManager.schedule(new SchedulerServer());
			
			start();
			
			
//			PlayerData pd = DBManager.getInstance().getPlayerDAO().getPlayerData(1);
//			if(pd != null)logger.info("pd : " + JSON.toJSONString(pd));
//			
//			LoginData ld = DBManager.getInstance().getLoginDAO().getLoginData("fdgadffa");
//			if(ld != null)logger.info("ld : " + JSON.toJSONString(ld));
//			
//			int count = DBManager.getInstance().getOrderDAO().getOrderCount("4009432001201705191583581935");
//			logger.info("count : " + count);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void start(){
		new MainLoop().start();
	}
	
	/**
	 * 游戏主线程
	 * 
	 * @author ShaoLong Wang
	 * 
	 */
	public class MainLoop extends Thread {
		long tickcount = 0;

		@Override
		public void run() {
			logger.info("MAIN LOOP THREAD START!");
			while (true) {
				try {
					// 开启服务计时
					monitor.start();
					long lastTime = monitor.startTime;
					// 游戏逻辑
					gameTick();
					long saveTimeElapse = TimeUtils.nowLong() - lastTime;
					monitor.RecordTime("saveTimeElapse", saveTimeElapse);
					// 停止服务计时
					monitor.stop();
					long sleepTime = monitor.getTimeRemain();

					if (sleepTime > 0) {
						Thread.sleep(sleepTime);
					}
					if (tickcount % monitor.PRINT_WORLD_MONITOR_TICK_COUNT == 0) {
						logger.info(monitor.toString());
					}
					tickcount++;
				} catch (Exception e) {
					logger.error("error in MainLoop tick : " + tickcount, e);
					tickcount++;
				}
			}
		}

//		long shoutTime = TimeUtils.nowLong();
//		boolean isFirstShout = true;// 第一次喊话

		/**
		 * 游戏逻辑
		 */
		public void gameTick() {
			GamePlayerManager.GetInstance().Tick();
			
			WXTicketManager.GetInstance().Tick();
			
			RoomManager.GetInstance().Update();
		}

	}
	
}
