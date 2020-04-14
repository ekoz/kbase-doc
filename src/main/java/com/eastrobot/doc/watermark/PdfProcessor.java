/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.doc.watermark;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.poi.util.IOUtils;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月17日 下午4:36:50
 * @version 1.0
 */
public class PdfProcessor extends AbstractProcessor {

	public PdfProcessor(File file, File imageFile) {
		super(file, imageFile);
	}

	@Override
	public void process() throws WatermarkException {
		PdfReader reader = null;
		PdfStamper stamper = null;
		try {
			reader = new PdfReader(new FileInputStream(file));
			stamper = new PdfStamper(reader, new FileOutputStream(file));
			int pageNo = reader.getNumberOfPages();
			// image watermark
			Image img = Image.getInstance(IOUtils.toByteArray(new FileInputStream(imageFile)));
			float w = Math.min(img.getScaledWidth(), 460);
			float h = Math.min(img.getScaledHeight(), 300);
			for (float f : img.matrix()) {
				System.out.println(f);
			}
			// transparency
			PdfGState gs = new PdfGState();
			gs.setFillOpacity(0.1f);
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
				over.setGState(gs);
				System.out.println(w + ", " + h + ", " + (x - (w / 2)) + ", " + (y - (h / 2)));
//				617.0, 400.0, -10.839996, 220.95999
				over.addImage(img, w, 0, 0, h, x - (w / 2), y - (h / 2));
				over.restoreState();
			}
			if (stamper!=null) {
				stamper.close();
			}
		} catch (FileNotFoundException e) {
			throw new WatermarkException("FileNotFoundException", e);
		} catch (BadElementException e) {
			throw new WatermarkException("BadElementException", e);
		} catch (MalformedURLException e) {
			throw new WatermarkException("MalformedURLException", e);
		} catch (IOException e) {
			throw new WatermarkException("IOException", e);
		} catch (DocumentException e) {
			throw new WatermarkException("DocumentException", e);
		} finally {
			if (reader!=null) {
				reader.close();
			}
		}
	}

}
