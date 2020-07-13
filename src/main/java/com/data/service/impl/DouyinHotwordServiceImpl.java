package com.data.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.entity.DouyinHotWordVideo;
import com.data.entity.DouyinHotword;
import com.data.mapper.DouyinHotwordMapper;
import com.data.service.IDouyinHotWordVideoService;
import com.data.service.IDouyinHotwordService;
import com.data.util.JsoupUtl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.ws.RespectBinding;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class DouyinHotwordServiceImpl extends ServiceImpl<DouyinHotwordMapper, DouyinHotword> implements IDouyinHotwordService {

    @Value("${douyin.hot.search}")
    private String douyinHotSearchUrl;

    @Value("${douyin.hot.search.video}")
    private String douyinHotSearchVideoUrl;

    @Resource
    private IDouyinHotWordVideoService douyinHotWordVideoService;


    @Override
    public void hotSearch() {
        log.info("查询抖音热搜榜，查询地址：{}", douyinHotSearchUrl);
        String message = JsoupUtl.getMessage(douyinHotSearchUrl);
        if (StrUtil.isEmpty(message) || !message.startsWith("{")) {
            log.info("抖音热搜榜查询失败，查询结果为：{}", message);
        }


        JSONObject hotSearchObj = JSON.parseObject(message);

        JSONObject data = hotSearchObj.getJSONObject("data");
        if (data != null) {
            JSONArray wordList = data.getJSONArray("word_list");


            Date activeTime = data.getDate("active_time");

            List<DouyinHotword> list = this.list(new LambdaQueryWrapper<DouyinHotword>().eq(DouyinHotword::getActiveTime, activeTime));

            if (list != null && list.size() > 0) {
                return;
            }

            for (int i = 0; i < wordList.size(); i++) {
                JSONObject jsonObject = wordList.getJSONObject(i);
                DouyinHotword douyinHotword = new DouyinHotword();

                douyinHotword.setActiveTime(activeTime);

                String word = jsonObject.getString("word");
                douyinHotword.setWord(word);

                Integer hotValue = jsonObject.getInteger("hot_value");
                douyinHotword.setHotValue(hotValue);

                Integer videoCount = jsonObject.getInteger("video_count");
                douyinHotword.setVideoCount(videoCount);

                String groupId = jsonObject.getString("group_id");
                douyinHotword.setGroupId(groupId);
                Integer position = jsonObject.getInteger("position");
                douyinHotword.setPosition(position);

                this.save(douyinHotword);

                /**
                 * 根据热词搜索热词相关视频信息
                 */


                String formatUrl;
                try {
                    formatUrl = MessageFormat.format(douyinHotSearchVideoUrl, URLEncoder.encode(douyinHotword.getWord(), StandardCharsets.UTF_8.name()));
                } catch (UnsupportedEncodingException e) {
                    log.info("url:{}编码异常",douyinHotSearchVideoUrl);
                    continue;
                }

                String hotSearchVideoResp = JsoupUtl.getMessage(formatUrl);

                if (!StrUtil.isEmpty(message) || message.startsWith("{")) {
                    JSONObject hotSearchVideoJson = JSON.parseObject(hotSearchVideoResp);
                    JSONArray videoArr = hotSearchVideoJson.getJSONArray("aweme_list");

                    for (int j = 0; j < videoArr.size(); j++) {
                        JSONObject videoObj = videoArr.getJSONObject(j);
                        DouyinHotWordVideo video = new DouyinHotWordVideo();
                        String aweme_id = videoObj.getString("aweme_id");
                        video.setAwemeId(aweme_id);
                        String desc = videoObj.getString("desc");

                        video.setTitle(desc);

                        Long create_time = videoObj.getLong("create_time");
                        video.setCreateTime(create_time);

                        String share_url = videoObj.getString("share_url");
                        video.setShareUrl(share_url);

                        video.setHotwordId(douyinHotword.getId());

                        /**
                         * 获得视频作者信息
                         */
                        JSONObject author = videoObj.getJSONObject("author");

                        String uid = author.getString("uid");

                        video.setUid(uid);

                        String sec_uid = author.getString("sec_uid");

                        video.setSecUid(sec_uid);


                        /**
                         * 获得视频统计信息
                         */

                        JSONObject statistics = videoObj.getJSONObject("statistics");

                        Integer comment_count = statistics.getInteger("comment_count");
                        video.setCommentCount(comment_count);

                        Integer digg_count = statistics.getInteger("digg_count");

                        video.setDiggCount(digg_count);

                        Integer download_count = statistics.getInteger("download_count");

                        video.setDownloadCount(download_count);

                        Integer forward_count = statistics.getInteger("forward_count");
                        video.setForwardCount(forward_count);


                        Integer share_count = statistics.getInteger("share_count");

                        video.setShareCount(share_count);

                        video.setCrawlTime(System.currentTimeMillis() / 1000L);

                        douyinHotWordVideoService.save(video);
                    }
                }
            }
        }
    }
}
