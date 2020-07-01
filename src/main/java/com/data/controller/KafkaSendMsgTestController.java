package com.data.controller;

import com.data.entity.ProxyIp;
import com.data.kafka.dto.ProxyIpDTO;
import com.data.kafka.producer.CheckIpSender;
import com.data.service.IDouyinRespFileScanService;
import com.data.service.IProxyIpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/kafka")
public class KafkaSendMsgTestController {
    private final KafkaTemplate<Object,Object> kafkaTemplate;

    @Value("${mq.topicName.checkIP}")
    private String checkIPTopicName;


    private final CheckIpSender checkIpSender;


    private final IProxyIpService proxyIpService;

    @Resource
    private IDouyinRespFileScanService douyinRespFileScanService;

    @Autowired
    public KafkaSendMsgTestController(KafkaTemplate<Object, Object> kafkaTemplate, CheckIpSender checkIpSender, IProxyIpService proxyIpService) {
        this.kafkaTemplate = kafkaTemplate;
        this.checkIpSender = checkIpSender;
        this.proxyIpService = proxyIpService;
    }

    @RequestMapping(value = "/send")
    public String send(String msg){
        kafkaTemplate.send(checkIPTopicName,msg);
        return "success";
    }

    @RequestMapping(value = "/send2")
    public String send2(){

        ProxyIp proxyIp = proxyIpService.selectByIpAndPort("60.205.132.71", 80);
        ProxyIpDTO proxyIpDTO = new ProxyIpDTO();
        proxyIpDTO.setId(proxyIp.getId());
        proxyIpDTO.setIp(proxyIp.getIp());
        proxyIpDTO.setPort(proxyIp.getPort());
        proxyIpDTO.setType(proxyIp.getType());
        proxyIpDTO.setAddr(proxyIp.getAddr());
        proxyIpDTO.setCheckType(ProxyIpDTO.CheckIPType.UPDATE);
        checkIpSender.send(checkIPTopicName,proxyIpDTO);
//        kafkaTemplate.send(checkIPTopicName,proxyIp);
        return "success";
    }

    @RequestMapping(value = "/scanner")
    public Integer scanner(String msg){
        int scanner = douyinRespFileScanService.scanner();
        return scanner;
    }
}
