/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.samples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.eastrobot.util.BaseTests;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;


/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年8月31日 下午1:41:38
 * @version 1.0
 */
public class WatermarkPdfTests extends BaseTests {

	/**
	 * pdf 用文字加水印，存在问题，如何支持中文
	 * @author eko.zhan at 2018年9月2日 下午1:44:40
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws DocumentException
	 */
	@Test
	public void testVisioAsPdfWithText() throws FileNotFoundException, IOException, DocumentException{
		File inputFile = new File("E:/ConvertTester/TestFiles/I_am_a_vsdx.vsdx");
		File outputFile = new File("E:/ConvertTester/TestFiles/I_am_a_vsdx_libreoffice.pdf");
		if (!outputFile.exists()) {
			convert(inputFile, outputFile);
		}
		File destFile = new File("E:/ConvertTester/TestFiles/I_am_a_vsdx_libreoffice_watermark.pdf");
		//转换成 pdf 后利用 itext 加水印 
		PdfReader reader = new PdfReader(new FileInputStream(outputFile));
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destFile));
		int pageNo = reader.getNumberOfPages();
		Font f = new Font(FontFamily.HELVETICA, 28);
		Phrase p = new Phrase("Xiaoi Robot", f);
		for (int i=1;i<=pageNo;i++) {
			PdfContentByte over = stamper.getOverContent(i);
			over.saveState();
			PdfGState gs1 = new PdfGState();
			gs1.setFillOpacity(0.5f);
			over.setGState(gs1);
			ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, 297, 450, 0);
			over.restoreState();
		}
		stamper.close();
		reader.close();
	}
	/**
	 * pdf 用图片加水印
	 * @author eko.zhan at 2018年9月2日 下午1:44:58
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws DocumentException
	 */
	@Test
	public void testVisioAsPdfWithImg() throws FileNotFoundException, IOException, DocumentException{
		File inputFile = new File("E:/ConvertTester/TestFiles/I_am_a_vsdx.vsdx");
		File outputFile = new File("E:/ConvertTester/TestFiles/I_am_a_vsdx_libreoffice.pdf");
		if (!outputFile.exists()) {
			convert(inputFile, outputFile);
		}
		File destFile = new File("E:/ConvertTester/TestFiles/I_am_a_vsdx_libreoffice_watermark.pdf");
		final String IMG = "D:\\Xiaoi\\logo\\logo.png";
		//转换成 pdf 后利用 itext 加水印 
		PdfReader reader = new PdfReader(new FileInputStream(outputFile));
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destFile));
		int pageNo = reader.getNumberOfPages();
		// text watermark
		Font f = new Font(FontFamily.HELVETICA, 30);
		Phrase p = new Phrase("Xiaoi Robot Image", f);
		// image watermark
		Image img = Image.getInstance(IMG);
		float w = img.getScaledWidth();
		float h = img.getScaledHeight();
		// transparency
		PdfGState gs1 = new PdfGState();
		gs1.setFillOpacity(0.5f);
		// properties
		PdfContentByte over;
		Rectangle pagesize;
		float x, y;
		// loop over every page
		for (int i = 1; i <= pageNo; i++) {
			pagesize = reader.getPageSizeWithRotation(i);
			x = (pagesize.getLeft() + pagesize.getRight()) / 2;
			y = (pagesize.getTop() + pagesize.getBottom()) / 2;
			over = stamper.getOverContent(i);
			over.saveState();
			over.setGState(gs1);
			if (i % 2 == 1)
				ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, x, y, 0);
			else
				over.addImage(img, w, 0, 0, h, x - (w / 2), y - (h / 2));
			over.restoreState();
		}
		stamper.close();
		reader.close();
	}
}
