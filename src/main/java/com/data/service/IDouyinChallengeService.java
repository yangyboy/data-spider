package com.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.data.entity.DouyinChallenge;

public interface IDouyinChallengeService extends IService<DouyinChallenge> {
    void queryDouyinChallengeVideo(String cid);
}
