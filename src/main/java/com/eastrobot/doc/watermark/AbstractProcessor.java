/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.doc.watermark;

import java.io.File;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月17日 下午4:37:19
 * @version 1.0
 */
public abstract class AbstractProcessor {
	
	protected File file;
	protected File imageFile;
	
	public AbstractProcessor(File file, File imageFile) {
		this.file = file;
		this.imageFile = imageFile;
	}
	
	/**
	 * 执行文件转换，将 file 添加 imageFile 水印
	 * @author eko.zhan at 2018年9月17日 下午6:08:24
	 * @throws WatermarkException
	 */
	public abstract void process() throws WatermarkException;
}
