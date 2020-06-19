package com.data.tasks;

import com.data.entity.ProxyIp;
import com.data.kafka.dto.ProxyIpDTO;
import com.data.kafka.producer.CheckIpSender;
import com.data.service.ICrawlService;
import com.data.service.IProxyIpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.Format;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 定时任务配置
 */
@Component
@Slf4j
public class ScheduledTasks {
    private final Format FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Value("${mq.topicName.checkIP}")
    private String checkIPTopicName;

    @Value("${mq.topicName.weather}")
    private String weatherTopicName;

    @Resource
    private IProxyIpService proxyIpService;
//
//    @Resource(name = "weatherStationManager")
//    private WeatherStationManager weatherStationManager;
//
    @Resource
    private CheckIpSender checkIpSender;
//
//    @Resource(name="weatherUpdateSender")
//    private WeatherUpdateSender weatherUpdateSender;
//
    @Resource
    private ICrawlService crawlService;

    /**
     * 天气定时更新任务
     * @author zifangsky
     * @date 2018/6/21 13:43
     * @since 1.0.0
     */
//    @Scheduled(cron = "${task.updateWeather.schedule}")
//    public void updateWeatherTask(){
//        Date current = new Date();
//        log.debug(MessageFormat.format("开始执行天气定时更新任务，Date：{0}",FORMAT.format(current)));
//
//        List<WeatherStation> list = weatherStationManager.selectAll();
//        if(list != null && list.size() > 0){
//            list.forEach(station -> {
//                weatherUpdateSender.updateWeather(weatherTopicName, station.getCode());
//            });
//        }
//    }

    /**
     * 代理IP定时检测任务（检查是否有效）
     */
    @Scheduled(cron = "${task.checkProxyIp.schedule}")
    public void checkProxyIpTask(){
        Date current = new Date();
        log.debug(MessageFormat.format("开始执行代理IP定时检测任务，Date：{0}",FORMAT.format(current)));

        //1 查询数据库中所有代理IP
        List<ProxyIp> list = proxyIpService.list();

        if(list != null && list.size() > 0){
            //2 遍历
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
        }
    }

    /**
     * 西刺代理IP定时获取任务
     */
    @Scheduled(cron = "${task.crawlProxyIp_1.schedule}")
    public void crawlProxyIpTask1(){
        Date current = new Date();
        log.debug(MessageFormat.format("开始执行西刺代理IP定时获取任务，时间：{0}",FORMAT.format(current)));

        crawlService.xiciProxyIPCrawl();
    }

    /**
     * 快代理IP定时获取任务
     */
    @Scheduled(cron = "${task.crawlProxyIp_2.schedule}")
    public void crawlProxyIpTask2(){
        Date current = new Date();
        log.debug(MessageFormat.format("开始执行快代理IP定时获取任务，时间：{0}",FORMAT.format(current)));

        crawlService.kuaidailiProxyIPCrawl();
    }

}
