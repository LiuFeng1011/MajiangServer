package com.dreamgear.majiang.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * excel类
 * 
 * @author xufangliang
 * 
 */
public class ExcelUtils {
	static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
	public static List<String> fileLst = new ArrayList<String>();

	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	/*
	 * public static ArrayList<String[]> excelInList(String filePath) throws
	 * Exception { logger.info(filePath + "文件读取开始>>>>>>>>>>>>>>>>>>>>>"); File f
	 * = new File(filePath); ArrayList<String[]> excelList;// = excelInList(f);
	 * logger.info(filePath + "文件读取成功>>>>>>>>>>>>>>>>>>>>>"); return excelList;
	 * }
	 */

	/**
	 * 读取excel文件
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static void excelInList(String filePath) throws Exception {
		File f = new File(filePath);
		InputStream stream = new FileInputStream(f);
		Workbook book = new XSSFWorkbook(stream);

		int sheetcount = book.getNumberOfSheets();

		for (int _count = 0; _count < sheetcount; _count++) {
			Sheet sheet = book.getSheetAt(_count);

			logger.info("开始" + "生成XML<<<<<<<<<<<<<<<" + sheet.getSheetName());
			int totalRows = sheet.getPhysicalNumberOfRows();
			if (totalRows == 0) {
				return;
			}
			int totalCells = sheet.getRow(0).getPhysicalNumberOfCells();

			ArrayList<String[]> list = new ArrayList<String[]>();
			for (int i = 0; i < totalRows; i++) {
				String[] objs = new String[totalCells];
				for (int j = 0; j < totalCells; j++) {
					if (totalRows >= 1 && sheet.getRow(0) != null) {
						try {
							String sh = sheet.getRow(i).getCell(j) == null ? "" : sheet.getRow(i).getCell(j).toString();
							if (sh.endsWith(".0")) {
								objs[j] = sh.substring(0, sh.length() - 2);
							} else {
								objs[j] = sh;
							}
						} catch (Exception e) {
							// system.out.println("row=" + i + " cell=" + j);
							e.printStackTrace();
						}
					}
				}
				list.add(objs);
			}
			toInXml(list, sheet.getSheetName());
		}

	}

	/**
	 * 获取英文名标签数组
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String[] fromExcelList(ArrayList<String[]> list) {
		String[] dataNotes = null;
		// 第一行为表头注释,第二行为英文字段说明
		if (list != null && list.size() > 1) {
			dataNotes = list.get(1);
		}
		return dataNotes;
	}

	/**
	 * excel信息生成字符串
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	/*
	 * public static String[] toXmlString(String filePath) throws Exception { //
	 * 读取Excel第二行 对应英文标签 ArrayList<String[]> list = excelInList(filePath);
	 * String[] dataNotes = fromExcelList(list); String[] data = null; if (list
	 * != null && list.size() > 2 && dataNotes != null && dataNotes.length > 0)
	 * { data = new String[list.size() - 2]; // 实际数据从第二行开始 for (int i = 2; i <
	 * list.size(); i++) { StringBuffer sb = new StringBuffer(); String[] datas
	 * = list.get(i); for (int j = 0; j < datas.length; j++) {
	 * sb.append(dataNotes[j]).append("=\"").append(datas[j]) .append("\" "); }
	 * data[i - 2] = sb.toString(); } } return data; }
	 */

