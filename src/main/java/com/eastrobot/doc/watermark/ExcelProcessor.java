/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.doc.watermark;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.relationships.Relationship;
import org.xlsx4j.exceptions.Xlsx4jException;
import org.xlsx4j.sml.CTSheetBackgroundPicture;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月17日 下午1:48:10
 * @version 1.0
 */
public class ExcelProcessor extends AbstractProcessor {
	
	private SpreadsheetMLPackage excelMLPackage;
	
	public ExcelProcessor(File file, File imageFile) {
		super(file, imageFile);
	}
	
	/**
	 * 传入一个 xlsx 文件，和一个水印文件，给这个 xlsx 文件加水印
	 * @author eko.zhan at 2018年9月17日 下午2:10:50
	 * @throws Docx4JException
	 * @throws Xlsx4jException
	 */
	@Override
	public void process() throws WatermarkException {
		try {
			SpreadsheetMLPackage excelMLPackage = SpreadsheetMLPackage.load(file);
			this.excelMLPackage = excelMLPackage;
			int size = excelMLPackage.getWorkbookPart().getContents().getSheets().getSheet().size();
			for (int i=0;i<size;i++) {
				WorksheetPart worksheet = excelMLPackage.getWorkbookPart().getWorksheet(i);
				createBgPic(worksheet);
			}
			excelMLPackage.save(file);
		} catch (Docx4JException e) {
			throw new WatermarkException("Docx4JException", e);
		} catch (Xlsx4jException e) {
			throw new WatermarkException("Xlsx4jException", e);
		}
	}
	
	/**
	 * 使用水印图片作为excel背景，达到水印效果<但打印时不会生效>
	 * @param pkg
	 * @param worksheet
	 * @throws Docx4JException 
	 * @throws Exception
	 * @throws IOException
	 */
	private void createBgPic(WorksheetPart worksheet) throws Docx4JException {
		CTSheetBackgroundPicture ctSheetBackgroundPicture = org.xlsx4j.jaxb.Context.getsmlObjectFactory().createCTSheetBackgroundPicture();
		worksheet.getContents().setPicture(ctSheetBackgroundPicture);
		try {
			BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(excelMLPackage, worksheet, FileUtils.readFileToByteArray(imageFile));
			Relationship sourceRelationship = imagePart.getSourceRelationships().get(0);
			String imageRelId = sourceRelationship.getId();
			ctSheetBackgroundPicture.setId(imageRelId);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
