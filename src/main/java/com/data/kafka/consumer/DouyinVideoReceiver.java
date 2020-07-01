package com.data.kafka.consumer;

import com.alibaba.fastjson.JSONObject;
import com.data.service.IDouyinRespFileScanService;
import com.data.service.IDouyinVideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class DouyinVideoReceiver {

	@Resource
	private IDouyinVideoService douyinVideoService;

	/**
	 * 接收消息并处理
	 * @param videoJsonObj
	 */
	@KafkaListener(topics = {"${mq.topicName.douyin.video}"},groupId = "group-douyin-video-spider")

	public void handle(JSONObject videoJsonObj){
		log.debug("收到消息:{}",videoJsonObj);
		douyinVideoService.handDouyinVideoData(videoJsonObj);


	}

}
