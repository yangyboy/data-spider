package com.data.webmagic.pipeline;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

@Component
public class DouyinUserSharePagePipeline implements Pipeline {

//	@Value("${mq.topicName.checkIP}")
//	private String checkIPTopicName;
//
//	private final CheckIpSender checkIPSender;
//	@Autowired
//	public DouyinUserSharePagePipeline(CheckIpSender checkIPSender) {
//		this.checkIPSender = checkIPSender;
//	}

	/**
	 * 保存数据
	 */
	@Override
	public void process(ResultItems resultItems, Task task) {
		List<String> list = resultItems.get("result");
		
		if(list != null && list.size() > 0){

			list.forEach(System.out::println);
//			list.forEach(videoId -> {
//
//				//检测任务添加到队列中
////				checkIPSender.send(checkIPTopicName, proxyIpDTO);
//			});
		}

	}

}
