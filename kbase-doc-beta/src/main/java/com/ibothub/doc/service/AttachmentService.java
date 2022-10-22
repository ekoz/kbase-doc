/*
 * powered by http://ibothub.com
 */
package com.ibothub.doc.service;

import com.ibothub.doc.entity.Attachment;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author <a href="mailto:eko.zhan@outlook.com">eko.zhan</a>
 * @version v1.0
 * @date 2022/10/22 12:44
 */
public interface AttachmentService {

  void upload(MultipartFile uploadFile) throws IOException;

  Iterable<Attachment> list();
}
