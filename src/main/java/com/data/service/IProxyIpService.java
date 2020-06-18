package com.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.data.entity.ProxyIp;


public interface IProxyIpService extends IService<ProxyIp> {

    /**
     * 根据ip和端口查询是否存在相同代理
     * @param ip
     * @param port
     * @return
     */
    ProxyIp selectByIpAndPort(String ip,Integer port);
}
