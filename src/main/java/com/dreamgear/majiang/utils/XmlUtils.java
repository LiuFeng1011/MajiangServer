package com.dreamgear.majiang.utils;

import java.io.CharArrayReader;
import java.io.File;
import java.io.FileFilter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * <p>
 * Title: XmlUtils
 * </p>
 * <p>
 * Description: XML文件处理工具
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Ocean Blue Mobile Tech.
 * </p>
 * 
 * @author bluesky
 * @version 1.0
 * 
 * 
 * @author Shaolong Wang 整理于 2010/11/26
 */
public class XmlUtils {
	public static final String BR = System.getProperty("line.separator");

	/**
	 * load a xml file from OS file system and interpret it into a Document no
	 * charset limited
	 * 
	 * @param xmlfile
	 *            String 文件路径名
	 * @return Document
	 * @throws Exception
	 */
	public static Document load(String xmlfile) throws Exception {
		javax.xml.parsers.DocumentBuilderFactory factory =

		javax.xml.parsers.DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(false);
		factory.setIgnoringElementContentWhitespace(false);
		factory.setValidating(false);
		factory.setCoalescing(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		return builder.parse(xmlfile);
	}

	/**
	 * load a xml file from OS file system and interpret it into a Document no
	 * charset limited
	 * 
	 * @param xmlfile
	 *            String 文件路径名
	 * @return Document
	 * @throws Exception
	 */
	public static Document load(File xmlfile) throws Exception {
		javax.xml.parsers.DocumentBuilderFactory factory =

		javax.xml.parsers.DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(false);
		factory.setIgnoringElementContentWhitespace(false);
		factory.setValidating(false);
		factory.setCoalescing(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		return builder.parse(xmlfile);
	}

	/**
	 * 取得文件名
	 * 
	 * @param filePath
	 *            String
	 * @return String
	 */
	public static String getFileName(String filePath) {
		Pattern p = Pattern.compile("[^\\" + File.separator + "]+.xml");
		Matcher m = p.matcher(filePath);
		if (m.find()) {
			return m.group().substring(0, m.group().length() - 4);
		}
		return "";
	}

	/**
	 * 验证文件名是否合法
	 * 
	 * @param filePath
	 *            String
	 * @return String
	 */
	public static boolean checkValidity(String filePath) {
		String[] array = filePath.split(".");
		if (array[array.length - 1].equals("xml")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isXml(String file) {
		if (file.toLowerCase().endsWith("xml")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * load a String without the title tag of xml into a Document
	 * 
	 * @param domContent
	 *            String 没有head的XML内容
	 * @return Document
	 * @throws Exception
	 */
	public static Document loadStringWithoutTitle(String domContent)
			throws Exception {
		domContent = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + BR
				+ domContent;
		return XmlUtils.loadString(domContent);
	}

	/**
	 * load a String with a title tag of xml into a Document
	 * 
	 * @param domContent
	 *            String XML内容
	 * @return Document
	 * @throws Exception
	 */
	public static Document loadString(String domContent) throws Exception {
		javax.xml.parsers.DocumentBuilderFactory factory =

		javax.xml.parsers.DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(false);
		factory.setIgnoringElementContentWhitespace(false);
		factory.setValidating(false);
		factory.setCoalescing(false);
		DocumentBuilder builder = factory.newDocumentBuilder();

		char[] chars = new char[domContent.length()];
		domContent.getChars(0, domContent.length(), chars, 0);
		InputSource is = new InputSource(new CharArrayReader(chars));
		return (builder.parse(is));
	}

	/**
	 * 根据完整路径得到整个文档的一个子节点的文字
	 * 
	 * @param doc
	 *            Document 文档
	 * @param fullname
	 *            String 子节点完整路径
	 * @return String
	 */
	public static String getTextByFullName(Document doc, String fullname) {
		String path[] = StringUtil.toStringArray(fullname, ".");
		Element e = doc.getDocumentElement();
		for (int i = 1; i < path.length; i++) {
			e = getChildByName(e, path[i]);
		}
		return getText(e);
	}

	/**
	 * 根据完整路径得到某个节点的一个子节点的文字
	 * 
	 * @param parent
	 *            Element 父节点
	 * @param fullname
	 *            String 子节点完整路径
	 * @return String
	 */
	public static String getTextByFullName(Element parent, String fullname) {
		String path[] = StringUtil.toStringArray(fullname, ".");
		Element e = parent;
		for (int i = 0; i < path.length; i++) {
			e = getChildByName(e, path[i]);
		}
		return getText(e);
	}

	/**
	 * @param parent
	 *            Element
	 * @param name
	 *            String
	 * @return String
	 */
	public static String getChildText(Element parent, String name) {
		Element e = getChildByName(parent, name);
		if (e == null) {
			return "";
		}
		return getText(e);
	}

	/**
	 * 根据名称得到一个父节点下所有的子节点
	 * 
	 * @param e
	 *            Element
	 * @param name
	 *            String
	 * @return Element[]
	 */
	public static Element[] getChildrenByName(Element e, String name) {
		NodeList nl = e.getChildNodes();
		int max = nl.getLength();
		LinkedList<Node> list = new LinkedList<Node>();
		for (int i = 0; i < max; i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE
					&& n.getNodeName().equals(name)) {
				list.add(n);
			}
		}
		return list.toArray(new Element[list.size()]);
	}

	/**
	 * 根据名字查找某个节点下的符合该名字的节点
	 * 
	 * @param e
	 *            Element 父节点
	 * @param name
	 *            String 子节点名称
	 * @return Element
	 */
	public static Element getChildByName(Element e, String name) {
		Element[] list = getChildrenByName(e, name);
		if (list.length == 0) {
			return null;
		}
		if (list.length > 1) {
			throw new IllegalStateException("Too many (" + list.length + ") '"
					+ name + "' elements found!");
		}
		return list[0];
	}

	/**
	 * 得到一个节点的文字
	 * 
	 * @param e
	 *            Element
	 * @return String
	 */
	public static String getText(Element e) {
		NodeList nl = e.getChildNodes();
		int max = nl.getLength();
		for (int i = 0; i < max; i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.TEXT_NODE) {
				return n.getNodeValue();
			}
		}
		return "";
	}

	public static String getAttribute(Element e, String name) {
		return e.getAttribute(name);
	}

	/**
	 * get Int value
	 * 
	 * @param player
	 * @param spouseName
	 * @return
	 */
	public static int getIntValue(Element e) {
		return Integer.valueOf(getText(e));
	}

	public static Long getLongValue(Element e) {
		return Long.valueOf(getText(e));
	}

	/**
	 * get byte value
	 * 
	 * @param player
	 * @param spouseName
	 * @return
	 */
	public static byte getByteValue(Element e) {
		return Byte.valueOf(getText(e));
	}

	/**
	 * 获取Properties格式的xml数据
	 * 
	 * @param root
	 * @return
	 */
	public static Map<String, Object> getProperties(Element root) {
		Map<String, Object> map = new HashMap<String, Object>();
		Element[] list = getChildrenByName(root, "property");
		for (int i = 0; i < list.length; i++) {
			String name = list[i].getAttribute("name");
			String type = list[i].getAttribute("type");
			String valueString = getText(list[i]);
			try {
				Class<?> cls = Class.forName(type);
				Constructor<?> con = cls
						.getConstructor(new Class[] { String.class

						});
				Object value = con.newInstance(new Object[] { valueString

				});
				map.put(name, value);
			} catch (Exception e) {
				System.err.println("Unable to parse property '" + name +

				"'='" + valueString + "': " + e.toString());
			}
		}
		return map;
	}

	/**
	 * 将dom中的内容存入xmlfile所指的文件中。 dom==null时，xml文件也是空的。
	 * 
	 * @param xmlfile
	 *            java.lang.String 保存的文件名
	 * @param doc
	 *            ort.w3c.dom.Document 需要保存的DOM
	 * @throws Exception
	 *             任何异常
	 */
	public static void save(String xmlfile, Document doc) throws Exception {
		// 首先创建一个DOMSource对象,该构造函数的参数可以是一个Document对象
		// doc代表更改后的DOM Tree。
		DOMSource doms = new DOMSource(doc);
//		Document doc1=XmlUtils.blankDocument("");
		// 创建一个File对象,代表DOM Tree所包含的数据的输出介质,这是一个XML文件。
		File f = new File(xmlfile);
		File dir = f.getParentFile();
		dir.mkdirs();
		// 创建一个StreamResult对象,该构造函数的参数可以取为File对象。
		StreamResult sr = new StreamResult(f);

		// 下面调用JAXP中的XSLT引擎来实现输出DOM Tree中的数据到XML文件中的功能。
		// XSLT引擎的输入为DOMSource对象,输出为StreamResut对象。
		try {
			// 首先创建一个TransformerFactory对象,再由此创建Transformer对象。Transformer
			// 类相当于一个XSLT引擎。通常我们使用它来处理XSL文件,但是在这里我们使
			// 用它来输出XML文档。
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			// 设置新的输出属性:输出字符编码为UTF-8,XSLT引擎所输出
			// 的XML文档如果包含了中文字符,可以正常显示,不会出现所谓的"汉字问题"。
			// 请留意OutputKeys类的字符串常数OutputKeys.ENCODING。
			Properties properties = t.getOutputProperties();
			properties.setProperty(OutputKeys.ENCODING, "UTF-8");
			properties.setProperty(OutputKeys.INDENT, "yes");
			// 更新XSLT引擎的输出属性。
			t.setOutputProperties(properties);
			// 关键的一步, 调用Transformer对象 (XSLT引擎)的transform()方法,该方法的第一
			// 个参数是DOMSource对象,第二个参数是StreamResult对象。
			t.transform(doms, sr);

		} catch (TransformerConfigurationException tce) {
			tce.printStackTrace();
		} catch (TransformerException te) {
			te.printStackTrace();
		}

	}

	public static void setAttribute(Element e, String name, String value) {
		e.setAttribute(name, value);
	}

	/**
	 * create a blank Document.
	 * 
	 * @param rootElementName
	 *            String
	 * @return Document
	 * @throws Exception
	 */
	public static Document blankDocument(String rootElementName)
			throws Exception {
		javax.xml.parsers.DocumentBuilderFactory factory =

		javax.xml.parsers.DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(false);
		factory.setIgnoringElementContentWhitespace(false);
		factory.setValidating(false);
		factory.setCoalescing(false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();
		Element root = doc.createElement(rootElementName);
		doc.appendChild(root);
		return doc;
	}

	public static Element createChild(Document doc, Element root, String name) {
		Element elem = doc.createElement(name);
		root.appendChild(elem);
		return elem;
	}

	public static void createChildText(Document doc, Element elem, String name,
			String value) {
		Element child = doc.createElement(name);
		child.appendChild(doc.createTextNode(value == null ? "" : value));
		elem.appendChild(child);
	}

	/**
	 * 创建一个带注释的子节点
	 * 
	 * @param doc
	 *            Document
	 * @param elem
	 *            Element
	 * @param name
	 *            String
	 * @param value
	 *            String
	 * @param comment
	 *            String
	 */
	public static void createChildTextWithComment(Document doc, Element elem,
			String name, String value, String comment) {
		Element child = doc.createElement(name);
		child.appendChild(doc.createTextNode(value == null ? "" : value));
		Comment c = doc.createComment(comment);
		elem.appendChild(c);
		elem.appendChild(child);

	}

	/**
	 * 创建一段注释
	 * 
	 * @param doc
	 *            Document
	 * @param comment
	 *            String
	 */
	public static void createComment(Document doc, String comment) {
		Comment c = doc.createComment(comment);
		doc.getDocumentElement().appendChild(c);
	}

	public static void createOptionalChildText(Document doc, Element elem,
			String name, String value) {
		if (value == null || value.length() == 0) {
			return;
		}
		Element child = doc.createElement(name);
		child.appendChild(doc.createTextNode(value));
		elem.appendChild(child);
	}

	/**
	 * document转化为string
	 * 
	 * @param doc
	 * @return
	 */
	public static String Doc2String(Document doc) {
		try {
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			// transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			transformer.transform(domSource, result);
			return writer.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static void applyProperties(Object o, Element root) {
		Map<String, Object> map = getProperties(root);
		Iterator<String> it = map.keySet().iterator();
		Field[] fields = o.getClass().getFields();
		Method[] methods = o.getClass().getMethods();
		while (it.hasNext()) {
			String name = (String) it.next();
			Object value = map.get(name);
			try {
				for (int i = 0; i < fields.length; i++) {
					if (fields[i].getName().equalsIgnoreCase(name)
							&& isTypeMatch(fields[i].getType(),

							value.getClass())) {
						fields[i].set(o, value);
						System.err.println("Set field " + fields

						[i].getName() + "=" + value);
						break;
					}
				}
				for (int i = 0; i < methods.length; i++) {
					if (methods[i].getName().equalsIgnoreCase("set" +

					name) && methods[i].getParameterTypes

					().length == 1 && isTypeMatch(methods

					[i].getParameterTypes()[0], value.getClass())) {
						methods[i].invoke(o, new Object[] { value

						});
						System.err.println("Set method " + methods

						[i].getName() + "=" + value);
						break;
					}
				}
			} catch (Exception e) {
				System.err.println("Unable to apply property '" + name + "': "
						+ e.toString());
			}
		}
	}

	private static boolean isTypeMatch(Class<?> one, Class<? extends Object> two) {
		if (one.equals(two)) {
			return true;
		}
		if (one.isPrimitive()) {
			if (one.getName().equals("int")
					&& two.getName().equals("java.lang.Integer")) {
				return true;
			}
			if (one.getName().equals("long")
					&& two.getName().equals("java.lang.Long")) {
				return true;
			}
			if (one.getName().equals("float")
					&& two.getName().equals("java.lang.Float")) {
				return true;
			}
			if (one.getName().equals("double")
					&& two.getName().equals("java.lang.Double")) {
				return true;
			}
			if (one.getName().equals("char")
					&& two.getName().equals("java.lang.Character")) {
				return true;
			}
			if (one.getName().equals("byte")
					&& two.getName().equals("java.lang.Byte")) {
				return true;
			}
			if (one.getName().equals("short")
					&& two.getName().equals("java.lang.Short")) {
				return true;
			}
			if (one.getName().equals("boolean")
					&& two.getName().equals("java.lang.Boolean")) {
				return true;
			}
		}
		return false;
	}

	static final FileFilter xmlFileExtFilter = new FileFilter() {

		public boolean accept(File pathname) {
			if (pathname.getName().endsWith(".xml"))
				return true;
			return false;
		}

	};

	// /**
	// * @return the xmlFileExtFilter
	// */
	public static final FileFilter getXmlFileExtFilter() {
		return xmlFileExtFilter;
	}
	
	/**
	 * 载入固化数据
	 *  例子   
	 * List<Object> list = GameDataManager.loadData(path, Hero.class);
		for (Object obj : list) {
			Hero data = (Hero) obj;
		}
	 * @param path
	 * @param T
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static List<Object> loadData(String path, Class<?> T)
			throws ClassNotFoundException {
		String fileName = T.getSimpleName();
		// //system.out.println("fileName=" + fileName);
		File file = new File(path + "/" + fileName + ".xml");
		Document d;
		List<Object> list = new ArrayList<Object>();
		try {
			d = XmlUtils.load(file);
			Element[] elements = XmlUtils.getChildrenByName(
					d.getDocumentElement(), fileName);
			for (Element element : elements) {
				Object data = T.newInstance();// 创建对象
				Field[] fs = T.getDeclaredFields();
				for (int i = 0; i < fs.length; i++) {
					Field f = fs[i];
					f.setAccessible(true); // 设置些属性是可以访问的
					String str = XmlUtils.getAttribute(element, f.getName());
					 ////system.out.println("str="+str+"name="+ f.getName());
					try {
						if (f.getType() == int.class) {
							if (str != null && !"".equalsIgnoreCase(str)) {
								f.set(data, Integer.parseInt(str));
							}

						} else if (f.getType() == long.class) {
							if (str != null && !"".equalsIgnoreCase(str)) {
								f.set(data, Long.parseLong(str));
							}

						} else if (f.getType() == boolean.class) {
							if (str != null && !"".equalsIgnoreCase(str)) {
								f.set(data, Boolean.parseBoolean(str));
							}

						} else if (f.getType() == byte.class) {
							if (str != null && !"".equalsIgnoreCase(str)) {
								f.set(data, Byte.parseByte(str));
							}
						} else if (f.getType() == String.class) {
							f.set(data, str);
						} else if (f.getType() == Short.class
								|| f.getType() == short.class) {
							if (str != null && !"".equalsIgnoreCase(str)) {
								f.set(data, Short.parseShort(str));
							}

						} else if (f.getType() == float.class
								|| f.getType() == Float.class) {
							if (str != null && !"".equalsIgnoreCase(str)) {
								f.set(data, Float.parseFloat(str));
							}

						}else if (f.getType() == double.class
								|| f.getType() == Double.class) {
							if (str != null && !"".equalsIgnoreCase(str)) {
								f.set(data, Double.parseDouble(str));
							}

						} else {
							 ////system.out.println("error type=" +
							 //f.getType()+" name="+f.getName());
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				list.add(data);
			}
			// 打印属性
			for (Object obj : list) {
				Field[] fs = T.getDeclaredFields();
				for (int i = 0; i < fs.length; i++) {
					Field f = fs[i];
					f.setAccessible(true); // 设置些属性是可以访问的
					Object val = f.get(obj);// 得到此属性的值
					 System.out.println("name:" + f.getName() + "\t value = "
					 + val);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return list;
	}
}