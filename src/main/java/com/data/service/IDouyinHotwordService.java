package com.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.data.entity.DouyinHotword;
import com.data.kafka.dto.DouyinUserQueryDTO;

public interface IDouyinHotwordService extends IService<DouyinHotword> {
    void hotSearch();
}