/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.watermark;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import com.eastrobot.util.FileExtensionUtils;

/**
 * 中央处理器
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月18日 上午9:40:29
 * @version 1.0
 */
public class WatermarkProcessor {

	/**
	 * 
	 * @author eko.zhan at 2018年9月18日 上午9:48:57
	 * @param file
	 * @param imageFile
	 * @throws WatermarkException
	 */
	public static void process(File file, File imageFile) throws WatermarkException {
		AbstractProcessor processor = null;
		if (FileExtensionUtils.isWord(file.getName())) {
			processor = new WordProcessor(file, imageFile);
		} else if (FileExtensionUtils.isExcel(file.getName())) {
			processor = new ExcelProcessor(file, imageFile);
		} else if (FileExtensionUtils.isPpt(file.getName())) {
			processor = new PowerPointProcessor(file, imageFile);
		} else if (FileExtensionUtils.isPdf(file.getName())) {
			processor = new PdfProcessor(file, imageFile);
		} else if (FileExtensionUtils.isImage(file.getName())) {
			processor = new ImageProcessor(file, imageFile);
		}
		if (processor!=null) {
			processor.process();
		}else {
			throw new WatermarkException("不支持文件格式为 " + FilenameUtils.getExtension(file.getName()) + " 的水印处理");
		}
	}
	
}
