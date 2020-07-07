package com.data.service.impl;

import com.data.service.ICrawlService;
import com.data.webmagic.downloader.MyDownloader;
import com.data.webmagic.pipeline.DouyinUserSharePagePipeline;
import com.data.webmagic.pipeline.ProxyIpPipeline;
import com.data.webmagic.processor.DouyinUserSharePageProcessor;
import com.data.webmagic.processor.KuaidailiProxyIpProcessor;
import com.data.webmagic.processor.XiciProxyIpProcessor;
import com.data.webmagic.spider.MySpider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.model.OOSpider;

import javax.annotation.Resource;

@Service
@Slf4j
public class CrawlManagerImpl implements ICrawlService {
	
	@Resource
	private ProxyIpPipeline proxyIpPipeline;

	@Resource
	private XiciProxyIpProcessor xiciProxyIpProcessor;
	@Resource
	private KuaidailiProxyIpProcessor kuaidailiProxyIpProcessor;

	@Resource
	private DouyinUserSharePagePipeline douyinUserSharePagePipeline;

	@Override
	public void xiciProxyIPCrawl() {
		log.info("开始爬取西刺代理ip数据");

		MySpider mySpider = MySpider.create(xiciProxyIpProcessor);

		mySpider.setDownloader(new MyDownloader());
		//mySpider.setScheduler(new RedisScheduler(SITE_CODE));
		mySpider.addPipeline(proxyIpPipeline);

		mySpider.thread(3);


		//添加起始url
		Request request = new Request("http://www.xicidaili.com/nn/1");
		//在Request额外信息中设置页面类型
		//request.putExtra(YousuuProcessor.TYPE, YousuuProcessor.LIST_TYPE);
		mySpider.addRequest(request);

		mySpider.run();
	}

	@Override
	public void kuaidailiProxyIPCrawl() {
		log.info("开始爬取块代理代理ip数据");

		MySpider kuaidailiSpider = MySpider.create(kuaidailiProxyIpProcessor);
		kuaidailiSpider.setDownloader(new MyDownloader());
		kuaidailiSpider.addPipeline(proxyIpPipeline);
		kuaidailiSpider.thread(3);

		//添加起始url
		Request request = new Request("http://www.kuaidaili.com/free/inha/1/");
		//在Request额外信息中设置页面类型
		//request.putExtra(YousuuProcessor.TYPE, YousuuProcessor.LIST_TYPE);
		kuaidailiSpider.addRequest(request);

		kuaidailiSpider.run();
	}

	@Override
	public void DouyinUserSharePageCrawl() {
		log.info("开始爬取块抖音用户主页视频");

		MySpider kuaidailiSpider = MySpider.create(new DouyinUserSharePageProcessor());
		kuaidailiSpider.setDownloader(new MyDownloader());
		kuaidailiSpider.addPipeline(douyinUserSharePagePipeline);
		kuaidailiSpider.thread(3);

		//添加起始url
		Request request = new Request("https://www.iesdouyin.com/share/user/95214577702?sec_uid=MS4wLjABAAAAyRdTLx-miJHXnDuW-ttuF0jI-JdmxILF0G4F9JVx1jI");
		//在Request额外信息中设置页面类型
		kuaidailiSpider.addRequest(request);

		kuaidailiSpider.run();
	}


}
