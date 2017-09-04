package com.dreamgear.majiang.game.player;

import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dreamgear.http.handler.domain.LoginData;
import com.dreamgear.http.handler.domain.WXPlayerInfo;
import com.dreamgear.majiang.common.GameCommon;
import com.dreamgear.majiang.common.GameTools;
import com.dreamgear.majiang.db.DBManager;
import com.dreamgear.majiang.utils.TimeUtils;

public class LoginManager {

	public ConcurrentHashMap<Long,WXPlayerInfo> uidMap = new ConcurrentHashMap<Long,WXPlayerInfo>();

	private static LoginManager instance;
	
	public static LoginManager GetInstance(){
		if(instance == null){
			instance = new LoginManager();
		}
		return instance;
	}

	public void Login(WXPlayerInfo p){
		boolean isfind = false;
		//内存中有没有
		synchronized (uidMap) {
			for (Map.Entry<Long,WXPlayerInfo> entry : uidMap.entrySet()) {
			    if(entry.getValue().openid.equals(p.openid)){
			    	p.setUid(entry.getValue().getUid());
			    	uidMap.put(entry.getKey(), p);
			    	isfind = true;
			    	break;
			    }
			}  
		}
		
		//数据库中寻找
		if(!isfind){
			//从数据库中获取
			LoginData pd = DBManager.getInstance().getLoginDAO().getLoginData(p.openid);
			if(pd == null){
				//数据库中也不存在
				AddLoginUser(p);
			}else{
				p.setUid(pd.getId());
				
				pd.nickname = p.nickname;
				pd.sex 		= p.sex;
				pd.province	= p.province;
				pd.city		= p.city;
				pd.country	= p.country;
				pd.head		= p.headimgurl;
				pd.setLastLoginTime(TimeUtils.nowLong());
				
				DBManager.getInstance().getLoginDAO().updateLoginData(pd);
			}

			uidMap.put(p.getUid(), p);
		}
		
//		p.gameToken = CreateToken(p.getUid()+"");
	}
	
	public String CreateToken(String s){
		String token;
		try
        {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	        crypt.reset();
	        crypt.update(s.getBytes("UTF-8"));
	        token = GameCommon.byteToHex(crypt.digest());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
		return token;
	}
	
	public LoginData GetLoginData(long uid){
		WXPlayerInfo w = GetPlayerInfo(uid);
		LoginData pd ;
		if(w != null){
			pd = CreateLoginData(w);
		}else{
			pd = DBManager.getInstance().getLoginDAO().getLoginData(uid);
		}
		return pd;
	}
	
	public LoginData CreateLoginData(WXPlayerInfo p){
		LoginData ld = new LoginData();
		ld.setId(p.getUid());
		ld.setOpenid(p.openid);
		ld.setCreateTime(TimeUtils.nowLong());
		ld.setLastLoginTime(TimeUtils.nowLong());
		
		ld.nickname = p.nickname;
		ld.sex 		= p.sex;
		ld.province	= p.province;
		ld.city		= p.city;
		ld.country	= p.country;
		ld.head		= p.headimgurl;
		return ld;
	} 
	
	public void AddLoginUser(WXPlayerInfo p){
		LoginData ld = CreateLoginData(p);

		DBManager.getInstance().getLoginDAO().addLoginData(ld);
		p.setUid(ld.getId());
	}
	
	public WXPlayerInfo GetPlayerInfo(long uid){
		if(uidMap.containsKey(uid)){
			return uidMap.get(uid);
		}
		return null;
	}
	
	public boolean isLogin(long uid){
		if(uidMap.containsKey(uid)){
			WXPlayerInfo wi = uidMap.get(uid);
			GameTools.Log("LoginManager isLogin : now time : " + TimeUtils.nowLong() + "  expires_in : " + wi.expires_in);
			if(Long.parseLong(wi.expires_in) > TimeUtils.nowLong()){
				GameTools.Log("user login : " + uid);
				return true;
			}
		}
		return false;
	}
	
	public void Tick(){
		
	}
}
