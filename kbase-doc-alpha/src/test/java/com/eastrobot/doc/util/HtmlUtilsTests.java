/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.doc.util;

import org.junit.Test;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年8月11日 上午10:04:33
 * @version 1.0
 */
public class HtmlUtilsTests {

	@Test
	public void replaceCharset(){
		String data = "CONTENT=\"text/html; charset=gb2312\"";
		String s = HtmlUtils.replaceCharset(data);
		System.out.println(s);
	}
}
