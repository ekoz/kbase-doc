package com.eastrobot.doc.service;

import com.eastrobot.doc.model.entity.Attachment;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @version 1.0
 * @date 2021/6/30 21:04
 */
public interface ConvertService {

  /**
   * 文件转换
   * @param attachment
   */
  void exec(Attachment attachment) throws FileNotFoundException;

  /**
   * 文档转换
   */
  void exec(File inputFile, File outputFile) throws FileNotFoundException;
}
