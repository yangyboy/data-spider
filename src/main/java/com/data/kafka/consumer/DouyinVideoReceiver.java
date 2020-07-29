package com.data.kafka.consumer;

import com.alibaba.fastjson.JSONObject;
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
	@KafkaListener(topics = {"${mq.topicName.douyin.video}"},groupId = "group-data-spider")
	public void handle(JSONObject videoJsonObj){
		douyinVideoService.handDouyinVideoData(videoJsonObj);
	}

}
