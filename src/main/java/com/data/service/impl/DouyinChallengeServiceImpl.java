package com.data.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.constant.CommonConst;
import com.data.entity.DouyinChallenge;
import com.data.mapper.DouyinChallengeMapper;
import com.data.service.IDouyinVideoService;
import com.data.util.JsoupUtl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

@Service
@Slf4j
public class DouyinChallengeServiceImpl extends ServiceImpl<DouyinChallengeMapper, DouyinChallenge> implements IDouyinChallengeService {

    @Value("${douyin.challenge.video}")
    private String challengeVideoUrl;

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
                log.info("抖音热搜榜查询失败，查询结果为：{}", message);
            }
            JSONObject videoSearchObj = JSON.parseObject(message);


            boolean hasMore = videoSearchObj.getBooleanValue("has_more");

            if (!hasMore) {
                break;
            }

            videoSearchObj.put("source", CommonConst.VideoConstant.VIDEO_SOURCE_CHALLENGE);
            douyinVideoService.handDouyinVideoData(videoSearchObj);

            cursor += count;

        }


    }
}
