/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.util;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.mozilla.universalchardet.UniversalDetector;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年8月9日 下午8:30:29
 * @version 1.0
 */
public class HtmlUtils {
	
	public static final String UTF8 = "UTF-8";
	
	//TODO 是否有思考 excel 和 ppt 转换后的头部？
	public static final String HEAD_TEMPLATE = 
		"<!DOCTYPE HTML/>" +
		"<HTML><HEAD>" + 
			"<META HTTP-EQUIV=\"CONTENT-TYPE\" CONTENT=\"text/html; charset=utf-8\">" +
			"<STYLE TYPE=\"text/css\">" +
				"@page { margin-left: 2cm; margin-right: 2cm; margin-top: 1.5cm; margin-bottom: 1.2cm }" +
				"P { text-indent: 0.35cm; margin-bottom: 0.21cm; direction: ltr; color: #000000; line-height: 150%; text-align: justify; widows: 2; orphans: 2 }" +
				"P.western { font-family: \"Calibri\", sans-serif; font-size: 10pt; so-language: en-US }" +
				"P.cjk { font-family: \"华文细黑\", \"微软雅黑\"; font-size: 10pt; so-language: zh-CN }" +
				"P.ctl { font-family: \"Times New Roman\", serif; font-size: 11pt; so-language: ar-SA }" +
				"A:link { color: #0000ff; text-decoration: none }" +
			"</STYLE>" +
		"</HEAD><BODY LANG=\"zh-CN\" TEXT=\"#000000\" LINK=\"#0000ff\" DIR=\"LTR\">";
	
	public static final String FOOT_TEMPLATE = "</BODY></HTML>";
	
	/** 
	 * http://blog.csdn.net/huweijun_2012/article/details/51900814
     * 替换指定标签的属性和值 
     * @param str 需要处理的字符串 
     * @param tag 标签名称 
     * @param tagAttrib 要替换的标签属性值 
     * @param startTag 新标签开始标记 
     * @param endTag  新标签结束标记 
     * @return 
     * @author huweijun 
     * @date 2016年7月13日 下午7:15:32 
     */
	public static String replaceHtmlTag(String str, String tag, String tagAttrib, String startTag, String endTag) {  
        String regxpForTag = "<\\s*" + tag + "\\s+([^>]*)\\s*" ;  
        String regxpForTagAttrib = tagAttrib + "=\\s*\"([^\"]+)\"" ;  
        Pattern patternForTag = Pattern.compile (regxpForTag,Pattern. CASE_INSENSITIVE );  
        Pattern patternForAttrib = Pattern.compile (regxpForTagAttrib,Pattern. CASE_INSENSITIVE );     
        Matcher matcherForTag = patternForTag.matcher(str);  
        StringBuffer sb = new StringBuffer();  
        boolean result = matcherForTag.find();  
        while (result) {  
            StringBuffer sbreplace = new StringBuffer( "<"+tag+" ");  
            Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag.group(1));  
            if (matcherForAttrib.find()) {  
                String attributeStr = matcherForAttrib.group(1);  
                matcherForAttrib.appendReplacement(sbreplace, startTag + attributeStr + endTag);  
            }  
            matcherForAttrib.appendTail(sbreplace);  
            matcherForTag.appendReplacement(sb, sbreplace.toString());  
            result = matcherForTag.find();  
        }  
        matcherForTag.appendTail(sb);           
        return sb.toString();  
    }
	
	/**
	 * 将 data 中的编码修改为 utf-8
	 * @author eko.zhan at 2017年8月11日 上午9:54:34
	 * @param data
	 * @return
	 */
	public static String replaceCharset(String data){
		return StringUtils.replaceAll(data, "CONTENT=\"text/html; charset=gb2312\"", "CONTENT=\"text/html; charset=utf-8\"");
	}
	
	/**
	 * 获取文件编码
	 * @author eko.zhan at Jul 3, 2017 1:54:50 PM
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	public static String getFileEncoding(File file) throws IOException{
		UniversalDetector detector = new UniversalDetector(null);
		byte[] bytes = FileUtils.readFileToByteArray(file);
		detector.handleData(bytes, 0, bytes.length);
		detector.dataEnd();
		return detector.getDetectedCharset();
	}
}
