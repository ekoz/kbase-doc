/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.doc.watermark;
/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月17日 下午4:33:28
 * @version 1.0
 */

import java.io.File;

import org.junit.Test;

public class PowerPointProcessorTests {

	@Test
	public void testProcess() {
		File file = new File("E:\\ConvertTester\\ppt\\看看addThread方法的源码1.pptx");
		File imgFile = new File("E:\\ConvertTester\\images\\jshrss-logo.png");
		
		PowerPointProcessor processor = new PowerPointProcessor(file, imgFile);
		try {
			processor.process();
		} catch (WatermarkException e) {
			e.printStackTrace();
		}
	}
}
