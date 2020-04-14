/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.doc.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.xmlbeans.XmlObject;
import org.springframework.stereotype.Service;

import com.eastrobot.doc.service.WatermarkService;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月1日 上午10:11:26
 * @version 1.0
 */
@Service
public class WatermarkServiceImpl implements WatermarkService {
	
	private final static String DEFAULT_WATERMARK = "powered by eko.zhan";
	private final static String DEFAULT_FONT_COLOR = "#d8d8d8";

	@Override
	public byte[] handle(File originFile, String watermark) throws IOException {
		return handle(originFile, watermark, DEFAULT_FONT_COLOR);
	}

	@Override
	public byte[] handle(File originFile, String watermark, String color) throws IOException {
		watermark = StringUtils.isBlank(watermark)?DEFAULT_WATERMARK:watermark;
		color = StringUtils.isBlank(color)?DEFAULT_FONT_COLOR:color;
		if (originFile.getName().toLowerCase().endsWith("docx")) {
			try (InputStream in = new FileInputStream(originFile)){
				XWPFDocument doc = new XWPFDocument(in);
				addWaterMark(doc, watermark, color);
				try (OutputStream out = new FileOutputStream(originFile)){
					doc.write(out);
					doc.close();
				}
			}
			return IOUtils.toByteArray(new FileInputStream(originFile));
		} else if (originFile.getName().toLowerCase().endsWith("doc")) {
			try (InputStream in = new FileInputStream(originFile)){
				HWPFDocument doc = new HWPFDocument(in);
				addWaterMark(doc, watermark, color);
				try (OutputStream out = new FileOutputStream(originFile)){
					doc.write(out);
					doc.close();
				}
			}
			return IOUtils.toByteArray(new FileInputStream(originFile));
		}
		return null;
	}

	
	

	private void addWaterMark(Object obj, String watermark, String color) {
		if (obj instanceof XWPFDocument) {
			XWPFDocument doc = (XWPFDocument) obj;
			// create header-footer
			XWPFHeaderFooterPolicy headerFooterPolicy = doc.getHeaderFooterPolicy();
			if (headerFooterPolicy == null) headerFooterPolicy = doc.createHeaderFooterPolicy();
			
			// create default Watermark - fill color black and not rotated
			headerFooterPolicy.createWatermark(watermark);
			
			// get the default header
			// Note: createWatermark also sets FIRST and EVEN headers 
			// but this code does not updating those other headers
			XWPFHeader header = headerFooterPolicy.getHeader(XWPFHeaderFooterPolicy.DEFAULT);
			XWPFParagraph paragraph = header.getParagraphArray(0);
			
			// get com.microsoft.schemas.vml.CTShape where fill color and rotation is set
			XmlObject[] xmlobjects = paragraph.getCTP().getRArray(0).getPictArray(0).selectChildren(new QName("urn:schemas-microsoft-com:vml", "shape"));
			if (xmlobjects.length > 0) {
				com.microsoft.schemas.vml.CTShape ctshape = (com.microsoft.schemas.vml.CTShape)xmlobjects[0];
				// set fill color
				ctshape.setFillcolor(color);
				// set rotation
				ctshape.setStyle(ctshape.getStyle() + ";rotation:315");
			}
		} else if (obj instanceof HWPFDocument) {
			
		}
	}
}
