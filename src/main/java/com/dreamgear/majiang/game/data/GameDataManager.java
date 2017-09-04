package com.dreamgear.majiang.game.data;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.dreamgear.majiang.common.GameConst;
import com.dreamgear.majiang.utils.XmlUtils;


/**
 * 游戏配置表管理
 * 每个配置表应该对应2个类，data.java ,dataManager.java
 * 
 * @author liufeng
 *
 */
public class GameDataManager {
	private static Logger logger = Logger.getLogger(GameDataManager.class.getName());

	//每张配置表都要有相应的数据类和数据管理类
	public static final MajiangDataManager majiangDataManager = new MajiangDataManager();
	public static final RoomManager roomManager = new RoomManager();
	public static final IapDataManager iapDataManager = new IapDataManager();
	public static final FaceDataManager faceDataManager = new FaceDataManager();
	
	
	public static void load(String path) {

		try {
			majiangDataManager.load(path);
			roomManager.load(path);
			iapDataManager.load(path);
			faceDataManager.load(path);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	

	/**
	 * 载入固化数据 例子 List<Object> list = GameDataManager.loadData(path, Hero.class);
	 * for (Object obj : list) { Hero data = (Hero) obj; }
	 * 
	 * @param path
	 * @param T
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static List<Object> loadData(String path, Class<?> T, String fileName) throws ClassNotFoundException {
		//system.out.println("fileName=" + fileName);
		String fullpath = path + "/" + fileName + ".xml";
		logger.info("loadData from " + fullpath + "<<<<<<<<");
		
		File file = new File(fullpath);
		
		Document d;
		List<Object> list = new ArrayList<Object>();
		try {
			d = XmlUtils.load(file);

			Element[] elements = XmlUtils.getChildrenByName(d.getDocumentElement(), fileName);
			for (Element element : elements) {
				Object data = T.newInstance();// 创建对象
				Field[] fs = T.getDeclaredFields();

				for (int i = 0; i < fs.length; i++) {
					Field f = fs[i];
					f.setAccessible(true); // 设置些属性是可以访问的
					String str = XmlUtils.getAttribute(element, f.getName());
					try {
						if (f.getType() == int.class) {
							if (str != null && !"".equalsIgnoreCase(str)) {
								float val = Float.parseFloat(str);
								f.set(data, (int) val);
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
						} else if (f.getType() == Short.class || f.getType() == short.class) {
							if (str != null && !"".equalsIgnoreCase(str)) {
								f.set(data, Short.parseShort(str));
							}

						} else if (f.getType() == float.class || f.getType() == Float.class) {
							if (str != null && !"".equalsIgnoreCase(str)) {
								f.set(data, Float.parseFloat(str));
							}

						} else if (f.getType() == double.class || f.getType() == Double.class) {
							if (str != null && !"".equalsIgnoreCase(str)) {
								f.set(data, Double.parseDouble(str));
							}

						} else {
							//System.out.println("error type=" +f.getType()+" name="+f.getName());
						}
					} catch (Exception ex) {
						logger.info("load data error,str=" + f.getName() + " fileName=" + fileName);
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
					
				}
			}

		} catch (Exception e) {
			logger.error("error in load " + fileName, e);
			e.printStackTrace();
			System.exit(0);
		}
		return list;
	}
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		GameDataManager.load(GameConst.RES_PATH);

		//String test = GameDataManager.heroAdvancedDataManager.GetData("hero001").getEquip_id3();
		//logger.info(test);
		
	}
}
