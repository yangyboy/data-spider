package com.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.entity.DouyinHotword;
import com.data.mapper.DouyinHotwordMapper;
import com.data.service.IDouyinHotwordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DouyinHotwordServiceImpl extends ServiceImpl<DouyinHotwordMapper, DouyinHotword> implements IDouyinHotwordService {
}
