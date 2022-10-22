/*
 * powered by http://ibothub.com
 */
package com.ibothub.doc.web.controller;

import com.ibothub.doc.entity.Attachment;
import com.ibothub.doc.service.AttachmentService;
import java.io.IOException;
import javax.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author <a href="mailto:eko.zhan@outlook.com">eko.zhan</a>
 * @version v1.0
 * @date 2022/10/22 12:41
 */
@RestController
@RequestMapping("")
public class IndexController {

  @Resource
  AttachmentService attachmentService;

  @RequestMapping(value = "/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, method = RequestMethod.POST)
  public ResponseEntity upload(@RequestPart @RequestParam("uploadFile") MultipartFile uploadFile){
    try {
      attachmentService.upload(uploadFile);
    } catch (IOException e) {
      return ResponseEntity.ok(e.getMessage());
    }
    return ResponseEntity.ok("");
  }

  @GetMapping("/list")
  public ResponseEntity<Iterable<Attachment>> list(){
    return ResponseEntity.ok(attachmentService.list());
  }

}
