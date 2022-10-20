/*
 * powered by http://ibothub.com
 */
package com.eastrobot.doc.dao;

import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author <a href="mailto:eko.zhan@outlook.com">eko.zhan</a>
 * @version v1.0
 * @date 2022/10/20 19:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AttachmentRepositoryTests {

  @Resource
  AttachmentRepository attachmentRepository;

  @Test
  public void testFindAll(){
    attachmentRepository.findAll()
        .forEach(attachment -> System.out.println(attachment));
  }

  @Test
  public void testFindById(){
    System.out.println(attachmentRepository.findById(1L));
  }
}
