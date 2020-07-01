package com.data.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.entity.DouyinUser;
import com.data.kafka.dto.DouyinUserQueryDTO;
import com.data.mapper.DouyinUserMapper;
import com.data.service.IDouyinUserService;
import com.data.util.JsoupUtl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DouyinUserServiceImpl extends ServiceImpl<DouyinUserMapper, DouyinUser> implements IDouyinUserService {

    @Value("${douyin.user.query}")
    private String douyinUserQueryUrl;


    @Override
    public DouyinUser selectByUid(String uid) {
        return baseMapper.selectOne(new LambdaQueryWrapper<DouyinUser>().eq(DouyinUser::getUid, uid));
    }

    @Override
    public void queryDouyinUserInfo(DouyinUserQueryDTO dto) {
        douyinUserQueryUrl = douyinUserQueryUrl + dto.getSecUid();
        String message = JsoupUtl.getMessage(douyinUserQueryUrl);
        if(StrUtil.isEmpty(message) || !message.startsWith("{")){
            log.info("抖音用户：{} 查询失败，查询结果为：{}",dto.getUid(),message);
        }

        DouyinUser douyinUser = this.selectByUid(dto.getUid());


        JSONObject userObj = JSON.parseObject(message);
        douyinUser.setAwemeCount(userObj.getInteger("aweme_count"));
        douyinUser.setFollowerCount(userObj.getInteger("follower_count"));

        douyinUser.setTotalFavorited(userObj.getInteger("total_favorited"));

        douyinUser.setFollowingCount(userObj.getInteger("following_count"));
        douyinUser.setFavoritingCount(userObj.getInteger("favoriting_count"));


        this.updateById(douyinUser);

    }
}
