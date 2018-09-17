/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.samples;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTextBox;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月3日 下午7:27:58
 * @version 1.0
 */
public class WatermarkExcelTests {

	@Test
	public void test1() throws IOException {
		
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		String imgPath = "D:\\Xiaoi\\logo\\logo.png";
		BufferedImage bufferImg = ImageIO.read(new File(imgPath));
        //这里要注意，第二个参数将会决定插入图片形式，如果是一个png的图片，背景透明，但是此处设置为jpg格式将会自动添加黑色背景
        ImageIO.write(bufferImg, "png", byteArrayOut);
        
        
        String filepath = "E:\\ConvertTester\\excel\\abcde.xlsx";
		File originFile = new File(filepath);
		InputStream in = new FileInputStream(originFile);
		XSSFWorkbook workbook = new XSSFWorkbook(in);
		XSSFSheet sheet = workbook.createSheet("testSheet");
         //画图的顶级管理器，一个sheet只能获取一个
        XSSFDrawing drawing = sheet.createDrawingPatriarch(); 
        //anchor主要用于设置图片的属性  
//        XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 1023, 255, (short) 2, 4, (short) 13, 26);
        /*
         * 参数定义：
         * 第一个参数是（x轴的开始节点）；
         * 第二个参数是（是y轴的开始节点）；
         * 第三个参数是（是x轴的结束节点）；
         * 第四个参数是（是y轴的结束节点）；
         * 第五个参数是（是从Excel的第几列开始插入图片，从0开始计数）；
         * 第六个参数是（是从excel的第几行开始插入图片，从0开始计数）；
         * 第七个参数是（图片宽度，共多少列）；
         * 第8个参数是（图片高度，共多少行）；
         */
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, Short.MAX_VALUE, Integer.MAX_VALUE, 0, 0, 10, 10);
        anchor.setAnchorType(AnchorType.DONT_MOVE_DO_RESIZE);
        //插入图片
        drawing.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_PNG));
        workbook.write(new FileOutputStream("E:\\ConvertTester\\excel\\abcd-011.xlsx"));
        workbook.close();
	}
	
	@Test
	public void test2() throws IOException {
		 //create a new workbook
		XSSFWorkbook wb = new XSSFWorkbook(); //or new HSSFWorkbook();
	    String imgPath = "D:\\Xiaoi\\logo\\logo.png";
	    //add picture data to this workbook.
	    InputStream is = new FileInputStream(imgPath);
	    byte[] bytes = IOUtils.toByteArray(is);
	    int pictureIdx = wb.addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG);
	    is.close();

	    CreationHelper helper = wb.getCreationHelper();

	    //create sheet
	    Sheet sheet = wb.createSheet();

	    // Create the drawing patriarch.  This is the top level container for all shapes. 
	    Drawing drawing = sheet.createDrawingPatriarch();

	    //add a picture shape
	    ClientAnchor anchor = helper.createClientAnchor();
	    //set top-left corner of the picture,
	    //subsequent call of Picture#resize() will operate relative to it
	    anchor.setCol1(3);
	    anchor.setRow1(2);
	    anchor.setAnchorType(AnchorType.DONT_MOVE_AND_RESIZE);
	    Picture pict = drawing.createPicture(anchor, pictureIdx);

	    //auto-size picture relative to its top-left corner
	    pict.resize();

	    //save workbook
	    String file = "E:\\ConvertTester\\excel\\picture.xls";
	    if(wb instanceof XSSFWorkbook) file += "x";
	    try (OutputStream fileOut = new FileOutputStream(file)) {
	        wb.write(fileOut);
	    }
	}
	
	@Test
	public void test3() {
//		String filepath = "E:\\ConvertTester\\excel\\abcde.xlsx";
//		File originFile = new File(filepath);
//		InputStream in = new FileInputStream(originFile);
//		XSSFWorkbook workbook = new XSSFWorkbook(in);
//		XSSFSheet sheet = workbook.createSheet("testSheet");

//		workbook.gets
//		XSLFSlideMaster slideMaster = sheet.getSlideMasters().get(0);
//		XSLFSlideLayout slidelayout = slideMaster.getLayout(SlideLayout.TITLE_AND_CONTENT);
//		XSLFPictureShape ps = slidelayout.createPicture(pd);
//		ps.setAnchor(new Rectangle2D.Double(100, 100, 400, 400));
		
	}
	
	@Test
	public void testExcel2() throws IOException {
		String filepath = "E:\\ConvertTester\\excel\\abcd.xlsx";
		File originFile = new File(filepath);
		InputStream in = new FileInputStream(originFile);
		XSSFWorkbook workbook = new XSSFWorkbook(in);
		XSSFSheet sheet = workbook.createSheet("testSheet");

		XSSFDrawing drawing = sheet.createDrawingPatriarch();
		
		XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 1023, 255, (short) 2, 4, (short) 13, 26);

	}
	
	@Test
	public void testExcel() throws IOException {
		String filepath = "E:\\ConvertTester\\excel\\abcd.xlsx";
		File originFile = new File(filepath);
		InputStream in = new FileInputStream(originFile);
		XSSFWorkbook workbook = new XSSFWorkbook(in);
		XSSFSheet sheet = workbook.createSheet("testSheet");

		XSSFDrawing drawing = sheet.createDrawingPatriarch();
		
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
}
