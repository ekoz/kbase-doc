/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.document.DefaultDocumentFormatRegistry;
import org.artofsolving.jodconverter.document.DocumentFormatRegistry;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

import com.sun.star.document.UpdateDocMode;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年9月2日 下午1:23:34
 * @version 1.0
 */
public class BaseTests {
	
	protected void convert(File inputFile, File outputFile){
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
	
	protected void convert(File inputFile, File outputFile, String password){
		DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
		configuration.setPortNumber(8100);
		configuration.setOfficeHome(new File("D:/Program Files/LibreOffice"));
//		configuration.setOfficeHome(new File("D:/Program Files/OpenOffice"));

		OfficeManager officeManager = configuration.buildOfficeManager();
        officeManager.start();
        DocumentFormatRegistry formatRegistry = new DefaultDocumentFormatRegistry();
        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager, formatRegistry);
        Map<String,?> defaultLoadProperties = createDefaultLoadProperties(password);
        converter.setDefaultLoadProperties(defaultLoadProperties);
        try {
        	 converter.convert(inputFile, outputFile);
        } catch (Exception e){
        	e.printStackTrace();
		} finally {
            officeManager.stop();
        }
	}
	
	protected Map<String,Object> createDefaultLoadProperties(String password) {
        Map<String,Object> loadProperties = new HashMap<String,Object>();
        loadProperties.put("Hidden", true);
        loadProperties.put("ReadOnly", true);
        loadProperties.put("UpdateDocMode", UpdateDocMode.QUIET_UPDATE);
        if (StringUtils.isNotBlank(password)){
            loadProperties.put("Password", password);
        }
        return loadProperties;
    }
}
