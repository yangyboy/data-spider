package com.data.kafka.producer;

import com.data.kafka.dto.ProxyIpDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@Slf4j
public class CheckIpSender {

    private final KafkaTemplate<Object,Object> kafkaTemplate;

    @Autowired
    public CheckIpSender(KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
	 * 发送检测某个代理IP可用性的消息到指定队列
	 * @param topicName  队列名称
	 * @param proxyIpDTO  消息内容
	 */
	public void send(String topicName, ProxyIpDTO proxyIpDTO){
        log.info(MessageFormat.format("开始向Kafka推送数据，topicName：{0}，代理IP：{1}",topicName, proxyIpDTO));

        try {
            kafkaTemplate.send(topicName, proxyIpDTO);
            log.info("推送数据成功！");
        } catch (Exception e) {
            log.error(MessageFormat.format("推送数据出错，topicName:{0},代理IP:{1}"
                    ,topicName,proxyIpDTO),e);
        }
	}

}
