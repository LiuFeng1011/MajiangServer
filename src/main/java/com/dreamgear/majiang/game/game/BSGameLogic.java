package com.dreamgear.majiang.game.game;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.dreamgear.majiang.common.GameCommon.BSHutype;
import com.dreamgear.majiang.common.GameCommon.OpenType;
import com.dreamgear.majiang.common.GameCommon.enCtrlType;
import com.dreamgear.majiang.common.GameConst;
import com.dreamgear.majiang.common.GameTools;
import com.dreamgear.majiang.game.data.GameDataManager;
import com.dreamgear.majiang.game.data.MajiangData;
import com.dreamgear.majiang.game.game.GameLogic.gamectrl;
import com.dreamgear.majiang.game.player.GamePlayerManager;
import com.dreamgear.majiang.game.player.PlayerManager;
import com.dreamgear.majiang.game.server.protocol.GameProtocol;
import com.dreamgear.majiang.game.server.resp.BaseResp;
import com.dreamgear.majiang.game.server.resp.GameReinroomResp;
import com.dreamgear.majiang.game.server.resp.InGameEatResp;
import com.dreamgear.majiang.game.server.resp.InGameFlushInfoResp;
import com.dreamgear.majiang.game.server.resp.InGameGetBrandResp;
import com.dreamgear.majiang.game.server.resp.InGameOutBrandResp;
import com.dreamgear.majiang.game.server.resp.InGameOverResp;
import com.dreamgear.majiang.game.server.resp.InGameStartInfo;
import com.dreamgear.majiang.game.server.resp.InGameTingResp;
import com.dreamgear.majiang.game.server.resp.PlayerControlResp;
import com.dreamgear.majiang.utils.GetLog;
import com.dreamgear.majiangserver.net.BaseMessage;

public class BSGameLogic {
	
	private Logger logger;

	public List<MajiangData> majiangList = new ArrayList<MajiangData>(); 
	
	//玩家列表
	List<Long> playerList = new ArrayList<Long>();
	List<BSPlayerBrand> playerBrandList = new ArrayList<BSPlayerBrand>();
	List<MajiangUtil> outBrandList = new ArrayList<MajiangUtil>();
	List<Integer> autoPlayerList = new ArrayList<Integer>();
	//玩家分数
	int[] playerScores = {0,0,0,0};

	List<List<Integer>> fanlist = new ArrayList<List<Integer>>();
	
	Room room = null;
	
	int nowIndex = -1;//当前出牌玩家
	
	MajiangUtil outBrand = null;//打出的牌
	
	enum gamectrl{
		normal,
		getbrand,//抓牌
		outbrand,//出牌
		gang//是否杠牌
	}
	
	gamectrl nowCtrl = gamectrl.normal;//记录是抓拍还是出牌 0 抓牌 1 出牌 
	int bossid;//庄家id
	
	//玩家操作队列
	List<GamePlayerControl> playerControlQueue = new ArrayList<GamePlayerControl>();
	
	//上一次向玩家下发的消息
	BaseMessage msg = null;
	
	int outCount = 0 ; //出牌次数
	boolean start = false;
	

	String anyBrandId = "";
	
	int ispao = 1;
	
