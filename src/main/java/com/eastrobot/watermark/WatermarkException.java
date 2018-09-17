/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.watermark;
/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月17日 下午4:38:13
 * @version 1.0
 */
@SuppressWarnings("serial")
public class WatermarkException extends Exception {
	
	public WatermarkException(String msg) {
		super(msg);
	}
	
	public WatermarkException(String msg, Exception e) {
		super(msg, e);
	}

	public WatermarkException(String msg, Throwable t) {
		super(msg, t);
	}
}
