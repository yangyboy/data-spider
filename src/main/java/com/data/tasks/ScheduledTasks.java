package com.data.tasks;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.data.constant.CommonConst;
import com.data.entity.DouyinChallenge;
import com.data.entity.ProxyIp;
import com.data.kafka.dto.ProxyIpDTO;
import com.data.kafka.producer.CheckIpSender;
import com.data.kafka.producer.DouYinChallengeVideoQuerySender;
import com.data.service.ICrawlService;
import com.data.service.IDouyinHotwordService;
import com.data.service.IDouyinRespFileScanService;
import com.data.service.IProxyIpService;
import com.data.service.IDouyinChallengeService;
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
    @Value("${mq.topicName.douyin.challenge.video}")
    private String challengeCrawlTopicName;

    @Resource
    private IProxyIpService proxyIpService;
    @Resource
    private CheckIpSender checkIpSender;
    @Resource
    private ICrawlService crawlService;
    @Resource
    private IDouyinRespFileScanService douyinRespFileScanService;

    @Resource
    private DouYinChallengeVideoQuerySender douYinChallengeVideoQuerySender;

    @Resource
    private IDouyinChallengeService douyinChallengeService;

    @Resource
    private IDouyinHotwordService douyinHotwordService;

    /**
     * 代理IP定时检测任务（检查是否有效）
     */
//    @Scheduled(cron = "${task.checkProxyIp.schedule}")
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
//    @Scheduled(cron = "${task.xiciCrawlProxyIp.schedule}")
    public void crawlProxyIpTask1(){
        Date current = new Date();
        log.debug(MessageFormat.format("开始执行西刺代理IP定时获取任务，时间：{0}",FORMAT.format(current)));

        crawlService.xiciProxyIPCrawl();
    }

    /**
     * 快代理IP定时获取任务
     */
//    @Scheduled(cron = "${task.kuaidailiCrawlProxyIp.schedule}")
    public void crawlProxyIpTask2(){
        Date current = new Date();
        log.debug(MessageFormat.format("开始执行快代理IP定时获取任务，时间：{0}",FORMAT.format(current)));

        crawlService.kuaidailiProxyIPCrawl();
    }



    /**
     * 扫描抖音app接口返回的数据文件，将抖音app接口返回的数据文件读取进系统，发送至kafka
     */
    @Scheduled(cron = "${task.scaner.resp.schedule}")
    public void scanDyRespFile(){
        Date current = new Date();
        log.info(MessageFormat.format("开始执行抖音接口数据文件定时扫描任务，时间：{0}",FORMAT.format(current)));

        int scanner = douyinRespFileScanService.scanner();
        log.info(MessageFormat.format("扫描到文件数量：{0}",scanner));
    }


    /**
     * 扫描抖音热榜词，并入库
     */
//    @Scheduled(cron = "${task.hot.word.schedule}")
//    public void hotword(){
//        Date current = new Date();
//        log.info(MessageFormat.format("开始执行抖音热榜查询任务，时间：{0}",FORMAT.format(current)));
//
//        douyinHotwordService.hotSearch();
//    }

    /**
     * 话题视频爬取
     */
    @Scheduled(cron = "${task.challenge.video.schedule}")
    public void queryChallengeVideo(){
        Date current = new Date();
        log.debug(MessageFormat.format("开始执行话题视频爬取任务，Date：{0}",FORMAT.format(current)));

        //1 查询数据库中所有未爬取的话题
        List<DouyinChallenge> list = douyinChallengeService.list(new LambdaQueryWrapper<DouyinChallenge>()
                .eq(DouyinChallenge::getCrawlStatus, CommonConst.ChallengeConstant.CHALLENGE_NOT_CRAWL));

        if(list != null && list.size() > 0){
            //2 遍历
            list.forEach(douyinChallenge -> {
                //3 添加到队列中
                douYinChallengeVideoQuerySender.sender(challengeCrawlTopicName, douyinChallenge.getCid());
            });
        }
    }

}
