package com.eastrobot.doc.service.impl;

import com.eastrobot.doc.config.SystemConstants;
import com.eastrobot.doc.config.WebappContext;
import com.eastrobot.doc.dao.AttachmentRepository;
import com.eastrobot.doc.model.entity.Attachment;
import com.eastrobot.doc.service.ConvertService;
import java.io.File;
import java.io.FileNotFoundException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @version 1.0
 * @date 2021/6/30 21:06
 */
@Service
@Slf4j
public class ConvertServiceImpl implements ConvertService {

  @Resource
  AttachmentRepository attachmentRepository;

  @Async
  @Override
  public void exec(Attachment attachment) throws FileNotFoundException {
    // 调用之前需要共享 ServletRequestAttributes
    HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder
        .currentRequestAttributes())).getRequest();
    WebappContext webappContext = WebappContext.get(request.getServletContext());
    OfficeDocumentConverter converter = webappContext.getDocumentConverter();


    File inputFile = new File(attachment.getPath());
    File outputFile = new File(attachment.getTargetPath());

    String inputExtension = FilenameUtils.getExtension(inputFile.getName());

    StopWatch stopWatch = new StopWatch();
    stopWatch.start("文件转换任务-" + attachment.getName());
    try {
      converter.convert(inputFile, outputFile);

      stopWatch.stop();
      log.info(stopWatch.prettyPrint());

      attachment.setStatus(1);
      attachmentRepository.save(attachment);
    } catch (Exception e) {
      log.error(String
          .format("failed conversion: %s [%db] to %s; %s; input file: %s", inputExtension,
              inputFile.length(), SystemConstants.OUTPUT_EXTENSION, e, inputFile.getName()));

      attachment.setStatus(-1);
      attachmentRepository.save(attachment);

      throw new FileNotFoundException();
    }
  }

  @Async
  @Override
  public void exec(File inputFile, File outputFile) throws FileNotFoundException {
    // 调用之前需要共享 ServletRequestAttributes
    HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder
        .currentRequestAttributes())).getRequest();
    WebappContext webappContext = WebappContext.get(request.getServletContext());
    OfficeDocumentConverter converter = webappContext.getDocumentConverter();
    String inputExtension = FilenameUtils.getExtension(inputFile.getName());

    try {
      long startTime = System.currentTimeMillis();
      converter.convert(inputFile, outputFile);
      long conversionTime = System.currentTimeMillis() - startTime;
      log.info(String.format("successful conversion: %s [%db] to %s in %dms", inputExtension,
          inputFile.length(), SystemConstants.OUTPUT_EXTENSION, conversionTime));
    } catch (Exception e) {
      log.error(String
          .format("failed conversion: %s [%db] to %s; %s; input file: %s", inputExtension,
              inputFile.length(), SystemConstants.OUTPUT_EXTENSION, e, inputFile.getName()));
      throw new FileNotFoundException();
    }
  }
}
