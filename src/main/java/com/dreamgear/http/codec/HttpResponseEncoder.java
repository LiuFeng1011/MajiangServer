package com.dreamgear.http.codec;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import com.dreamgear.http.response.HttpResponseMessage;
import com.dreamgear.majiang.common.GameTools;

/**
 * A {
 * 
 * @link MessageEncoder } that encodes {
 * @link HttpResponseMessage }.
 * 
 * @author The Apache Directory Project (mina-dev@directory.apache.org)
 * @version $Rev: 555855 $, $Date: 2007-07-13 12:19:00 +0900 (Fri, 13 Jul 2007)
 *          $
 */
public class HttpResponseEncoder implements MessageEncoder<HttpResponseMessage> {

	private static final Set<Class<?>> TYPES;
	
	public static String defaultEncoding;
	
	static {
		Set<Class<?>> types = new HashSet<Class<?>>();
		types.add(HttpResponseMessage.class);
		TYPES = Collections.unmodifiableSet(types);
	}

	static final byte[] CRLF = new byte[] { 0x0D, 0x0A };

	public void encode(IoSession session, HttpResponseMessage msg , ProtocolEncoderOutput out) throws Exception {
		IoBuffer buf = IoBuffer.allocate(256);
		buf.setAutoExpand(true);
		try {
			CharsetEncoder encoder = Charset.defaultCharset().newEncoder();
			buf.putString("HTTP/1.1 ",encoder);
			buf.putString(String.valueOf(msg.getResponseCode()), encoder);
			switch (msg.getResponseCode()) {
			case HttpResponseMessage.HTTP_STATUS_SUCCESS:
				buf.putString(" OK", encoder);
				break;
			case HttpResponseMessage.HTTP_STATUS_NOT_FOUND:
				buf.putString(" Not Found", encoder);
				break;
			}
			buf.put(CRLF);
			for (Iterator<Entry<String,String>> it = msg.getHeaders().entrySet().iterator() ; it.hasNext();) {
				Entry<String,String> entry =  it.next();
				buf.putString(entry.getKey(),encoder);
				buf.putString(": ", encoder);
				buf.putString(entry.getValue(),encoder);
				buf.put(CRLF);
			}
			buf.putString("Content-Length: ", encoder);
			buf.putString(String.valueOf(msg.getBodyLength()), encoder);
			buf.put(CRLF);
			buf.put(CRLF);
			buf.put(msg.getBody());
		} catch (Exception ex) {
			GameTools.Log("http tranform error" + ex);
		}
		buf.flip();
		out.write(buf);
	}

	public Set<Class<?>> getMessageTypes() {
		return TYPES;
	}
}
