/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dreamgear.majiangserver.net;


import java.io.UnsupportedEncodingException;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dreamgear.majiangserver.net.buffer.JoyBuffer;

/**
 *编码器
 */
public class MinaCMDEncoder implements ProtocolEncoder {
	private static final Logger logger = LoggerFactory.getLogger(MinaCMDEncoder.class);
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
            throws Exception {
//        System.out.println("编码");
		IoBuffer buffer = IoBuffer.allocate(65536);

        BaseMessage msg = (BaseMessage)message;
        //用户信息
        //msg.getUserInfo().serialize(joyBuffer);
//        msg.serialize(joyBuffer);
        msg.setUserInfo(null);//这里如果不设置为空，转json会报错
        msg.SetProtocol();
        if(msg.protocol != 0) logger.info("send msg : " + JSON.toJSONString(msg));

    	byte[] bb = encode(msg.GetString());
    	buffer.put(bb);
    	buffer.flip();
    	
        out.write(buffer);
    }

    public void dispose(IoSession is) throws Exception {
    }

    // / 对传入数据进行无掩码转换
 	public static byte[] encode(String msg) throws UnsupportedEncodingException {
 		// 掩码开始位置
 		int masking_key_startIndex = 2;

 		byte[] msgByte = msg.getBytes("UTF-8");

 		// 计算掩码开始位置
 		if (msgByte.length <= 125) {
 			masking_key_startIndex = 2;
 		} else if (msgByte.length > 65536) {
 			masking_key_startIndex = 10;
 		} else if (msgByte.length > 125) {
 			masking_key_startIndex = 4;
 		}

 		// 创建返回数据
 		byte[] result = new byte[msgByte.length + masking_key_startIndex];

 		// 开始计算ws-frame
 		// frame-fin + frame-rsv1 + frame-rsv2 + frame-rsv3 + frame-opcode
 		result[0] = (byte) 0x81; // 129

 		// frame-masked+frame-payload-length
 		// 从第9个字节开始是 1111101=125,掩码是第3-第6个数据
 		// 从第9个字节开始是 1111110>=126,掩码是第5-第8个数据
 		if (msgByte.length <= 125) {
 			result[1] = (byte) (msgByte.length);
 		} else if (msgByte.length > 65536) {
 			result[1] = 0x7F; // 127
 		} else if (msgByte.length > 125) {
 			result[1] = 0x7E; // 126
 			result[2] = (byte) (msgByte.length >> 8);
 			result[3] = (byte) (msgByte.length % 256);
 		}

 		// 将数据编码放到最后
 		for (int i = 0; i < msgByte.length; i++) {
 			result[i + masking_key_startIndex] = msgByte[i];
 		}
 		
 		return result;
 	}
}
