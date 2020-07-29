package com.data.kafka.consumer;

import com.alibaba.fastjson.JSON;
import com.data.service.IDouyinChallengeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class DouyinChallengeVideoQueryReceiver {

	@Resource
	private IDouyinChallengeService douyinChallengeService;

	/**
	 * 接收消息并处理
	 * @param cid
	 */
	@KafkaListener(topics = {"${mq.topicName.douyin.challenge.video}"},groupId = "group-data-spider")
	public void handle(String cid){
		log.info("收到消息:{}", JSON.toJSONString(cid));

		douyinChallengeService.queryDouyinChallengeVideo(cid);
	}

}
