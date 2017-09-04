package com.dreamgear.majiang.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * zip压缩，解压缩类
 * 
 * @author xufangliang
 * 
 */
public class ZipUtils {

	static Logger logger = LoggerFactory.getLogger(ZipUtils.class);

	static int readSize = 1024;

	/**
	 * path地址过滤
	 * 
	 * @param path
	 * @return
	 */
	public static String pathSeparator(String path) {
		if (!path.endsWith(File.separator)) {
			path = path + File.separator;
		}
		return path;
	}

	/**
	 * 关闭输出流
	 * 
	 * @param out
	 */
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

	/**
	 * 关闭输入流
	 * 
	 * @param in
	 */
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
	 * 文件打成zip文件
	 * 
	 * @param fileName
	 * @param zipName
	 * @throws IOException
	 */
	public static void zip(String fileName, String zipName) throws IOException {
		ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(
				pathSeparator("") + zipName));
		File f = new File(pathSeparator("") + fileName);
		ZipEntry ze = new ZipEntry(f.getName());
		ze.setTime(f.lastModified());
		zip.putNextEntry(ze);
		InputStream in = new FileInputStream(f);
		byte[] b = new byte[readSize];
		while (in.read(b) != -1) {
			zip.write(b);
		}
		zip.flush();
		zip.close();
	}

	/**
	 * 获取一组文件打包
	 * 
	 * @param fileLst
	 * @param zipPath
	 */
	public static void zipAllFiles(List<File> fileLst, String zipPath) {
		if (fileLst != null && fileLst.size() > 0) {
			ZipOutputStream zip = null;
			try {
				// 生城的zip文件地址
				zip = new ZipOutputStream(new FileOutputStream(zipPath));
				for (File f : fileLst) {
					ZipEntry ze = new ZipEntry(f.getName());
					ze.setTime(f.lastModified());
					zip.putNextEntry(ze);
					InputStream in = null;
					try {
						in = new FileInputStream(f);
						byte[] b = new byte[readSize];
						while (in.read(b) != -1) {
							zip.write(b);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						closeInputStream(in);
					}

				}
				zip.flush();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			} finally {
				closeOutputStream(zip);
			}
		}
	}

	/**
	 * 解压文件
	 * 
	 * @param zippath
	 *            压缩文件路径
	 * @param savepath
	 *            解压出文件保存路径
	 */
	public static void unZips(String zippath, String savepath) {
		// 过滤path地址
		savepath = pathSeparator(savepath);
		int count = -1;
		int index = -1;
		BufferedOutputStream bos = null;
		ZipInputStream zis = null;
		FileInputStream fis = null;
		try {
			bos = null;
			ZipEntry entry = null;
			fis = new FileInputStream(zippath);
			zis = new ZipInputStream(new BufferedInputStream(fis));
			while ((entry = zis.getNextEntry()) != null) {
				byte data[] = new byte[readSize];

				String temp = entry.getName();
				index = temp.lastIndexOf("/");
				if (index > -1)
					temp = temp.substring(index + 1);
				temp = savepath + temp;
				File f = new File(temp);
				f.createNewFile();

				FileOutputStream fos = new FileOutputStream(f);
				bos = new BufferedOutputStream(fos, readSize);

				while ((count = zis.read(data, 0, readSize)) != -1) {
					bos.write(data, 0, count);
				}

				bos.flush();
				bos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeInputStream(fis);
			closeOutputStream(bos);
			closeInputStream(zis);
		}
	}

	/**
	 * 把文件下的所有文件打成zip包
	 * 
	 * @param directoryPath
	 * @param outPath
	 */
	public static void zipInPath(String directoryPath, String outPath,
			String filter) {
		Map<String, File> fileMap = new HashMap<String, File>();
		FileUtil.allFileInDirectory(directoryPath, fileMap, filter, true,
				false, directoryPath);
		zipAllFiles(new ArrayList<File>(fileMap.values()), outPath);
	}

	/*
	 * inputFileName 输入一个文件夹 zipFileName 输出一个压缩文件夹
	 */
	public static void zipFolder(String inputFileName, String zipFileName)
			throws Exception {
		//system.out.println(zipFileName);
		int endindex = zipFileName.lastIndexOf(File.separator);// 找到"/"所在的位置
		String mkdirPath = zipFileName.substring(0, endindex);
		File tempFile = new File(mkdirPath);
		if(!tempFile.exists()){
			tempFile.mkdirs();// 创建立文件夹 
		}
		zip(zipFileName, new File(inputFileName));
	}

	private static void zip(String zipFileName, File inputFile)
			throws Exception {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				zipFileName));
		zip(out, inputFile, "");
		//system.out.println("zip done");
		out.close();
	}

	private static void zip(ZipOutputStream out, File f, String base)
			throws Exception {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		} else {
			out.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			int b = -1;
			//system.out.println(base);
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
		}
	}
}
