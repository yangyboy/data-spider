package com.data.kafka.producer;

import com.data.kafka.dto.DouyinUserQueryDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@Slf4j
public class DouYinUserDataQuerySender {

	private final KafkaTemplate<Object,Object> kafkaTemplate;

	@Autowired
	public DouYinUserDataQuerySender(KafkaTemplate<Object, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	/**
	 * 抖音用户信息查询推送
	 * @param topicName 队列的名称
	 * @param dto 查询参数
	 */
	public void sender(String topicName, DouyinUserQueryDTO dto){
        log.debug(MessageFormat.format("开始向Kafka推送数据，topicName:{0}", topicName));

        try {
            kafkaTemplate.send(topicName, dto);
			log.debug("推送数据成功！");
        } catch (Exception e) {
			log.error(MessageFormat.format("推送数据出错，topicName:{0}"
                    ,topicName),e);
        }
	}
}
