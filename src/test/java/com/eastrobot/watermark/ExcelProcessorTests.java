/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.watermark;
/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月17日 下午2:11:36
 * @version 1.0
 */

import java.io.File;

import org.junit.Test;

import com.eastrobot.watermark.ExcelProcessor;
import com.eastrobot.watermark.WatermarkException;

public class ExcelProcessorTests {

	@Test
	public void testProcess() throws WatermarkException {
		File file = new File("E:\\ConvertTester\\excel\\什么是中央处理器.xlsx");
		File imgFile = new File("E:\\ConvertTester\\images\\jshrss-logo.png");
		
		ExcelProcessor excelProcessor = new ExcelProcessor(file, imgFile);
		excelProcessor.process();
	}
}
