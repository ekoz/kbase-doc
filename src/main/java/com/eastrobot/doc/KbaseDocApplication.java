package com.eastrobot.doc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author eko.zhan
 */
@SpringBootApplication
@EnableAsync
public class KbaseDocApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(KbaseDocApplication.class, args);
	}
}
