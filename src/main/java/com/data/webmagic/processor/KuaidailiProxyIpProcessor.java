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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 快代理 ip 抓取
 */
@Component
@Slf4j
public class KuaidailiProxyIpProcessor implements PageProcessor {

	@Override
	public Site getSite() {
		Site site = Site.me().setTimeOut(6000).setRetryTimes(3)
				.setSleepTime(1000).setCharset("UTF-8").addHeader("Accept-Encoding", "/")
				.setUserAgent(AgentUtils.radomAgent());
		
		return site;
	}

	@Override
	public void process(Page page) {
		List<String> ipList = page.getHtml().xpath("//table[@class='table table-bordered table-striped']/tbody/tr").all();
		List<ProxyIp> result = new ArrayList<>();
	
		if(ipList != null && ipList.size() > 0){
			for(String tmp : ipList){
				Html html = Html.create(tmp);
				ProxyIp proxyIp = new ProxyIp();
				String[] data = html.xpath("//body/text()").toString().trim().split("\\s+");
				String dataStr = html.xpath("//body/text()").toString();
				
				proxyIp.setIp(data[0]);
				proxyIp.setPort(Integer.valueOf(data[1]));
				
				Pattern pattern = Pattern.compile("HTTPS?\\s(.*)?\\s\\d秒");
				Matcher matcher = pattern.matcher(dataStr);
				if(matcher.find()){
					proxyIp.setAddr(matcher.group(1));
				}

				proxyIp.setType(data[3]);
				
				result.add(proxyIp);
			} 
		}
		page.putField("result", result);
		page.addTargetRequest("https://www.kuaidaili.com/free/inha/2/");
		page.addTargetRequest("https://www.kuaidaili.com/free/intr/1/");
	}

}