	//r 所属房间
	//bossid 庄家id
	public BSGameLogic(Room r,int bossid,Logger _logger){
		room = r;
		this.bossid = bossid;
		nowIndex = bossid;//庄家开始出牌
		logger = _logger;
		
		for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i ++){
			fanlist.add(new ArrayList<Integer>());
		}
	}
	
	public void Start(int ispao){
		this.ispao = ispao;
		logger.info("********************************************************************\n"
				+ "********************************************************************\n"
				+ "**************************  NEW GAME  *************************\n"
				+ "********************************************************************\n"
				+ "********************************************************************\n");
		logger.info("nowIndex : " + nowIndex);
		List<MajiangData> dataList = GameDataManager.majiangDataManager.getDataList();
		
		for(int i = 0 ; i < dataList.size(); i ++){
			for(int j = 0 ; j < GameConst.ONE_BRAND_COUNT ; j ++){
				majiangList.add(dataList.get(i));
			}
		}
		FlushBrand();
		
		//发牌
		StartPollBrand();
//		TestStartPollBrand();
		//发送数据

		for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i ++){
			InGameStartInfo resp = new InGameStartInfo(playerBrandList.get(i).brandList,i,bossid);
			logger.info("resp : " + JSON.toJSONString(resp));
			this.SendMessageToPlayer(resp, i);
		}
		
		start = true;
		GetBrand();
	}
	
	//发牌
	public void StartPollBrand(){
		for(int i = 0 ; i < 13 ; i++){
			for(int j = 0 ; j < GameConst.MAX_PLAYER_COUNT ; j ++){
				PollBrand(j);
			}
		}
	}

	public void TestStartPollBrand(){
		for(int i = 1 ; i < 10 ; i ++){
			MajiangData m1 = new MajiangData();
			m1.setId("0");
			majiangList.add(0, m1);
		}
		
//		m1 = new MajiangData();
//		m1.setId("14");
//		majiangList.add(1, m1);
		String[] list1 = {
		        "0",
		        "0",
		        "0",
		        "6",
		        "7",
		        "8",
		        "12",
		        "13",
		        "14",
		        "16",
		        "16",
		        "16",
		        "9"};
		String[] list2 = list1;
//		String[] list2 = {
//		        "14",
//		        "14",
//		        "14",
//		        "2",
//		        "3",
//		        "12",
//		        "13"};
		
		for(int i = 0 ; i < list2.length ; i ++){
			for(int j = 0 ; j < GameConst.MAX_PLAYER_COUNT ; j ++){
				if(j == 2){
					if(i >= list2.length){
						continue;
					}
					MajiangUtil m = new MajiangUtil();
					m.setBrandid(list2[i]);
					m.setGetPlayer(j);
					playerBrandList.get(j).AddBrand(m);
				}else{
					if(i >= list1.length){
						continue;
					}
					MajiangUtil m = new MajiangUtil();
					m.setBrandid(list1[i]);
					m.setGetPlayer(j);
					playerBrandList.get(j).AddBrand(m);
				}
			}
		}
		//playerBrandList.get(0).AddChaTest("30");
		
		
//		for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i ++){
//			for(int j = 0 ; j < 2 ; j ++){
//				MajiangUtil m = new MajiangUtil();
//				m.setBrandid("1");
//				m.setGetPlayer(i);
//				playerBrandList.get(i).AddBrand(m);
//			}
//		}
//		for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i ++){
//			for(int j = 0 ; j < 2 ; j ++){
//				MajiangUtil m = new MajiangUtil();
//				m.setBrandid("1");
//				m.setGetPlayer(i);
//				playerBrandList.get(i).AddBrand(m);
//			}
//		}
//
//		for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i ++){
//			for(int j = 0 ; j < 3 ; j ++){
//				MajiangUtil m = new MajiangUtil();
//				m.setBrandid("2");
//				m.setGetPlayer(i);
//				playerBrandList.get(i).AddBrand(m);
//			}
//		}
	}
	//获取玩家索引
	public int GetPlayerIndex(PlayerManager pm){
		return GetPlayerIndexByUid(pm.getPd().getUid());
	}
	public int GetPlayerIndexByUid(long uid){
		for(int i = 0 ; i < playerList.size() ; i ++){
			if(uid == playerList.get(i)){
				return i;
			}
		}
		return -1;
	}
	//添加玩家
	public boolean AddPlayer(Long uid){
		
		if( playerList.size() >= GameConst.MAX_PLAYER_COUNT ){
			return false;
		}
		playerList.add(uid);
		
		BSPlayerBrand pb = new BSPlayerBrand(anyBrandId);
		playerBrandList.add(pb);
		pb.logger = logger;
		return true;
	}
	
	//发出一张牌
	MajiangUtil PollBrand(int playerindex){
		if(start)logger.info("=======================PollBrand=======================");
		if(majiangList.size() <= 0) {
			this.GameOver(-1);
			return null;
		}
		
		MajiangUtil m = new MajiangUtil();
		m.setBrandid(majiangList.get(0).getId());
		m.setGetPlayer(playerindex);
		playerBrandList.get(playerindex).AddBrand(m);
		if(start)logger.info("m : " + JSON.toJSONString(majiangList.get(0)));
		
		majiangList.remove(0);

		SendInfo();
		return m;
	}
	
	void SendInfo(){
		if(!start) return;
		
		//下发数据
		InGameFlushInfoResp resp = new InGameFlushInfoResp();
		resp.brandCount = majiangList.size();
		for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i ++){
			resp.playerBCount[i] = playerBrandList.get(i).GetBrandCount();
		}
		
		this.SendMessageToAllPlayer(resp);
	}
	
	public void SetReinInfo(GameReinroomResp resp){
		resp.brandCount = majiangList.size();
		resp.bossid = this.bossid;
		resp.index = this.nowIndex;
		for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i ++){
			resp.playerBCount[i] = playerBrandList.get(i).GetBrandCount();
			resp.playerTing[i] = playerBrandList.get(i).GetIsTing() ? 1 : 0 ;
		}
	}
	
	
	//洗牌
	void FlushBrand(){
		for(int i = 0 ; i < majiangList.size() - 1 ; i ++){
			MajiangData now = majiangList.get(i);
			int changeIndex = (int) (i+1 + Math.random() * (majiangList.size() - (i + 1)));
			majiangList.set(i, majiangList.get(changeIndex));
			majiangList.set(changeIndex, now);
		}
		
		//减去21张牌
		majiangList = majiangList.subList(0, majiangList.size() - 21);
	}
	
	//出牌
	public boolean OutBrand(int playerindex,String bid){
		logger.info("==============出牌===============");
		if(playerindex < 0 || playerindex >= playerBrandList.size()  ){

			return false;
		}
		if(nowIndex != playerindex){
			logger.info("没有轮到此玩家出牌 ：" + playerindex);
			return false;
		}
		
//		for(int i = 0 ; i < playerBrandList.size() ; i ++){
//			logger.info("========" + i + "=======");
//			playerBrandList.get(i).Log();
//		}
		playerBrandList.get(playerindex).Log();
		MajiangUtil _outBrand = playerBrandList.get(playerindex).OutBrand(bid);
		if(_outBrand == null){
			logger.info("玩家没有这张牌 ：" + bid);
			return false;
		}

		outBrand = _outBrand;
		outBrandList.add(outBrand);
		
		SendInfo();
		outCount++;
		//＝＝＝＝下发数据＝＝＝＝
		//广播打出的牌
		this.SendMessageToAllPlayer(new InGameOutBrandResp( playerList.get(playerindex), outBrand ));

		if(playerBrandList.get(nowIndex).IsCanTing()){
			this.AddPlayerControlTing(nowIndex);
		}
		
		//检测是否有玩家胡牌或者吃牌 叉牌
		CheckHu(outBrand);
		isgangpao = false;
		//如果有人可以胡牌
		nowCtrl = gamectrl.outbrand;
		if(playerControlQueue.size() > 0){
			EnablePlayerControl();
			if(isgang){
				isgangpao = true;
			}
			return true;
		}
		isgang = false;
		
		//下一个玩家
		NextRound(0);
		
//		
//		List<String> ganglist = playerBrandList.get(nowIndex).IsGang();
//		if(ganglist.size() > 0){
//			nowCtrl = gamectrl.gang;
//			String val = "";
//			for(int i = 0 ; i < ganglist.size() ; i ++){
//				val += ganglist.get(i);
//				if(i < ganglist.size()-1) val += ",";
//			}
//			this.AddPlayerControlGang(nowIndex, enCtrlType.backgang, val);
//			EnablePlayerControl();
//			return true;
//		}
		//抓牌
		GetBrand();
		return true;
	}
	
	GamePlayerControl lastpc ; 
	
	public void ClearPlayerControlQueue(){
		playerControlQueue.clear();
		lastpc = null;
	}
	//
	public void EnablePlayerControl(){
		lastpc = playerControlQueue.get(0);
		playerControlQueue.remove(0);
		SendPC(lastpc);
	}
	
	public void SendPC(GamePlayerControl pc){
		logger.info("=============Send Player Control============");
		logger.info("PlayerControl : " + JSON.toJSONString(pc));
		//通知客户端叉牌胡牌吃牌
		PlayerControlResp resp = new PlayerControlResp();
		resp.pc = pc;
		
		this.SendMessageToAllPlayer(resp);		
	}
	
	//胡牌
	public boolean Hu(int playerindex){
		logger.info("=============胡牌============");
		if(playerindex < 0 || playerindex >= playerBrandList.size()  ){
			logger.info("****[Hu] Can`t find player index : " + playerindex);
			return false;
		}

		//boolean ishu = playerBrandList.get(playerindex).IsHu( nowCtrl == gamectrl.getbrand ? null : outBrand , nowCtrl == gamectrl.getbrand);
		boolean ishu = playerBrandList.get(playerindex).isIshu();
		ClearPlayerControlQueue();
		
		//和了
		if(ishu){
			if(nowCtrl == gamectrl.outbrand){
				playerBrandList.get(playerindex).AddBrand(outBrand);
				
				outBrandList.remove(outBrand);
				//通知客户端获得的牌
				InGameGetBrandResp resp = new InGameGetBrandResp(outBrand);
				this.SendMessageToPlayer(resp, playerindex);
			}
			GameOver(playerindex);
		}else{
			playerBrandList.get(playerindex).Log();
			logger.info("*****no hu!!!"); 
		}
		return ishu;
	}
	
	//过
	public void Pass(int playerindex){
		logger.info("=============过============");
		//是否有操作队列
		if(playerControlQueue.size() > 0){
			EnablePlayerControl();
			return;
		}
		isganghu = false;
		isgangpao = false;
		isgang = false;
		
		//已经抓完牌
		if(nowCtrl == gamectrl.getbrand){
			//通知客户端出牌
			SendBaseMessageToAllPlayer(GameProtocol.GAME_CAN_OUT_BRAND,nowIndex);
		}else if(nowCtrl == gamectrl.gang){
			GetBrand();
		}else{
			//抓牌
			NextRound(0);
			GetBrand();
		}
		
	}
	
	//杠牌
	public boolean GangBrand(int playerindex,String bid){
		if(playerindex < 0 || playerindex >= playerBrandList.size()  ){
			logger.info("****IsCha Cant find player index : " + playerindex);
			return false;
		}

		BSPlayerBrand brands = playerBrandList.get(playerindex);

		ClearPlayerControlQueue();
		
		if(nowCtrl == gamectrl.outbrand){
			brands.AddBrand(outBrand);
			outBrandList.remove(outBrand);
			outBrand.eatPlayer = playerindex;
		}

		OpenBrand ob = brands.Gang(bid);

		if(nowCtrl == gamectrl.outbrand){
			ob.type = OpenType.gang;
		}
		if(ob == null){
			logger.info("gangList is null !!!");
			return false;
		}

		//应该当前玩家出牌
		nowIndex = playerindex;
		
		int score = 4;
		if(ob.type == OpenType.gang){
			score = 2;
		} 
		
		//大杠
		if(bid.equals("0") || bid.equals("9") || bid.equals("18") ){
			score = 10;
			if(ob.type == OpenType.gang){
				score = 5;
			} 
		}
		logger.info("score : " + score);
		//加分
		for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i++){
			if(i == playerindex){
				this.playerScores[i] += score * (GameConst.MAX_PLAYER_COUNT - 1);
			}else{
				this.playerScores[i] -= score ;
			}
		}
		
		//广播杠牌成功
		InGameEatResp resp = new InGameEatResp(playerindex,ob.list);
		resp.SetProtocol(GameProtocol.GAME_GANG);
		this.SendMessageToAllPlayer(resp);

		isgang = true;
		//抓一张牌
		GetBrand();
		
		return true;
	}
	//是不是杠抓牌
	boolean isgang = false;
	//是不是杠抓来的牌胡的（杠开）
	boolean isganghu = false;
	boolean isgangpao = false;
	//吃牌
	public boolean EatBrand(int playerindex,String bid1,String bid2){
		if(playerindex < 0 || playerindex >= playerBrandList.size()  ){
			logger.info("****IsCha Cant find player index : " + playerindex);
			return false;
		}

		BSPlayerBrand brands = playerBrandList.get(playerindex);
		List<MajiangUtil> eatList = brands.EatBrand(bid1, bid2, outBrand);

		if(eatList == null){
			SendPC(lastpc);
			return false;
		}
		
		outBrandList.remove(outBrand);
		
		ClearPlayerControlQueue();
		
		outBrand.eatPlayer = playerindex;
		nowIndex = playerindex;
		//广播吃牌成功
		InGameEatResp resp = new InGameEatResp(playerindex,eatList);
		resp.SetProtocol(GameProtocol.GAME_EAT);
		this.SendMessageToAllPlayer(resp);
		SendInfo();
		
		//通知客户端出牌
		this.SendBaseMessageToAllPlayer(GameProtocol.GAME_CAN_OUT_BRAND, playerindex);
		return true;
	}
	
	//叉牌
	public boolean ChaBrand(int playerindex){
		if(playerindex < 0 || playerindex >= playerBrandList.size()  ){
			logger.info("****IsCha Cant find player index : " + playerindex);
			return false;
		}
		List<MajiangUtil> chalist = playerBrandList.get(playerindex).ChaBrand(outBrand);
		if(chalist == null){
			logger.info("没有可以叉的牌");
			return false;
		}

		outBrandList.remove(outBrand);
		ClearPlayerControlQueue();
		
		//应该当前玩家出牌
		nowIndex = playerindex;
		
		//广播叉牌成功
		InGameEatResp resp = new InGameEatResp(playerindex,chalist);
		resp.SetProtocol(GameProtocol.GAME_CHA);
		this.SendMessageToAllPlayer(resp);
		
		if(chalist.size() >= GameConst.MAX_PLAYER_COUNT){//杠 抓一张牌
			GetBrand();
			return true;
		}
		SendInfo();
		//通知客户端出牌
		this.SendBaseMessageToAllPlayer(GameProtocol.GAME_CAN_OUT_BRAND, playerindex);
		return true;
	}
	//抓牌
	void GetBrand(){
		//下一个玩家
		MajiangUtil m = PollBrand(nowIndex);
		if(m == null){
			return;
		}
		nowCtrl = gamectrl.getbrand;
		

		if(this.autoPlayerList.contains(nowIndex)){
			this.OutBrand(nowIndex, m.brandid);
			return;
		}
		//通知客户端获得的牌
		InGameGetBrandResp resp = new InGameGetBrandResp(m);
		this.SendMessageToPlayer(resp, nowIndex);
		
		//可以胡牌
		if(playerBrandList.get(nowIndex).IsHu(null)){ 
			if(isgang){
				isganghu = true;
			}

			this.AddPlayerControlHu(nowIndex, m.brandid);
		}
//		if(playerBrandList.get(nowIndex).HaveGang(m.brandid)){
//			if(m.brandid.equals(anyBrandId)){
//				this.AddPlayerControlHu(nowIndex, m.brandid);
//			}
//
//			this.AddPlayerControlGang(nowIndex, enCtrlType.backgang, m.brandid);
//		}
		List<String> ganglist = playerBrandList.get(nowIndex).IsGang();
		if(ganglist.size() > 0){
			//nowCtrl = gamectrl.gang;
			String val = "";
			for(int i = 0 ; i < ganglist.size() ; i ++){
				val += ganglist.get(i);
				if(i < ganglist.size()-1) val += ",";
			}
			this.AddPlayerControlGang(nowIndex, enCtrlType.backgang, val);
		}
		
		if(playerControlQueue.size() > 0){
			EnablePlayerControl();
			return;
		}
		
		//可以出牌
		this.SendBaseMessageToAllPlayer(GameProtocol.GAME_CAN_OUT_BRAND, nowIndex);
	}
	//听牌
	public void Ting(){
		logger.info("ting!!!!");
		playerBrandList.get(nowIndex).Ting();
		//广播听牌成功
		InGameTingResp resp = new InGameTingResp();
		resp.index = this.nowIndex;
		resp.SetProtocol(GameProtocol.GAME_TING);
		this.SendMessageToAllPlayer(resp);

		if(playerControlQueue.size() > 0){
			EnablePlayerControl();
			return;
		}
		
		NextRound(0);
		GetBrand();
	}
	
	//是否和牌
	void CheckHu(MajiangUtil m){
		logger.info("================CheckHu===============");
		
		//遍历所有玩家 检测是否可以胡牌
		
		for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT - 1 ; i ++){
			logger.info("--------"+i+"-------");
			int index = nowIndex + i + 1 ;
			if(index >= GameConst.MAX_PLAYER_COUNT) index -= GameConst.MAX_PLAYER_COUNT;
			if(this.autoPlayerList.contains(index)){
				continue;
			}
			logger.info("index : " + index);
			if(playerBrandList.get(index).IsHu(m)){
				this.AddPlayerControlHu(index, outBrand.brandid);
			}
		}
		//全体玩家是否可以叉牌
		for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT - 1 ; i ++){
			int index = nowIndex + i + 1 ;
			if(index >= GameConst.MAX_PLAYER_COUNT) index -= GameConst.MAX_PLAYER_COUNT;

			if(this.autoPlayerList.contains(index) || playerBrandList.get(index).GetIsTing()){
				continue;
			}
			List<MajiangUtil> eatList = playerBrandList.get(index).IsCha(outBrand);
			if(eatList.size() >= 2){
				//通知客户端可以叉牌
				this.AddPlayerControlCha(index, outBrand.brandid);
				continue;
			}
		}
		
		//下家是否可以吃牌
		int islink = GameDataManager.majiangDataManager.GetData(outBrand.getBrandid()).getIslink();
		if(islink != 0 ){
			//下家索引
			int nextindex = nowIndex + 1;
			if(nextindex >= GameConst.MAX_PLAYER_COUNT){
				nextindex = 0;
			}

			if(!this.autoPlayerList.contains(nextindex) && !playerBrandList.get(nextindex).GetIsTing()){
				List<List<String>> eatList = playerBrandList.get(nextindex).IsEat(outBrand);
				if(eatList.size() > 0){
					this.AddPlayerControlChi(nextindex, outBrand.brandid, eatList);
				}
			}
			
		}

		//是否可以杠
		for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT - 1 ; i ++){
			int index = nowIndex + i +1 ;
			if(index >= GameConst.MAX_PLAYER_COUNT) index -= GameConst.MAX_PLAYER_COUNT;

			if(this.autoPlayerList.contains(index) || playerBrandList.get(index).GetIsTing()){
				continue;
			}
			//是否可以杠
			if(playerBrandList.get(index).IsGang(outBrand.getBrandid())){
				this.AddPlayerControlGang(index, enCtrlType.gang, outBrand.getBrandid());
			}
		}

	}

	public void AddPlayerControlTing(int index){
		AddPlayerControl(index,enCtrlType.ting,null,null,null,null,null);
	}

	public void AddPlayerControlChi(int index,String chi,List<List<String>> eatlist){
		AddPlayerControl(index,enCtrlType.chi,chi,null,null,null,eatlist);
	}

	public void AddPlayerControlCha(int index,String cha){
		AddPlayerControl(index,enCtrlType.cha,null,cha,null,null,null);
	}

	public void AddPlayerControlGang(int index,enCtrlType type,String gang){
		AddPlayerControl(index,type,null,null,gang,null,null);
	}

	public void AddPlayerControlHu(int index,String hu){
		AddPlayerControl(index,enCtrlType.hu,null,null,null,hu,null);
	}
	
	public void AddPlayerControl(int index,enCtrlType type,String chi,String cha,String gang,String hu,List<List<String>> eatlist){
		GamePlayerControl pc = null;
		for(int i = 0 ; i < playerControlQueue.size() ;  i++){
			GamePlayerControl p = playerControlQueue.get(i);
			if(p.playerIndex == index){
				pc = p;
				break;
			}
		}
		if(pc == null){
			pc = new GamePlayerControl();
			pc.playerIndex = index;
			playerControlQueue.add(pc);
		}

		pc.AddType(type);
		if(eatlist != null ) pc.eatList = eatlist;
		if(chi != null && !chi.equals("")) pc.chi = chi;
		if(cha != null && !cha.equals("")) pc.cha = cha;
		if(gang != null && !gang.equals("")) pc.gang = gang;
		if(hu != null && !hu.equals("")) pc.hu = hu;
	}
	
	//下一个回合
	void NextRound(int count){
//		if(count >= 3){
//			return;
//		}
		nowIndex ++;
		if(nowIndex >= GameConst.MAX_PLAYER_COUNT){
			nowIndex = 0;
		}
//		if(autoPlayerList.contains(nowIndex)){
//			NextRound(count + 1);
//		}
	}
	
	void GameOver(int winPlayer){
		GameResult r = new GameResult();
		r.winPlayer = winPlayer;
		
		//流局
		if(winPlayer == -1){
			for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i++){
				if(i != this.bossid) {
					playerScores[i] += 3;
				}else{
					playerScores[i] -= ((GameConst.MAX_PLAYER_COUNT - 1) * 3 );
				}
			}
			
			r.playerScores = playerScores;
			
			InGameOverResp resp = new InGameOverResp(r);
			
			SendMessageToAllPlayer(resp);
			this.room.GameOver(r);
			return;
		}
		
		int fan[] = {0,0,0,0};
		//整理数组
		List<List<Integer>> resultMap = new ArrayList<List<Integer>>();
		List<List<Integer>> openMap = new ArrayList<List<Integer>>();
		//把开门的牌加到结果数组
		if(winPlayer != -1){
			resultMap = playerBrandList.get(winPlayer).resultMap;
			r.brand = playerBrandList.get(winPlayer).getLastBrand().brandid;
			playerBrandList.get(winPlayer).AddOpenBrandToResultArray(openMap);
		}
		resultMap.addAll(openMap);
		//把索引换成id
		for(int i = 0 ; i < resultMap.size() ;i ++){
			List<Integer> arr = resultMap.get(i);
			for(int j = 0 ; j < arr.size() ; j ++){
				if(arr.get(j) < 100) continue;
				int index = GameDataManager.majiangDataManager.GetIdByIndex(101+arr.get(j));
				arr.set(j, index);
			}
		}
		
