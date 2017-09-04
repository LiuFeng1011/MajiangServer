package com.dreamgear.majiang.game.game;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.dreamgear.majiang.common.GameCommon.OpenType;
import com.dreamgear.majiang.common.GameConst;
import com.dreamgear.majiang.common.GameTools;
import com.dreamgear.majiang.game.data.GameDataManager;
import com.dreamgear.majiang.game.data.MajiangData;
import com.dreamgear.majiang.utils.GetLog;

//玩家手中的牌组
public class PlayerBrand {
	public Logger logger ;

	//手中还站着的牌
	public List<MajiangUtil> brandList = new ArrayList<MajiangUtil>();
	//已经躺下的牌
	public List<OpenBrand> openBrandList = new ArrayList<OpenBrand>();
	
	//0 ： 百搭充当的牌列表
	//1 ：对子牌
	public List<List<Integer>> resultMap = new ArrayList<List<Integer>>();
	
	MajiangUtil lastBrand;
	
	String anyBrandId = "33";
	int anyBrandIndexX;
	int anyBrandIndexY;
	
	boolean ishu = false;
	
	public PlayerBrand(String anyBrandId){
		int anyBrandIndex = GameDataManager.majiangDataManager.GetData(anyBrandId).getIndex();
		anyBrandIndexX = anyBrandIndex/100 - 1;
		anyBrandIndexY = anyBrandIndex%10 - 1;
		this.anyBrandId = anyBrandId;
	}
	
	public void Log(){
		logger.info("brandList : " + JSON.toJSONString(brandList));
	}
	public int GetBrandCount(){
		int ret = 0;
		ret = brandList.size();
//		for(int i = 0 ; i < openBrandList.size() ; i ++){
//			ret += openBrandList.get(i).list.size();
//		}
		return ret;
	}
	
	public List<String> GetBrandIDList(){
		List<String> ret = new ArrayList<String>();
		
		for(int i = 0 ; i < brandList.size() ; i ++){
			ret.add(brandList.get(i).brandid);
		}
		
		return ret;
	} 
	
	public void AddBrand(MajiangUtil m){
		logger.info("AddBrand : " + m.brandid);
		this.setIshu(false);
		brandList.add(m);
		lastBrand = m;
	}
	public void RemoveBrand(MajiangUtil m){
		brandList.remove(m);
	}
	public MajiangUtil OutBrand(String bid){
		logger.info("OutBrand");
		this.setIshu(false);
		int index = -1;
		for(int i = 0 ; i < brandList.size() ; i ++){
			MajiangUtil b = brandList.get(i);
			if(b.brandid.equals(bid)){
				index = i;
				break;
			}
		}
		if(index == -1){
			logger.info("brandList : " + JSON.toJSONString(brandList));
			logger.info("********Cant fin brand : " + bid);
			return null;
		}
		MajiangUtil ret = brandList.get(index);
		brandList.remove(index);
		return ret;
	}
	
	
	//是否有百搭
	public boolean IsHaveAnyBrand(){

		for(int i = 0 ; i < brandList.size() ; i++){
			if(brandList.get(i).brandid.equals(anyBrandId)){
				return true;
			}
		}
		
		return false;
	}
	//手中是否有杠
	public boolean HaveGang(String bid){
		if("".equals(bid)){
			return false;
		}
		logger.info("HaveGang bid : " + bid + "   openBrandList : " + JSON.toJSONString(openBrandList));
		for(int i = 0 ; i < openBrandList.size() ; i ++){
			if(openBrandList.get(i).type == OpenType.cha && openBrandList.get(i).list.get(0).brandid.equals(bid)){
				return true;
			}
			//ret += openBrandList.get(i).list.size();
		}
		//logger.info("brandList : " + JSON.toJSONString(brandList));
		int count = 0;
		for(int i = 0 ; i < brandList.size() ; i++){
			if(brandList.get(i).brandid.equals(bid)){
				count ++;
			}
		}
		//logger.info("count : " + count);
		if(count >= 4){
			return true;
		}
		
		
		return false;
	}
	
