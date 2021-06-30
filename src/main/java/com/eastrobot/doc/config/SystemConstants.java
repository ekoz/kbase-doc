/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.doc.config;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年12月6日 下午4:26:58
 * @version 1.0
 */
public class SystemConstants {

	public static String HTML_HEADER = "";
	
	public static String HTML_FOOTER = "</BODY></HTML>";

	public final static String API_KEY = "apiKey";
	public final static String API_TOKEN = "apiToken";
	public final static String API_PASSAS = "header";

	/**
	 * 目标文件后缀
	 */
	public final static String OUTPUT_EXTENSION = "html";

	/**
	 * 正在转换的文件集合
	 */
	public static Map<String, Integer> AT_CONVERT_MAP = Maps.newHashMap();
	
}