//		while(resultMap.get(0).size() > 0){
//			for(int i = 0 ; i < resultMap.size() ;i ++){
//				List<Integer> arr = resultMap.get(i);
//				for(int j = 0 ; j < arr.size() ; j ++){
//					if(arr.get(j) == resultMap.get(0).get(0)){
//						logger.info("change : " + arr.get(j) );
//						arr.set(j, Integer.parseInt(this.anyBrandId));
//						break;
//					}
//				}
//			}
//			resultMap.get(0).remove(0);
//		}

		
		if(nowCtrl == gamectrl.outbrand){
			//点炮
			r.badPlayer = outBrand.getGetPlayer();
			fanlist.get(r.badPlayer).add(BSHutype.pao.ordinal());
			fan[r.badPlayer]++;
		}else{
			//自摸
			fanlist.get(winPlayer).add(BSHutype.selfget.ordinal());
			for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i++){
				if(i == winPlayer) continue;
				fan[i]++;
			}
		}
		
		//		庄胡
		if(winPlayer == this.bossid ){
			fanlist.get(winPlayer).add(BSHutype.bosshu.ordinal());
			for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i++){
				if(i == winPlayer) continue;
				fan[i]++;
			}
		}else{
			fanlist.get(this.bossid).add(BSHutype.badbosshu.ordinal());
			fan[this.bossid]++;
		}
		
		//		手把一

		//赢家手中只有2张牌
		boolean winplayeronlyone = (this.playerBrandList.get(winPlayer).GetBrandCount() <= 2);
		if(winplayeronlyone) fanlist.get(winPlayer).add(BSHutype.only.ordinal());
		for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i++){
			if(winplayeronlyone){
				if(i != winPlayer) fan[i]++;
			}
			if(i == winPlayer) continue;
			if(this.playerBrandList.get(i).GetBrandCount() <= 1){
				fanlist.get(i).add(BSHutype.badonly.ordinal());
				fan[i]++;
			}
		}
		
		//		站立胡

		boolean winplayestand = !(this.playerBrandList.get(winPlayer).IsOpen());
		boolean isAllStand = true;
		for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i++){
			if(i == winPlayer) continue;
			if(this.playerBrandList.get(i).IsOpen()){
				isAllStand = false;
			}
		}
		
		//三家清
		if(isAllStand){
			//四家清
			if(winplayestand){
				fanlist.get(winPlayer).add(BSHutype.fourclear.ordinal());
				for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i++){
					if(i == winPlayer) continue;
					fan[i]++;
				}
			}else{
				fanlist.get(winPlayer).add(BSHutype.threeclear.ordinal());
				for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i++){
					if(i == winPlayer) continue;
					fan[i]++;
				}
			}
		}else{
			if(winplayestand) fanlist.get(winPlayer).add(BSHutype.stand.ordinal());
			for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i++){
				if(winplayestand){
					if(i != winPlayer) fan[i]++;
				}

				if(i == winPlayer) continue;
				if(!this.playerBrandList.get(i).IsOpen()){
					fanlist.get(i).add(BSHutype.badstand.ordinal());
					fan[i]++;
				}
			}
		}
		//		一般高
		boolean isSameHigh = GameTools.FindSameList(resultMap);
		if(isSameHigh) fanlist.get(winPlayer).add(BSHutype.samehight.ordinal());
		for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i++){
			if(isSameHigh){
				if(i != winPlayer) fan[i]++;
			}
			if(i == winPlayer) continue;
			List<List<Integer>> _openMap = new ArrayList<List<Integer>>();
			playerBrandList.get(i).AddOpenBrandToResultArray(_openMap);
			if(GameTools.FindSameList(_openMap)){
				fanlist.get(i).add(BSHutype.badsamehight.ordinal());
				fan[i]++;
			}
		}
		
		//		四归一
		boolean isFindFour = GameTools.FindFourCount(resultMap);
		if(isFindFour) fanlist.get(winPlayer).add(BSHutype.fourtoone.ordinal());
		for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i++){
			if(isFindFour){
				if(i != winPlayer) fan[i]++;
			}
			if(i == winPlayer) continue;
			List<List<Integer>> _openMap = new ArrayList<List<Integer>>();
			playerBrandList.get(i).AddOpenBrandToResultArray(_openMap);
			if(GameTools.FindFourCount(_openMap)){
				fanlist.get(i).add(BSHutype.badfourtoone.ordinal());
				fan[i]++;
			}
		}
		
		//		一个听
		if(playerBrandList.get(winPlayer).isTing){
			fanlist.get(winPlayer).add(BSHutype.ting.ordinal());
		}
		for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i++){
			if(playerBrandList.get(winPlayer).isTing){
				if(i != winPlayer) fan[i]++;
			}
			if(i == winPlayer) continue;
			if(playerBrandList.get(i).isTing){
				fanlist.get(i).add(BSHutype.badting.ordinal());
				fan[i]++;
			}
		}
		
		//		杠后开
		if(isganghu){
			for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i++){
				fanlist.get(i).add(BSHutype.ganglast.ordinal());
				if(i == winPlayer) continue;
				fan[i]++;
			}
		}
		//		杠后炮
		if(isgangpao){
			fanlist.get(r.badPlayer).add(BSHutype.gangpao.ordinal());
			fan[r.badPlayer]++;
		}
		//		对对胡
		boolean isdoublehu = true;
		logger.info("resultMap : " + JSON.toJSONString(resultMap));
		for(int i = 0 ; i < resultMap.size() ; i ++){
			List<Integer> list = resultMap.get(i);
			if(list.size()>=3 && list.get(0) != list.get(1)){
				isdoublehu = false;
				break;
			}
		}
		if(isdoublehu){
			fanlist.get(winPlayer).add(BSHutype.doublehu.ordinal());
			for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i++){
				if(i == winPlayer) continue;
				fan[i]+=2;
			}
		}

		int[] color = {0,0,0,0,0};
		int colortype = 0;
		for(int i = 0 ; i < resultMap.size() ; i ++){
			List<Integer> list = resultMap.get(i);
			for(int j = 0 ; j < list.size() ; j ++){
				int index = GameDataManager.majiangDataManager.GetData(list.get(j)+"").getIndex();
				int x = index/100 - 1;
				if(color[x] == 0) color[x]++;
			}
		}
		
		if(color[0] + color[1] + color[2] > 1){
			colortype = 0;
		}else {
			if(color[3] +color[4] >= 1){
				colortype = 1;
			}else{
				colortype = 2;
			}
		}
		
		//		混一色
		if(colortype == 1){
			fanlist.get(winPlayer).add(BSHutype.doublecolor.ordinal());
			for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i++){
				if(i == winPlayer) continue;
				fan[i]+=2;
			}
		}
		
		//		清一色
		if(colortype == 2){
			fanlist.get(winPlayer).add(BSHutype.onlycolor.ordinal());
			for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i++){
				if(i == winPlayer) continue;
				fan[i]+=3;
			}
		}
		//		七小对
		if(playerBrandList.get(winPlayer).isSevenDouble){
			fanlist.get(winPlayer).add(BSHutype.alldouble.ordinal());
			for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i++){
				if(i == winPlayer) continue;
				fan[i]+=3;
			}
		}
		
		//		十三幺

		if(playerBrandList.get(winPlayer).allone){
			fanlist.get(winPlayer).add(BSHutype.onenine.ordinal());
			for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i++){
				if(i == winPlayer) continue;
				playerScores[i] -= 50;
			}
			playerScores[winPlayer] += 150;
		}
		logger.info("fanlist : " + JSON.toJSONString(fanlist));
		logger.info("playerScores 1: " + JSON.toJSONString(playerScores));
		//结算分数
		for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i++){
			if(i == winPlayer) continue;
			
			int fancount = fan[i];
			if(fancount > GameConst.MAX_FAN_COUNT){
				fancount = GameConst.MAX_FAN_COUNT;
			}
			int s = (1 << fancount);

			playerScores[winPlayer] += s;
			if(this.ispao == 1 && r.badPlayer != -1){
				playerScores[r.badPlayer] -= s;
			}else{
				playerScores[i] -= s;
			}
			
//			List<Integer> list = fanlist.get(i);
//			String msg = "";
//			for(int j = 0 ; j < list.size() ; j ++){
//				msg += BSHutype.values()[list.get(j)].GetName() + "/";
//			}
//
//			PlayerManager pm = GamePlayerManager.GetInstance().GetPlayerByUid(playerList.get(i));
//			
//			GameTools.SendMessage(pm.getUi(), msg);
		}
		logger.info("playerScores 2: " + JSON.toJSONString(playerScores));
