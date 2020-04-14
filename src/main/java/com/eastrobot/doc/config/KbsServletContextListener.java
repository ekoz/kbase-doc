/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.doc.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年8月8日 下午8:32:46
 * @version 1.0
 */
@Configuration
public class KbsServletContextListener implements ServletContextListener {

	@Autowired
	private OpenOffice openOffice;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		WebappContext.init(event.getServletContext(), openOffice);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		WebappContext.destroy(event.getServletContext());
	}

}
