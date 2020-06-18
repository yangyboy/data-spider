package com.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("spider_proxy_ip")
public class ProxyIp {
    private Long id;

    private String ip;

    private Integer port;

    private String type;

    private String addr;

    private Integer successCount;

    private Integer failCount;

    private Integer successRate;

}
