package com.dreamgear.majiang.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
	public final static String[] postfix = new String[]{".jpg",".png",".gif",".jpeg"};
	public final static String RES_PATH = "./img/resources/";
	static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * ��ȡ�ı����
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static final String readContent(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		byte[] data = new byte[is.available()];
		is.read(data);
		is.close();
		return new String(data, "utf-8");
	}

	/**
	 * ��ȡ�ı����
	 * 
	 * @param file
	 * @param charset
	 *            �����ʽ
	 * @return
	 * @throws IOException
	 */
	public static final String readContent(File file, String charset)
			throws IOException {
		InputStream is = new FileInputStream(file);
		byte[] data = new byte[is.available()];
		is.read(data);
		is.close();
		return new String(data, charset);
	}
	
	/**
	 * 保存图片
	 * @param fileName
	 * @param content
	 */
	public static void createImage(String fileName,byte[] content) throws Exception{
		// 获取web应用根路径,也可以直接指定服务器任意盘符路径
		String savePath = RES_PATH;
		//String savePath = "d:/upload/";
		// 检查路径是否存在,如果不存在则创建之
		File file = new File(savePath);
		if (!file.exists()) {
			file.mkdir();
		}
		File fileToCreate = new File(savePath, fileName);
		if (!fileToCreate.exists()) {
			FileUtil.writeToFile(fileToCreate, content);
		}
	}
	
	/**
	 * 读取文件返回流
	 * @param file 文件
	 * @return  byte[] 数组
	 * @throws IOException
	 */
	public static final byte[] readContentReturnFlow(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		byte[] data = new byte[is.available()];
		is.read(data);
		is.close();
		return data;
	}

	/**
	 * 
	 * @param file
	 * @param content
	 */
	public static final void writeToFile(File file, byte[] content) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(content);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建导出缓存
	 * 
	 * @param capCode
	 *            需要导出的列数组（必须大写），可以为空
	 * @param capName
	 *            需要导出列的中文名数组，可以为空
	 * @param capWidth
	 *            导出列的宽度
	 * @return 导出数据的缓存
	 */
	public static byte[] createExcelExportBuf(String[] capCode,
			String[] capName, int[] capWidth, List<Map<String, ?>> lst) {

		// 当前第几个sheet页
		int iSheet = 0;

		// 输出缓存
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();

		// Excel的文件对象
		jxl.write.WritableWorkbook workbook = null;

		// Excel的sheet
		jxl.write.WritableSheet sheet = null;

		// 偶数行样式
		jxl.write.WritableCellFormat styleEven = null;

		// 奇数行样式
		jxl.write.WritableCellFormat styleOdd = null;

		try {

			// 创建对象

			workbook = jxl.Workbook.createWorkbook(baos);
			styleEven = new jxl.write.WritableCellFormat();
			styleOdd = new jxl.write.WritableCellFormat();
			styleEven.setBackground(jxl.format.Colour.GRAY_25);
			styleOdd.setBackground(jxl.format.Colour.WHITE);
			sheet = workbook.createSheet(new java.text.SimpleDateFormat(
					"yyyyMMdd HHmmss ").format(new java.util.Date()) + iSheet,
					iSheet++);

			// 查询数据
			if (lst == null || lst.size() < 1) {
				return null;
			}

			// 处理表头
			if (capCode == null) {
				Map<String, ?> hm = (HashMap) lst.get(0);
				List<String> ls = new ArrayList<String>();
				for (String key : hm.keySet()) {
					ls.add(key);
				}
				capCode = new String[ls.size()];
				for (int i = 0; i < ls.size(); i++) {
					capCode[i] = ls.get(i);
				}
			}
			if (capName == null) {// 说明
				capName = capCode;
			}
			if (capName.length < capCode.length) {
				String[] capNameNew = new String[capCode.length];
				for (int i = 0; i < capName.length; i++) {
					capNameNew[i] = capName[i];
				}
				for (int i = capName.length; i < capCode.length; i++) {
					capNameNew[i] = capCode[i];
				}
				capName = capNameNew;
			}
			if (capWidth == null) {
				capWidth = new int[capCode.length];
				for (int i = 0; i < capCode.length; i++) {
					capWidth[i] = 50;
				}
			}

			// 设置表头
			jxl.write.WritableFont font = new jxl.write.WritableFont(
					jxl.write.WritableFont.TIMES, 10,
					jxl.write.WritableFont.BOLD);
			jxl.write.WritableCellFormat title = new jxl.write.WritableCellFormat(
					font);
			title.setAlignment(jxl.format.Alignment.CENTRE);
			title.setBackground(jxl.format.Colour.GRAY_50);
			for (int i = 0; i < capCode.length; i++) {
				jxl.write.Label label = new jxl.write.Label(i, 0, capName[i],
						title);
				sheet.addCell(label);
				sheet.setColumnView(i, capWidth[i]);

			}

			// 添加数据
			int size = lst.size();
			int length = capCode.length;
			for (int i = 0; i < size; i++) {

				Map<String, ?> hm = (HashMap) lst.get(i);

				for (int j = 0; j < length; j++) {
					Object val = String.valueOf(hm.get(capCode[j]));
					if (val == null)
						val = "";
					jxl.write.Label label = null;
					// 添加对齐方式
					if (i % 2 == 0) {
						label = new jxl.write.Label(j, i + 1, val.toString(),
								styleEven);
					} else {
						label = new jxl.write.Label(j, i + 1, val.toString(),
								styleOdd);
					}
					sheet.addCell(label);
				}
				// }
			}

			// 关闭对象
			workbook.write();
			workbook.close();

			logger.info("创建" + "Excel导出缓存成功！");
		} catch (Exception e) {
			logger.info("创建" + "Excel导出缓存出错！");
			logger.error(e.toString());
			e.printStackTrace();
		}

		return baos.toByteArray();
	}
	public static void download(String name,List<Map<String, ?>> lst ,HttpServletResponse response){
		 FileUtil.download(name + ".xls",
					FileUtil.createExcelExportBuf(null, null, null, lst), response);
	}
	/**
	 * 导出文件
	 * 
	 * @param fileName
	 *            文件名
	 * @param buffer
	 *            文件内容缓存
	 * @param response
	 *            HttpServletResponse对象
	 * @return 如果出错返回错误信息
	 */
	public static String download(String fileName, byte[] buffer,
			HttpServletResponse response) {
		String ret = "hello";
		javax.servlet.ServletOutputStream outputStream = null;
		try {

			if (buffer != null && buffer.length > 0) {
				String strFileName = new String(fileName.getBytes("gb2312"),
						"iso-8859-1");

				response.reset();
				response.setContentType("application/x-download");

				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + strFileName + "\"");

				int length = buffer.length;

				outputStream = response.getOutputStream();
				outputStream.write(buffer, 0, length);
				outputStream.flush();

				outputStream.close();
				response.setStatus(HttpServletResponse.SC_OK);
				response.flushBuffer();
			}

		} catch (Exception e) {
			ret = e.toString();
			logger.info("下载excel文件出错！");
			logger.error(e.toString());
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 根据序号返回sheet对象
	 * 
	 * @param workbook
	 *            可读取的excel对象
	 * @param i
	 *            序号
	 * @return 返回具体的sheet对象
	 */
	public static Sheet getSheet(Workbook workbook, int i) {
		Sheet sheet = null;
		if (workbook != null) {
			Sheet[] sheets = workbook.getSheets();
			if (i < sheets.length) {
				sheet = sheets[i];
			}
		}
		return sheet;
	}

	/**
	 * 根据名称返回sheet对象
	 * 
	 * @param workbook
	 *            可读取的excel对象
	 * @param name
	 *            sheet名称
	 * @return 返回具体的sheet对象
	 */
	public static Sheet getSheet(Workbook workbook, String name) {
		Sheet sheet = null;
		if (workbook != null) {
			Sheet[] sheets = workbook.getSheets();
			for (int i = 0; i < sheets.length; i++) {
				if (name.equalsIgnoreCase(sheets[i].getName())) {
					sheet = sheets[i];
				}
			}
		}
		return sheet;
	}

	/**
	 * 返回sheet对应列号和行号的单元格的值
	 * 
	 * @param sheet
	 *            sheet
	 * @param col
	 *            列号
	 * @param row
	 *            行号
	 * @return 返回单元格的值
	 */
	public static String getCellValue(Sheet sheet, int col, int row) {
		String ret = "";
		try {
			Cell cell = sheet.getCell(col, row);
			String temp = cell.getContents();
			ret = temp.trim();
		} catch (Exception e) {
			ret = "";
			//system.out.println("读取“" + sheet.getName() + "”单元格(" + row + ","+ col + ")时出错！");
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 如果参数是null或空字符串，则返回"0"。 否则不做任何处理。
	 * 
	 * @param
	 * @return
	 */
	static public String changeNullToZero(String strTemp) {

		if (strTemp == null || strTemp.equals("") || strTemp.equals("null")) {
			strTemp = "0";
		}

		return strTemp;
	}

	public static void getFile(String path) {
		// get file list where the path has
		File file = new File(path);
		// get the folder list
		File[] array = file.listFiles();

		for (int i = 0; i < array.length; i++) {
			if (array[i].isFile()) {
				//system.out.println(" " + array[i].getName() + " ");
				// only take file name
				// //system.out.println("^^^^^" + array[i].getName());
				// // take file path and name
				// //system.out.println("#####" + array[i]);
				// // take file path and name
				// //system.out.println("*****" + array[i].getPath());
			} else if (array[i].isDirectory()) {
				getFile(array[i].getPath());
			}
		}
	}

	// 删除文件夹
	// param folderPath 文件夹完整绝对路径
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除指定文件夹下所有文件
	// param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 文件夹名字
	 * @param path
	 */
	public static Map<String,File> allDirectoryInPath(String path) {
		Map<String,File> fileMap = new HashMap<String,File>();
		File file = new File(path);
		if (!file.exists()) {
			logger.error("文件或目录是不存在");
		}
		if (!file.isDirectory()) {
			logger.error("不是文件夹！");
		}else{
			String[] tempList = file.list();
			File temp = null;
			for (int i = 0; i < tempList.length; i++) {
				if (path.endsWith(File.separator)) {
					temp = new File(path + tempList[i]);
				} else {
					temp = new File(path + File.separator + tempList[i]);
				}
				if (temp.isDirectory()) {
					fileMap.put(temp.getPath(), temp);
				}
			}
		}
		return fileMap;
	}

	/**
	 * 获取文件夹下所有文件
	 * 
	 * @param path
	 *            文件夹地址
	 * @param fileMap
	 *            文件map
	 * @param isSub
	 *            是否包含子文件夹
	 * @param isHidden
	 *            是否包含隐藏文件,文件夹
	 * @param filter
	 *            文件类型过滤，全文件输入""
	 */
	public static void allFileInDirectory(String path,
			Map<String,File> fileMap, String filter,boolean isSub, boolean isHidden,String prefixFileName){
		File file = new File(path);
		File prefixFile = new File(prefixFileName);
		if (!file.exists()) {
			logger.error("文件或目录是不存在");
		}
		if (!file.isDirectory()) {
			logger.error("不是文件夹！");
		} else {
			if (fileMap == null) {
				logger.error("文件map is null");
				return;
			}
			String[] tempList = file.list();
			File temp = null;
			for (int i = 0; i < tempList.length; i++) {
				if (path.endsWith(File.separator)) {
					temp = new File(path + tempList[i]);
				} else {
					temp = new File(path + File.separator + tempList[i]);
				}
				if (temp.isFile()) {
					if (!isHidden) {
						if (!temp.isHidden()) {
							if(!"".equals(filter.trim()) && temp.getName().endsWith(filter)){
								//system.out.println(temp.getAbsolutePath());
								fileMap.put(temp.getPath().replace(prefixFile.getPath(), ""),temp);
							}else if("".equals(filter.trim())){
								//system.out.println(temp.getAbsolutePath());
								fileMap.put(temp.getPath().replace(prefixFile.getPath(), ""),temp);
							}
						}
					} else {
						if(!"".equals(filter.trim()) && temp.getName().endsWith(filter)){
							//system.out.println(temp.getAbsolutePath());
							fileMap.put(temp.getPath().replace(prefixFile.getPath(), ""),temp);
						}else if("".equals(filter.trim())){
							//system.out.println(temp.getAbsolutePath());
							fileMap.put(temp.getPath().replace(prefixFile.getPath(), ""),temp);
						}
					}
				}
				if (isSub) {
					if (temp.isDirectory()) {
						if (!isHidden) {
							if (!temp.isHidden()) {
								allFileInDirectory(temp.getPath(), fileMap,filter,
										isSub, isHidden,prefixFile.getPath());
							}
						} else {
							allFileInDirectory(temp.getPath(), fileMap, filter,isSub,
									isHidden,prefixFile.getPath());
						}
					}
				}

			}
		}
	}
	public static void closeOutputStream(OutputStream out) {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
	}

	public static void closeInputStream(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
	}
	/**
	 * copy 文件到另一个文件
	 * @param from
	 * @param to
	 */
	public static void copy(String from, String to) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(from);
			out = new FileOutputStream(to);
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = in.read(buff)) != -1) {
				out.write(buff, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeInputStream(in);
			closeOutputStream(out);
		}
	}
	
	public static void copyFiles(File copyFile,File to,String fileName){
		if(copyFile.isDirectory()){
			return ;
		}
		String tempfrom = copyFile.getAbsolutePath();
		String tempto = "";//后面的路径 替换前面的路径名
		String dirTo = to.getPath();
		if (dirTo.endsWith(File.separator)) {
			tempto = dirTo + fileName;
		} else {
			tempto = dirTo + File.separator + fileName;
		}
		//system.out.println("源文件:" + copyFile.getAbsolutePath());
		int endindex = tempto.lastIndexOf(File.separator);// 找到"/"所在的位置
		String mkdirPath = tempto.substring(0, endindex);
		File tempFile = new File(mkdirPath);
		if(!tempFile.exists()){
			tempFile.mkdirs();// 创建立文件夹 
		}
		//system.out.println("目标点:" + tempto);
		copy(tempfrom, tempto);
	}
	
	public static void listFileInDir(File dirFrom,File dirTo,boolean isSub, boolean isHidden ) {
		if(!dirFrom.isDirectory()){
			return ;
		}
		File[] files = dirFrom.listFiles();
		for (File f : files) {
			if(!isHidden && f.isHidden()){
				continue ;
			}
			String tempfrom = f.getAbsolutePath();
			String tempto = tempfrom.replace(dirFrom.getAbsolutePath(),
					dirTo.getAbsolutePath()); // 后面的路径 替换前面的路径名 
			if (f.isDirectory() && isSub) {
				File tempFile = new File(tempto);
				tempFile.mkdirs();
				listFileInDir(f,tempFile,isSub,isHidden);
			} 
			if (!f.isDirectory()) {
				copyFiles(f, new File(f.getAbsolutePath()),f.getName());
			}
		}
	}
	
	public static void main(String[] args){
		try {
			byte[] data = readContentReturnFlow(new File("C:\\Users\\xufangliang\\Pictures\\收藏\\Noname.bmp"));
			createImage("123.jpg", data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
