/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.doc.watermark;
/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月17日 上午11:22:38
 * @version 1.0
 */

import java.io.File;

import org.junit.Test;

public class WordProcessorTests {

	@Test
	public void testProcess() {
		File file = new File("E:\\ConvertTester\\docx\\NVR5X-I人脸比对配置-ekozhan1.docx");
		File imgFile = new File("E:\\ConvertTester\\images\\jshrss-logo.png");
		
		WordProcessor wordProcesser = new WordProcessor(file, imgFile);
		try {
			wordProcesser.process();
		} catch (WatermarkException e) {
			e.printStackTrace();
		}
	}
}
