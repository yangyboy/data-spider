package com.data.spider;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestProxyIP {

	/**
	 * 测试代理IP的可用性
	 * @throws Exception
	 */
	@Test
	public void testConn() throws Exception{
		URL url = new URL("https://www.sogou.com");
		//代理服务器
		InetSocketAddress proxyAddr = new InetSocketAddress("113.161.116.90", 8080);
		Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddr);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
		connection.setReadTimeout(4000);
		connection.setConnectTimeout(4000);
		connection.setRequestMethod("GET");


		if(connection.getResponseCode() == 200){
			System.out.println("可用");
		}else{
			System.out.println("不可用");
		}


	}

	@Test
	public void testConn2() throws Exception{
		URL url = new URL("http://www.ip138.com");
		//代理服务器
		InetSocketAddress proxyAddr = new InetSocketAddress("113.161.116.90", 8080);
		Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddr);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
		connection.setReadTimeout(3000);
		connection.setConnectTimeout(3000);
		connection.setRequestMethod("GET");

		if(connection.getResponseCode() == 200){
			InputStream inputStream = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line = "";
			while((line = reader.readLine()) != null){
				System.out.println(line);
			}
		}else{
			System.out.println("不可用");
		}
	}
}
