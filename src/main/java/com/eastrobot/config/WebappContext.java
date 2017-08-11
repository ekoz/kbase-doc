/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.config;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年8月8日 下午8:50:28
 * @version 1.0
 */
public class WebappContext {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String KEY = WebappContext.class.getName();

	private final ServletFileUpload fileUpload;

	private final OfficeManager officeManager;
	private final OfficeDocumentConverter documentConverter;

	public WebappContext(ServletContext servletContext, OpenOffice openOffice) {
		DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
		fileUpload = new ServletFileUpload(fileItemFactory);
		if (openOffice.getFileSizeMax() != null) {
			fileUpload.setFileSizeMax(Integer.parseInt(openOffice.getFileSizeMax()));
			logger.info("max file upload size set to " + openOffice.getFileSizeMax());
		} else {
			logger.warn("max file upload size not set");
		}
		
		DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
		if (openOffice.getPort() != null) {
		    configuration.setPortNumber(Integer.parseInt(openOffice.getPort()));
		}
		if (openOffice.getHome() != null) {
		    configuration.setOfficeHome(new File(openOffice.getHome()));
		}
		if (StringUtils.isNotBlank(openOffice.getProfile())) {
		    configuration.setTemplateProfileDir(new File(openOffice.getProfile()));
		}

		officeManager = configuration.buildOfficeManager();
		documentConverter = new OfficeDocumentConverter(officeManager);
	}

	protected static void init(ServletContext servletContext, OpenOffice openOffice) {
		WebappContext instance = new WebappContext(servletContext, openOffice);
		servletContext.setAttribute(KEY, instance);
		instance.officeManager.start();
	}

	protected static void destroy(ServletContext servletContext) {
		WebappContext instance = get(servletContext);
		instance.officeManager.stop();
	}

	public static WebappContext get(ServletContext servletContext) {
		return (WebappContext) servletContext.getAttribute(KEY);
	}

	public ServletFileUpload getFileUpload() {
		return fileUpload;
	}

	public OfficeManager getOfficeManager() {
        return officeManager;
    }

	public OfficeDocumentConverter getDocumentConverter() {
        return documentConverter;
    }

}
