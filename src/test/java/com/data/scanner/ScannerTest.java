package com.data.scanner;

import com.data.service.IDouyinRespFileScanService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 抖音文件扫描测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ScannerTest {


    @Resource
    private IDouyinRespFileScanService douyinRespFileScanService;
    /**
     * 测试读取文件
     */
    @Test
    public void testSend() throws InterruptedException {

        douyinRespFileScanService.scanner();

    }
}
