package com.dreamgear.gm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dreamgear.http.handler.impl.GMFunction;
import com.dreamgear.majiang.common.GameCommon;
import com.dreamgear.majiang.common.GameTools;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.utils.TimeUtils;

public class GMUserManager {
	private static final Logger logger = LoggerFactory.getLogger(GMUserManager.class);

	private static GMUserManager instance;
	
	public static GMUserManager GetInstance(){
		if(instance == null){
			instance = new GMUserManager();
		}
		return instance;
	}
	
	Map<String , GMLoginData> loginDataMap = new HashMap<String , GMLoginData>();
	
	public GMLoginData Login(String uname,String pw){
		GMLoginData ret = null;
		if(loginDataMap.containsKey(uname)){
			ret = loginDataMap.get(uname);
			if(!ret.data.getPw().equals(pw)){
				return null;
			}
			if(TimeUtils.nowLong() - ret.time < 7200000){
				return ret;
			}
		}
		GMUserData data = DBManager.getInstance().getGmUserDAO().getLoginData(uname, pw);
		if(data == null){
			return null;
		}
		
		GMLoginData loginData = new GMLoginData();
		loginData.setData(data);
		loginData.setTime( TimeUtils.nowLong());
		String s = uname + pw + TimeUtils.nowLong();
		
		try
        {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	        crypt.reset();
	        crypt.update(s.getBytes("UTF-8"));
	        String token = GameCommon.byteToHex(crypt.digest());
	        loginData.setToken(token);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
		loginDataMap.put(uname, loginData);
		return loginData;
	}
	
	 public GMLoginData Token(String uname,String t){
		 if(loginDataMap.containsKey(uname)){
			 GMLoginData ret = loginDataMap.get(uname);
			 if(!ret.token.equals(t)){
				 return null;
			 }
			if(TimeUtils.nowLong() - ret.time < 7200000){
				
				return ret;
			}
		 }
		 return null;
	 }
}