//		{
//			//无百搭
//			//6.无百搭杠开，收12分/每人，一共36分
//			if(isganghu){
//				r.winType = 6;
//				this.AddScores(winPlayer, 12, -1);
//			}else if(isself){
//				//自摸
//				//2，无百搭自摸收6分/每人，一共18分
//				r.winType = 2;
//				this.AddScores(winPlayer, 6, -1);
//			}else{
//				//点炮 
//				//3.无百搭放茺，点炮人给4分，其他两人2分，一共8分
//				r.winType = 3;
//				for(int i = 0 ; i < GameConst.MAX_PLAYER_COUNT ; i++){
//					if(winPlayer == i){
//						playerScores[i] += 8;
//					}else if(r.badPlayer == i){
//						playerScores[i] -= 4;
//					}else{
//						playerScores[i] -= 2;
//					}
//				}
//			}
//		}
		r.playerScores = playerScores;

		if(fanlist.get(winPlayer).size() <= 0){
			fanlist.get(winPlayer).add(BSHutype.normalhu.ordinal());
		}
		//通知全体玩家 
		InGameOverResp resp = new InGameOverResp(r);
		
		//resp.resultMap = openMap;
		resp.fanlist = fanlist;
		for(int i = 0 ; i < playerBrandList.size() ; i ++){
			resp.playerBrandList.add(playerBrandList.get(i).GetBrandIDList());
			List<List<Integer>> open = new ArrayList<List<Integer>>();
			playerBrandList.get(i).AddOpenBrandToResultArrayHaveType(open);
			resp.resultMap.add(open);
		}
		
		SendMessageToAllPlayer(resp);
		this.room.GameOver(r);
	}
	
	public List<List<OpenBrand>> GetAllPlayerOpenBrand(){
		List<List<OpenBrand>> ret = new ArrayList<List<OpenBrand>>();
		for(int i = 0 ; i < playerBrandList.size() ; i ++){
			ret.add(playerBrandList.get(i).openBrandList);
		}
		
		return ret;
	}
	
	
	public List<String> GetPlayerBrandList(PlayerManager pm){
		int index = this.GetPlayerIndex(pm);
		if(index == -1){
			return new ArrayList<String>();
		}else{
			return playerBrandList.get(index).GetBrandIDList();
		}
	}
	
	//玩家托管
	public void PlayerAuto(long uid){
		int index = this.GetPlayerIndexByUid(uid);
		if(this.autoPlayerList.contains(index)){
			return;
		}
		this.autoPlayerList.add(index);
		
		if(lastpc != null){
			this.Pass(lastpc.playerIndex);
		}else if(nowIndex == index && nowCtrl == gamectrl.getbrand){
			this.OutBrand(nowIndex, playerBrandList.get(index).lastBrand.brandid);
		}
	}
	
	//取消托管
	public void PlayerCancelAuto(PlayerManager pm){
		int index = this.GetPlayerIndex(pm);
		if(!this.autoPlayerList.contains(index)){
			return;
		}
		this.autoPlayerList.remove((Object)index);
	}
	
	void SendMessageToAllPlayer(BaseMessage msg){
		this.msg = msg;
		this.room.SendMessageToAllPlayer(msg);
	}
	
	void SendMessageToPlayer(BaseMessage msg,int playerIndex){
		logger.info("send msg : " + JSON.toJSONString(msg) + "   to player : " + playerIndex);
		
		PlayerManager pm = GamePlayerManager.GetInstance().GetPlayerByUid(playerList.get(playerIndex));
		if(pm == null) return;

		if(pm.getRoomid() != this.room.roomId) return;
		this.msg = msg;
		msg.setUserInfo(pm.getUi());
		msg.Send();
	}
	
	void SendBaseMessageToAllPlayer(int protocol,int playerIndex){
		BaseResp msg = new BaseResp();
		msg.SetProtocol(protocol);
		msg.pindex = playerIndex;
		SendMessageToAllPlayer(msg);
	}
	
	void SendBaseMessage(int protocol,int playerIndex){
		BaseResp msg = new BaseResp();
		msg.SetProtocol(protocol);
		msg.pindex = playerIndex;
		SendMessageToPlayer(msg,playerIndex);
	}
	
	
	
	public List<MajiangUtil> getOutBrandList() {
		return outBrandList;
	}

	public void setOutBrandList(List<MajiangUtil> outBrandList) {
		this.outBrandList = outBrandList;
	}

	//***********************test***************************
    public static void main( String[] args ) throws Exception
    {
		// 游戏配置表数据载入
		GameDataManager.load(GameConst.RES_PATH);
		
    	GameLogic gl = new GameLogic(null,0, GetLog.getLoggerByName("-1", "room"));
    	gl.Start();
    	gl.logger.info(JSON.toJSONString(gl.majiangList));
    }
	
}
