package com.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("douyin_hotword")
public class DouyinHotword {
    private Integer videoCount;
    private String groupId;
    private String word;
    private Integer hotValue;
}
