/*
 * powered by http://ibothub.com
 */
package com.ibothub.doc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author <a href="mailto:eko.zhan@outlook.com">eko.zhan</a>
 * @version v1.0
 * @date 2022/10/21 20:04
 */
@SpringBootApplication
@EnableAsync
public class KbaseDocBetaApplication {

  public static void main(String[] args) {
    SpringApplication.run(KbaseDocBetaApplication.class, args);
  }

}
