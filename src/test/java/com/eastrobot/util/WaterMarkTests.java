/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.util;

import java.io.File;
import java.io.FileInputStream;
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

import com.eastrobot.service.impl.WaterMarkServiceImpl;


/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年8月31日 下午1:41:38
 * @version 1.0
 */
public class WaterMarkTests {

	/**
	 * 给 docx 文件加水印
	 * @author eko.zhan at 2018年8月31日 下午1:41:50
	 * @throws IOException 
	 */
	@Test
	public void testDocx() throws IOException {
		String filepath = "E:\\ConvertTester\\docx\\NVR5X-I人脸比对配置-ekozhan.docx";

		WaterMarkServiceImpl service = new WaterMarkServiceImpl();
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
}
