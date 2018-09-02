/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ddf.EscherComplexProperty;
import org.apache.poi.ddf.EscherContainerRecord;
import org.apache.poi.ddf.EscherOptRecord;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.OfficeDrawing;
import org.apache.poi.hwpf.usermodel.OfficeDrawings;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTextBox;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import com.eastrobot.service.impl.WatermarkServiceImpl;
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
public class WatermarkTests extends BaseTests {

	/**
	 * 给 docx 文件加水印
	 * @author eko.zhan at 2018年8月31日 下午1:41:50
	 * @throws IOException 
	 */
	@Test
	public void testDocx() throws IOException {
		String filepath = "E:\\ConvertTester\\docx\\NVR5X-I人脸比对配置-ekozhan.docx";

		WatermarkServiceImpl service = new WatermarkServiceImpl();
		byte[] bytes = service.handle(new File(filepath), "中华民国100");
		//		FileOutputStream out = new FileOutputStream("E:\\1.docx");
		//		IOUtils.write(bytes, out);
		try (FileOutputStream out = new FileOutputStream("E:\\1.docx")){
			IOUtils.write(bytes, out);
		}
	}

	@Test
	public void testExcel() throws IOException {
		String filepath = "E:\\ConvertTester\\excel\\abcd.xlsx";
		File originFile = new File(filepath);
		InputStream in = new FileInputStream(originFile);
		XSSFWorkbook workbook = new XSSFWorkbook(in);
		XSSFSheet ws = workbook.createSheet("testSheet");

		XSSFDrawing drawing = ws.createDrawingPatriarch();

		XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 1023, 255, (short) 2, 4, (short) 13, 26);

		XSSFTextBox textbox = drawing.createTextbox(anchor);
		XSSFRichTextString rtxt = new XSSFRichTextString("ekozhan");
		XSSFFont font = workbook.createFont();
		font.setColor((short) 27);
		font.setBold(true);
		font.setFontHeightInPoints((short) 192);
		font.setFontName("Verdana");
		rtxt.applyFont(font);
		textbox.setText(rtxt);
		textbox.setLineStyle(XSSFShape.EMU_PER_POINT);
		textbox.setNoFill(true);
		workbook.write(new FileOutputStream(filepath));
		workbook.close();
	}

	@Test
	public void testDoc() throws IOException {
		String filepath = "E:\\ConvertTester\\docx\\请稍候在注册表里注册.doc";
		File originFile = new File(filepath);
		InputStream in = new FileInputStream(originFile);
		HWPFDocument doc = new HWPFDocument(in);

		if ( doc.getOfficeDrawingsHeaders() != null )
		{
			System.out.println( "=== Document part: HEADER ===" );
			for ( OfficeDrawing officeDrawing : doc
					.getOfficeDrawingsHeaders().getOfficeDrawings() )
			{
				System.out.println( officeDrawing );
			}
		}
		if ( doc.getOfficeDrawingsHeaders() != null )
		{
			System.out.println( "=== Document part: MAIN ===" );
			for ( OfficeDrawing officeDrawing : doc
					.getOfficeDrawingsMain().getOfficeDrawings() )
			{
				System.out.println( officeDrawing );
			}
		}

		if (true) return;
		OfficeDrawings officeDrawings = doc.getOfficeDrawingsHeaders();
		OfficeDrawing officeDrawingAt = officeDrawings.getOfficeDrawingAt(0);
		Collection<OfficeDrawing> officeDrawings2 = officeDrawings.getOfficeDrawings();
		OfficeDrawing drawing = doc.getOfficeDrawingsHeaders().getOfficeDrawings().iterator().next();
		EscherContainerRecord escherContainerRecord = drawing.getOfficeArtSpContainer();

		EscherOptRecord officeArtFOPT = escherContainerRecord.getChildById((short) 0xF00B);
		EscherComplexProperty gtextUNICODE = officeArtFOPT.lookup(0x00c0);
		String text = StringUtil.getFromUnicodeLE(gtextUNICODE.getComplexData());
		System.out.println(text);
		doc.close();
	}
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
