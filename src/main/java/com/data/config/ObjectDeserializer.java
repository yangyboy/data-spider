package com.data.config;

import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.util.SerializationUtils;

import java.util.Map;

/**
 * 自定义消息解码器
 *
 */
public class ObjectDeserializer implements Deserializer<Object> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    /**
     * 反序列化
     */
    @Override
    public Object deserialize(String topic, byte[] data) {
        return SerializationUtils.deserialize(data);
    }

    @Override
    public void close() {

    }
}
