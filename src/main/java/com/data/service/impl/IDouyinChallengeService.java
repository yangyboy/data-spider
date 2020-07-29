package com.data.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.data.entity.DouyinChallenge;
import com.data.kafka.dto.DouyinUserQueryDTO;

public interface IDouyinChallengeService extends IService<DouyinChallenge> {
    void queryDouyinChallengeVideo(String cid);
}
