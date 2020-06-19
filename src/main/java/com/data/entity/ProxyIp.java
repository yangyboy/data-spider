package com.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@TableName("spider_proxy_ip")
@NoArgsConstructor
@AllArgsConstructor
public class ProxyIp implements Serializable {
    private static final long serialVersionUID = 3481659414444503619L;
    private Long id;

    private String ip;

    private Integer port;

    private String type;

    private String addr;

    private Integer successCount;

    private Integer failCount;

    private Integer successRate;

}
