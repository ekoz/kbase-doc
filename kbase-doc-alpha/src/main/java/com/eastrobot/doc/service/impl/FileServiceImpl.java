package com.eastrobot.doc.service.impl;

import com.eastrobot.doc.config.SystemConstants;
import com.eastrobot.doc.dao.AttachmentRepository;
import com.eastrobot.doc.model.entity.Attachment;
import com.eastrobot.doc.service.ConvertService;
import com.eastrobot.doc.service.FileService;
import com.eastrobot.doc.util.FileExtensionUtils;
import com.eastrobot.doc.util.HtmlUtils;
import com.google.common.collect.Streams;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @version 1.0
 * @date 2021/6/29 20:11
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

  @Resource
  ConvertService convertService;

  @Resource
  AttachmentRepository attachmentRepository;

  @Override
  public Boolean upload(MultipartFile multipartFile) {
    try {
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

      // 异步执行文件转换
      ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) (RequestContextHolder
          .currentRequestAttributes());
      RequestContextHolder.setRequestAttributes(servletRequestAttributes, true);
      convertService.exec(attachment);
    } catch (IOException e) {
      return false;
    }
    return true;
  }

  @Override
  public String loadData(Long id) throws IOException {

    Optional<Attachment> attachmentOptional = attachmentRepository.findById(id);

    if (attachmentOptional.isPresent()) {
      Attachment attachment = attachmentOptional.get();
      File file = ResourceUtils.getFile(attachment.getPath());

      if (attachment.getStatus()!=1) {
        // 转换中
        return String.format(" 文件 [%s] 正在转换中", file.getName());
      }

      String basename = FilenameUtils.getBaseName(file.getName());
      File targetFile = new File(file.getParent() + "/" + basename + "/" + basename + "."
          + SystemConstants.OUTPUT_EXTENSION);
      if (targetFile.exists()) {
        //logger.debug(HtmlUtils.getFileEncoding(targetFile));
        String data = IOUtils.toString(new FileInputStream(targetFile), HtmlUtils.getFileEncoding(targetFile));
        //logger.debug(data);
        //获取文件头部，每个文件转换出的html头部样式都不一样，动态获取
        //截图 <BODY 之前的所有代码
        String header = HtmlUtils.HEAD_TEMPLATE;
        try {
          header = data.substring(0, data.toLowerCase().indexOf("<body"));
          String tmp = data.substring(data.toLowerCase().indexOf("<body"));
          header += tmp.substring(0, tmp.indexOf(">") + 1);
          header = HtmlUtils.replaceCharset(header);
        } catch (StringIndexOutOfBoundsException e) {
          e.printStackTrace();
          log.error("html页面数据解析异常");
        }

//            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
//            request.getSession().setAttribute(SystemConstants.HTML_HEADER, header);
        //TODO 如果是网络图片，如何处理？
        //TODO 保存后再次打开html文档，如何处理？
        //data = HtmlUtils.replaceHtmlTag(data, "img", "src", "src=\"" + request.getContextPath() + "/index/loadFileImg?name=" + name + "&imgname=", "\"");
        return data;
      }
      return "当前文档转换失败";
    }

    return id + "对应的文件不存在";
  }

  @Override
  public Iterable<Attachment> list() throws FileNotFoundException {
    return Streams.stream(attachmentRepository.findAll())
        .peek(attachment -> {
          attachment.setSizeAlias(FileExtensionUtils.readFilesize(attachment.getSize()));
        })
        .collect(Collectors.toList());
//    File dir = ResourceUtils.getFile("classpath:static/DATAS");
//    File[] files = dir.listFiles();
//    List<Attachment> list = Lists.newArrayList();
//    for (File file : files) {
//      if (file.isFile()) {
//        list.add(Attachment
//            .builder()
//            .path(file.getPath())
//            .name(file.getName())
//            .size(file.length())
//            .build());
//      }
//    }
//    return list;
  }

  @Override
  public void delete(Long id) {
    attachmentRepository.findById(id)
        .ifPresent(attachment -> {
          //TODO windows操作系统上如果html文件被占用则无法删除，是否可以用 File.creteTempFile 来解决？
          try {
            if (StringUtils.isNotBlank(attachment.getTargetPath())) {
              // html 路径，需要删除 parent 文件夹
              File targetFile = new File(attachment.getTargetPath());
              if (targetFile.exists()){
                FileUtils.deleteDirectory(targetFile.getParentFile());
              }
            }

            if (StringUtils.isNotBlank(attachment.getPath())) {
              File file = new File(attachment.getPath());
              if (file.exists()){
                FileUtils.forceDelete(file);
              }
            }
          } catch (IOException e) {
            e.printStackTrace();
          }

          attachmentRepository.delete(attachment);

        });
  }
}
