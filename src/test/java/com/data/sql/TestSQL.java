package com.data.sql;


import com.data.entity.ProxyIp;
import com.data.service.IProxyIpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 测试基本数据库连接
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSQL {

	@Autowired
	private IProxyIpService proxyIpService;
	

	
	@Test
	public void testProxyIPAll(){
		List<ProxyIp> list = proxyIpService.list();
		list.forEach(System.out::println);
	}

	@Test
	public void testSelectProxyIPByIpAndPort(){
		ProxyIp ip = proxyIpService.selectByIpAndPort("60.191.45.92",8080);
		System.out.println(ip);
	}

}
