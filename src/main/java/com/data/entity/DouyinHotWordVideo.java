package com.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@TableName("douyin_hotword_video")
@Data
public class DouyinHotWordVideo {

    @TableId
    private Long id;

    /**
     * 作者id
     */
    private String uid;

    private String secUid;
    /**
     * 关联的热点词id
     */
    private String hotwordId;

    /**
     * 标题
     */
    private String title;

    /**
     * 视频id
     */
    private String awemeId;

    /**
     * 视频分享链接
     */
    private String shareUrl;

    /**
     * 创建时间
     */
    private long createTime;

    /**
     * 评论数
     */
    private Integer commentCount;
    /**
     * 点赞数
     */
    private Integer diggCount;
    /**
     * 分享数
     */
    private Integer shareCount;
    /**
     * 转发数
     */
    private Integer forwardCount;
    /**
     * 下载数
     */
    private Integer downloadCount;

    /**
     * 抓取时间
     */
    private long crawlTime;
}
