package com.data.service;

public interface ICrawlService {
	
	/**
	 * 天气爬虫
	 * @param stationCode 县城（区）的CODE
	 */
//	public void weatherCrawl(String stationCode);
	
	/**
	 * 西刺代理IP爬虫，地址：http://www.xicidaili.com
	 */
	public void xiciProxyIPCrawl();
	
	/**
	 * 快代理 代理IP爬虫，地址：http://www.kuaidaili.com
	 */
	public void kuaidailiProxyIPCrawl();
}