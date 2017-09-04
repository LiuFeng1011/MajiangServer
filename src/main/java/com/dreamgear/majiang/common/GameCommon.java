package com.dreamgear.majiang.common;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

public class GameCommon {
	
	public enum enPlayerState{
		online,
		offline
	}

	public enum enCtrlType{
		hu,//胡牌=0
		cha,//叉牌=1
		chi,//吃牌=2
		gang,//杠牌=3
		backgang,//暗杠=4
		ting,//听牌=5
	};

	public enum OpenType{
		chi,//1 吃
		cha,//叉
		gang,//杠
		angang//暗杠
	}
	
	public enum BSHutype{
		pao("点炮"),			//0点炮
		selfget("自摸"),		//1自摸
		bosshu("庄胡"),		//2庄胡
		badbosshu("庄没胡"),	//3庄没胡
		only("手把一"),		//4手把一
		badonly("手把一输"),	//5手把一输
		stand("站立胡"),		//6站立胡
		badstand("站立输"),	//7站立输
		samehight("一般高"),	//8一般高
		badsamehight("一般高输"),	//9一般高输
		fourtoone("四归一"),	//10四归一
		badfourtoone("四归一输"),	//11四归一输
		ting("一个听"),		//12一个听
		badting("一个听输"),	//13一个听输
		ganglast("杠后开"),	//14杠后开
		gangpao("杠后炮"),	//15杠后炮
		doublehu("对对胡"),	//16对对胡
		doublecolor("混一色"),	//17混一色
		onlycolor("清一色"),	//18清一色
		alldouble("七小对"),	//19七小对
		onenine("十三幺"),	//20十三幺
		threeclear("三家清"),	//21
		fourclear("四家清"),	//22
		normalhu("屁胡");	//23
		
		String name;
		
		BSHutype(String _name){
			name = _name;
		}
		
		public String GetName(){
			return name;
		}
		
	}
	
	public enum enPlayerRoomState{
		enWait,//0 等待
		enDown,//1 坐下
		enLeave;//2 离开
		
		public static enPlayerRoomState GetState(int state){
			switch(state){
			case 0 :
				return enWait;
			case 1 :
				return enDown;
			case 2 :
				return enLeave;
			}
			return enWait;
		}
	}
	
	public enum enDelegateState{
		enNull,//不是代理
		enWait,//审核中
		enYes,//通过审核，成为代理
		enNo,//审核拒绝
		enCancel;//解除
		
		public static enDelegateState GetState(int state){
			switch(state){
			case 0 :
				return enNull;
			case 1 :
				return enWait;
			case 2 :
				return enYes;
			case 3 :
				return enNo;
			case 4 :
				return enCancel;
			}
			return enNull;
		}
	}
	
	public enum enRoomFindType{
		firend,//好友房
		random,//随机房
	}
	public enum enRoomPaoType{
		normal,//正常
		pao,//点炮包3家
	}
	
	
	public static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
	}
	
	/**
	 * 输出xml字符
	 * @throws WxPayException
	**/
	public static String ToXml(Map<String,String> map)
	{
    	String xml = "<xml>";
    	for (Map.Entry<String, String> entry : map.entrySet()) {  
    		xml+="<"+entry.getKey()+">"+entry.getValue()+"</"+entry.getKey()+">";
        }
        xml+="</xml>";
        return xml; 
	}
	

	static String[] chars = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
			"1","2","3","4","5","6","7","8","9","0"};
	/**
	 * 
	 * 产生随机字符串，不长于32位
	 * @param int $length
	 * @return 产生的随机字符串
	 */
	public static String getNonceStr(int length) 
	{  
		String ret ="";
		for ( int i = 0; i < length; i++ )  {  
			ret += chars[(int) (Math.random() * chars.length)];
		} 
		return ret;
	}
	
	public static String getMd5(String str){
		try {
	        // 生成一个MD5加密计算摘要
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        // 计算md5函数
	        md.update(str.getBytes());
	        // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
	        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
	        return (new BigInteger(1, md.digest())).toString(16);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return "";
	}
	
	/** 
	    *  
	    * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<br> 
	    * 实现步骤: <br> 
	    *  
	    * @param paraMap   要排序的Map对象 
	    * @param urlEncode   是否需要URLENCODE 
	    * @param keyToLower    是否需要将Key转换为全小写 
	    *            true:key转化成小写，false:不转化 
	    * @return 
	    */  
	   public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower)  
	   {  
	       String buff = "";  
	       Map<String, String> tmpMap = paraMap;  
	       try  
	       {  
	           List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());  
	           // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）  
	           Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>()  
	           {  
	  
	               public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2)  
	               {  
	                   return (o1.getKey()).toString().compareTo(o2.getKey());  
	               }  
	           });  
	           // 构造URL 键值对的格式  
	           StringBuilder buf = new StringBuilder();  
	           for (Map.Entry<String, String> item : infoIds)  
	           {  
	               if (item.getKey() != null && !"".equals(item.getKey()))  
	               {  
	                   String key = item.getKey();  
	                   String val = item.getValue();  
	                   if (urlEncode)  
	                   {  
	                       val = URLEncoder.encode(val, "utf-8");  
	                   }  
	                   if (keyToLower)  
	                   {  
	                       buf.append(key.toLowerCase() + "=" + val);  
	                   } else  
	                   {  
	                       buf.append(key + "=" + val);  
	                   }  
	                   buf.append("&");  
	               }  
	  
	           }  
	           buff = buf.toString();  
	           if (buff.isEmpty() == false)  
	           {  
	               buff = buff.substring(0, buff.length() - 1);  
	           }  
	       } catch (Exception e)  
	       {  
	    	   e.printStackTrace();
	          return null;  
	       }  
	       return buff;  
	   }  
	   
	   
}
