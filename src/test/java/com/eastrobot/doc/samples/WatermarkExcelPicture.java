package com.eastrobot.doc.samples;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.relationships.Relationship;
import org.xlsx4j.sml.CTSheetBackgroundPicture;


/**
 * <ns2:worksheet xmlns:ns2="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:ns4="http://schemas.microsoft.com/office/excel/2006/main" xmlns:xdr="http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" xmlns:ns5="http://schemas.microsoft.com/office/excel/2008/2/main">
    <ns2:dimension ref="A1"/>
    <ns2:sheetViews>
        <ns2:sheetView tabSelected="true" workbookViewId="0"/>
    </ns2:sheetViews>
    <ns2:sheetFormatPr defaultRowHeight="13.5"/>
    <ns2:sheetData>
        <ns2:row r="1" spans="1:1">
            <ns2:c r="A1" t="s">
                <ns2:v>0</ns2:v>
            </ns2:c>
        </ns2:row>
    </ns2:sheetData>
    <ns2:phoneticPr fontId="1" type="noConversion"/>
    <ns2:pageMargins left="0.7" right="0.7" top="0.75" bottom="0.75" header="0.3" footer="0.3"/>
    <ns2:headerFooter>
        <ns2:oddHeader>&amp;G</ns2:oddHeader>
    </ns2:headerFooter>
    <ns2:legacyDrawingHF r:id="rId1"/>
    <ns2:picture r:id="rId2"/>
</ns2:worksheet>
 * @author Administrator
 *
 */
public class WatermarkExcelPicture {
	
//	String fileName = System.getProperty("user.dir") + "/waterMarksample.xlsx";
	String fileName = "E:/waterMarksample.xlsx";
//	String fileName = "d:/word/print/test4.xlsx";
	String waterMark = "WATER-MARK";
//	String outMarkPic = System.getProperty("user.dir") + "/waterMark.png";
	String outMarkPic = "E:\\\\ConvertTester\\\\images\\\\jshrss-logo.png";
	
	public void addWatermark() throws Exception{
		File f = new File("E:/提取英文名.xlsx");
		SpreadsheetMLPackage pkg = SpreadsheetMLPackage.load(f);
		int size = pkg.getWorkbookPart().getContents().getSheets().getSheet().size();
		for (int i=0;i<size;i++) {
			WorksheetPart worksheet = pkg.getWorkbookPart().getWorksheet(i);
			createBgPic(pkg, worksheet);
		}
		pkg.save(new File(fileName));
		System.out.println("\n\n done .. " + fileName);
	}
	
	/**
	 * 使用水印图片作为excel背景，达到水印效果<但打印时不会生效>
	 * @param pkg
	 * @param worksheet
	 * @throws Exception
	 * @throws IOException
	 */
	private void createBgPic(SpreadsheetMLPackage pkg, WorksheetPart worksheet) throws Exception, IOException {
		CTSheetBackgroundPicture ctSheetBackgroundPicture = org.xlsx4j.jaxb.Context.getsmlObjectFactory().createCTSheetBackgroundPicture();
		worksheet.getContents().setPicture(ctSheetBackgroundPicture);
		
		BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(pkg, worksheet, FileUtils.readFileToByteArray(new File(outMarkPic)));
		Relationship sourceRelationship = imagePart.getSourceRelationships().get(0);
		String imageRelID = sourceRelationship.getId();
		ctSheetBackgroundPicture.setId(imageRelID);
	}
	
	public static void main(String[] args) {
		try {
			new WatermarkExcelPicture().addWatermark();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    
}