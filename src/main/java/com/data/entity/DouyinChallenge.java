package com.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 抖音话题实体，数据存储到mysql 方便取出id查询话题相关的视频
 */
@TableName("douyin_challenge")
@Data
public class DouyinChallenge {

    private Long id;
    private String chaName;
    private String cid;

    private String chaDesc;

    private Integer viewCount;

    private Integer userCount;
    
}
