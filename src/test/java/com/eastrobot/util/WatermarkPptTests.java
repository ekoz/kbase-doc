/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.util;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.sl.usermodel.PictureData.PictureType;
import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFAutoShape;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.junit.Test;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月3日 下午7:27:58
 * @version 1.0
 */
public class WatermarkPptTests {

	@Test
	public void test1() throws IOException {
		 // create a ppt
        XMLSlideShow ppt = new XMLSlideShow();
        XSLFPictureData pd = ppt.addPicture(new File("D:\\Xiaoi\\logo\\logo.png"), PictureType.PNG);
        XSLFSlideMaster slideMaster = ppt.getSlideMasters().get(0);
        XSLFSlideLayout slidelayout = slideMaster.getLayout(SlideLayout.TITLE_AND_CONTENT);
        XSLFPictureShape ps = slidelayout.createPicture(pd);
        ps.setAnchor(new Rectangle2D.Double(100, 100, 400, 400));

        XSLFSlide sl = ppt.createSlide(slidelayout);
        ((XSLFAutoShape)sl.getShapes().get(0)).setText("title");
        ((XSLFAutoShape)sl.getShapes().get(1)).setText("content");

        FileOutputStream fos = new FileOutputStream("E:\\ConvertTester\\excel\\bla.pptx");
        ppt.write(fos);
        fos.close();
	}
	
}
