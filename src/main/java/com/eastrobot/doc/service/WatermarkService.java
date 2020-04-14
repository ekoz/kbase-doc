/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.doc.service;

import java.io.File;
import java.io.IOException;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月1日 上午10:09:26
 * @version 1.0
 */
public interface WatermarkService {

	/**
	 * 给源文件加水印
	 * @author eko.zhan at 2018年9月1日 下午4:42:15
	 * @param originFile
	 * @param watermark
	 * @return
	 * @throws IOException
	 */
	public byte[] handle(File originFile, String watermark) throws IOException;
	/**
	 * 给源文件加水印
	 * @author eko.zhan at 2018年9月1日 下午4:42:15
	 * @param originFile
	 * @param watermark
	 * @return
	 * @throws IOException
	 */
	public byte[] handle(File originFile, String watermark, String color) throws IOException;
}
