package com.data.kafka.consumer;

import com.alibaba.fastjson.JSON;
import com.data.kafka.dto.DouyinUserQueryDTO;
import com.data.service.IDouyinUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class DouyinUserDataQueryReceiver {

	@Resource
	private IDouyinUserService douyinUserService;

	/**
	 * 接收消息并处理
	 * @param dto
	 */
	@KafkaListener(topics = {"${mq.topicName.douyin.user.query}"},groupId = "group-data-spider")
	public void handle(DouyinUserQueryDTO dto){
		log.info("收到消息:{}", JSON.toJSONString(dto));

		douyinUserService.queryDouyinUserInfo(dto);
	}

}
