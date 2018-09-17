/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.watermark;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.poi.sl.usermodel.PictureData.PictureType;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.xlsx4j.exceptions.Xlsx4jException;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月17日 下午1:48:10
 * @version 1.0
 */
public class PowerPointProcessor extends AbstractProcessor {
	
	public PowerPointProcessor(File file, File imageFile) {
		super(file, imageFile);
	}
	/**
	 * 传入一个 pptx 文件，和一个水印文件，给这个 pptx 文件加水印
	 * @author eko.zhan at 2018年9月17日 下午2:10:50
	 * @throws IOException 
	 * @throws  
	 * @throws Docx4JException
	 * @throws Xlsx4jException
	 */
	@Override
	public void process() throws WatermarkException {
		XMLSlideShow pptx = null;
		FileOutputStream output = null;
		try {
			pptx = new XMLSlideShow(new FileInputStream(file));
			XSLFPictureData pictureData = pptx.addPicture(imageFile, PictureType.PNG);
			for (int i=0;i<pptx.getSlideMasters().size();i++) {
				XSLFSlideMaster slideMaster = pptx.getSlideMasters().get(i);
				XSLFSlideLayout[] slideLayouts = slideMaster.getSlideLayouts();
				for (XSLFSlideLayout slidelayout : slideLayouts) {
					XSLFPictureShape pictureShape = slidelayout.createPicture(pictureData);
					pictureShape.setAnchor(new Rectangle2D.Double(20, 20, 640, 400));
				}
			}
			output = new FileOutputStream(file);
			pptx.write(output);
		} catch (FileNotFoundException e) {
			throw new WatermarkException("FileNotFoundException", e);
		} catch (IOException e) {
			throw new WatermarkException("IOException", e);
		} finally {
			IOUtils.closeQuietly(output, pptx);
		}
	}
}
