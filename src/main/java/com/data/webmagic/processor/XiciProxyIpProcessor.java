package com.data.webmagic.processor;

import com.data.entity.ProxyIp;
import com.data.webmagic.utils.AgentUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;

/**
 * 西刺代理ip抓取
 */
@Component
@Slf4j
public class XiciProxyIpProcessor implements PageProcessor {

	@Override
	public Site getSite() {
		Site site = Site.me().setTimeOut(6000).setRetryTimes(3)
				.setSleepTime(1000).setCharset("UTF-8").addHeader("Accept-Encoding", "/")
				.setUserAgent(AgentUtils.radomAgent());
		
		return site;
	}

	@Override
	public void process(Page page) {
		List<String> ipList = page.getHtml().xpath("//table[@id='ip_list']/tbody/tr").all();
		List<ProxyIp> result = new ArrayList<>();
	
		if(ipList != null && ipList.size() > 0){
			ipList.remove(0);  //移除表头
			for(String tmp : ipList){
				Html html = Html.create(tmp);
				ProxyIp proxyIp = new ProxyIp();
				String[] data = html.xpath("//body/text()").toString().trim().split("\\s+");
				
				proxyIp.setIp(data[0]);
				proxyIp.setPort(Integer.valueOf(data[1]));
				proxyIp.setAddr(html.xpath("//a/text()").toString());
				proxyIp.setType(data[3]);
				
				result.add(proxyIp);
			} 
		}
		page.putField("result", result);
		page.addTargetRequest("http://www.xicidaili.com/nn/2");
		page.addTargetRequest("http://www.xicidaili.com/nt/");
	}

}
