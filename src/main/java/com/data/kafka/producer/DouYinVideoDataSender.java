package com.data.kafka.producer;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@Slf4j
public class DouYinVideoDataSender {

	private final KafkaTemplate<Object,Object> kafkaTemplate;

	@Autowired
	public DouYinVideoDataSender(KafkaTemplate<Object, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	/**
	 * 抖音视频数据入库推送
	 * @param topicName 抖音视频队列的名称
	 * @param jsonData 抓取到的抖音视频搜索数据
	 */
	public void sender(String topicName, JSONObject jsonData){
        log.debug(MessageFormat.format("开始向Kafka推送数据，topicName:{0}", topicName));

        try {
            kafkaTemplate.send(topicName, jsonData);
			log.debug("推送数据成功！");
        } catch (Exception e) {
			log.error(MessageFormat.format("推送数据出错，topicName:{0}"
                    ,topicName),e);
        }
	}
}
