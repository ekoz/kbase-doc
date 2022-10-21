/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.doc.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.eastrobot.doc.config.BaseController;
import com.eastrobot.doc.service.WatermarkService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.service.ResponseMessage;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月2日 下午2:00:44
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/watermark")
public class WatermarkController extends BaseController {

	@Autowired
    WatermarkService watermarkService;
	
	/**
	 * 输出文件demo https://o7planning.org/en/11765/spring-boot-file-download-example
	 * @author eko.zhan at 2018年9月2日 下午4:26:10
	 * @param file
	 * @param text
	 * @param color
	 * @return
	 * @throws IOException
	 */
	@ApiOperation(value="传入文件并返回水印文件", response=ResponseMessage.class)
	@ApiImplicitParams({
	        @ApiImplicitParam(name="file", value="待添加水印的文件", dataType="__file", required=true, paramType="form"),
	        @ApiImplicitParam(name="text", value="水印内容", dataType="string", required=false, paramType="form"),
	        @ApiImplicitParam(name="color", value="颜色码，以#开头", dataType="string", required=false, paramType="form")
	})
	@PostMapping("/handle")
	public ResponseEntity<ByteArrayResource> handle(@RequestParam("file") MultipartFile file, @RequestParam(value="text", required=false) String text, @RequestParam(value="color", required=false) String color) throws IOException {
		if (!file.getOriginalFilename().toLowerCase().endsWith(".docx")) {
			log.error("上传的文件必须是 docx 类型");
		}
		File dir = ResourceUtils.getFile("classpath:static/DATAS");
		String pardir = DateFormatUtils.format(new Date(), "yyyyMMdd");
		String filename = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "." + FilenameUtils.getExtension(file.getOriginalFilename());
		File originFile = new File(dir + "/" + pardir + "/" + filename);
		FileUtils.copyInputStreamToFile(file.getInputStream(), originFile);
		byte[] bytes = watermarkService.handle(originFile, text, color);
		ByteArrayResource resource = new ByteArrayResource(bytes);
		return ResponseEntity.ok()
				// Content-Disposition
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + originFile.getName())
				// Content-Type
				.contentType(getMediaType(originFile.getName()))
				// Contet-Length
				.contentLength(bytes.length)
				.body(resource);
	}
}
