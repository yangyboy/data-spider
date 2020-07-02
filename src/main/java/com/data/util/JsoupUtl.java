package com.data.util;

import com.data.webmagic.utils.AgentUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

@Slf4j
public class JsoupUtl {

    public static String getMessage(String url ){
        log.info("抖音用户查询URL：{}",url);
        Connection.Response execute;
        String body = null ;
        try {
            execute = Jsoup.connect(url).header("User-Agent", AgentUtils.radomWebAgent())
                    .ignoreContentType(true).timeout(15000).execute();
            body = execute.body();
            log.info("抖音用户查询结果：{}",body);
        } catch (IOException e) {
            log.error("jsoup爬取接口：{}失败",url,e);
        }

        return  body ;
    }
}
