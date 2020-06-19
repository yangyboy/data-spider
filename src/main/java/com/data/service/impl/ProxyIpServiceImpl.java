package com.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.entity.ProxyIp;
import com.data.mapper.ProxyIpMapper;
import com.data.service.IProxyIpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProxyIpServiceImpl extends ServiceImpl<ProxyIpMapper, ProxyIp> implements IProxyIpService {

    @Override
    public ProxyIp selectByIpAndPort(String ip, Integer port) {
        return baseMapper.selectOne(new LambdaQueryWrapper<ProxyIp>().eq(ProxyIp::getIp, ip).eq(ProxyIp::getPort,port));
    }
}
