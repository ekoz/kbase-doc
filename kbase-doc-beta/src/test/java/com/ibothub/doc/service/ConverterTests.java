/*
 * powered by http://ibothub.com
 */
package com.ibothub.doc.service;

import java.io.File;
import javax.annotation.Resource;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.document.DocumentFormat;
import org.jodconverter.core.office.OfficeException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

/**
 * @author <a href="mailto:eko.zhan@outlook.com">eko.zhan</a>
 * @version v1.0
 * @date 2022/10/21 20:15
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConverterTests {

  @Resource
  DocumentConverter documentConverter;

  @Test
  public void testConvert() throws OfficeException {

    File inputFile = new File("D:\\Documents\\tmp\\1.docx");
    File outputFile = new File("D:\\Documents\\tmp\\1\\1.html");

    final DocumentFormat targetFormat =
        DefaultDocumentFormatRegistry.getFormatByExtension("html");
    Assert.notNull(targetFormat, "targetFormat must not be null");
    documentConverter
        .convert(inputFile)
        .as(targetFormat)
        .to(outputFile)
        .execute();
  }

}
