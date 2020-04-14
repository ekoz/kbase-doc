/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.doc.util;

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
		String srcFilePath = "E:\\converter-html\\sgcc\\京电发展〔2019〕82号(盖章).ceb";
		String destFilePath = srcFilePath + ".html";
		PDDocument pdf = PDDocument.load(new File(srcFilePath));
		PDFDomTree parser = new PDFDomTree();
//		Document dom = parser.createDOM(pdf);
		File htmlFile = new File(destFilePath);
		parser.writeText(pdf, new OutputStreamWriter(new FileOutputStream(htmlFile)));
	}
}
