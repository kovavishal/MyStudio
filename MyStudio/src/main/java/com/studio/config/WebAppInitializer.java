/**
 * 
 */
package com.studio.config;

import javax.persistence.Entity;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author ezhilraja_k
 *
 */

public class WebAppInitializer implements WebApplicationInitializer  {

	/* (non-Javadoc)
	 * @see org.springframework.web.WebApplicationInitializer#onStartup(javax.servlet.ServletContext)
	 */
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
		webContext.register(DatabaseConfig.class);
	    webContext.register(MvcConfiguration.class);
	    webContext.setServletContext(servletContext);
	    ServletRegistration.Dynamic reg=servletContext.addServlet("dispatcherServlet", new DispatcherServlet(webContext));
	    reg.setLoadOnStartup(1);
	    reg.addMapping("*.action");
		
	}

}
