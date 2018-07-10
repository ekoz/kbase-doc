/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.fit.pdfdom.PDFDomTree;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年7月10日 下午5:00:24
 * @version 1.0
 */
public class PdfToHtmlTests {

	@Test
	public void pdf2html() throws InvalidPasswordException, IOException, ParserConfigurationException{
		PDDocument pdf = PDDocument.load(new File("E:/ConvertTester/ppt/推荐系统分享.pdf"));
		PDFDomTree parser = new PDFDomTree();
//		Document dom = parser.createDOM(pdf);
		File htmlFile = new File("E:/ConvertTester/ppt/推荐系统分享.html");
		parser.writeText(pdf, new OutputStreamWriter(new FileOutputStream(htmlFile)));
	}
}
