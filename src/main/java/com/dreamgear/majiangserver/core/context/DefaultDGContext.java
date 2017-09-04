package com.dreamgear.majiangserver.core.context;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.dreamgear.majiangserver.core.annotation.ServiceInterpreter;
import com.dreamgear.majiangserver.net.BaseMessage;
import com.dreamgear.majiangserver.net.BaseServer;
import com.dreamgear.tools.XmlUtils;

public class DefaultDGContext implements DGContext {
	private static final Logger logger = LoggerFactory.getLogger(DefaultDGContext.class);

	static ServiceInterpreter interpreter = ServiceInterpreter.getInstance();
	Map<Long, Class<? extends BaseMessage>> messages = new HashMap();

	Map<Long, Method> messageToHandler = new HashMap<Long, Method>();

	Map<Long, BaseServer> messageToService = new HashMap<Long, BaseServer>();
	static String CONF_LOCAL_SERVICES = "LocalServices.xml";
	private Map<Object, Object> attributes = Collections.synchronizedMap(new HashMap(4));

	public DefaultDGContext(String dir) throws Exception {
		loadDefaultServices();
		loadLocalServices(dir);
	}

	private void loadLocalServices(String dir) throws Exception {
		File file = new File(dir + CONF_LOCAL_SERVICES);

		if (!file.exists()) {
			return;
		}

		Document d = XmlUtils.load(file);

		Element root = d.getDocumentElement();

		Element[] localServices = XmlUtils.getChildrenByName(root, "LocalService");

		for (int i = 0; i < localServices.length; i++) {
			String clazzName = XmlUtils.getAttribute(localServices[i], "class");
			String override = XmlUtils.getAttribute(localServices[i], "override");
			boolean isOverride = Boolean.parseBoolean(override);

			registService((Class<? extends BaseServer>) Class.forName(clazzName), isOverride);
		}
	}

	private void loadDefaultServices() {

	}

	private void registService(Class<? extends BaseServer> serviceClazz, boolean isOverride) throws Exception {
		Constructor constructor = serviceClazz.getDeclaredConstructor(new Class[0]);

		constructor.setAccessible(true);

		BaseServer service = (BaseServer) constructor.newInstance(new Class[0]);

		if (interpreter.loadMessageService(service) == null) {
			throw new RuntimeException(serviceClazz.getName() + " isn't a service");
		}
		// registHandlers(service,isOverride);
		
		
		// 协议设计时未考虑到message类型，key使用协议ID即可，保证协议ID不重复即可
		// long messageID = message.getMessageID();
		long protocolID = service.GetProtocol();
		// long messageKey = protocolID << 16 | messageID;
		long messageKey = protocolID << 16;

		if ((!isOverride) && ((getService(messageKey) != null) || (getHandler(messageKey) != null))) {
			throw new RuntimeException("too many message handlers...");
		}
		this.messageToService.put(Long.valueOf(messageKey), service);
	}

	private void registHandlers(BaseServer service, boolean isOverride) throws Exception {
		List<Method> handlers = interpreter.loadMessageHandlers();

		for (Method handler : handlers) {
			Class[] params = handler.getParameterTypes();

			if (params.length != 2) {
				throw new RuntimeException("params error: " + service.getClass().getName() + "." + handler.getName());
			}
			// 协议设计时未考虑到message类型，key使用协议ID即可，保证协议ID不重复即可
			BaseServer message = (BaseServer) params[0].newInstance();
			// long messageID = message.getMessageID();
			long protocolID = message.GetProtocol();
			// long messageKey = protocolID << 16 | messageID;
			long messageKey = protocolID << 16;

			if ((!isOverride) && ((getService(messageKey) != null) || (getHandler(messageKey) != null))) {
				throw new RuntimeException("too many message handlers...");
			}

			this.messageToService.put(Long.valueOf(messageKey), service);
			this.messageToHandler.put(Long.valueOf(messageKey), handler);
			this.messages.put(Long.valueOf(messageKey), params[0]);
		}
	}

	public Object getContextAttribute(Object paramObject) {
		// TODO Auto-generated method stub
		return this.attributes.get(paramObject);
	}

	public Object setContextAttribute(Object paramObject1, Object paramObject2) {
		// TODO Auto-generated method stub
		return this.attributes.put(paramObject1, paramObject2);
	}

	public Object removeContextAttribute(Object paramObject) {
		// TODO Auto-generated method stub
		return this.attributes.remove(paramObject);
	}

	public Method getHandler(long messageKey) {
		// TODO Auto-generated method stub
		return (Method) this.messageToHandler.get(Long.valueOf(messageKey));
	}

	public Method getHandler(int protocolID, int messageID) {
		// TODO Auto-generated method stub
		long tempProtocolID = protocolID;
		long tempMessageID = messageID;

		return getHandler(tempProtocolID << 16 | tempMessageID);
	}

	public BaseServer getService(long messageKey) {
		// TODO Auto-generated method stub
		return (BaseServer) this.messageToService.get(Long.valueOf(messageKey));
	}

	public BaseServer getService(int protocolID, int messageID) {
		// TODO Auto-generated method stub
		long tempProtocolID = protocolID;
		long tempMessageID = messageID;

		return getService(tempProtocolID << 16 | tempMessageID);
	}

	public BaseMessage getMessage(long paramLong) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public BaseMessage getMessage(int protocolID, int messageID) {
		// TODO Auto-generated method stub
		long tempProtocolID = protocolID;
		long tempMessageID = messageID;
		try {
			return getMessage(tempProtocolID << 16 | tempMessageID);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("messageID:" + messageID + " error");
		}
		return null;
	}

	public Map<Long, BaseServer> getMessageToService() {
		return messageToService;
	}

}
