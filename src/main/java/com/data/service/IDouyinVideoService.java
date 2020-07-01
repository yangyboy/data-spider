package com.data.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.data.entity.DouyinVideo;

public interface IDouyinVideoService extends IService<DouyinVideo> {

    void handDouyinVideoData(JSONObject videoJsonObj);
}