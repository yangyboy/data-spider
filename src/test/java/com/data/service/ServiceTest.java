package com.data.service;


import com.data.kafka.dto.DouyinUserQueryDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {


    @Resource
    private IDouyinUserService douyinUserService;

    @Resource
    private IDouyinHotwordService douyinHotwordService;

    @Test
    public void testQueryDouyin() {
        douyinUserService.queryDouyinUserInfo(new DouyinUserQueryDTO());
    }

    @Test
    public void testHotSearch() {
        douyinHotwordService.hotSearch();
    }
}
