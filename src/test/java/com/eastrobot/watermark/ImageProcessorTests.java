/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.watermark;

import java.io.File;

import org.junit.Test;

import com.eastrobot.watermark.ImageProcessor;
import com.eastrobot.watermark.WatermarkException;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月17日 下午5:03:51
 * @version 1.0
 */
public class ImageProcessorTests {

	@Test
	public void testProcess() {
		File file = new File("E:\\ConvertTester\\images\\2.png");
		File imgFile = new File("E:\\ConvertTester\\images\\jshrss-logo-s.png");
		
		ImageProcessor processor = new ImageProcessor(file, imgFile);
		try {
			processor.process();
		} catch (WatermarkException e) {
			e.printStackTrace();
		}
	}
}
