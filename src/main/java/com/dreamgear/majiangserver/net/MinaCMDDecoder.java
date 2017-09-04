/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dreamgear.majiangserver.net;

import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dreamgear.majiang.utils.JsonUtil;

/**
 *解码器，解开消息的格式，变成可以处理的数据格式。
 */
public class MinaCMDDecoder extends CumulativeProtocolDecoder {
	private static final Logger logger = LoggerFactory.getLogger(MinaCMDDecoder.class);
	
	private static final int SESSION_TAG_SHAKE_HANDS=1;//会话握手标识 用来标识是否握手
	public static final byte FIN = 0x1; // 1000 0000
	public static final byte OPCODE = 0x0F;// 0000 1111
	public static final byte MASK = 0x1;// 1000 0000
	public static final byte PAYLOADLEN = 0x7F;// 0111 1111
	public static final byte HAS_EXTEND_DATA = 126;
	public static final byte HAS_EXTEND_DATA_CONTINUE = 127;
	
	
    /**
     * 
     * @return true 解码完成，释放缓存，false 解码未完成，等待下一次解码。
     * @throws Exception 
     */
    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput pdo) throws Exception {
    	Object attribute = session.getAttribute(SESSION_TAG_SHAKE_HANDS);

		if(attribute==null){
			//握手
			byte[] bytes=new byte[in.limit()]; 
			in.get(bytes);	
			String m = new String(bytes);
			String rtn = getSecWebSocketAccept(m);
			byte[] rtnbytes = rtn.getBytes("utf-8");
			IoBuffer resp = IoBuffer.allocate(rtnbytes.length);
			resp.put(rtnbytes);
			resp.flip();
			session.write(resp);
			session.setAttribute(SESSION_TAG_SHAKE_HANDS,true);
			return false;
		}
		//不够一个消息
		if(in.remaining()<2){
			//消息头不完整
			return false;
		}
		in.mark();
		byte head1 = in.get();
		byte head2 = in.get();
//		int isend =head1>>7&FIN;
		int opcode=head1 & OPCODE;
		if(opcode==8){
			session.close(true);
			return false;
		}
		int ismask=head2>>7&MASK;
		int length=0;
		byte datalength= (byte) (head2& PAYLOADLEN);
		if(datalength<HAS_EXTEND_DATA ){
			length=datalength;
		}else if (datalength==HAS_EXTEND_DATA){
			if(in.remaining()<2){
				logger.info("datalength==HAS_EXTEND_DATA/////in.remaining()<2////消息头不完整");
				//消息头不完整
				in.reset();
				return false;
			}
			byte[] extended = new byte[2];
			in.get(extended);
			int shift = 0;
            length = 0;
            for (int i = extended.length - 1; i >= 0; i--) {
            	length = length + ((extended[i] & 0xFF) << shift);
                shift += 8;
            }
		}else if(datalength==HAS_EXTEND_DATA_CONTINUE){
			if(in.remaining()<2){
				logger.info("datalength==HAS_EXTEND_DATA_CONTINUE/////in.remaining()<2////消息头不完整");
				//消息头不完整
				in.reset();
				return false;
			}
			byte[] extended = new byte[2];
			in.get(extended);
			int shift = 0;
            length = 0;
            for (int i = extended.length - 1; i >= 0; i--) {
            	length = length + ((extended[i] & 0xFF) << shift);
                shift += 8;
            }
		}

		byte[] date=new byte[length];
		if(ismask==1){
			if(in.remaining()<4+length){
				logger.info("ismask==1/////in.remaining()<4+length");
				in.reset();
				return false;
			}
			// 利用掩码对org-data进行异或
			byte[] mask =new byte[4];
			in.get(mask);
			in.get(date);
			for (int i = 0; i < date.length; i++) {
				//把数据进行异或运算
				date[i] = (byte) (date[i] ^ mask[i % 4]);
			}
		}else{
			if(in.remaining()<length){
				logger.info("in.remaining()<length");
				in.reset();
				return false;
			}
			in.get(date);
		}
		HandleMessage(session,in, new String(date));
		return true;
    }
    
    public static void HandleMessage(IoSession is,IoBuffer in,String data){
    	//通过字符串，获得最外部的json对象
    	JSONObject jsonObj = JSON.parseObject(data);
    	int protocol = jsonObj.getIntValue("protocol");
    	if(protocol != 0) logger.info("data : " + data);
    	if(ServerManager.GetInstance().getServerList().containsKey(protocol)){
			BaseServer server = ServerManager.GetInstance().getServerList().get(protocol);
			BaseMessage message = server.GetRequest();

			message = JsonUtil.JsonToObject(data, message.getClass());
			if(message != null){
				UserInfo userInfo = null;
				if(protocol == 100) {
					//创建用户信息
					userInfo = new UserInfo();
					userInfo.deserialize(jsonObj);
					userInfo.setSession(is);
				}else {
					userInfo = ServerManager.GetInstance().getUserInfo(is);
				}
				if(userInfo == null){
					logger.info("userInfo == null!!");
					return;
				}

				message.setUserInfo(userInfo);

				BaseMessage msg = server.handle(is,message);

				if(msg != null){
					is.write(msg);
				}
			}else{
				logger.info("BaseMessage is null : " + protocol);
			}
		}else{
			logger.info("cant find protocol : " + protocol);
		}
    	
    }
    
    public static String getSecWebSocketAccept(String key) {
		String secKey = getSecWebSocketKey(key);

		//private static final String WEBSOCKET_GUID=
					//"258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
		String guid = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
		secKey += guid;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(secKey.getBytes("iso-8859-1"), 0, secKey.length());
			byte[] sha1Hash = md.digest();
			secKey = base64Encode(sha1Hash);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String rtn = "HTTP/1.1 101 Switching Protocols\r\nUpgrade: websocket\r\nConnection: Upgrade\r\nSec-WebSocket-Accept: "
				+ secKey + "\r\n\r\n";
		return rtn;
	}
    
    public static boolean checkHeader(IoSession session,IoBuffer in){
//		Object attribute = session.getAttribute(SESSION_TAG_SHAKE_HANDS);
		
		return false;
	}
	
	
	public static String getSecWebSocketKey(String req) {
		Pattern p = Pattern.compile("^(Sec-WebSocket-Key:).+",
				Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher m = p.matcher(req);
		if (m.find()) {
			String foundstring = m.group();
			return foundstring.split(":")[1].trim();
		} else {
			return null;
		}
	}
	public static String base64Encode(byte[] input) {
		return new String(Base64.encodeBase64(input));
	}
}
