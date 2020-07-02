package com.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@TableName("douyin_video")
@Data
public class DouyinVideo {

    @TableId
    private Long id;
    /**
     * 作者id
     */
    private String uid;
    /**
     * 标题
     */
    private String title;

    /**
     * 视频id
     */
    private String awemeId;
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
     *  位置分享链接
     */
    private String poiShareUrl;

    /**
     * 视频分享链接
     */
    private String shareUrl;

    /**
     * 创建时间
     */
    private long createTime;

    /**
     * 店铺名
     */
    private String poiName;

    /**
     * 定位经度
     */
    private String poiLongitude;

    /**
     * 定位纬度
     */
    private String poiLatitude;

    /**
     * 一下为视频定位信息
     * 国家  省  城市  区县 地址  详细地址
     */
    private String country;
    private String province;
    private String city;
    private String district;
    private String address;
    private String simpleAddr;


    /**
     * 抓取时间
     */
    private long crawlTime;
}
