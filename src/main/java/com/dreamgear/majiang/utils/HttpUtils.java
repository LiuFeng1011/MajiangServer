package com.dreamgear.majiang.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.dreamgear.majiang.common.GameTools;

public class HttpUtils {

	public static String GetHttpData(String url,String param,String Method ){
		PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestMethod(Method);
            if(Method.equals("POST")){
            	// 发送POST请求必须设置如下两行
                conn.setDoOutput(true);
                conn.setDoInput(true);
                // 获取URLConnection对象对应的输出流
                out = new PrintWriter(conn.getOutputStream());
                // 发送请求参数
                if(!"".equals(param)){
                	out.print(param);
                }
                
                // flush输出流的缓冲
                out.flush();
            }
            
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;

            while ((line = in.readLine()) != null) {
                result += line;
            }

        } catch (Exception e) {
            System.out.println("发送 HTTP 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
		
	}
	
	
	//

    public static void main( String[] args ) throws Exception
    {
    	String url = "https://api.weixin.qq.com/sns/userinfo?"
    			+ "access_token=E5wfUXNsle72A2D9StN4-bh4846sveRD88B6Me-8onLW5c0YQcJ10a5RVe4ndwIpB6-DCGe-0jjRWsR898_1FZC-9YxT2T51deT1dsFcsmk"
    			+ "&openid=oL95_v7iZF_bEOvWEVqoNchFd7fk"
    			+ "&lang=zh_CN";
    	String s  = GetHttpData(url,"","GET");
    	GameTools.Log(s);
    }
}
