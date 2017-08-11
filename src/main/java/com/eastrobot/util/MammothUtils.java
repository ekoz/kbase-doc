/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.util;

import java.io.File;
import java.io.IOException;

import org.zwobble.mammoth.DocumentConverter;
import org.zwobble.mammoth.Result;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年8月3日 下午8:45:46
 * @version 1.0
 */
public class MammothUtils {

	/**
	 * word 转 html
	 * @author eko.zhan at 2017年8月3日 下午8:51:10
	 * @param file
	 * @return
	 */
	public static String wordToHtml(File file){
		//java.util.zip.ZipException: error in opening zip file
		try {
			DocumentConverter converter = new DocumentConverter();
			Result<String> result = converter.convertToHtml(file);
			String content = result.getValue();
			return content;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
