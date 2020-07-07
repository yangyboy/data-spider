package com.data.webmagic.downloader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

/**
 * 自定义下载器，将下载失败的url记录到redis中
 */
@Slf4j
@Component
public class MyDownloader extends HttpClientDownloader {

    private static final String DOWNLOAD_START_MILLS = "download_start_mills";
    private static final String DOWNLOAD_EXPAND_MILLS = "download_expand_mills";

//    @Autowired
//    private RedisTemplate<String,Object> redisTemplate;
//    private String keyPrefix;
//    private static final String FAIL_KEY = "fail";

//    public MyDownloader(String keyPrefix) {
//        this.keyPrefix = keyPrefix;
//    }

    @Override
    public Page download(Request request, Task task) {
//        super.setProxyProvider();

        request.putExtra(DOWNLOAD_START_MILLS, System.currentTimeMillis());
        return super.download(request, task);
    }

    @Override
    protected void onSuccess(Request request) {
        super.onSuccess(request);

        calcExpandMills(request);
        log.info("download expand: {} ms, url: {}", request.getExtra(DOWNLOAD_EXPAND_MILLS), request.getUrl());
    }

    @Override
    protected void onError(Request request) {
        super.onError(request);
        calcExpandMills(request);
        log.info("download error!!! expand: {} ms, url: {}", request.getExtra(DOWNLOAD_EXPAND_MILLS), request.getUrl());
        //下载失败时重设代理ip
//        super.setProxyProvider(SimpleProxyProvider.from(new Proxy(ips[0], Integer.parseInt(ips[1]))));
        // 将下载失败的url记录到redis
//        String key = StringUtils.join(keyPrefix, ":", FAIL_KEY);
//        BoundSetOperations setOps = redisTemplate.boundSetOps(key);
//        setOps.add(request.getUrl());
    }

    /**
     * 计算下载耗费毫秒数
     * @param request
     */
    private void calcExpandMills(Request request) {
        long downloadEndMills = System.currentTimeMillis();
        Object downloadStartMills = request.getExtra(DOWNLOAD_START_MILLS);
        if(downloadStartMills != null) {
            long expandMills = downloadEndMills - Long.valueOf(downloadStartMills.toString()).longValue();
            request.putExtra(DOWNLOAD_EXPAND_MILLS, expandMills);
        }
    }
}
