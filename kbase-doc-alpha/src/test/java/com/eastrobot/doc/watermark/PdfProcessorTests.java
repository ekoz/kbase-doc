/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.doc.watermark;

import java.io.File;

import org.junit.Test;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月17日 下午5:03:51
 * @version 1.0
 */
public class PdfProcessorTests {

	@Test
	public void testProcess() {
		File file = new File("E:\\ConvertTester\\pdf\\增值税开票_ekozhan.pdf");
		File imgFile = new File("E:\\ConvertTester\\images\\jshrss-logo-s.png");
		
		PdfProcessor processor = new PdfProcessor(file, imgFile);
		try {
			processor.process();
		} catch (WatermarkException e) {
			e.printStackTrace();
		}
	}
}
