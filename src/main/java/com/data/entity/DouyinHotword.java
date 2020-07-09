package com.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("douyin_hotword")
public class DouyinHotword {
    @TableId
    private Long id;
    private Integer videoCount;
    private String groupId;
    private String word;
    private Integer hotValue;
    private Integer position;

    private Date activeTime;
}
