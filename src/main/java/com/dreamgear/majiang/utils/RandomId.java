package com.dreamgear.majiang.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomId {
	private Random random;  
    private String table;  
    public RandomId() {  
        random = new Random();  
        table = "0123456789";  
    }  
    public String randomId(int id) {  
        String ret = null,  
            num = String.format("%05d", id);  
        int key = random.nextInt(10),   
            seed = random.nextInt(100);  
        Caesar caesar = new Caesar(table, seed);  
        num = caesar.encode(key, num);  
        ret = num   
            + String.format("%01d", key)   
            + String.format("%02d", seed);  
          
        return ret;  
    }  
    public static void main(String[] args) {  
        RandomId r = new RandomId();  
        Map<Integer,Integer> map = new HashMap<Integer,Integer>();
        for (int i = 0; i < 800000; i += 1) {
        	map.put(Integer.parseInt(r.randomId(i)), 0);
            ////system.out.println(r.randomId(i));  
        }  
        //system.out.println(map.size());
    }  

}
