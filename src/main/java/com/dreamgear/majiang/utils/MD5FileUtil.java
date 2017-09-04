package com.dreamgear.majiang.utils;

import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

/**
 * MD5比较文件字符
 * 
 * @author xufangliang
 * 
 */
public class MD5FileUtil {
    public static String getHash(String path) throws IOException{
    	File file = new File(path);
    	HashCode hashCode = Files.hash(file, Hashing.md5());
    	return hashCode.toString();

    }
}
