/*
 * powered by http://ibothub.com
 */
package com.ibothub.doc.service.impl;

import com.google.common.collect.Streams;
import com.ibothub.doc.config.SystemConstants;
import com.ibothub.doc.dao.AttachmentRepository;
import com.ibothub.doc.entity.Attachment;
import com.ibothub.doc.service.AttachmentService;
import com.ibothub.doc.util.FileExtensionUtils;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.io.FilenameUtils;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.office.OfficeException;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author <a href="mailto:eko.zhan@outlook.com">eko.zhan</a>
 * @version v1.0
 * @date 2022/10/22 12:44
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {


  @Resource
  AttachmentRepository attachmentRepository;

  @Resource
  DocumentConverter documentConverter;


  @Override
  public void upload(MultipartFile multipartFile) throws IOException {
    File targetDir = ResourceUtils.getFile("classpath:static/DATAS/");
    String inputExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

    Attachment attachment = Attachment.builder()
        .name(multipartFile.getOriginalFilename())
        .size(multipartFile.getSize())
        // 待转换
        .status(0)
        .createTime(LocalDateTime.now())
        .build();
    attachmentRepository.save(attachment);
    String inputFilename = attachment.getId() + "";

    File file = new File(targetDir.getAbsolutePath() + "/" + inputFilename + "." + inputExtension);
    FileCopyUtils.copy(multipartFile.getBytes(), file);

    File outputFile = new File(targetDir.getAbsolutePath() + "/" + inputFilename +
        "/" + inputFilename + "." + SystemConstants.OUTPUT_EXTENSION);

    // 保存源文件和目标文件的路径
    attachment.setPath(file.getPath());
    attachment.setTargetPath(outputFile.getPath());
    attachmentRepository.save(attachment);

    CompletableFuture.runAsync(()->{
      try {
        documentConverter
            .convert(file)
            .to(outputFile)
            .execute();
      } catch (OfficeException e) {
        // 转换失败
        attachment.setStatus(-1);
        attachmentRepository.save(attachment);
      }
    }/*, threadPoolExecutor*/).whenComplete((result, e) -> {
      // 转换成功
      attachment.setStatus(1);
      attachmentRepository.save(attachment);
    });
  }

  @Override
  public Iterable<Attachment> list() {
    return Streams.stream(attachmentRepository.findAll())
        .peek(attachment -> {
          attachment.setSizeAlias(FileExtensionUtils.readFilesize(attachment.getSize()));
        })
        .collect(Collectors.toList());
  }
}
