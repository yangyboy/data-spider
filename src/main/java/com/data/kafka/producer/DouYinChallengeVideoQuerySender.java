package com.data.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@Slf4j
public class DouYinChallengeVideoQuerySender {

	private final KafkaTemplate<Object,Object> kafkaTemplate;

	@Autowired
	public DouYinChallengeVideoQuerySender(KafkaTemplate<Object, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	/**
	 * 抖音视频数据入库推送
	 * @param topicName 队列的名称
	 * @param cid 话题id
	 */
	public void sender(String topicName, String cid){
        log.debug(MessageFormat.format("开始向Kafka推送数据，topicName:{0}", topicName));

        try {
            kafkaTemplate.send(topicName, cid);
			log.debug("推送数据成功！");
        } catch (Exception e) {
			log.error(MessageFormat.format("推送数据出错，topicName:{0}"
                    ,topicName),e);
        }
	}
}
