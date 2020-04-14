/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.doc.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.converter.ExcelToHtmlUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocumentCore;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.converter.WordToHtmlUtils;
import org.w3c.dom.Document;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年8月3日 下午8:34:35
 * @version 1.0
 */
public class POIUtils {

	/**
	 * word to html
	 * @author eko.zhan at 2017年8月3日 下午8:51:23
	 * @param file
	 * @return
	 * @throws ParserConfigurationException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws TransformerException
	 */
	public static String wordToHtml(File file){
		String result = "";
		ByteArrayOutputStream out = null;
		try {
			HWPFDocumentCore wordDocument = WordToHtmlUtils.loadDoc(file);

			WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
			        DocumentBuilderFactory.newInstance().newDocumentBuilder()
			                .newDocument());
			
			wordToHtmlConverter.processDocument(wordDocument);
			Document htmlDocument =  wordToHtmlConverter.getDocument();
			out = new ByteArrayOutputStream();
			DOMSource domSource = new DOMSource(htmlDocument);
			StreamResult streamResult = new StreamResult(out);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer serializer = tf.newTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty(OutputKeys.METHOD, "html");
			serializer.transform(domSource, streamResult);
			
			result = new String(out.toByteArray());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(out);
		}
		return result;
	}
	
	/**
	 * excel to html
	 * @author eko.zhan at 2017年8月3日 下午9:36:51
	 * @param file
	 * @return
	 */
	public static String excelToHtml(File file){
		String result = "";
		
		return result;
	}
}
