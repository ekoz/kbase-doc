/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.config;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import lombok.extern.slf4j.Slf4j;


/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月2日 下午2:12:10
 * @version 1.0
 */
@Slf4j
public class BaseController {
	
	@Autowired
	protected ServletContext servletContext;

	/**
	 * 转换文件
	 * @author eko.zhan at 2018年9月2日 下午2:16:14
	 * @param originFile
	 * @param targetFile
	 */
	protected void convert(File originFile, File targetFile) {
		WebappContext webappContext = WebappContext.get(servletContext);
		OfficeDocumentConverter converter = webappContext.getDocumentConverter();
		try {
        	long startTime = System.currentTimeMillis();
        	converter.convert(originFile, targetFile);
        	long conversionTime = System.currentTimeMillis() - startTime;
        	log.info(String.format("successful conversion: %s [%db] to %s in %dms", FilenameUtils.getExtension(originFile.getName()), originFile.length(), FilenameUtils.getExtension(targetFile.getName()), conversionTime));
        } catch (Exception e) {
        	e.printStackTrace();
            log.error(String.format("failed conversion: %s [%db] to %s; %s; input file: %s", FilenameUtils.getExtension(originFile.getName()), originFile.length(), FilenameUtils.getExtension(targetFile.getName()), e, targetFile.getName()));
        }
	}
	
	/**
	 * 根据指定的文件名称获取文件类型
	 * @author eko.zhan at 2018年9月2日 下午3:12:57
	 * @param filename
	 * @return
	 */
	protected MediaType getMediaType(String filename) {
        // application/pdf
        // application/xml
        // image/gif, ...
        String mineType = servletContext.getMimeType(filename);
        try {
            MediaType mediaType = MediaType.parseMediaType(mineType);
            return mediaType;
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
