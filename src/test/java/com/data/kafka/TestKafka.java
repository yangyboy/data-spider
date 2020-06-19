package com.data.kafka;

import com.data.entity.ProxyIp;
import com.data.kafka.dto.ProxyIpDTO;
import com.data.kafka.producer.CheckIpSender;
import com.data.service.IProxyIpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测试通过Kafka发送和接收消息
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestKafka {
    @Value("${mq.topicName.checkIP}")
	private String checkIPTopicName;

    @Resource
    private IProxyIpService proxyIpService;

	@Resource
	private CheckIpSender checkIpSender;



    /**
     * 测试更新代理IP
     */
	@Test
	public void testSend() throws InterruptedException {
		//1 查询数据库中所有代理IP
		List<ProxyIp> list = proxyIpService.list();

		if(list != null && list.size() > 0){
			//2 遍历并检测其可用性
		    list.forEach(proxyIp -> {
				ProxyIpDTO proxyIpDTO = new ProxyIpDTO();
                proxyIpDTO.setId(proxyIp.getId());
                proxyIpDTO.setIp(proxyIp.getIp());
                proxyIpDTO.setPort(proxyIp.getPort());
                proxyIpDTO.setType(proxyIp.getType());
                proxyIpDTO.setAddr(proxyIp.getAddr());
                proxyIpDTO.setCheckType(ProxyIpDTO.CheckIPType.UPDATE);

				//3 添加到队列中
                checkIpSender.send(checkIPTopicName, proxyIpDTO);
			});

            //暂停线程，查看效果
            Thread.sleep(1000 * 60);
        }

    }

}
