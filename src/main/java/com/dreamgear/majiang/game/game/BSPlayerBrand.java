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

public class BSPlayerBrand {
	public Logger logger ;

	//手中还站着的牌
	public List<MajiangUtil> brandList = new ArrayList<MajiangUtil>();
	//已经躺下的牌
	public List<OpenBrand> openBrandList = new ArrayList<OpenBrand>();
	
	//0 ： 百搭充当的牌列表
	//1 ：对子牌
	public List<List<Integer>> resultMap = new ArrayList<List<Integer>>();
	
	MajiangUtil lastBrand;
	
	boolean ishu = false;
	boolean isTing = false;
	
	public boolean isSevenDouble = false;//七小对
	public boolean allone = false;//十三1

	public BSPlayerBrand(String anyBrandId){
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
			if(!this.isTing){
				MajiangData data = GameDataManager.majiangDataManager.GetData(bid);
				int index = data.getIndex();
				int x = index/100 - 1;
				int y = index%10 - 1;
				brandMap[x][y] ++;
				if(brandMap[x][y] >= 4){
					brandMap[x][y] = 0;
					ganglist.add(data.getId());
				}
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
	public boolean IsCanTing(){
		if(this.isTing){
			return false;
		}
		List<MajiangData> list = GameDataManager.majiangDataManager.getDataList();
		int count = 0;
		for(int i = 0 ; i < list.size() ; i ++){
			MajiangData md = list.get(i);
			MajiangUtil m = new MajiangUtil();
			m.setBrandid(md.getId());
			if(IsHu(m)){
				count ++;
				if(count > 1){
					return false;
				}
			}
			
		}
		if(count == 1){
			return true;
		}
		return false;
	}
	
	public void Ting(){
		isTing = true;
	}
	
	public boolean GetIsTing(){
		return isTing;
	}
	
	//isself 是不是自己抓的牌
	public boolean IsHu(MajiangUtil m){
		isSevenDouble = false;
		allone = false;
		
		int[][] brandMap = {
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0}};
		resultMap.clear();
		resultMap.add(new ArrayList<Integer>());//记录百搭都用在什么地方
		resultMap.add(new ArrayList<Integer>());//记录头（双）
		
		boolean isHaveThree = false;
		boolean isHaveOneNine = false;

		for(int i = 0 ; i < brandList.size() ; i++){
			int index = GameDataManager.majiangDataManager.GetData(brandList.get(i).brandid).getIndex();
			int x = index/100 - 1;
			int y = index%10 - 1;
			brandMap[x][y] ++;
			if(brandMap[x][y] >= 3){
				isHaveThree = true;
			}
			
			if(y == 0 || y == 8 || x >= 3){
				isHaveOneNine = true;
			}
		}
		logger.info("m : " + JSON.toJSONString(m));
		if(m != null){
			int index = GameDataManager.majiangDataManager.GetData(m.brandid).getIndex();
			int x = index/100 - 1;
			int y = index%10 - 1;

			brandMap[x][y] ++;
			if(brandMap[x][y] >= 3){
				isHaveThree = true;
			}

			if(y == 0 || y == 8 || x >= 3){
				isHaveOneNine = true;
			}
		}
		
		if(SevenDouble(brandMap)){
			isSevenDouble = true;
			this.setIshu(true);
			return true;
		}
		if(AllOneNine(brandMap)){
			allone = true;
			this.setIshu(true);
			return true;
		}
		
		if(!isHaveThree && !IsHaveThree()){
			logger.info("没有平胡");
			return false;
		}
		
		if(!isHaveOneNine && !IsHaveOneNine()){
			logger.info("没有19");
			return false;
		}

		logger.info("brandMap : " + JSON.toJSONString(brandMap));
		
		this.setIshu(Hu(0,0,brandMap,false));
		return this.isIshu();
	}
	
	//是不是七小对
	boolean SevenDouble(int[][] brandMap){
		if(openBrandList.size() > 0){
			return false;
		}
		logger.info("SevenDouble : " + JSON.toJSONString(brandMap));
		for(int i = 0 ; i < 5 ; i ++){//5个大类
			for(int j = 0 ; j < 9 ; j ++){//每个大类最多9个小类
				if(brandMap[i][j] > 0 && brandMap[i][j] != 2){
					return false;
				}
			}
		}
		
		return true;
	}
	
	//是不是十三幺
	boolean AllOneNine(int[][] brandMap){
		int[] indexs = {101,109,201,209,301,309,401,402,403,404,501,502,503};
		
		for(int i = 0 ; i < indexs.length ; i ++){
			int x = indexs[i]/100 - 1;
			int y = indexs[i]%10 - 1;
			
			if(brandMap[x][y] <= 0){
				return false;
			}
		}
		
		return true;
	}
	
	//是否有平湖
	boolean IsHaveThree(){
		for(int i = 0 ; i < openBrandList.size() ; i ++){
			if(openBrandList.get(i).type == OpenType.cha || openBrandList.get(i).type == OpenType.gang){
				return true;
			}
		}
		return false;
	}
	
	//是否有19
	boolean IsHaveOneNine(){
		for(int i = 0 ; i < openBrandList.size() ; i ++){
			if(openBrandList.get(i).type == OpenType.cha || openBrandList.get(i).type == OpenType.gang){
				int index = GameDataManager.majiangDataManager.GetData(openBrandList.get(i).list.get(0).brandid).getIndex();
				int x = index/100 - 1;
				int y = index%10 - 1;
				if(y == 0 || y == 8 || x >= 3){
					return true;
				}
			}else{
				for(int j = 0 ; j < openBrandList.get(i).list.size() ; j ++){
					int index = GameDataManager.majiangDataManager.GetData(openBrandList.get(i).list.get(j).brandid).getIndex();
					int x = index/100 - 1;
					int y = index%10 - 1;
					if(y == 0 || y == 8 || x >= 3){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	//是否开门
	public boolean IsOpen(){
		for(int i = 0 ; i < openBrandList.size() ; i ++){
			if(openBrandList.get(i).type != OpenType.angang){
				return true;
			}
		}
		return false;
	}

	//当前牌能不能胡
	boolean Hu(int _i,int _j,int[][] brandMap,boolean isDouble){
		//logger.info("brandMap : i:" + _i  + "  j:" + _j + "    " + JSON.toJSONString(brandMap));
		int allBrandCount = 0;//记录剩余牌的数量
		for(int i = _i ; i < 5 ; i ++){//5个大类
			for(int j = _j ; j < 9 ; j ++){//每个大类最多9个小类

				int count = brandMap[i][j];
				
				if(count == 0 ) {
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
				}
				
				return false;
			}
			_j = 0;
		}
		
		//所有牌都组合上的话 说明和了
		if(allBrandCount == 0){
			return true;
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
	public void AddOpenBrandToResultArrayHaveType(List<List<Integer>> rmap){
		for(int i = 0 ; i< this.openBrandList.size() ;i ++){
			List<Integer> arr = new ArrayList<Integer>();
			arr.add(openBrandList.get(i).type.ordinal());
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
	public OpenBrand Gang(String bid){
		List<MajiangUtil> gangList = new ArrayList<MajiangUtil>();
		for(int i = 0 ; i < brandList.size() ; i ++){
			MajiangUtil b = brandList.get(i);
			if(b.brandid.equals(bid)){
				gangList.add(b);
			}
		}
		OpenType gang = OpenType.gang;
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
		}else{
			gang = OpenType.angang;
		}
		OpenBrand ob = new OpenBrand(gang,gangList);
		
		openBrandList.add(ob);
		
		for(int i = 0 ; i < gangList.size() ; i ++){
			brandList.remove(gangList.get(i));
		}
		
		return ob;
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

    	BSPlayerBrand pb = new BSPlayerBrand("");
		pb.logger = GetLog.getLoggerByName(-1 + "", "room");
		
		String[] list = {"1","2","3","1","2","3","9","10","11","5","6","7","5","5"};
		
		for(int i = 0 ; i < list.length ; i ++){
			MajiangUtil m = new MajiangUtil();
			m.brandid = list[i];
			pb.AddBrand(m);
		}
		pb.IsHu(null);
    	pb.logger.info("last brand : " + pb.lastBrand.brandid);
		pb.logger.info("resultMap : " + JSON.toJSONString(pb.resultMap));
		
		if(pb.isIshu()){
	    	pb.logger.info("huuuuu" );
		}else{
	    	pb.logger.info("no hu!!!" );
		}
    }

	public MajiangUtil getLastBrand() {
		return lastBrand;
	}

	public void setLastBrand(MajiangUtil lastBrand) {
		this.lastBrand = lastBrand;
	}

	public boolean isIshu() {
		return ishu;
	}

	public void setIshu(boolean ishu) {
		this.ishu = ishu;
	}

	
}
