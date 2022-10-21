package com.eastrobot.doc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author eko.zhan
 */
@SpringBootApplication
@EnableAsync
public class KbaseDocAlphaApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(KbaseDocAlphaApplication.class, args);
	}
}
