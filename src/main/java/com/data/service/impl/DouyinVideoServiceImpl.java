package com.data.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.entity.DouyinChallenge;
import com.data.entity.DouyinUser;
import com.data.entity.DouyinVideo;
import com.data.kafka.dto.DouyinUserQueryDTO;
import com.data.kafka.producer.DouYinChallengeDataQuerySender;
import com.data.kafka.producer.DouYinUserDataQuerySender;
import com.data.mapper.DouyinVideoMapper;
import com.data.service.IDouyinUserService;
import com.data.service.IDouyinVideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class DouyinVideoServiceImpl extends ServiceImpl<DouyinVideoMapper, DouyinVideo> implements IDouyinVideoService {

    @Resource
    private IDouyinUserService douyinUserService;

    @Resource
    private DouYinUserDataQuerySender douYinUserDataQuerySender;

    @Resource
    private DouYinChallengeDataQuerySender douYinChallengeDataQuerySender;


    @Resource
    private IDouyinChallengeService douyinChallengeService;

    @Value("${mq.topicName.douyin.user.query}")
    private String DouyinUserQueryTopic;

    @Override
    public void handDouyinVideoData(JSONObject videoJsonObj) {
        JSONArray awemeList = videoJsonObj.getJSONArray("aweme_list");


        Integer source = videoJsonObj.getInteger("source");

        if (awemeList == null) {
            return;
        }
        for (int i = 0; i < awemeList.size(); i++) {
            DouyinVideo video = new DouyinVideo();
            DouyinUser user = new DouyinUser();
            JSONObject awemeObj = awemeList.getJSONObject(i);
            if (awemeObj != null) {

                DouyinVideo oldVideo = this.baseMapper.selectOne(new LambdaQueryWrapper<DouyinVideo>()
                        .eq(DouyinVideo::getAwemeId, awemeObj.getString("aweme_id")));
                if (oldVideo != null) {
                    continue;
                }

                // 视频基本信息获取
                video.setSource(source);
                video.setAwemeId(awemeObj.getString("aweme_id"));
                video.setTitle(awemeObj.getString("desc"));
                video.setCreateTime(awemeObj.getLong("create_time"));
                video.setShareUrl(awemeObj.getString("share_url"));

                //视频数据信息获取（点赞，转发，评论数等等）
                JSONObject statistics = awemeObj.getJSONObject("statistics");
                if (statistics != null) {
                    video.setCommentCount(statistics.getInteger("comment_count"));
                    video.setDiggCount(statistics.getInteger("digg_count"));
                    video.setDownloadCount(statistics.getInteger("download_count"));
                    video.setShareCount(statistics.getInteger("share_count"));
                    video.setForwardCount(statistics.getInteger("forward_count"));
                }


                //获取话题信息
                JSONArray chaList = awemeObj.getJSONArray("cha_list");
                if (chaList != null) {
                    for (int j = 0; j < chaList.size(); j++) {
                        JSONObject chaObj = chaList.getJSONObject(j);
                        DouyinChallenge douyinChallenge = new DouyinChallenge();
                        douyinChallenge.setCid(chaObj.getString("cid"));

                        synchronized (this){
                            DouyinChallenge old = douyinChallengeService.getOne(new LambdaQueryWrapper<DouyinChallenge>().eq(DouyinChallenge::getCid, douyinChallenge.getCid()));

                            if(old != null){
                                continue;
                            }
                        }

                        douyinChallenge.setChaDesc(chaObj.getString("desc"));
                        douyinChallenge.setChaName(chaObj.getString("cha_name"));
                        douyinChallenge.setViewCount(chaObj.getInteger("view_count"));
                        douyinChallenge.setUserCount(chaObj.getInteger("user_count"));
                        douyinChallengeService.save(douyinChallenge);

                        //设置当前循环视频的话题id
                        video.setChaId(douyinChallenge.getCid());


//                        douYinChallengeDataQuerySender.sender();
                    }

                }


                //视频位置信息
                JSONObject poiInfo = awemeObj.getJSONObject("poi_info");
                if (poiInfo != null) {
                    video.setPoiName(poiInfo.getString("poi_name"));
                    JSONObject shareInfo = poiInfo.getJSONObject("share_info");
                    if (shareInfo != null) {
                        video.setPoiShareUrl(shareInfo.getString("share_url"));
                    }
                    JSONObject addressInfo = poiInfo.getJSONObject("address_info");

                    if (addressInfo != null) {
                        video.setCountry(addressInfo.getString("country"));
                        video.setProvince(addressInfo.getString("province"));
                        video.setCity(addressInfo.getString("city"));
                        video.setDistrict(addressInfo.getString("district"));
                        video.setAddress(addressInfo.getString("address"));
                        video.setSimpleAddr(addressInfo.getString("simple_addr"));
                    }
                }

                //保存能拿到的作者信息
                JSONObject author = awemeObj.getJSONObject("author");
                if (author != null) {
                    video.setUid(author.getString("uid"));


                    synchronized (this) {
                        DouyinUser hasUser = douyinUserService.selectByUid(author.getString("uid"));

                        if (hasUser == null) {
                            user.setUid(author.getString("uid"));
                            user.setShortId(author.getString("short_id"));
                            user.setNickname(author.getString("nickname"));
                            user.setSignature(author.getString("signature"));
                            user.setCustomVerify(author.getString("custom_verify"));
                            user.setSecUid(author.getString("sec_uid"));

                            douyinUserService.save(user);
                            DouyinUserQueryDTO dto = new DouyinUserQueryDTO();
                            dto.setSecUid(user.getSecUid());
                            dto.setUid(user.getUid());
                            douYinUserDataQuerySender.sender(DouyinUserQueryTopic, dto);
                        }
                    }

                    video.setCrawlTime(System.currentTimeMillis() / 1000L);//设置抓取时间 unix 时间搓
                    this.save(video);

                }

            }


        }
    }
}
