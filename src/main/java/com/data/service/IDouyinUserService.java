package com.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.data.entity.DouyinUser;
import com.data.kafka.dto.DouyinUserQueryDTO;

public interface IDouyinUserService extends IService<DouyinUser> {
    DouyinUser selectByUid(String uid);
    void queryDouyinUserInfo(DouyinUserQueryDTO dto);
}
