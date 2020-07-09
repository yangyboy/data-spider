package com.data.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.entity.DouyinHotword;
import com.data.mapper.DouyinHotwordMapper;
import com.data.service.IDouyinHotwordService;
import com.data.util.JsoupUtl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
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

//    MessageFormat.format("接收到消息，代理IP:{0}:{1}", proxyIpDTO.getIp(),proxyIpDTO.getPort())

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

            if (list != null && list.size() >0) {
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

            }
        }
    }


    public static void main(String[] args) {

        System.out.println(URLDecoder.decode("%E8%A1%97%E5%A4%B4%E9%9B%B6%E9%A3%9F"));
    }
}
