package com.dreamgear.majiang.common;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.alibaba.fastjson.JSON;
import com.dreamgear.majiang.game.server.resp.MessageResp;
import com.dreamgear.majiang.utils.XmlUtils;
import com.dreamgear.majiangserver.core.annotation.ServiceInterpreter;
import com.dreamgear.majiangserver.net.BaseServer;
import com.dreamgear.majiangserver.net.UserInfo;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

public class GameTools {
	private static final Logger logger = LoggerFactory.getLogger(GameTools.class);

	public static Map<Integer,BaseServer> GetServerMap() {
		try{
			return registServer();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private static Map<Integer,BaseServer> registServer() throws Exception {
		ServiceInterpreter interpreter = ServiceInterpreter.getInstance();
		
		String CONF_LOCAL_SERVICES = "LocalServices.xml";
		
		Map<Integer,BaseServer> servermap = new HashMap<Integer,BaseServer>();
		
		File file = new File("./config/" + CONF_LOCAL_SERVICES);

		if (!file.exists()) {
			return null;
		}

		Document d = XmlUtils.load(file);

		Element root = d.getDocumentElement();

		Element[] localServices = XmlUtils.getChildrenByName(root, "LocalService");

		for (int i = 0; i < localServices.length; i++) {
			String clazzName = XmlUtils.getAttribute(localServices[i], "class");
			String override = XmlUtils.getAttribute(localServices[i], "override");
			boolean isOverride = Boolean.parseBoolean(override);
			Class<? extends BaseServer> serviceClazz = (Class<? extends BaseServer>) Class.forName(clazzName);
			Constructor constructor = serviceClazz.getDeclaredConstructor(new Class[0]);

			constructor.setAccessible(true);

			BaseServer service = (BaseServer) constructor.newInstance(new Class[0]);

			if (interpreter.loadMessageService(service) == null) {
				throw new RuntimeException(serviceClazz.getName() + " isn't a service");
			}
			// registHandlers(service,isOverride);
			
			
			// 协议设计时未考虑到message类型，key使用协议ID即可，保证协议ID不重复即可
			// long messageID = message.getMessageID();
			int protocolID = service.GetProtocol();
			servermap.put(protocolID, service);
		}
		
//		for (Entry<Integer, BaseServer> iterable_element : servermap.entrySet()) {
//			logger.info("Service: protocol:" + iterable_element.getValue().GetProtocol() + " Service Name: " + iterable_element.getValue().getClass().getName());
//		}
		
		return servermap;
	}
	public static void SendMessage(UserInfo ui,String msg){
		MessageResp resp = new MessageResp();
		resp.setUserInfo(ui);
		resp.setMessage(msg);
		resp.Send();
	}
	
	

	//判断一个数组中的数字是否连续
	public static boolean IsContnunity(Integer[] nList,int step){
		//先排序
		for(int i = 0 ; i < nList.length - 1 ; i ++){
			for(int j = i ; j < nList.length ; j ++){
				if(nList[i] > nList[j]){
					int temp = nList[i];
					nList[i] = nList[j];
					nList[j] = temp;
				}
			}
		}
		//是否连续
		for(int i = nList.length - 1 ; i > 0 ; i --){
			if(nList[i] - nList[i-1] != step){
				return false;
			}
		}
		
		return true;
	}
	

    public static void Log(String msg){
    	logger.info(msg);
    }
 // 将 s 进行 BASE64 编码 
    public static String getBASE64(String s) {
    	BASE64Encoder encoder = new BASE64Encoder();
    	try{
    		s = encoder.encode(s.getBytes("UTF-8"));
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return s;
    } 
     
    // 将 BASE64 编码的字符串 s 进行解码 
    public static String getFromBASE64(String s) { 
    	BASE64Decoder decoder = new BASE64Decoder();
    	String ret="";
    	try{
    		byte[] bytes = decoder.decodeBuffer(s);
    		ret = new String(bytes, "UTF-8");
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return ret;
    }
    
    //判断一个列表中是否有重复列表
    public static boolean FindSameList(List<List<Integer>> resultMap){
    	boolean isfind = false;
		for(int i = 0 ; i < resultMap.size() -1 ; i ++){
			List<Integer> list = resultMap.get(i);
			if(list.size() != 3) continue;//数量不＝＝3说明不是顺
			if(list.get(0) == list.get(1)) continue;//0==1是平胡
			
			//与之后每个元素比较
			for(int j = i+1 ; j < resultMap.size() ; j ++){
				List<Integer> otherlist = resultMap.get(j);
				if(otherlist.size() != 3) continue;//数量不＝＝3说明不是顺
				if(otherlist.get(0) == list.get(1)) continue;//0==1是平胡
				logger.info("list : " + JSON.toJSONString(list) + "   otherlist : " + JSON.toJSONString(otherlist));
				
				boolean findlist = true;
				//挨个元素比较
				for(int k = 0 ; k < 3 ;  k ++){
					boolean findsame = false;
					for(int x = 0 ; x < 3 ; x ++){
						if(list.get(k) == otherlist.get(x)){
							findsame = true;
							break;
						}
					}
					if(!findsame){
						findlist = false;
						break;
					}
				}
				if(findlist ){
					isfind = true;
					break;
				}
			}
			if(isfind){
				break;
			}
		}
		return isfind;
    }

    //是否有4张一样的牌
    public static boolean FindFourCount(List<List<Integer>> resultMap){
    	int[] a = new int[50];
    	
    	for(int i = 0 ; i < resultMap.size() ; i ++){
    		List<Integer> list = resultMap.get(i);
    		if(list.size() >= 4) continue;//杠牌不参与计算
    		for(int j =0 ; j < list.size() ; j++){
    			a[list.get(j)]++;
    			if(a[list.get(j)] >= 4){
    				return true;
    			}
    		}
    	}
    	
    	return false;
    }
    
    public static void main( String[] args ){
    	//GetServerMap();
//    	String s = "{\"access_token\":\"-tTLNtUpdp1JXdaoZThFf2ckofZQXq1JH1LFkf1tGDhnzzVJF1re12SZofTgEnr_DHjhcqil7biqDCAgflOBLY4ds6hse3_6S8fzn2qF-E4\",\"city\":\"通化\",\"country\":\"中国\",\"expires_in\":\"7200\",\"headimgurl\":\"http://wx.qlogo.cn/mmopen/ajNVdqHZLLAz8tzjAVsgtctECsJW6qhCgYXreJjtDolxStKe8uYmOw2xXiao60tjEic6PYtYd9M7nTiaqrGjxBRrg/0\",\"nickname\":\"刘峰\",\"openid\":\"oL95_v7iZF_bEOvWEVqoNchFd7fk\",\"privilege\":\"[]\",\"province\":\"吉林\",\"refresh_token\":\"WA_197sU94kA61jEAd7QrmD5tcW_LIiHtkG2EZpfPgjM6zNDiCCKQmxbjQK_3lgxCjrPT4GnZ2fD0d9dSBdmI89IWMp4dzgwqVgVPqKFFmc\",\"scope\":\"snsapi_userinfo\",\"sex\":\"1\"}";
//    	
//    	String base64 = getBASE64(s);
//    	logger.info("base64 : " + base64);
//    	String enBase64 = getFromBASE64(base64);
//    	logger.info("enBase64 : " + enBase64);
    	List<List<Integer>> resultMap = new ArrayList<List<Integer>>();
    	List<Integer> l1 = new ArrayList<Integer>();
    	l1.add(1);
    	l1.add(2);
    	l1.add(3);
    	resultMap.add(l1);
    	List<Integer> l2 = new ArrayList<Integer>();
    	l2.add(2);
    	l2.add(3);
    	l2.add(4);
    	resultMap.add(l2);

    	List<Integer> l3 = new ArrayList<Integer>();
    	l3.add(1);
    	l3.add(2);
    	l3.add(3);
    	resultMap.add(l3);
    	

    	List<Integer> l4 = new ArrayList<Integer>();
    	l4.add(1);
    	l4.add(1);
    	l4.add(1);
    	resultMap.add(l4);
    	
    	
    	if(FindFourCount(resultMap)){
    		logger.info("find!!");
    	}else{
    		logger.info("no find!");
    	}
    	
    	
    }
    
}
