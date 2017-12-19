/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.document.DefaultDocumentFormatRegistry;
import org.artofsolving.jodconverter.document.DocumentFamily;
import org.artofsolving.jodconverter.document.DocumentFormat;
import org.artofsolving.jodconverter.document.DocumentFormatRegistry;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.junit.Test;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年12月6日 下午7:55:08
 * @version 1.0
 */
public class ConverteTests {
	
	
	
	@Test
	public void testVisioAsPdf(){
		File inputFile = new File("E:/ConvertTester/TestFiles/I_am_a_vsdx.vsdx");
		File outputFile = new File("E:/ConvertTester/TestFiles/I_am_a_vsdx_openoffice.pdf");
//		if (!outputFile.exists()){
//			outputFile.createNewFile();
//		}
		convert(inputFile, outputFile);
	}

	@Test
	public void testSaveAsDocx() throws FileNotFoundException, IOException{
		
		File inputFile = new File("D:/Workspace/kbase-doc/target/classes/static/DATAS/1512561737109/1.doc");
		File outputFile = new File("D:/Workspace/kbase-doc/target/classes/static/DATAS/1512561737109/1.docx");
		IOUtils.copy(new FileInputStream(inputFile), new FileOutputStream(outputFile));
		
	}
	
	@Test
	public void testConvert() throws IOException {
//		File inputFile = new File("D:/Workspace/kbase-doc/target/classes/static/DATAS/1512561737109/1.doc");
		File inputFile = new File("D:/Workspace/kbase-doc/target/classes/static/DATAS/1512561737109/1512561737109.html");
		File outputFile = new File("D:/Workspace/kbase-doc/target/classes/static/DATAS/1512561737109/" + Calendar.getInstance().getTimeInMillis() + ".docx");
//		if (!outputFile.exists()){
//			outputFile.createNewFile();
//		}
		DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
		configuration.setPortNumber(8100);
		configuration.setOfficeHome(new File("D:/Program Files/LibreOffice"));

		OfficeManager officeManager = configuration.buildOfficeManager();
        officeManager.start();
        DocumentFormatRegistry formatRegistry = new DefaultDocumentFormatRegistry();
        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager, formatRegistry);
        
        try {
        	 converter.convert(inputFile, outputFile);
        } catch (Exception e){
        	e.printStackTrace();
		} finally {
            officeManager.stop();
        }
	}
	
	@Test
	public void testGetFormatByExtension(){
		DocumentFormatRegistry formatRegistry = new DefaultDocumentFormatRegistry();
		DocumentFormat formatByExtension = formatRegistry.getFormatByExtension("docx");
		System.out.println(formatByExtension.getName());
		Map<DocumentFamily, Map<String, ?>> storePropertiesByFamily = formatByExtension.getStorePropertiesByFamily();
		System.out.println(storePropertiesByFamily.size());
		
	}
	
	private void convert(File inputFile, File outputFile){
		DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
		configuration.setPortNumber(8100);
		configuration.setOfficeHome(new File("D:/Program Files/LibreOffice"));
//		configuration.setOfficeHome(new File("D:/Program Files/OpenOffice"));

		OfficeManager officeManager = configuration.buildOfficeManager();
        officeManager.start();
        DocumentFormatRegistry formatRegistry = new DefaultDocumentFormatRegistry();
        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager, formatRegistry);
        
        try {
        	 converter.convert(inputFile, outputFile);
        } catch (Exception e){
        	e.printStackTrace();
		} finally {
            officeManager.stop();
        }
	}
}
