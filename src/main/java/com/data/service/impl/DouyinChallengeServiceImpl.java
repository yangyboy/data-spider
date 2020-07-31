package com.data.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.constant.CommonConst;
import com.data.entity.DouyinChallenge;
import com.data.mapper.DouyinChallengeMapper;
import com.data.service.IDouyinChallengeService;
import com.data.service.IDouyinVideoService;
import com.data.util.JsoupUtl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;

@Service
@Slf4j
public class DouyinChallengeServiceImpl extends ServiceImpl<DouyinChallengeMapper, DouyinChallenge> implements IDouyinChallengeService {

    @Value("${douyin.challenge.video}")
    private String challengeVideoUrl;

    @Resource
    private IDouyinChallengeService douyinChallengeService;

    @Resource
    private IDouyinVideoService douyinVideoService;

    @Override
    public void queryDouyinChallengeVideo(String cid) {

        int count = 100;

        int cursor = 0;

        for (; ; ) {

            String formatUrl = MessageFormat.format(challengeVideoUrl, cid, count, cursor);
            String message = JsoupUtl.getMessage(formatUrl);
            if (StrUtil.isEmpty(message) || !message.startsWith("{")) {
                log.info("抖音话题视频查询失败，查询结果为：{}", message);
                try {
                    Thread.sleep(60000L);
                } catch (InterruptedException e) {
                    log.info("爬取话题视频线程被中断",e);
                }
                continue;
            }
            JSONObject videoSearchObj = JSON.parseObject(message);


            boolean hasMore = videoSearchObj.getBooleanValue("has_more");

            if (!hasMore) {
                break;
            }
            videoSearchObj.put("source", CommonConst.VideoConstant.VIDEO_SOURCE_CHALLENGE);
            videoSearchObj.put("cid", cid);

            douyinVideoService.handDouyinVideoData(videoSearchObj);

            cursor += count;

            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                log.info("爬取话题视频线程被中断",e);
            }

        }

        //证明该话题已爬取完成，修改状态为已爬取
        douyinChallengeService.update(new LambdaUpdateWrapper<DouyinChallenge>().
                eq(DouyinChallenge::getCid, cid)
                .set(DouyinChallenge::getCrawlStatus,CommonConst.ChallengeConstant.CHALLENGE_ALREADY_CRAWL));


    }
}
