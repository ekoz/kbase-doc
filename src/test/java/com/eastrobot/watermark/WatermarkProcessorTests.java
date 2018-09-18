/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.watermark;

import java.io.File;

import org.junit.Test;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月18日 上午9:49:25
 * @version 1.0
 */
public class WatermarkProcessorTests {

	@Test
	public void testProcessor() throws WatermarkException {
		File file = new File("E:\\ConvertTester\\excel\\什么是中央处理器.xlsx");
		File imgFile = new File("E:\\ConvertTester\\images\\jshrss-logo.png");
		
		WatermarkProcessor.process(file, imgFile);
	}
	
	@Test
	public void testProcessorText() throws WatermarkException {
		File file = new File("E:\\ConvertTester\\excel\\什么是中央处理器.xlsx");
		WatermarkProcessor.process(file, "小i机器人");
	}
}
