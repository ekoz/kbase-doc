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
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.Test;

import com.eastrobot.service.impl.WatermarkServiceImpl;


/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年8月31日 下午1:41:38
 * @version 1.0
 */
public class WatermarkWordTests extends BaseTests {

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
	public void testDocx2() throws IOException {
		String filepath = "E:\\ConvertTester\\docx\\NVR5X-I人脸比对配置-ekozhan.docx";
		XWPFDocument doc = new XWPFDocument(new FileInputStream(filepath));
		XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(doc);
		
		policy.createWatermark("ekozhan123");
		doc.write(new FileOutputStream("E:\\ConvertTester\\docx\\NVR5X-I人脸比对配置-ekozhan-11.docx"));
		doc.close();
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
