package com.data.service.impl;

import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.data.constant.CommonConst;
import com.data.kafka.producer.DouYinVideoDataSender;
import com.data.service.IDouyinRespFileScanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

@Service
@Slf4j
public class DouyinRespFileScanServiceImpl implements IDouyinRespFileScanService {

    @Value("${scanner.douyin.respPath}")
    private String respPath;

    @Value("${mq.topicName.douyin.video}")
    private String douyinVideoTopic;

    @Resource
    private DouYinVideoDataSender douYinVideoDataSender;

    @Override
    public int scanner() {
        File file = new File(respPath);
        File[] tempList = file.listFiles();

        if(tempList == null || tempList.length == 0){
            log.debug("文件列表微空！！");
            return 0;
        }
        int count = 0;
        for (File respFile : tempList) {

            try {
                FileReader fileReader = new FileReader(respFile.getAbsolutePath());
                String result = fileReader.readString();
                JSONObject videoJsonObj = JSON.parseObject(result);
                videoJsonObj.put("source", CommonConst.VideoConstant.VIDEO_SOURCE_WORDKEY);
                douYinVideoDataSender.sender(douyinVideoTopic,videoJsonObj);

                respFile.delete();//扫描完成后 删除该文件
                count++;
            } catch (Exception e) {
                log.error("文件：{}读取失败",respFile.getAbsolutePath(),e);
            }
        }

        return count;
    }
}
