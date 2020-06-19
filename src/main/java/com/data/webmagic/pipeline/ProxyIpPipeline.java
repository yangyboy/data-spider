package com.data.webmagic.pipeline;

import com.data.entity.ProxyIp;
import com.data.kafka.dto.ProxyIpDTO;
import com.data.kafka.producer.CheckIpSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * 自定义Pipeline处理抓取的数据
 * @author zifangsky
 *
 */
@Component
public class ProxyIpPipeline implements Pipeline {

	@Value("${mq.topicName.checkIP}")
	private String checkIPTopicName;
	
	private final CheckIpSender checkIPSender;
	@Autowired
	public ProxyIpPipeline(CheckIpSender checkIPSender) {
		this.checkIPSender = checkIPSender;
	}

	/**
	 * 保存数据
	 */
	@Override
	public void process(ResultItems resultItems, Task task) {
		List<ProxyIp> list = resultItems.get("result");
		
		if(list != null && list.size() > 0){
			list.forEach(proxyIp -> {
				ProxyIpDTO proxyIpDTO = new ProxyIpDTO();
				proxyIpDTO.setId(proxyIp.getId());
				proxyIpDTO.setIp(proxyIp.getIp());
				proxyIpDTO.setPort(proxyIp.getPort());
				proxyIpDTO.setType(proxyIp.getType());
				proxyIpDTO.setAddr(proxyIp.getAddr());
				proxyIpDTO.setCheckType(ProxyIpDTO.CheckIPType.ADD);

				//检测任务添加到队列中
				checkIPSender.send(checkIPTopicName, proxyIpDTO);
			});
		}

	}

}
