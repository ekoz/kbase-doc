/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.config;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年8月9日 上午9:30:15
 * @version 1.0
 */
@Component
@Getter
@Setter
public class OpenOffice {

	@Value("${office.port}")
    private String port;
	@Value("${office.home}")
	private String home;
	@Value("${office.profile}")
	private String profile;
	@Value("${office.fileSizeMax}")
	private String fileSizeMax;
	
}
