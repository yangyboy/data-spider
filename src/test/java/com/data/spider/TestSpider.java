package com.data.spider;

import com.data.kafka.dto.ProxyIpDTO;
import com.data.service.ICrawlService;
import com.data.service.IProxyIpService;
import com.data.webmagic.utils.CheckIPUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 测试基于WebMagic框架的爬虫效果
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSpider{
    @Resource
    private IProxyIpService proxyIpService;

    @Resource
    private ICrawlService crawlService;

    /**
     * 测试检测代理IP是否可用
     */
    @Test
	public void testCheckProxyIp(){
        ProxyIpDTO proxyIpDTO = new ProxyIpDTO();
        proxyIpDTO.setIp("113.161.116.90");
        proxyIpDTO.setPort(8080);

        System.out.println(CheckIPUtils.checkValidIP(proxyIpDTO.getIp(), proxyIpDTO.getPort()));
    }

    /**
     * 测试爬取西刺ip代理
     */
    @Test
    public void xiciProxyIPCrawl(){
        crawlService.xiciProxyIPCrawl();
    }

    /**
     * 测试爬取快代理ip
     */
    @Test
    public void kuaidailiProxyIPCrawl(){
        crawlService.kuaidailiProxyIPCrawl();
    }


    @Test
    public void douyinCrawl(){
        crawlService.DouyinUserSharePageCrawl();
    }

}
