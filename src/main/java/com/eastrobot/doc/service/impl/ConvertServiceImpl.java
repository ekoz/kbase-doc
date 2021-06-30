package com.eastrobot.doc.service.impl;

import com.eastrobot.doc.config.SystemConstants;
import com.eastrobot.doc.config.WebappContext;
import com.eastrobot.doc.service.ConvertService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @version 1.0
 * @date 2021/6/30 21:06
 */
@Service
@Slf4j
public class ConvertServiceImpl implements ConvertService {

    @Async
    @Override
    public void exec(File inputFile, File outputFile) throws FileNotFoundException {
        // 调用之前需要共享 ServletRequestAttributes
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        WebappContext webappContext = WebappContext.get(request.getServletContext());
        OfficeDocumentConverter converter = webappContext.getDocumentConverter();
        String inputExtension = FilenameUtils.getExtension(inputFile.getName());

        try {
            long startTime = System.currentTimeMillis();
            SystemConstants.AT_CONVERT_MAP.put(inputFile.getName(), 1);
            // 停顿30s，模拟转换等待状态
//            Thread.currentThread().sleep(30*1000);
            converter.convert(inputFile, outputFile);
            SystemConstants.AT_CONVERT_MAP.remove(inputFile.getName());
            long conversionTime = System.currentTimeMillis() - startTime;
            log.info(String.format("successful conversion: %s [%db] to %s in %dms", inputExtension, inputFile.length(), SystemConstants.OUTPUT_EXTENSION, conversionTime));
        } catch (Exception e) {
            log.error(String.format("failed conversion: %s [%db] to %s; %s; input file: %s", inputExtension, inputFile.length(), SystemConstants.OUTPUT_EXTENSION, e, inputFile.getName()));
            throw new FileNotFoundException();
        }
    }
}