	/**
	 * 生成注释
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public static String toXmlcomment(ArrayList<String[]> list) throws Exception {
		String string = "";
		String[] dataNotes = fromExcelList(list);
		if (list != null && list.size() > 2 && dataNotes != null && dataNotes.length > 0) {
			for (int i = 0; i < dataNotes.length; i++) {
				string += "  |" + dataNotes[i] + ":" + list.get(0)[i];
			}
		}
		return string;
	}

	/**
	 * 
	 * @param excelFilePath
	 *            excel文件地址
	 * @throws Exception
	 */
	public static void toInXml(ArrayList<String[]> list, String sheetname) throws Exception {

		// 读取Excel第二行 对应英文标签
		String[] dataNotes = fromExcelList(list);

		if (list != null && list.size() > 2 && dataNotes != null && dataNotes.length > 0) {
			String name = sheetname;
			Document doc = XmlUtils.blankDocument(name + "s");
			// 注释String
			String sb = toXmlcomment(list);
			XmlUtils.createComment(doc, sb);
			// 实际数据从第二行开始
			for (int i = 2; i < list.size(); i++) {

				Element e = XmlUtils.createChild(doc, doc.getDocumentElement(), name);
				String[] datas = list.get(i);
				for (int j = 0; j < datas.length; j++) {
					e.setAttribute(dataNotes[j], datas[j]);
				}

			}

			String[] strs = name.split("_");

			if (strs[0].equals("string")) {
				XmlUtils.save("./resources/excel/string/string/" + name + ".xml", doc);
			} else {
				XmlUtils.save("./resources/excel" + " " + "/" + name + ".xml", doc);
			}

		}
		logger.info(sheetname + "生成XML成功<<<<<<<<<<<<<<<");
	}

	/**
	 * 读取文件下所有文件
	 * 
	 * @param root
	 * @param fileType
	 * @return
	 */
	public static void recursion(String root, String fileType) {

		File file = new File(root);
		File[] subFile = file.listFiles();
		for (int i = 0; i < subFile.length; i++) {
			if (subFile[i].isDirectory()) {
				recursion(subFile[i].getAbsolutePath(), fileType);
			} else {
				String filename = subFile[i].getName();
				if (filename.endsWith(fileType)) {
					fileLst.add(root + filename);
				}
			}
		}
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
//		excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/配置表信息.xlsx");

//		 excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/帮助表.xlsx");
//		 excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/数值表.xlsx");
//		 excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/宠物表.xlsx");
//		 excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/技能表.xlsx");
//		 excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/道具表.xlsx");
//		 excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/副本表.xlsx");
//		 excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/任务表.xlsx");
		
//		 excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/奖池表.xlsx");
//		 excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/远征表.xlsx");
//		 excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/竞技场表.xlsx");
//		 excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/玩家信息.xlsx");
//		 excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/付费表.xlsx");
//		excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/商店表.xlsx");
//		excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/寻找规则相关.xlsx");
//		excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/公告.xlsx");
//		excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/战斗血条坐标点.xlsx");
//		 excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/战斗演示表.xlsx");
		
//		 excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/引导表.xlsx");

		
//		 excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/活动表.xlsx");
//		 excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/字库表.xlsx");
		
		//---------------dota--------------
//		excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/dota/0.玩家信息.xlsx");
//		excelInList("/Users/reddog/Desktop/unityproj/Konan0./策划表/dota/1.英雄基础信息.xlsx");
//		excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/dota/2.装备配置表.xlsx");
//		excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/dota/3.装备洗练表.xlsx");
//		excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/dota/4.技能设计表.xlsx");
//		excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/dota/5.物品道具表.xlsx");
//		excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/dota/6.竞技场配置表.xlsx");
//		excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/dota/7.关卡配置表.xlsx");
//		excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/dota/8.任务表.xlsx");
//		excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/dota/9.付费表.xlsx");
//		excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/dota/10.奖池表.xlsx");
//		excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/dota/11.远征表.xlsx");
//		excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/dota/12.商店表.xlsx");
//		excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/dota/13.战斗演示表.xlsx");
//		excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/dota/14.基地机器人.xlsx");
//		excelInList("/Users/reddog/Desktop/unityproj/Konan/策划表/dota/15.团队副本.xlsx");
		
		logger.info("end<<<<<<<<<<<<<<<");
		System.out.println("end<<<<<<<<<<<<<<<");
	}

}
