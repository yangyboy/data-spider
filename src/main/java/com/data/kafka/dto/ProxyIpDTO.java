package com.data.kafka.dto;


import com.data.entity.ProxyIp;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
public class ProxyIpDTO extends ProxyIp implements Serializable{
	private static final long serialVersionUID = -5395611529404702931L;
	/**
	 * 标志检测代理可用性时的状态，新增或者更新
	 */
	private CheckIPType checkType;


	@Getter
	public enum CheckIPType {
		ADD(1, "新增代理IP"), UPDATE(2, "检测数据库中IP");
		private Integer code;
		private String description;

		CheckIPType(Integer code, String description) {
			this.code = code;
			this.description = description;
		}
	}
}