	//是否可以杠
	public boolean IsGang(String bid){
		int count = 0;
		for(int i = 0 ; i < brandList.size() ; i++){
			if(bid.equals(brandList.get(i).getBrandid())){
				count++;
			}
		}
		if(count >= 3){
			if(this.anyBrandId.equals(bid)){
				this.setIshu(true);
			}
			return true;
		}
		return false;
	}
	
	public List<String> IsGang(){
		List<String> ganglist = new ArrayList<String>();
		int[][] brandMap = {
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0}};
		
		for(int i = 0 ; i < brandList.size() ; i++){
			String bid = brandList.get(i).brandid;
			if(bid.equals(this.anyBrandId)) continue;
			MajiangData data = GameDataManager.majiangDataManager.GetData(bid);
			int index = data.getIndex();
			int x = index/100 - 1;
			int y = index%10 - 1;
			brandMap[x][y] ++;
			if(brandMap[x][y] >= 4){
				brandMap[x][y] = 0;
				ganglist.add(data.getId());
			}
			
			for(int j = 0 ; j < openBrandList.size() ; j ++){
				if(openBrandList.get(j).type == OpenType.cha && openBrandList.get(j).list.get(0).brandid.equals(bid)){
					ganglist.add(bid);
					break;
				}
			}
		}
		
