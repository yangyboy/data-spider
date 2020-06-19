package com.data.kafka.dto;


import com.data.entity.ProxyIp;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProxyIpDTO extends ProxyIp implements Serializable{

	private static final long serialVersionUID = 4077490514226821862L;
	/**
	 * 标志检测代理可用性时的状态，新增或者更新
	 */
	private CheckIPType checkType;


	@Getter
	public enum CheckIPType {
		ADD(1, "新增代理IP"), UPDATE(2, "检测数据库中IP");
		private final Integer code;
		private final String description;

		CheckIPType(Integer code, String description) {
			this.code = code;
			this.description = description;
		}
	}
}
