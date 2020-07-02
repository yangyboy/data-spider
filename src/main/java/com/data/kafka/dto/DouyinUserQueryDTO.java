package com.data.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DouyinUserQueryDTO implements Serializable {
    private static final long serialVersionUID = 2732200586189779835L;
    private String secUid;
    private String uid;
}