		return ganglist;
	}

	public boolean IsQiangHu(MajiangUtil m){
		return IsHu(m,true,true);
	}
	public boolean IsHu(MajiangUtil m,boolean isself){
		return IsHu(m,isself,false);
	}
	//isself 是不是自己抓的牌
	public boolean IsHu(MajiangUtil m,boolean isself,boolean isqianghu){
		//白板直接胡
		if((!isqianghu && m == null) || (m != null && m.brandid.equals(this.anyBrandId) )){
			if(isself ){
				if(this.HaveGang(this.anyBrandId)){
					this.setIshu(true);
					return true;
				}
			}else if(IsGang(this.anyBrandId)){
				this.setIshu(true);
				return true;
			}
		}
		
		int[][] brandMap = {
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0}};
		
		resultMap.clear();
		resultMap.add(new ArrayList<Integer>());//记录百搭都用在什么地方
		resultMap.add(new ArrayList<Integer>());//记录头（双）
		
		
		for(int i = 0 ; i < brandList.size() ; i++){
			int index = GameDataManager.majiangDataManager.GetData(brandList.get(i).brandid).getIndex();
			brandMap[index/100 - 1][index%10 - 1] ++;
		}
		if(m != null){
			int index = GameDataManager.majiangDataManager.GetData(m.brandid).getIndex();
			brandMap[index/100 - 1][index%10 - 1] ++;
		}

		//爆头
		if(isself && brandMap[anyBrandIndexX][anyBrandIndexY] > 0){
			brandMap[anyBrandIndexX][anyBrandIndexY] --;//白板减掉
			int index = GameDataManager.majiangDataManager.GetData(this.lastBrand.brandid).getIndex();
			
			int bix = index/100 - 1;
			int biy = index%10 - 1;
			brandMap[bix][biy] --;//最后抓来的牌减掉
			
			boolean ishu = Hu(0,0,brandMap,true);
			
			//如果可以胡 ，说明爆头
			if(ishu){
				resultMap.get(0).add(bix*100+biy);//设置白板替换的牌
				//设置对子
				AddResult(bix*100+biy,2);
				
				this.setIshu(true);
				return true;
			}
			brandMap[bix][biy] ++;
			brandMap[anyBrandIndexX][anyBrandIndexY] ++;
		}
		
		if(!isself && brandMap[anyBrandIndexX][anyBrandIndexY] > 0){
			return false;
		}
		logger.info("brandMap : " + JSON.toJSONString(brandMap));
		
		this.setIshu(Hu(0,0,brandMap,false));
		return this.isIshu();
	}

	//当前牌能不能胡
	boolean Hu(int _i,int _j,int[][] brandMap,boolean isDouble){
		//logger.info("brandMap : i:" + _i  + "  j:" + _j + "    " + JSON.toJSONString(brandMap));
		int allBrandCount = 0;//记录剩余牌的数量
		for(int i = _i ; i < 5 ; i ++){//5个大类
			for(int j = _j ; j < 9 ; j ++){//每个大类最多9个小类
				if(i == anyBrandIndexX && j == anyBrandIndexY) continue;
				
				int count = brandMap[i][j];
				
				if(count == 0 ) {
					boolean ishu = UseAnyBrand(i,j,count,brandMap,isDouble);
					if(ishu){
						return true;
					}
					continue;
				}
				
				allBrandCount += (count);
				
//				if(count >= 4){
//					brandMap[i][j] -= 4;
//					
//					//当四张牌的时候能不能胡
//					boolean ishu = Hu(i,j,brandMap,isDouble);
//					if(ishu) {
//						AddResult(i*100+j,4);
//						return true;
//						}
//					brandMap[i][j] = count;
//				}
				if(count >= 3){
					brandMap[i][j] -= 3;
					boolean ishu = Hu(i,j,brandMap,isDouble);
					if(ishu) {
						AddResult(i*100+j,3);
						return true;
						}
					brandMap[i][j] = count;
				}
				
				if(!isDouble && count >= 2){
					brandMap[i][j] -= 2;
					isDouble = true;
					boolean ishu = Hu(i,j,brandMap,isDouble);
					if(ishu) {
						AddResult(i*100+j,2);
						return true;
						}
					isDouble = false;
					brandMap[i][j] = count;
				}
				
				//顺子
				if(count >= 1 && j< 7 && i <= 2){
					//是否有后两张牌
					boolean use1 = false;
					boolean use2 = false;
					
					if(brandMap[i][j + 1] <= 0 && 
							brandMap[anyBrandIndexX][anyBrandIndexY] >= 1){
						brandMap[i][j + 1] ++;
						brandMap[anyBrandIndexX][anyBrandIndexY] --;
						use1 = true;
					}

					if(brandMap[i][j + 2] <= 0 && 
							brandMap[anyBrandIndexX][anyBrandIndexY] >= 1){
						brandMap[i][j + 2] ++;
						brandMap[anyBrandIndexX][anyBrandIndexY] --;
						use2 = true;
					}
					
					
					if(brandMap[i][j + 1] > 0 && brandMap[i][j + 2] > 0 ){
						brandMap[i][j] 		-= 1;
						brandMap[i][j + 1]	-= 1;
						brandMap[i][j + 2]	-= 1;

						boolean ishu = Hu(i,j,brandMap,isDouble);
						if(ishu) {
							List<Integer> list = new ArrayList<Integer>();
							list.add(i*100 + j);
							list.add(i*100 + j + 1);
							list.add(i*100 + j + 2);
							resultMap.add(list);
							if(use1){
								resultMap.get(0).add(i*100+j+1);
							}
							if(use2){
								resultMap.get(0).add(i*100+j+2);
							}
							return true;
						}
						
						brandMap[i][j] 		+= 1;
						brandMap[i][j + 1]	+= 1;
						brandMap[i][j + 2]	+= 1;
					}
					
					if(use1){
						brandMap[i][j + 1] --;
						brandMap[anyBrandIndexX][anyBrandIndexY] ++;
					}
					if(use2){
						brandMap[i][j + 2] --;
						brandMap[anyBrandIndexX][anyBrandIndexY] ++;
					}
				}
				
				return UseAnyBrand(i,j,count,brandMap,isDouble);
			}
			_j = 0;
		}
		
		//所有牌都组合上的话 说明和了
		if(allBrandCount == 0){
			return true;
		}
		return false;
	}
	
	boolean UseAnyBrand(int i,int j,int count,int[][] brandMap,boolean isDouble){
		int anyCount = brandMap[anyBrandIndexX][anyBrandIndexY];
		//logger.info("anyCount : " + anyCount + "   anyBrandIndexX : " + anyBrandIndexX + "   anyBrandIndexY : " + anyBrandIndexY);
		//百搭
		//使用几个原牌
		for(int k = 0 ; k <= 4 ; k ++){
			if(count < k){
				break;
			}
			//使用几个百搭
			for(int x = 1 ; x+k < 4 ; x ++){
				if(anyCount < x){//原牌数量一定要足够
					break;
				}

				brandMap[anyBrandIndexX][anyBrandIndexY] -= x;
				brandMap[i][j] += x;

				boolean ishu = Hu(i,j,brandMap,isDouble);
				if(ishu) {
					for(int y = 0 ; y < x ; y++){
						resultMap.get(0).add(i*100+j);
					}
					return true;
				}
				
				brandMap[anyBrandIndexX][anyBrandIndexY] += x;
				brandMap[i][j] -= x;
			}
		}
		return false;
	}
	
	//记录胡牌组合
	void AddResult(int bid,int count){
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0 ; i < count ;  i++){
			list.add(bid);
		}
		
		if(count == 2){
			resultMap.set(1, list);
		}else{
			resultMap.add(list);
		}
		
	}
	
	public void AddOpenBrandToResultArray(List<List<Integer>> rmap){
		for(int i = 0 ; i< this.openBrandList.size() ;i ++){
			List<Integer> arr = new ArrayList<Integer>();
			List<MajiangUtil> l = openBrandList.get(i).list;
			for(int j = 0 ; j < l.size() ; j ++){
				arr.add(Integer.parseInt(l.get(j).brandid));
			}
			rmap.add(arr);
		}
	}
	
	/**
	 * 获得可以杠的类型
	 * @param bid 1明杠 2暗杠
	 * @return
	 */
	public int GetGangType(String bid){
		for(int i = 0 ; i < openBrandList.size() ; i ++){
			if(openBrandList.get(i).type == OpenType.cha && openBrandList.get(i).list.get(0).brandid.equals(bid)){
				return 1;
			}
		}
		return 2;
	}
	//杠
	public List<MajiangUtil> Gang(String bid){
		List<MajiangUtil> gangList = new ArrayList<MajiangUtil>();
		for(int i = 0 ; i < brandList.size() ; i ++){
			MajiangUtil b = brandList.get(i);
			if(b.brandid.equals(bid)){
				gangList.add(b);
			}
		}
		
		if(gangList.size() < 4){
			boolean isfind = false;
			for(int i = 0 ; i < openBrandList.size() ; i ++){
				if(openBrandList.get(i).type == OpenType.cha && openBrandList.get(i).list.get(0).brandid.equals(bid)){
					gangList.addAll(openBrandList.get(i).list);
					openBrandList.remove(i);
					isfind = true;
					break;
				}
				//ret += openBrandList.get(i).list.size();
			}
			if(!isfind){
				logger.info("gangList.size() is : " + gangList.size() );
				return null;
			}
		}
		OpenBrand ob = new OpenBrand(OpenType.gang,gangList);
		
		openBrandList.add(ob);
		
		for(int i = 0 ; i < gangList.size() ; i ++){
			brandList.remove(gangList.get(i));
		}
		
		return gangList;
	}
	//吃牌
	public List<MajiangUtil> EatBrand(String bid1,String bid2,MajiangUtil outBrand){
		//List<Integer> eatList = IsEat(playerindex,outBrand);
		
		int index1 = -1;
		int index2 = -1;
		
		for(int i = 0 ; i < brandList.size() ; i ++){
			MajiangUtil b = brandList.get(i);
			if(b.brandid.equals(bid1)){
				index1 = i;
			}else if(b.brandid.equals(bid2)){
				index2 = i;
			}
		}
		
		if(index1 == -1 || index2 == -1){
			logger.info("****Cant find brand : 1:[" + bid1 + "]  2 : [" + bid2 + " ]");
			return null;
		}
		
		Integer[] nList = new Integer[3];
		int n1 = GameDataManager.majiangDataManager.GetData(brandList.get(index1).getBrandid()).getIndex();
		int n2 = GameDataManager.majiangDataManager.GetData(brandList.get(index2).getBrandid()).getIndex();
		int n3 = GameDataManager.majiangDataManager.GetData(outBrand.getBrandid()).getIndex();

		nList[0] = n1;
		nList[1] = n2;
		nList[2] = n3;
		boolean isContinunity = GameTools.IsContnunity(nList,1);
		
//		//n1,n2,n3是否连续
//		if(		 (n1 + 1 == n2 && n1 - 1 == n3) || (n1 + 1 == n3 && n1 - 1 == n2) ){
//			//n1为中间数
//			isContinunity = true;
//		}else if((n2 + 1 == n1 && n2 - 1 == n3) || (n2 + 1 == n3 && n2 - 1 == n1) ){
//			//n2为中间数
//			isContinunity = true;
//		}else if((n3 + 1 == n1 && n3 - 1 == n2) || (n3 + 1 == n2 && n3 - 1 == n1) ){
//			//n3为中间数
//			isContinunity = true;
//		}
		
		if(!isContinunity){
			logger.info("不连续");
			return null;
		}
		List<MajiangUtil> list = new ArrayList<MajiangUtil>();
		list.add(brandList.get(index1));
		list.add(brandList.get(index2));
		list.add(outBrand);
		
		OpenBrand ob = new OpenBrand(OpenType.chi,list);
		
		openBrandList.add(ob);
		for(int i = 0 ; i < list.size() ; i ++){
			brandList.remove(list.get(i));
		}
		
		return list;
	}	
	
	//叉牌
	public List<MajiangUtil> ChaBrand(MajiangUtil outBrand){
		List<MajiangUtil> chaList = IsCha(outBrand);
		if(chaList.size() <= 1){
			logger.info("没有可以叉的牌");
			return null;
		}
		
		while(chaList.size() > 2){
			chaList.remove(0);
		}

		chaList.add(outBrand);
		
		OpenBrand ob = new OpenBrand(OpenType.cha,chaList);
		openBrandList.add(ob);
		
		for(int i = 0 ; i < chaList.size() ; i ++){
			brandList.remove(chaList.get(i));
		}
		return chaList;
	}	
	
	public void AddChaTest(String bid){
		MajiangUtil b = new MajiangUtil();
		b.brandid = bid;
		List<MajiangUtil> chaList = new ArrayList<MajiangUtil>();
		chaList.add(b);
		chaList.add(b);
		chaList.add(b);
		
		OpenBrand ob = new OpenBrand(OpenType.cha,chaList);
		openBrandList.add(ob);
	}
	//是否可以吃牌
	//先找出可以连续的牌里最小的，再找出最大的，如果最大－最小>=2	则可以吃
	List<List<String>> IsEat(MajiangUtil m){
		logger.info("==================IsEat==================" );
		//找出连续最小的
		int thisIndex = GameDataManager.majiangDataManager.GetData(m.getBrandid()).getIndex();
		
		List<MajiangUtil> retlist = new ArrayList<MajiangUtil>();
		
		//最小连续
		FindContinuityIndex(retlist,brandList,thisIndex,-1,2);
		//最大连续
		FindContinuityIndex(retlist,brandList,thisIndex,1,2);
		
		List<List<String>> list = new ArrayList<List<String>>();
		
		for(int i = 0 ; i < retlist.size() - 1 ; i++){
			List<String> _l = new ArrayList<String>();
			_l.add(retlist.get(i).brandid);
			_l.add(retlist.get(i+1).brandid);
			list.add(_l);
		}
		
		logger.info("retlist : " + JSON.toJSONString(retlist));
		logger.info("list : " + JSON.toJSONString(list));
		return list;
	}
	
	//获得连续的牌
	List<MajiangUtil> FindContinuityIndex(List<MajiangUtil> retList,List<MajiangUtil> playerMList,int index,int add,int count){
		if(count <= 0) return retList;
		count--;

		index = index + add;
		for(int i = 0 ; i < playerMList.size() ; i ++){
			MajiangUtil _m = playerMList.get(i);
			if(GameDataManager.majiangDataManager.GetData(_m.getBrandid()).getIndex() == index){
				if(add < 0){
					retList.add(0, _m);
				}else{
					retList.add(_m);
				}
				FindContinuityIndex(retList,playerMList,index,add,count);
				break;
			}
		}
		
		return retList;
	}
	
	//是否可以叉牌
	List<MajiangUtil> IsCha(MajiangUtil m){
		logger.info("==========IsCha ==========");
		List<MajiangUtil> mlist = new ArrayList<MajiangUtil>();
		//相同id的牌数量>=2就可以叉
		for(int i = 0 ; i < brandList.size() ; i ++){
			if(brandList.get(i).brandid.equals(m.brandid)){
				mlist.add(brandList.get(i));
			}
//			if(mlist.size() >= 2){
//				break;
//			}
		}
		logger.info("brandList : " + JSON.toJSONString(brandList));
		logger.info("mlist : " + JSON.toJSONString(mlist));
		//如果结果小于1张牌 清空数组
		if(mlist.size() <= 1){
			mlist.clear();
		}
		
		return mlist;
	}
	
	//***********************test***************************
	
	public void HuTest(){
		int[][] brandMap = {
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,2,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,1,0,0,0,0,0,0}};

		resultMap.add(new ArrayList<Integer>());//记录百搭都用在什么地方
		resultMap.add(new ArrayList<Integer>());//记录头（双）
		
		boolean ishu = Hu(0,0,brandMap,false);
		if(ishu){
			logger.info("和了");
		}else{
			logger.info("没胡");
		}

		logger.info("resultMap : " + JSON.toJSONString(resultMap));
	}
	
	public void GangTest(){
		MajiangUtil m = new MajiangUtil();
		m.setBrandid("2");
		this.AddBrand(m);

		MajiangUtil m2 = new MajiangUtil();
		m2.setBrandid("2");
		this.AddBrand(m2);
		

		MajiangUtil m3 = new MajiangUtil();
		m3.setBrandid("2");
		this.ChaBrand(m3);
		
		boolean ishave = this.HaveGang("2");
		if(ishave){
			System.out.println("have");
		}else{
			System.out.println("no have");
		}
		
	}
	
	public void EatTest(){
		MajiangUtil m = new MajiangUtil();
		m.setBrandid("3");
		

		MajiangUtil m1 = new MajiangUtil();
		m1.setBrandid("1");
		this.AddBrand(m1);

		MajiangUtil m2 = new MajiangUtil();
		m2.setBrandid("2");
		this.AddBrand(m2);
		
		MajiangUtil m3 = new MajiangUtil();
		m3.setBrandid("4");
		this.AddBrand(m3);
		
		List<List<String>> test = this.IsEat(m);
		logger.info("test : " + JSON.toJSONString(test));
		
		
	}
    public static void main( String[] args ) throws Exception
    {
		// 游戏配置表数据载入
		GameDataManager.load(GameConst.RES_PATH);

    	PlayerBrand pb = new PlayerBrand("33");
		pb.logger = GetLog.getLoggerByName(-1 + "", "room");
		
		String[] list = {"5","4","19","13","33","19","24","3","12","19","25","6","14","23"};
		
		for(int i = 0 ; i < list.length ; i ++){
			MajiangUtil m = new MajiangUtil();
			m.brandid = list[i];
			pb.AddBrand(m);
		}
		pb.IsHu(null, true);
    	pb.logger.info("last brand : " + pb.lastBrand.brandid);
		pb.logger.info("resultMap : " + JSON.toJSONString(pb.resultMap));
		
		if(pb.isIshu()){
			System.out.println("hu");
		}else{

			System.out.println("no hu");
		}
		
		boolean isAnyHead = false;
		
		boolean islastbrand = false;

		String doubleid = GameDataManager.majiangDataManager.GetIdByIndex(101+pb.resultMap.get(1).get(0))+"";

		for(int i = 0 ; i < pb.resultMap.get(0).size() ; i ++ ){
			if(pb.resultMap.get(0).get(i).equals(pb.resultMap.get(1).get(0))){
				isAnyHead = true;
			}

			if(doubleid.equals(pb.lastBrand.brandid)){
				islastbrand = true;
			}
			if(isAnyHead && islastbrand) {
				isAnyHead = true ;break;
			}
		}
		
    }

	public MajiangUtil getLastBrand() {
		return lastBrand;
	}

	public void setLastBrand(MajiangUtil lastBrand) {
		this.lastBrand = lastBrand;
	}

	public String getAnyBrandId() {
		return anyBrandId;
	}

	public void setAnyBrandId(String anyBrandId) {
		this.anyBrandId = anyBrandId;
	}

	public boolean isIshu() {
		return ishu;
	}

	public void setIshu(boolean ishu) {
		this.ishu = ishu;
	}
    
}
