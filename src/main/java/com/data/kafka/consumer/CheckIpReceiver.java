package com.data.kafka.consumer;

import com.alibaba.fastjson.JSON;
import com.data.entity.ProxyIp;
import com.data.kafka.dto.ProxyIpDTO;
import com.data.service.IProxyIpService;
import com.data.webmagic.utils.CheckIPUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Objects;

@Component
@Slf4j
public class CheckIpReceiver {

	@Resource
	private IProxyIpService proxyIpService;

	/**
	 * 接收消息并处理
	 * 验证代理ip是否可用，可用ip入库，不可用ip抛弃
	 * @param proxyIpDTO 待处理的代理IP
	 */
	@KafkaListener(topics = {"${mq.topicName.checkIP}"},groupId = "group-ip-spider")
	public void handle(ProxyIpDTO proxyIpDTO) {
        log.debug(MessageFormat.format("接收到消息，代理IP:{0}:{1}", proxyIpDTO.getIp(),proxyIpDTO.getPort()));

		boolean usable = CheckIPUtils.checkValidIP(proxyIpDTO.getIp(), proxyIpDTO.getPort());
		// 根据该IP是待入库的新IP或者数据库中的旧IP分两种情况判断
		if (proxyIpDTO.getCheckType() == ProxyIpDTO.CheckIPType.ADD) {
			if (usable) {
				// 1 查询该IP是否已存在
				ProxyIp existsIp = proxyIpService.selectByIpAndPort(proxyIpDTO.getIp(), proxyIpDTO.getPort());
				// 2如果不存在则插入数据
				if (Objects.isNull(existsIp)) {
					proxyIpService.save(proxyIpDTO);
				}
			}
		} else if (proxyIpDTO.getCheckType() == ProxyIpDTO.CheckIPType.UPDATE) {
			// 不能使用则删除
			if (!usable) {
				proxyIpService.removeById(proxyIpDTO.getId());
			}
		}

	}

}
