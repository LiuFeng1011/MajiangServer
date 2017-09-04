package com.dreamgear.majiang.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * json管理类
 * 
 * @author xufangliang
 * @since 2013-3-7
 */
public class JsonUtil {
    /**
     * //详细地址:
     * http://code.alibabatech.com/wiki/display/FastJSON/Tutorial
     *
     */
    static Logger logger = org.slf4j.LoggerFactory.getLogger(JsonUtil.class);
    
    private static final SerializerFeature[] features = { 
		SerializerFeature.WriteMapNullValue, // 输出空置字段
        SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
        SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
        SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
        SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
    };
    
    private static final SerializerFeature[] featuresWithClassName = { 
		SerializerFeature.WriteMapNullValue, // 输出空置字段
        SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
        SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
        SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
        SerializerFeature.WriteNullStringAsEmpty, // 字符类型字段如果为null，输出为""，而不是null
        SerializerFeature.WriteClassName
    };
    /**
     * 还原成对象list
     *
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> List<T> JsonToObjectList(String jsonString, Class<T> clazz) {
        try {
            if (StringUtil.isNull(jsonString, "")) {
                return new ArrayList<T>();
            } else {
                if (null == JSONArray.parseArray(jsonString,clazz)) {
                    return null;
                } else {
                    return JSONArray.parseArray(jsonString,clazz);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回类型的map
     * @param jsonString
     * 使用环境：
     * Map<T,F> map = JsonToObjectMap(str);
     * @return
     */
    public static <T, F> Map<T, F> JsonToObjectMap(String jsonString) {
        try {
            if (StringUtil.isNull(jsonString, "")) {
                return null;
            } else {
                return JSON.parseObject(jsonString,new TypeReference<Map<T, F>>(){});
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    /**
//     * 返回类型的map
//     * @param jsonString
//     * 直接将返回数据做参数
//     * 使用环境：
//     * 假设方法：get（Map<T, F> map）；
//     * get(JsonToObjectMap(str,new HashMap<T,F>()));
//     * @return
//     */
//    public static <T, F> Map<T, F> JsonToObjectMap(String jsonString, Map<T, F> map) {
//        try {
//            if (StringUtil.isNull(jsonString, "")) {
//                logger.info("jsonString=" + jsonString + " 错误");
//                return null;
//            } else {
//                logger.info("jsonString=" + jsonString + " 解析");
//                return JSON.parseObject(jsonString,new TypeReference<Map<T, F>>(){});
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    /**
     * 返回对应对象
     *
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> T JsonToObject(String jsonString, Class<T> clazz) {
        try {
            if (StringUtil.isNull(jsonString, "")) {
                return null;
            } else {
                if (JSON.parseObject(jsonString,clazz) != null) {
                    return JSON.parseObject(jsonString,clazz);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 将对象编程字符串
     * @return
     */
    public static String ObjectToJsonString(Object obj) {
        if (obj == null) {
            logger.info("obj=" + obj + "");
            return "";
        }
        return JSON.toJSONString(obj,features);
    }
    
    /**
     * 将对象编程字符串
     * @return
     */
    public static String ObjectToJsonStringWithClass(Object obj) {
        if (obj == null) {
            logger.info("obj=" + obj + "");
            return "";
        }
        return JSON.toJSONString(obj,featuresWithClassName);
    }
    static class A {
    	int id;
    	int age;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
    }
    

    public static void main(String[] args) {
    	Map<Long,Integer> aaa = new TreeMap<Long, Integer>();
    	aaa.put(100L, 11);
    	aaa.put(300L, 11);
    	//system.out.println(ObjectToJsonString(aaa));
	}
}
