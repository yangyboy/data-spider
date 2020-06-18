package com.data.spider.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.text.MessageFormat;

@Slf4j
public class CheckIPUtils {
	/**
	 * 校验代理IP的有效性，测试地址为：http://www.ip138.com
	 * @param ip 代理IP地址
	 * @param port  代理IP端口
	 * @return  此代理IP是否有效
	 */
	public static boolean checkValidIP(String ip,Integer port){
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL("http://www.ip138.com");
			//代理服务器
			InetSocketAddress proxyAddr = new InetSocketAddress(ip, port);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddr);
			connection = (HttpURLConnection) url.openConnection(proxy);
			connection.setReadTimeout(4000);
			connection.setConnectTimeout(4000);
			connection.setRequestMethod("GET");
			log.info(MessageFormat.format("代理IP[{0}:{1}] 返回码为：{2}", ip,port.toString(),connection.getResponseCode()));
			if(connection.getResponseCode() == 200){
				return true;
			}
			
		} catch (Exception e) {
			log.error(MessageFormat.format("代理IP[{0}:{1}]不可用", ip,port.toString()));
		}finally {
            if(connection != null){
                connection.disconnect();
            }
        }
		return false;
	}


	public static boolean isValid(String ip,Integer port) {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
		try {
			URLConnection httpCon = new URL("http://www.ip138.com").openConnection(proxy);
			httpCon.setConnectTimeout(5000);
			httpCon.setReadTimeout(5000);
			int code = ((HttpURLConnection) httpCon).getResponseCode();
			System.out.println(code);
			return code == 200;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static void main(String[] args) {
		System.out.println(isValid("121.8.146.99",8060));
	}


}
