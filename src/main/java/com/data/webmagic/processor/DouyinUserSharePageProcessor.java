package com.data.webmagic.processor;

import com.data.webmagic.utils.AgentUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * 西刺代理ip抓取
 */
@Component
@Slf4j
public class DouyinUserSharePageProcessor implements PageProcessor {

	@Override
	public Site getSite() {
		Site site = Site.me().setTimeOut(6000).setRetryTimes(3)
				.setSleepTime(2000).setCharset("UTF-8").addHeader("Accept-Encoding", "/")
				.setUserAgent(AgentUtils.radomWebAgent());
		
		return site;
	}

	@Override
	public void process(Page page) {
		log.info(page.getHtml().toString());
		List<String> videoList = page.getHtml().xpath("//div[@id='pagelet-worklist']/ul/li@data-id").all();
		List<String> result = new ArrayList<>();
	
//		if(videoList != null && videoList.size() > 0){
//			for(String tmp : videoList){
//				Html html = Html.create(tmp);
//				html.xpath("@src");
//				String[] data = html.xpath("//body/text()").toString().trim().split("\\s+");
//
//
//
//				result.add(proxyIp);
//			}
//		}
		page.putField("result", videoList);
	}

}
