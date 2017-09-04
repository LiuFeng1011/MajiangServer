package com.dreamgear.majiang.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public  class PropertyManager {
	private   Properties properties = new Properties();
	private 	Enumeration<?> en =null;
	public PropertyManager(String fileName) throws FileNotFoundException, IOException{
		properties.load(new FileInputStream(fileName));
		en = properties.propertyNames();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			String Property = properties.getProperty(key);
			System.out.println(key + Property);
		}
	}

	/**
	 * 设置
	 * 
	 * @param key
	 * @param value
	 */
	public  void set(String key, String value) {
		properties.setProperty(key, value);
	}

	/**
	 * 保存
	 * 
	 * @param key
	 * @param value
	 */
	public void save(String fileName) {
		try {
			FileOutputStream outputFile = new FileOutputStream(fileName);                                                            
			properties.store(outputFile, "");// property类关键的store方法
			outputFile.close();
			// propertie.list(System.out);
			//system.out.println("PropertyManager File was saved!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public  String getString(String key) {
		return properties.getProperty(key);
	}

	public  int getInt(String key, int defaultValue) {
		String data = getString(key, String.valueOf(defaultValue));
		return Integer.parseInt(data);
	}

	public  boolean getBoolean(String key, boolean defaultValue) {
		if (!properties.containsKey(key)) {
			return defaultValue;
		}
		String data = properties.getProperty(key);
		return Boolean.parseBoolean(data);
	}

	public  String getString(String key, String defaultValue) {
		if (!properties.containsKey(key)) {
			return defaultValue;
		}
		return properties.getProperty(key);
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
