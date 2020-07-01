package com.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("douyin_user")
public class DouyinUser {

    @TableId()
    private Long id;
    /**
     * 用户uid
     */
    private String uid;
    /**
     * 用户抖音号
     */
    private String shortId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 签名描述
     */
    private String signature;
    /**
     * 认证信息
     */
    private String customVerify;

    /**
     * 用户信息查询接口的加密参数
     */
    private String secUid;

    /**
     * 作品数
     */
    private Integer awemeCount;

    /**
     * 粉丝数
     */
    private Integer followerCount;
    /**
     * 总获赞数
     */
    private Integer totalFavorited;

    /**
     *  用户的关注数
     */
    private Integer followingCount;

    /**
     * 喜欢的作品数
     */
    private Integer favoritingCount;


}
